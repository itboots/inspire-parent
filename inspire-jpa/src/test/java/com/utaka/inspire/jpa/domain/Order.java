/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */
package com.utaka.inspire.jpa.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 */
@Entity
@Table(name = "ord_order")
public class Order extends AbstractObject {
    private static final long serialVersionUID = 1L;

    /**
     * 产品描述
     */
    private String description;

    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 数量
     */
    private int qty = 1;
    /**
     * 支付总金额
     */
    private BigDecimal amount;

    private String currency;

    @Enumerated(EnumType.ORDINAL)
    private Status status = Status.None;

    @Enumerated(EnumType.ORDINAL)
    private PayStatus payStatus = PayStatus.None;

    private String paymentRef;
    private String payer;
    private int paymentMethod;

    private boolean deleted = false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredTime;

    /**
     * 已退款金额
     */
    private BigDecimal refundmentAmount = BigDecimal.ZERO;

    /**
     * 退款人
     */
    private String refundedBy;

    /**
     * 退款时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date refundmentDate;

    /**
     * 结算币别
     */
    private String refundmentCurrency = "CNY";

    private String owner;

    private String ownerName;
    private String supplier;
    private String partitionBy;
    private String remark;

    @Column(name = "pdt_product_id")
    private String productId;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public PayStatus getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(PayStatus payStatus) {
        this.payStatus = payStatus;
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigDecimal getRefundmentAmount() {
        return refundmentAmount;
    }

    public void setRefundmentAmount(BigDecimal refundmentAmount) {
        this.refundmentAmount = refundmentAmount;
    }

    public String getRefundedBy() {
        return refundedBy;
    }

    public void setRefundedBy(String refundedBy) {
        this.refundedBy = refundedBy;
    }

    public Date getRefundmentDate() {
        return refundmentDate;
    }

    public void setRefundmentDate(Date refundmentDate) {
        this.refundmentDate = refundmentDate;
    }

    public String getRefundmentCurrency() {
        return refundmentCurrency;
    }

    public void setRefundmentCurrency(String refundmentCurrency) {
        this.refundmentCurrency = refundmentCurrency;
    }

    /**
     * @return the payer
     */
    public String getPayer() {
        return payer;
    }

    /**
     * @param payer the payer to set
     */
    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPartitionBy() {
        return this.partitionBy;
    }

    public void setPartitionBy(String partitionBy) {
        this.partitionBy = partitionBy;
    }

    public void copy(final Order source) {
        super.copy(source);
        this.price = source.price;
        this.qty = source.qty;
        this.amount = source.amount;
        this.currency = source.currency;
        this.remark = source.remark;
        this.payStatus = source.payStatus;
        this.paymentRef = source.paymentRef;
        this.payer = source.payer;

        this.description = source.description;
        this.remark = source.remark;
        this.partitionBy = source.partitionBy;
        this.paymentDate = source.paymentDate;
        this.supplier = source.supplier;
        this.owner = source.owner;
        this.productId = source.productId;
    }


    public static Order newInstance() {
        return new Order();

    }

    public boolean isCanRefunding() {
        if (Order.PayStatus.DoublePay.equals(payStatus)) {
            return true;

        }
        return false;

    }

    public boolean isCanPaying() {
        return Order.PayStatus.None.equals(this.payStatus);
    }

    public boolean isCanCancelling() {
        return Order.PayStatus.None.equals(this.payStatus);

    }

    public enum Status {
        /**
         * 初始状态
         */
        None,

        /**
         * 已完成
         */
        Completed,

        /**
         * 已经关闭
         */
        Closed;

        public boolean equals(Status status) {
            return status.ordinal() == this.ordinal();
        }


    }

    public enum PayStatus {
        /**
         * 初始状态，待支付
         */
        None,

        /**
         * 已支付
         */
        Paid,
        /**
         * 退款中
         */
        Refunding,

        /**
         * 重复支付
         */
        DoublePay,

        /**
         * 退款成功
         */
        Refunded;

        public boolean equals(PayStatus other) {
            return this.ordinal() == other.ordinal();

        }

        public boolean equals(int other) {
            return this.ordinal() == other;

        }

    }
}
