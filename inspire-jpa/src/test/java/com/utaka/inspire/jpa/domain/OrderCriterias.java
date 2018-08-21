/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

/**
 *
 */
package com.utaka.inspire.jpa.domain;

import com.utaka.inspire.jpa.annotation.*;
import com.utaka.inspire.util.DateTimeUtils;

import java.util.Date;
import java.util.Set;

public class OrderCriterias extends AbstractCriteria {

    public static final String COUNT_BY_FREE_FILTER = "SELECT COUNT(distinct ord.id) FROM Order ord";

    @OrderByPart
    public String orderByCreatedDate = "ord.createdDate";

    @SelectPart("SELECT ord FROM Order ord")
    public Boolean select = Boolean.TRUE;

    @SelectCountPart("SELECT count(ord) FROM Order ord")
    public Boolean count = Boolean.TRUE;

    @GroupByPart("ord.paymentRef")
    public Boolean groupBy = Boolean.TRUE;

    @FilterPart(where = "ord.deleted = :deleted")
    public Boolean deleted = Boolean.FALSE;

    @FilterPart(where = "ord.paymentRef=:paymentRef")
    public String paymentRef;

    @FilterPart(where = "ord.paymentRef LIKE :query OR ord.name LIKE :query", pattern = FilterPart.MatchPattern.FullText)
    public String query;


    @FilterPart(where = "ord.businessRef=:businessRef")
    public String businessRef;

    @FilterPart(where = "ord.createdDate >= :createdDateBegin")
    public Date createdDateBegin;

    @FilterPart(where = "ord.createdDate < :createdDateEnd")
    public Date createdDateEnd;

    @FilterPart(where = "ord.expiredTime >= :expiredTimeBegin")
    public Date expiredTimeBegin;

    @FilterPart(where = "ord.expiredTime < :expiredTimeEnd")
    public Date expiredTimeEnd;

    @FilterPart(where = "ord.paymentDate >= :paymentDateBegin")
    public Date paymentDateBegin;

    @FilterPart(where = "ord.paymentDate < :paymentDateEnd")
    public Date paymentDateEnd;

    @FilterPart(where = "ord.supplier = :supplier")
    public String supplier;

    @FilterPart(where = "ord.owner = :owner")
    public String owner;

    @FilterPart(where = "ord.name = :name")
    public String name;

    @FilterPart(where = "ord.name in (:nameSet)")
    public Set<String> nameSet;

    @FilterPart(where = "ord.refundedBy = :refundedBy")
    public String refundedBy;

    @FilterPart(where = "ord.refundmentDate >= :refundmentDateFrom")
    public Date refundmentDateFrom;

    @FilterPart(where = "ord.refundmentDate <= :refundmentDateTo")
    public Date refundmentDateTo;

    @HavingPart(having = "ord.having <= :refundmentDateTo")
    public Date having;


    public OrderCriterias createdDate(Date createdDateBegin, Date createdDateEnd) {
        this.createdDateBegin = createdDateBegin;
        this.createdDateEnd = createdDateEnd;
        return this;

    }


    @Override
    protected void setupCollect() {
        if (this.expiredTimeBegin != null) {
            this.expiredTimeBegin = DateTimeUtils.withTimeAtStartOfDay(this.expiredTimeBegin);
        }

        if (this.expiredTimeEnd != null) {
            this.expiredTimeEnd = DateTimeUtils.withTimeAtEndOfDay(this.expiredTimeEnd);
        }

        if (this.createdDateBegin != null) {
            this.createdDateBegin = DateTimeUtils.withTimeAtStartOfDay(this.createdDateBegin);
        }

        if (this.createdDateEnd != null) {
            this.createdDateEnd = DateTimeUtils.withTimeAtEndOfDay(this.createdDateEnd);
        }
        if (this.paymentDateBegin != null) {
            this.paymentDateBegin = DateTimeUtils.withTimeAtStartOfDay(this.paymentDateBegin);
        }

        if (this.paymentDateEnd != null) {
            this.paymentDateEnd = DateTimeUtils.withTimeAtEndOfDay(this.paymentDateEnd);
        }
        if (this.refundmentDateFrom != null) {
            this.refundmentDateFrom = DateTimeUtils.withTimeAtStartOfDay(this.refundmentDateFrom);
        }

        if (this.refundmentDateTo != null) {
            this.refundmentDateTo = DateTimeUtils.withTimeAtEndOfDay(this.refundmentDateTo);
        }
    }


}
