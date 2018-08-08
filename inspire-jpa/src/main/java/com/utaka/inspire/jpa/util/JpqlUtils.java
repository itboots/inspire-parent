/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.util;

import com.google.common.annotations.Beta;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utaka.inspire.jpa.domain.*;
import com.utaka.inspire.util.ObjectUtils;
import com.utaka.inspire.util.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

/**
 * {@link JpqlQuery} 工具类，用于构造{@link JpqlQuery}对象。
 *
 * @author XINEN
 */
public abstract class JpqlUtils {
    private static final Splitter PARAMETER_SPLITTER = Splitter.on(':').omitEmptyStrings().trimResults();
    private static final Joiner PARAMETER_JOINER = Joiner.on(":").skipNulls();

    private static final Splitter WHERE_SPLITTER = Splitter.on(Pattern.compile(" where ", Pattern.CASE_INSENSITIVE)).omitEmptyStrings();
    private static final Joiner WHERE_JOINER = Joiner.on(" WHERE ").skipNulls();

    private static final Joiner SELECT_JOINER = Joiner.on(" ").skipNulls();
    private static final Joiner GROUP_BY_JOINER = Joiner.on(", ").skipNulls();
    private static final Joiner ORDER_BY_JOINER = Joiner.on(", ").skipNulls();

    private static final Pattern FETCH_PARTNER = Pattern.compile(" fetch ", Pattern.CASE_INSENSITIVE);
    private static final Pattern DISTINCT_PARTNER = Pattern.compile(" distinct ", Pattern.CASE_INSENSITIVE);
    private static final String NATIVE_COUNT_SQL_FORMATTER = "select count(1) from (%s)";
    private static final String NATIVE_ORDER_BY_SQL_FORMATTER = "select * from (%s) order by %s";

    private static final String IDENTIFIER = "[\\p{Alnum}._$]+";
    private static final String IDENTIFIER_GROUP = String.format("(%s)", IDENTIFIER);
    private static final String LEFT_JOIN = "left (outer )?join " + IDENTIFIER + " (as )?" + IDENTIFIER_GROUP;
    private static final Pattern LEFT_JOIN_PATTERN = Pattern.compile(LEFT_JOIN, Pattern.CASE_INSENSITIVE);
    private static final String COUNT_QUERY_FORMATTER = "SELECT COUNT(x.id) FROM %s x";
    private static final String SELECT_QUERY_FORMATTER = "SELECT x FROM %s x";
    private static final String SELECT_DISTINCT_QUERY_FORMATTER = "SELECT DISTINCT x FROM %s x";

    private static final Pattern ORDER_BY = Pattern.compile(".*order\\s+by\\s+.*", CASE_INSENSITIVE);
    private static final Pattern ALIAS_MATCH;
    private static final Pattern COUNT_MATCH;
    private static final String SIMPLE_COUNT_VALUE = "$2";
    private static final String COMPLEX_COUNT_VALUE = "$3$6";
    private static final String PK_REPLACEMENT_TEMPLATE = "select %s.%s $5$6$7";


    private static final int VARIABLE_NAME_GROUP_INDEX = 4;

    //单个集合最大个数限制
    private static final int MAX_COLLECTION_PARAMS_LIMIT = 500;

    /**
     * 支持空字符串的时候就只处理过滤条件，不添加参数的情况
     */
    private static final Predicate<FreeFilter> VALID_FREE_FILTER = new Predicate<FreeFilter>() {
        @Override
        public boolean apply(@Nullable FreeFilter input) {
            //过滤没有值的过滤条件
            return !(StringUtils.isNotEmpty(input.getName()) && ObjectUtils.isEmpty(input.getValue()));
        }
    };

    private static final Predicate<HavingToken> VALID_HAVING_PART = new Predicate<HavingToken>() {
        @Override
        public boolean apply(@Nullable HavingToken input) {
            return !(StringUtils.isNotEmpty(input.getName()) && ObjectUtils.isEmpty(input.getValue()));
        }
    };

