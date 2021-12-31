package com.kz.audit.entity.fanxing.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.kz.audit.entity.db.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退款单
 *
 * @author leibo
 * @version 1.1
 * @date 2021-07-01 14:44:33
 */
@Data
@Entity.Cache(region = "RefundPayOrder")
@ApiModel(value = "退款单")
public class RefundPayOrder extends OrderEntity implements Serializable {

    public static final RefundPayOrder ME = new RefundPayOrder();

    @Override
    public String tableName() {
        return "s_refund_pay_order";
    }

    /**
     * 商户号
     */
    @ApiModelProperty(value = "商户号", hidden = true)
    private String tenantCode;

    /**
     * 微信支付流水号
     */
    @ApiModelProperty(value = "支付流水号")
    private String transactionId;

    /**
     * 微信退款流水号
     */
    @ApiModelProperty(value = "退款流水号")
    private String refundId;

    /**
     * 订单编码
     */
    @ApiModelProperty(value = "订单编码")
    @JSONField(name = "order_no")
    private String orderNo;

    /**
     * 退单编码
     */
    @ApiModelProperty(value = "退单编码")
    @JSONField(name = "refund_order_no")
    private String refundOrderNo;

    /**
     * 退款单流水号
     */
    @ApiModelProperty(value = "退单流水号")
    @JSONField(name = "pay_number")
    private String payNumber;

    /**
     * 退款方式  1.微信支付 2.支付宝支付 3.余额支付 4.积分抵扣 5.线下支付
     */
    @ApiModelProperty(value = "支付方式  1.微信支付 2.支付宝支付 3.余额支付 4.积分抵扣 5.线下支付")
    @JSONField(name = "pay_type")
    private Integer payType;

    /**
     * 退款状态 4.申请退款 5.退款成功 6.退款失败
     */
    @ApiModelProperty(value = "4.申请退款 5.退款成功 6.退款失败")
    @JSONField(name = "pay_status")
    private Integer payStatus;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "实退金额")
    @JSONField(name = "pay_amount")
    private BigDecimal payAmount;

    /**
     * 应付金额
     */
    @ApiModelProperty(value = "应付金额")
    @JSONField(name = "payable_amount")
    private BigDecimal payableAmount;

    /**
     * 支付补贴金额
     */
    @ApiModelProperty(value = "支付补贴金额")
    @JSONField(name = "subsidy_amount")
    private BigDecimal subsidyAmount;

    /**
     * 抵扣积分
     */
    @ApiModelProperty(value = "抵扣积分")
    @JSONField(name = "pay_integral")
    private BigDecimal payIntegral;

}
