package com.kz.audit.entity.fanxing.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.kz.audit.entity.db.Entity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付订单
 */
@Data
@Entity.Cache(region = "PayOrder")
public class PayOrder extends OrderEntity implements Serializable {

    public static final PayOrder ME = new PayOrder();

    @Override
    public String tableName() {
        return "s_pay_order";
    }

    /**
     * 商户号
     */
    private String tenantCode;

    /**
     * 订单编码
     */
    @JSONField(name = "order_no")
    private String orderNo;

    /**
     * 支付单号
     */
    @JSONField(name = "pay_number")
    private String payNumber;

    /**
     * 渠道支付流水号
     */
    @JSONField(name = "transaction_id")
    private String transactionId;

    /**
     * 支付方式  1.微信支付 2.支付宝支付 3.余额支付 4.积分抵扣 5.线下支付
     */
    @JSONField(name = "pay_type")
    private Integer payType;

    /**
     * 支付状态 0.未支付 1.支付成功 2.支付失败 3.已取消
     */
    @JSONField(name = "pay_status")
    private Integer payStatus;

    /**
     * 实付金额
     */
    @JSONField(name = "pay_amount")
    private BigDecimal payAmount;

    /**
     * 应付金额
     */
    @JSONField(name = "payable_amount")
    private BigDecimal payableAmount;

    /**
     * 支付补贴金额
     */
    @JSONField(name = "subsidy_amount")
    private BigDecimal subsidyAmount;

    /**
     * 抵扣积分
     */
    @JSONField(name = "pay_integral")
    private BigDecimal payIntegral;

    /**
     * 券码
     */
    @JSONField(name = "coupon_id")
    private String couponId;

    /**
     * 支付来源 1 线上 2 线下
     */
    private Integer sourceType;

    /**
     * 终端编号
     */
    private String terminalId;

    /**
     * 门店Id
     */
    private String shopId;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 下单渠道
     */
    private Integer orderSource;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