    static {

        StringBuilder builder = new StringBuilder();
        // from as starting delimiter
        builder.append("(?<=from)");
        // at least one space separating
        builder.append("(?: )+");
        // Entity name, can be qualified (any
        builder.append(IDENTIFIER_GROUP);
        // exclude possible "as" keyword
        builder.append("(?: as)*");
        // at least one space separating
        builder.append("(?: )+");
        // the actual alias
        builder.append("(\\w*)");

        ALIAS_MATCH = compile(builder.toString(), CASE_INSENSITIVE);

        builder = new StringBuilder();
        builder.append("(select\\s+((distinct )?(.+?)?)\\s+)?(from\\s+");
        builder.append(IDENTIFIER);
        builder.append("(?:\\s+as)?\\s+)");
        builder.append(IDENTIFIER_GROUP);
        builder.append("(.*)");

        COUNT_MATCH = compile(builder.toString(), CASE_INSENSITIVE);

    }


    /**
     * 构造原生的COUNT查询语句
     *
     * @param originalQuery 原来的查询
     * @return
     */
    public static String createNativeCountQueryFor(String originalQuery) {
        checkArgument(!Strings.isNullOrEmpty(originalQuery), "originalQuery");

        return String.format(NATIVE_COUNT_SQL_FORMATTER, originalQuery);

    }

    /**
     * 根据给定的查询语句构造 count 查询语句
     *
     * @param originalQuery 原查询JPQL字符串
     * @return
     */

    public static String createCountQueryFor(String originalQuery) {
        checkArgument(!Strings.isNullOrEmpty(originalQuery), "originalQuery");

        String query = QueryUtils.createCountQueryFor(originalQuery);
        query = FETCH_PARTNER.matcher(query).replaceAll(" ");
        return query;

    }

    /**
     * 根据给定的查询语句构造只查询主键（ID）的查询语句
     *
     * @param originalQuery 原查询JPQL字符串
     * @return
     */
    public static String createPrimaryKeyQueryFor(String originalQuery) {
        return createPrimaryKeyQueryFor(originalQuery, "id");

    }

    /**
     * 根据给定的查询语句构造只查询主键（ID）的查询语句
     *
     * @param originalQuery 原查询JPQL字符串
     * @return
     */
    public static String createPrimaryKeyQueryFor(String originalQuery, Sort sort) {
        return createPrimaryKeyQueryFor(originalQuery, "id", sort);

    }

    /**
     * 根据给定的查询语句构造只查询主键（ID）的查询语句
     *
     * @param originalQuery 原查询JPQL字符串
     * @return
     */
    public static String createPrimaryKeyQueryFor(String originalQuery, String pk) {
        return createPrimaryKeyQueryFor(originalQuery, pk, null);

    }

    /**
     * 根据给定的查询语句构造只查询主键（ID）的查询语句
     *
     * @param originalQuery 原查询JPQL字符串
     * @return
     */
    public static String createPrimaryKeyQueryFor(String originalQuery, String pk, Sort sort) {
        Assert.hasText(originalQuery, "OriginalQuery must not be null or empty!");

        String alias = detectAlias(originalQuery);
        Matcher matcher = COUNT_MATCH.matcher(originalQuery);
        String primaryKeyQuery = null;


        String variable = matcher.matches() ? matcher.group(VARIABLE_NAME_GROUP_INDEX) : null;
        boolean useVariable = variable != null && org.springframework.util.StringUtils.hasText(variable) && !variable.startsWith("new")
                && !variable.startsWith("count(") && !variable.contains(",");

        String replacement = useVariable ? SIMPLE_COUNT_VALUE : COMPLEX_COUNT_VALUE;
        primaryKeyQuery = matcher.replaceFirst(String.format(PK_REPLACEMENT_TEMPLATE, replacement, pk));
        primaryKeyQuery = FETCH_PARTNER.matcher(primaryKeyQuery).replaceAll(" ");
        //去除DISTINCT，避免和ORDER BY 冲突
        primaryKeyQuery = DISTINCT_PARTNER.matcher(primaryKeyQuery).replaceAll(" ");

        if (sort == null) {
            sort = new Sort(String.format("%s.%s", alias, pk));
        } else {
            sort = sort.and(new Sort(String.format("%s.%s", alias, pk)));
        }
        return JpqlUtils.applySorting(primaryKeyQuery, sort);
    }

