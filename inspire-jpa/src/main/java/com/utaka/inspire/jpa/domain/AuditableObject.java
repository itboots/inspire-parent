/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.utaka.inspire.context.AuditorAware;
import com.utaka.inspire.util.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;

import javax.persistence.*;
import java.util.Date;

/**
 * User domain class that uses auditing functionality of Spring Data that can either be aquired
 * implementing {@link Auditable} or extend {@link Auditable}.
 *
 * @author lanxe
 */
@MappedSuperclass
public class AuditableObject extends AbstractObject implements AuditorAware {

    private static final long serialVersionUID = 1L;

    @Version
    @Column(name = "version")
    private long version = 1;

    @Column(length = 50, name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(length = 50, name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    /**
     * default constructor
     */
    protected AuditableObject() {
        super();
    }

    /**
     * init the entity with id.
     *
     * @param id
     */
    protected AuditableObject(String id) {
        super(id);
    }

    /**
     * @return the version
     */
    public long getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(long version) {
        this.version = version;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.domain.Auditable#getCreatedBy()
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.domain.Auditable#setCreatedBy(java.lang.Object)
     */
    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.domain.Auditable#getCreatedDate()
     */
    public DateTime getCreatedDate() {
        return toDateTime(createdDate);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.domain.Auditable#setCreatedDate(org.joda.time .DateTime)
     */
    public void setCreatedDate(final DateTime createdDate) {
        this.createdDate = toDate(createdDate);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.domain.Auditable#getLastModifiedBy()
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.domain.Auditable#setLastModifiedBy(java.lang .Object)
     */
    public void setLastModifiedBy(final String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.domain.Auditable#getLastModifiedDate()
     */
    public DateTime getLastModifiedDate() {

        return toDateTime(lastModifiedDate);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.domain.Auditable#setLastModifiedDate(org.joda .time.DateTime)
     */
    public void setLastModifiedDate(final DateTime lastModifiedDate) {

        this.lastModifiedDate = toDate(lastModifiedDate);
    }

    /**
     * audit this object.
     */
    @Override
    public void audit(String auditor) {
        if (StringUtils.isEmpty(auditor)) {
            auditor = AuditorAware.DEFAULT_AUDITOR;
        }

        if (this.isNew()) {
            DateTime now = DateTime.now();
            this.setCreatedBy(auditor);
            this.setCreatedDate(now);
            this.setLastModifiedBy(auditor);
            this.setLastModifiedDate(now);
        } else {
            this.setLastModifiedBy(auditor);
            this.setLastModifiedDate(DateTime.now());

        }
    }

    /**
     * 用于获取该对象的字符串表示形式，子类可通过重写此方法来更好地表述该对象。 使用方法为
     * {@code super.toStringHelper().add("propertyName", propertyValue);}
     */
    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("version", this.version)
                .add("createdBy", this.createdBy)
                .add("createdDate", this.createdDate)
                .add("lastModifedBy", this.lastModifiedBy)
                .add("lastModifiedDate", this.lastModifiedDate);

    }

    @Override
    protected void copy(final AbstractObject source) {
        super.copy(source);
        if (source instanceof AuditableObject) {
            AuditableObject ae = (AuditableObject) source;
            this.version = ae.version;
            this.createdBy = ae.createdBy;
            this.createdDate = ae.createdDate;
            this.lastModifiedBy = ae.lastModifiedBy;
            this.lastModifiedDate = ae.lastModifiedDate;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuditableObject)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AuditableObject that = (AuditableObject) o;
        return version == that.version &&
                Objects.equal(createdBy, that.createdBy) &&
                Objects.equal(createdDate, that.createdDate) &&
                Objects.equal(lastModifiedBy, that.lastModifiedBy) &&
                Objects.equal(lastModifiedDate, that.lastModifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), version, createdBy, createdDate, lastModifiedBy, lastModifiedDate);
    }
}
