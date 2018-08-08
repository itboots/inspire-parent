/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.base.Predicates.not;
import static com.utaka.inspire.util.Serializing.json;

/**
 * 动态过滤条件构造器器
 */
public abstract class AbstractCriteria {

    /**
     * 收集当前对象的过滤条件
     */
    public final List<FreeFilter> collect() {
        this.setupCollect();
        List<FreeFilter> ffs = Filters.collect(this);
        return this.afterCollect(ffs);

    }

    /**
     * 收集当前对象的过滤条件
     */
    public final List<JpqlToken> tokens() {
        this.setupCollect();
        List<JpqlToken> tokens = JpqlTokens.collect(this);
        return Lists.newArrayList(this.afterTokens(tokens));

    }

    /**
     * 开始收集过滤条件之前执行的方法。可根据form提交的值对生成的过滤条件进行设置。
     */
    protected abstract void setupCollect();

    /**
     * 收集并构建成{@link List <FreeFilter>}后的处理方法。
     */
    protected List<FreeFilter> afterCollect(
            List<FreeFilter> result) {
        return result;
    }

    /**
     * 收集并构建成{@link List <FreeFilter>}后的处理方法。
     */
    protected <T extends JpqlToken> Iterable<T> afterTokens(List<T> tokens) {
        return (Iterable<T>) Iterables.concat(
                afterCollect(Lists.newArrayList(Iterables.filter(tokens, FreeFilter.class))),
                Iterables.filter(tokens, not(instanceOf(FreeFilter.class)))


        );
    }


    @Override
    public String toString() {
        return json().toString(this);
    }

    /**
     * 从字符串反序列化为指定类型的对象
     *
     * @param requiredType 要返回的类型
     * @param content      {@code JSON}字符串
     * @param <T>          要返回的对象类型
     * @return 返回一个 T 类型的对象实例
     */
    public static final <T> T fromString(String content, Class<T> requiredType) {
        return json().toObject(content, requiredType);
    }

}