    /**
     * 根据给定的查询语句构造只查询主键（ID）的查询语句
     *
     * @param originalQuery 原查询JPQL字符串
     * @return
     */
    public static JpqlQuery createQueryByPrimaryKey(String originalQuery, Map<String, Object> originalParams, Set<String> pks) {
        Assert.hasText(originalQuery, "OriginalQuery must not be null or empty!");

        String alias = detectAlias(originalQuery);
        List<String> sqlParts = WHERE_SPLITTER.splitToList(originalQuery);
        return createByPrimaryKeyWherePart(sqlParts, alias, originalParams, pks);
//        if (sqlParts.size() == 1 || sqlParts.size() == 2) {
//            return createByPrimaryKeyWherePart(sqlParts.get(0), alias, pks);
//
//        } else {
//            String select = WHERE_JOINER.join(sqlParts.subList(0, sqlParts.size() - 1));
//            return createByPrimaryKeyWherePart(select, alias, pks);
//        }
    }

    private static JpqlQuery createByPrimaryKeyWherePart(List<String> sqlParts, String alias, Map<String, Object> originalParams, Set<String> pks) {
        StringBuilder queryBuilder = new StringBuilder(sqlParts.get(0)).append(" WHERE (");
        Iterable<List<String>> pkGroups = Iterables.partition(pks, MAX_COLLECTION_PARAMS_LIMIT);
        NamedParamBuilder paramBuilder = NamedParamBuilder.newBuilder();
        int i = 0;
        for (List<String> pk : pkGroups) {
            if (i > 0) {
                queryBuilder.append(" OR ");
            }
            String paramName = "pk" + i++;
            queryBuilder.append(alias).append(".id IN (:").append(paramName).append(")");
            paramBuilder.and(paramName, pk);
        }
        if (sqlParts.size() == 1) {
            queryBuilder.append(")");
        }
        if (sqlParts.size() > 1) {
            queryBuilder.append(") AND ").append(sqlParts.get(1));

        }
        if (sqlParts.size() > 2) {
            for (int index = 2; index < sqlParts.size(); index++) {
                queryBuilder.append(" WHERE ").append(sqlParts.get(index));
            }
        }

        return new JpqlQuery(queryBuilder.toString(), paramBuilder.and(originalParams).build());
    }


    /**
     * 添加排序
     *
     * @param query
     * @param sort
     * @return
     */
    public static String applySorting(String query, Sort sort) {
        checkArgument(!Strings.isNullOrEmpty(query), "query");

        if (null == sort || !sort.iterator().hasNext()) {
            return query;
        }

        StringBuilder builder = new StringBuilder(query);

        if (!ORDER_BY.matcher(query).matches()) {
            builder.append(" ORDER BY ");
        } else {
            builder.append(", ");
        }

        for (Order order : sort) {
            builder.append(getOrderClause(order)).append(", ");
        }

        builder.delete(builder.length() - 2, builder.length());

        return builder.toString();
    }

