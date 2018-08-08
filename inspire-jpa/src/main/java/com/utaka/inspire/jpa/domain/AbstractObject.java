/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * 所有实体类的基类
 *
 * @author XINEN
 */
@MappedSuperclass
public abstract class AbstractObject implements Serializable {

    private static final long serialVersionUID = 8078550144051485413L;

    @Id
    @GeneratedValue(generator = "UUIDGen")
    @GenericGenerator(name = "UUIDGen", strategy = "uuid")
    @Column(name = "id", unique = true, length = 32, nullable = false)
    private String id = null;

    /**
     * default constructor
     */
    protected AbstractObject() {
        super();
    }

    /**
     * init the entity with id.
     *
     * @param id
     */
    protected AbstractObject(String id) {
        this();
        this.id = id;
    }

    /*
     * (non-Javadoc)
     *
     */
    public String getId() {

        return id;
    }

    /*
     * (non-Javadoc)
     *
     */
    public void setId(String id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     *
     */
    @JsonIgnore
    public boolean isNew() {
        return StringUtils.isEmpty(getId());

    }

    /*
     * transfor the @see java.util.Date to @see org.joda.time.DateTime
     */
    public DateTime toDateTime(final Date date) {
        return null == date ? null : new DateTime(date);
    }

    /*
     * transfor the @see org.joda.time.DateTime to @see java.util.Date
     */
    public Date toDate(final DateTime datetime) {
        return null == datetime ? null : datetime.toDate();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractObject)) {
            return false;
        }
        AbstractObject that = (AbstractObject) o;

        //保留先创建的两个对象不相等的语义
        return null == this.id ? false : Objects.equal(id, that.id);
//        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.toStringHelper().toString();

    }

    /**
     * 用于获取该对象的字符串表示形式，子类可通过重写此方法来更好地表述该对象。 使用方法为
     * {@code super.toStringHelper().add("propertyName", propertyValue);}
     */
    protected MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this.getClass()).add("id", this.getId());

    }

    /**
     * copy property from source;
     */
    protected void copy(final AbstractObject source) {
        this.id = source.id;
    }
}
