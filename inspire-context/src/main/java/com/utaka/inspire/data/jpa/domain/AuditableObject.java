/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.data.jpa.domain;

import com.google.common.base.MoreObjects;
import com.utaka.inspire.data.domain.StringAuditable;
import com.utaka.inspire.util.StringUtils;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * 支持审计及乐观锁控制字段的实体类。
 *
 * @author LANXE
 */
@MappedSuperclass
public class AuditableObject extends AbstractObject implements StringAuditable {

    private static final long serialVersionUID = 1L;

    @Version
    @Column(name = "version")
    private long version = 1;

    @CreatedBy
    @Column(length = 50, name = "created_by")
    private String createdBy;

    @CreatedDate
    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @LastModifiedBy
    @Column(length = 50, name = "last_modified_by")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedTime;

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
     * @see org.springframework.data.domain.Auditable#getCreatedTime()
     */
    public Optional<LocalDateTime> getCreatedTime() {
        return null == createdTime ? Optional.empty()
                : Optional.of(LocalDateTime.ofInstant(createdTime.toInstant(), ZoneId.systemDefault()));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Auditable#setCreatedTime(java.time.temporal.TemporalAccessor)
     */
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = Date.from(createdTime.atZone(ZoneId.systemDefault()).toInstant());
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
     * @see org.springframework.data.domain.Auditable#getLastModifiedTime()
     */
    public Optional<LocalDateTime> getLastModifiedTime() {
        return null == lastModifiedTime ? Optional.empty()
                : Optional.of(LocalDateTime.ofInstant(lastModifiedTime.toInstant(), ZoneId.systemDefault()));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Auditable#setLastModifiedTime(java.time.temporal.TemporalAccessor)
     */
    public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
        this.lastModifiedTime = Date.from(lastModifiedTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * audit this object.
     */
    @Override
    public void audit(@NonNull String auditor) {
        if (StringUtils.isEmpty(auditor)) {
            auditor = StringAuditable.DEFAULT_AUDITOR;
        }
        LocalDateTime now = LocalDateTime.now();
        if (this.isNew()) {
            this.setCreatedBy(auditor);
            this.setCreatedTime(now);
            this.setLastModifiedBy(auditor);
            this.setLastModifiedTime(now);
        } else {
            this.setLastModifiedBy(auditor);
            this.setLastModifiedTime(now);

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
                .add("createdTime", this.createdTime)
                .add("lastModifiedBy", this.lastModifiedBy)
                .add("lastModifiedTime", this.lastModifiedTime);

    }

    @Override
    protected void copy(final AbstractObject source) {
        super.copy(source);
        if (source instanceof AuditableObject) {
            AuditableObject ae = (AuditableObject) source;
            this.version = ae.version;
            this.createdBy = ae.createdBy;
            this.createdTime = ae.createdTime;
            this.lastModifiedBy = ae.lastModifiedBy;
            this.lastModifiedTime = ae.lastModifiedTime;
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
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(createdTime, that.createdTime) &&
                Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
                Objects.equals(lastModifiedTime, that.lastModifiedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), version, createdBy, createdTime, lastModifiedBy, lastModifiedTime);
    }
}