    public static String applyNativeSorting(String originalQuery, Sort sort) {
        checkArgument(!Strings.isNullOrEmpty(originalQuery), "originalQuery");

        if (sort == null || !(sort.iterator().hasNext())) {
            return originalQuery;
        }

        StringBuilder builder = new StringBuilder();
        for (Order order : sort) {
            builder.append(getOrderClause(order)).append(", ");
        }
        builder.delete(builder.length() - 2, builder.length());
        return String.format(NATIVE_ORDER_BY_SQL_FORMATTER, originalQuery, builder.toString());

    }


//    /**
//     * 创建JPQL查询
//     *
//     * @param entityType 要查询的对象
//     * @param filters    动态过滤条件
//     * @return 返回一个 {@link JpqlQuery}  对象
//     */
//    public static JpqlQuery createQuery(Class<?> entityType, Iterable<FreeFilter> filters) {
//        String jpql = "FROM " + entityType.getSimpleName() + " x ";
//        return createQuery(jpql, filters);
//    }

    /**
     * 创建 JPQL 查询
     *
     * @param entityType 要查询的对象
     * @param filters    动态过滤条件
     * @param orderBy    排序
     * @return 返回一个 {@link JpqlQuery}  对象
     */
    public static <T extends JpqlToken> JpqlQuery createQuery(Class<?> entityType, Iterable<T> filters, Sort orderBy) {
        String jpql = String.format(SELECT_QUERY_FORMATTER, entityType.getSimpleName());
        return createQuery(jpql, filters, orderBy);
    }

    /**
     * 创建用于查询个数 SELECT COUNT(X) .. 的 JPQL查询
     *
     * @param entityType 要查询的对象
     * @param filters    动态过滤条件
     * @return 返回一个 {@link JpqlQuery}  对象
     */
    public static <T extends JpqlToken> JpqlQuery createCountQuery(Class<?> entityType, Iterable<T> filters) {
        String jpql = String.format(COUNT_QUERY_FORMATTER, entityType.getSimpleName());
        return createQuery(jpql, filters);
    }

    /**
     * 创建用于查询 SELECT DISTINCT X .. 的 JPQL查询
     *
     * @param entityType 要查询的对象
     * @param parts      JPQL 部件
     * @return 返回一个 {@link JpqlQuery}  对象
     */
    public static <T extends JpqlToken> JpqlQuery createDistinctQuery(Class<?> entityType, Iterable<T> parts) {
        String jpql = String.format(SELECT_DISTINCT_QUERY_FORMATTER, entityType.getSimpleName());
        return createQuery(jpql, parts);
    }

    /**
     * 创建 JPQL查询
     *
     * @param beforeWhereJpql JPQL查询语句 WHERE 之前的部分。
     * @param filters         动态过滤条件
     * @return 返回一个 {@link JpqlQuery}  对象
     */
    public static <T extends JpqlToken> JpqlQuery createQuery(String beforeWhereJpql, T... filters) {
        return createQuery(beforeWhereJpql, Lists.newArrayList(filters));
    }


    public static <T extends JpqlToken> JpqlQuery createQuery(Class<?> entityType, Iterable<T> parts) {
        String jpql = String.format(SELECT_QUERY_FORMATTER, entityType.getSimpleName());
        return createQuery(jpql, parts);
    }


    /**
     * 创建 JPQL查询
     *
     * @param beforeWhereJpql JPQL查询语句 WHERE 之前的部分。
     * @param filters         动态过滤条件
     * @return 返回一个 {@link JpqlQuery}  对象
     */
    public static <T extends JpqlToken> JpqlQuery createQuery(String beforeWhereJpql, Iterable<T> filters) {
        return createQuery(beforeWhereJpql, filters, (Sort) null);
    }


    /**
     * 创建 JPQL查询
     *
     * @param beforeWhereJpql JPQL查询语句 WHERE 之前的部分。
     * @param filters         动态过滤条件
     * @param orderBy         排序
     * @return 返回一个 {@link JpqlQuery}  对象
     */
    public static <T extends JpqlToken> JpqlQuery createQuery(String beforeWhereJpql, Iterable<T> filters, Sort orderBy) {
        return createQuery(
                Iterables.concat(
                        toSelectPart(beforeWhereJpql),
                        filters,
                        toOrderByPart(orderBy)
                )
        );
    }


