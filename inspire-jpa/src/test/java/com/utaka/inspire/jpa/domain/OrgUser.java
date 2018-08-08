/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "ORG_USER", schema = "EEIR", catalog = "")
public class OrgUser {
    private String id;
    private String code;
    private Long sex;
    private Time birthday;
    private String createdBy;
    private Time createdDate;
    private String lastModifiedBy;
    private Time lastModifiedDate;
    private Long version;
    private String name;
    private String type;
    private String email;
    private String tel;
    private Long status;
    private String company;
    private String properties;
    private Boolean viewSealTips;
    private Long flag;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "SEX")
    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "BIRTHDAY")
    public Time getBirthday() {
        return birthday;
    }

    public void setBirthday(Time birthday) {
        this.birthday = birthday;
    }

    @Basic
    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "CREATED_DATE")
    public Time getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Time createdDate) {
        this.createdDate = createdDate;
    }

    @Basic
    @Column(name = "LAST_MODIFIED_BY")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Basic
    @Column(name = "LAST_MODIFIED_DATE")
    public Time getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Time lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Basic
    @Column(name = "VERSION")
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "TEL")
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Basic
    @Column(name = "STATUS")
    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Basic
    @Column(name = "COMPANY")
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Basic
    @Column(name = "PROPERTIES")
    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    @Basic
    @Column(name = "VIEW_SEAL_TIPS")
    public Boolean getViewSealTips() {
        return viewSealTips;
    }

    public void setViewSealTips(Boolean viewSealTips) {
        this.viewSealTips = viewSealTips;
    }

    @Basic
    @Column(name = "FLAG")
    public Long getFlag() {
        return flag;
    }

    public void setFlag(Long flag) {
        this.flag = flag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrgUser orgUser = (OrgUser) o;

        if (id != null ? !id.equals(orgUser.id) : orgUser.id != null) return false;
        if (code != null ? !code.equals(orgUser.code) : orgUser.code != null) return false;
        if (sex != null ? !sex.equals(orgUser.sex) : orgUser.sex != null) return false;
        if (birthday != null ? !birthday.equals(orgUser.birthday) : orgUser.birthday != null)
            return false;
        if (createdBy != null ? !createdBy.equals(orgUser.createdBy) : orgUser.createdBy != null)
            return false;
        if (createdDate != null ? !createdDate.equals(orgUser.createdDate) : orgUser.createdDate != null)
            return false;
        if (lastModifiedBy != null ? !lastModifiedBy.equals(orgUser.lastModifiedBy) : orgUser.lastModifiedBy != null)
            return false;
        if (lastModifiedDate != null ? !lastModifiedDate.equals(orgUser.lastModifiedDate) : orgUser.lastModifiedDate != null)
            return false;
        if (version != null ? !version.equals(orgUser.version) : orgUser.version != null)
            return false;
        if (name != null ? !name.equals(orgUser.name) : orgUser.name != null) return false;
        if (type != null ? !type.equals(orgUser.type) : orgUser.type != null) return false;
        if (email != null ? !email.equals(orgUser.email) : orgUser.email != null) return false;
        if (tel != null ? !tel.equals(orgUser.tel) : orgUser.tel != null) return false;
        if (status != null ? !status.equals(orgUser.status) : orgUser.status != null) return false;
        if (company != null ? !company.equals(orgUser.company) : orgUser.company != null)
            return false;
        if (properties != null ? !properties.equals(orgUser.properties) : orgUser.properties != null)
            return false;
        if (viewSealTips != null ? !viewSealTips.equals(orgUser.viewSealTips) : orgUser.viewSealTips != null)
            return false;
        if (flag != null ? !flag.equals(orgUser.flag) : orgUser.flag != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (sex != null ? sex.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (lastModifiedDate != null ? lastModifiedDate.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (tel != null ? tel.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        result = 31 * result + (viewSealTips != null ? viewSealTips.hashCode() : 0);
        result = 31 * result + (flag != null ? flag.hashCode() : 0);
        return result;
    }
}
