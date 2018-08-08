/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.data.jpa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * 所有实体类的基类
 *
 * @author LANXE
 */
@MappedSuperclass
public abstract class AbstractObject implements Persistable<String>, Serializable {

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
    @Override
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
    @Override
    @JsonIgnore
    @Transient
    public boolean isNew() {
        return Strings.isNullOrEmpty(getId());

    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Objects#equals(java.lang.Object)
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

        return null == this.id ? false : Objects.equals(id, that.id);
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