    public static <T extends JpqlToken> JpqlQuery createQuery(Iterable<? super T> parts) {
        Iterable<SelectToken> select = Iterables.filter(parts, SelectToken.class);
        Iterable<FreeFilter> where = Iterables.filter(parts, FreeFilter.class);
        Iterable<GroupByToken> groupBy = Iterables.filter(parts, GroupByToken.class);
        Iterable<HavingToken> having = Iterables.filter(parts, HavingToken.class);
        Iterable<OrderByToken> orderBy = Iterables.filter(parts, OrderByToken.class);
        return createQuery(select, where, groupBy, having, orderBy);
    }

    public static <T extends JpqlToken> JpqlQuery createCountQuery(Iterable<? super T> parts) {
        Iterable<SelectCountToken> select = Iterables.filter(parts, SelectCountToken.class);
        if (Iterables.isEmpty(select)) {
            return null;
        }
        Iterable<FreeFilter> where = Iterables.filter(parts, FreeFilter.class);
        Iterable<GroupByToken> groupBy = Iterables.filter(parts, GroupByToken.class);
        Iterable<HavingToken> having = Iterables.filter(parts, HavingToken.class);
        Iterable<OrderByToken> orderBy = Iterables.filter(parts, OrderByToken.class);
        return createQuery(select, where, groupBy, having, orderBy);
    }

    /**
     * 创建 JPQL查询
     *
     * @param select  JPQL查询语句 WHERE 之前的部分。
     * @param where   动态过滤条件
     * @param groupBy 分组条件
     * @param having  分组过滤条件
     * @param orderBy 排序
     * @return 返回一个 {@link JpqlQuery}  对象
     */
    public static JpqlQuery createQuery(Iterable<? extends JpqlToken> select,
                                        Iterable<? extends FreeFilter> where,
                                        Iterable<? extends GroupByToken> groupBy,
                                        Iterable<? extends HavingToken> having,
                                        Iterable<? extends OrderByToken> orderBy) {
        String beforeWhere = SELECT_JOINER.join(select);
        StringBuilder queryBuilder = new StringBuilder(beforeWhere);
        NamedParamBuilder paramBuilder = NamedParamBuilder.newBuilder();

        if (ObjectUtils.isNotEmpty(where)) {
            if (!beforeWhere.toUpperCase().contains(" WHERE ")) {
                queryBuilder.append(" WHERE (1=1)");
            }
            where = Iterables.filter(where, VALID_FREE_FILTER);
            for (FreeFilter filter : where) {
                applyFilter(filter, queryBuilder, paramBuilder);
            }
        }

        if (ObjectUtils.isNotEmpty(groupBy)) {
            queryBuilder.append(" GROUP BY ");
            queryBuilder.append("(").append(GROUP_BY_JOINER.join(groupBy)).append(") ");
        }

        if (ObjectUtils.isNotEmpty(having)) {
            queryBuilder.append(" HAVING (1=1)");
            having = Iterables.filter(having, VALID_HAVING_PART);
            for (HavingToken filter : having) {
                applyFilter(filter, queryBuilder, paramBuilder);
            }
        }

        if (ObjectUtils.isNotEmpty(orderBy)) {
            queryBuilder.append(" ORDER BY ");
            queryBuilder.append(ORDER_BY_JOINER.join(orderBy));
        }
        return new JpqlQuery(queryBuilder.toString(), paramBuilder.build());
    }


    /**
     * 使用 UNION ALL 合并两个 JQPL 的查询结果
     *
     * @param querys 要合并的 {@link JpqlQuery} 对象
     * @return 返回一个 {@link JpqlQuery}  对象
     */
    @Beta
    public static JpqlQuery union(JpqlQuery... querys) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM (");
        Map<String, Object> parameters = Maps.newHashMap();
        boolean unionNeeded = false;
        for (int i = 0; i < querys.length; i++) {
            if (unionNeeded) {
                queryBuilder.append(" UNION ALL ");
            }
            queryBuilder.append(querys[i].getJpqlString());
            unionNeeded = true;
            parameters.putAll(querys[i].getParameters());
        }
        queryBuilder.append(") union_all_result");

        return new JpqlQuery(queryBuilder.toString(), parameters);

    }

