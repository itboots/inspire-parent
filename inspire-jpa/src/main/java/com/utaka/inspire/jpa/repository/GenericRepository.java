/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * JPA specific extension of {@link org.springframework.data.jpa.repository.JpaRepository}.
 * <p/>
 * <pre>
 *     通过方法名字定义对应的数据库操作；包括select，delete，update操作。
 *     select:
 *          ReturnType(Collection|Slice|Page|Domain) findByXXXX(xxx)
 *     delete:
 *          @Modifying
 *          int deleteByXXX(xxx)
 *     update:
 *          @Modifying
 *          int updateByXXX(xxx)
 * </pre>
 * <p/>
 * <pre>
 * Distinct     --- 等价于 SQL 中的 distinct 关键字，比如 findDistinctByUsernameAndPassword(String user, Striang pwd);
 * And          --- 等价于 SQL 中的 and 关键字，比如 findByUsernameAndPassword(String user, Striang pwd);
 * Or           --- 等价于 SQL 中的 or 关键字，比如 findByUsernameOrAddress(String user, String addr);
 * Is,Equals    --- 比如 findByFirstname,findByFirstnameIs,findByFirstnameEquals
 * Between      --- 等价于 SQL 中的 between 关键字，比如 findBySalaryBetween(int max, int min);
 * LessThan     --- 等价于 SQL 中的 "<"，比如 findBySalaryLessThan(int max);
 * GreaterThan  --- 等价于 SQL 中的">"，比如 findBySalaryGreaterThan(int min);
 * IsNull       --- 等价于 SQL 中的 "is null"，比如 findByUsernameIsNull();
 * IsNotNull    --- 等价于 SQL 中的 "is not null"，比如 findByUsernameIsNotNull();
 * NotNull      --- 与 IsNotNull 等价;
 * Like         --- 等价于 SQL 中的 "like"，比如 findByUsernameLike(String user);
 * NotLike      --- 等价于 SQL 中的 "not like"，比如 findByUsernameNotLike(String user);
 * StartingWith --- 比如 findByFirstnameStartingWith(String name);
 * OrderBy      --- 等价于 SQL 中的 "order by"，比如 findByUsernameOrderBySalaryAsc(String user);
 * Not          --- 等价于 SQL 中的 "！ ="，比如 findByUsernameNot(String user);
 * In           --- 等价于 SQL 中的 "in"，比如 findByUsernameIn(Collection<String> userList),方法的参数可以是 Collection 类型，也可以是数组或者不定长参数;
 * NotIn        --- 等价于 SQL 中的 "not in"，比如 findByUsernameNotIn(Collection<String> userList),方法的参数可以是 Collection 类型，也可以是数组或者不定长参数;
 * </pre>
 *
 * @param <T> 处理的{@code domain}的类型。
 * @author XINEN
 */
@NoRepositoryBean
public interface GenericRepository<T>
        extends JpaRepository<T, String>, JpaSpecificationExecutor<T>, QueryRepository {

}