    private static void applyFilter(ParamToken filter, StringBuilder queryBuilder, NamedParamBuilder paramBuilder) {
        String name = filter.getName();
        Object val = filter.getValue();
        queryBuilder.append(" AND (");
        //如果名字为空或者值为空，表示无需添加参数
        if (StringUtils.isEmpty(name) || ObjectUtils.isEmpty(val)) {
            queryBuilder.append(filter.getWhere());
        } else if (val != null && val instanceof Iterable
            /*&& Iterables.size((Iterable) val) > MAX_COLLECTION_PARAMS_LIMIT  */) {
            applyLargeParameters(filter, queryBuilder, paramBuilder);
        } else {
            queryBuilder.append(filter.getWhere());
            paramBuilder.and(name, val);
        }
        queryBuilder.append(")");

    }

    /**
     * 处理大参数
     */
    private static void applyLargeParameters(ParamToken filter, StringBuilder queryBuilder, NamedParamBuilder paramBuilder) {
        String where = formatWhere(filter.getWhere());
        String originalName = ":" + filter.getName();
        Iterable filterValue = (Iterable) filter.getValue();
        Iterable<List<?>> valGroups = Iterables.partition(filterValue, MAX_COLLECTION_PARAMS_LIMIT);
        int i = 0;
        for (List<?> val : valGroups) {
            if (i > 0) {
                queryBuilder.append(" OR ");
            }
            String paramName = filter.getName() + i++;

            queryBuilder.append(" (")
                    .append(where.replace(originalName, ":" + paramName))
                    .append(")");
            paramBuilder.and(paramName, val);
        }
    }

    private static String formatWhere(String where) {
        return PARAMETER_JOINER.join(PARAMETER_SPLITTER.split(where));
    }


    /**
     * 提起出查询语句中的别名
     *
     * @param query JPQL字符串
     */
    private static String detectAlias(String query) {

        Matcher matcher = ALIAS_MATCH.matcher(query);

        return matcher.find() ? matcher.group(2) : null;
    }

    /**
     * Returns the aliases used for {@code left (outer) join}s.
     *
     * @param query
     * @return
     */
    private static Set<String> getOuterJoinAliases(String query) {

        Set<String> result = new HashSet<String>();
        Matcher matcher = LEFT_JOIN_PATTERN.matcher(query);

        while (matcher.find()) {

            String alias = matcher.group(3);
            if (org.springframework.util.StringUtils.hasText(alias)) {
                result.add(alias);
            }
        }

        return result;
    }

    /**
     * Returns the order clause for the given {@link Order}. Will prefix the clause with the given alias if the referenced
     * property refers to a join alias.
     *
     * @param order the order object to build the clause for.
     * @return
     */
    private static String getOrderClause(Order order) {
        String property = order.getProperty();
        String wrapped = order.isIgnoreCase() ? String.format("lower(%s)", property) : property;
        return String.format("%s %s", wrapped, toJpaDirection(order));
    }

    private static String toJpaDirection(Order order) {
        return order.getDirection().name().toLowerCase(Locale.US);
    }

    private static List<SelectToken> toSelectPart(String select) {
        List<SelectToken> selectTokens = Lists.newArrayList();
        if (StringUtils.isEmpty(select)) {
            return selectTokens;
        }
        selectTokens.add(new SelectToken(select));
        return selectTokens;
    }

    private static List<OrderByToken> toOrderByPart(Sort orderBy) {

        List<OrderByToken> orderByParts = Lists.newArrayList();
        if (orderBy == null) {
            return orderByParts;
        }
        for (Order ord : orderBy) {
            orderByParts.add(new OrderByToken(getOrderClause(ord)));
        }
        return orderByParts;
    }
}
