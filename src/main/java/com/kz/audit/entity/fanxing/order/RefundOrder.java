package com.kz.audit.entity.fanxing.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kz.audit.entity.db.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 退单
 *
 * @author leibo
 * @version 1.1
 * @date 2021-06-21 11:49:03
 */
@Data
@Entity.Cache(region = "RefundOrder")
@ApiModel(value = "退单")
public class RefundOrder extends OrderEntity implements Serializable {

    public static final RefundOrder ME = new RefundOrder();

    @Override
    public String tableName() {
        return "s_refund_order";
    }

    /**
     * 商户号
     */
    @ApiModelProperty(value = "商户号", hidden = true)
    private String tenantCode;

    /**
     * 门店id(下单门店)
     */
    @ApiModelProperty(value = "门店id")
    @JSONField(name = "shop_id")
    private String shopId;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    @JSONField(name = "user_id")
    private String userId;

    /**
     * 门店名称
     */
    @ApiModelProperty("门店名称")
    private String shopName;

    /**
     * 原订单号
     */
    @ApiModelProperty(value = "原订单号")
    @JSONField(name = "order_no")
    private String orderNo;

    /**
     * 退单号
     */
    @ApiModelProperty(value = "退单号")
    @JSONField(name = "refund_order_no")
    private String refundOrderNo;

    /**
     * 退单方式 1.退款单 2.退货退款单
     */
    @ApiModelProperty(value = "退单方式 1.退款单 2.退货退款单")
    @JSONField(name = "refund_method")
    private Integer refundMethod;

    /**
     * 退单类型 1.整单退 2.部分退
     */
    @ApiModelProperty(value = "退单类型 1.整单退 2.部分退")
    @JSONField(name = "refund_type")
    private Integer refundType;

    /**
     * 退款类型，1称重退差 2过履约期 3工单退款 4用户申请  5缺货退款
     */
    @ApiModelProperty(value = "退款类型，1称重退差 2过履约期 3工单退款 4用户申请 5缺货退款")
    @JSONField(name = "drawback_type")
    private Integer drawbackType;

    /**
     * 退单序号
     */
    @ApiModelProperty(value = "退单序号")
    @JSONField(name = "refund_seq")
    private Integer refundSeq;

    /**
     * 渠道来源
     */
    @ApiModelProperty(value = "渠道来源")
    @JSONField(name = "refund_source")
    private Integer refundSource;

    /**
     * 操作来源 1 用户（需要审核） 2 商家
     */
    @ApiModelProperty(value = "操作来源 1 用户 2 商家")
    @JSONField(name = "operation_source")
    private Integer operationSource;

    /**
     * 库存门店id
     */
    @ApiModelProperty(value = "库存门店id")
    private String stockShopId;

    /**
     * 履约门店id
     */
    @ApiModelProperty(value = "履约门店id")
    private String pickShopId;

    /**
     * 退单状态
     */
    @ApiModelProperty(value = "退单状态")
    @JSONField(name = "refund_status")
    private Integer refundStatus;

    /**
     * 退单原因
     */
    @ApiModelProperty(value = "退单原因")
    @JSONField(name = "refund_reason")
    private String refundReason;

    /**
     * sku总数量
     */
    @ApiModelProperty(value = "sku总数量")
    @JSONField(name = "sku_count")
    private Integer skuCount;

    /**
     * 退单商品总数量
     */
    @ApiModelProperty(value = "退单商品总数量")
    @JSONField(name = "goods_count")
    private BigDecimal goodsCount;

    /**
     * 退单商品总重量
     */
    @ApiModelProperty(value = "退单商品总重量")
    @JSONField(name = "goods_weight")
    private BigDecimal goodsWeight;

    /**
     * 退单商品总价
     */
    @ApiModelProperty(value = "退单商品总价")
    @JSONField(name = "goods_amount")
    private BigDecimal goodsAmount;

    /**
     * 退单商品分销金额
     */
    @ApiModelProperty(value = "退单商品分销金额")
    @JSONField(name = "goods_retail_amount")
    private BigDecimal goodsRetailAmount;

    /**
     * 退单打包费
     */
    @ApiModelProperty(value = "退单打包费")
    @JSONField(name = "package_amount")
    private BigDecimal packageAmount;

    /**
     * 退单配送费
     */
    @ApiModelProperty(value = "退单配送费")
    @JSONField(name = "delivery_amount")
    private BigDecimal deliveryAmount;

    /**
     * 退单总优惠金额
     */
    @ApiModelProperty(value = "退单总优惠金额")
    @JSONField(name = "discount_amount")
    private BigDecimal discountAmount;

    /**
     * 退单配送优惠金额
     */
    @ApiModelProperty(value = "退单配送优惠金额")
    @JSONField(name = "delivery_disc_amount")
    private BigDecimal deliveryDiscAmount;

    /**
     * 退单打包优惠金额
     */
    @ApiModelProperty(value = "退单打包优惠金额")
    @JSONField(name = "package_disc_amount")
    private BigDecimal packageDiscAmount;

    /**
     * 退单商品优惠金额
     */
    @ApiModelProperty(value = "退单商品优惠金额")
    @JSONField(name = "goods_disc_amount")
    private BigDecimal goodsDiscAmount;

    /**
     * 退单整单优惠金额
     */
    @ApiModelProperty(value = "退单整单优惠金额")
    @JSONField(name = "whole_disc_amount")
    private BigDecimal wholeDiscAmount;

    /**
     * 实付退单金额
     */
    @ApiModelProperty(value = "实付退单金额")
    @JSONField(name = "refund_amount")
    private BigDecimal refundAmount;

    /**
     * 退单佣金
     */
    @ApiModelProperty(value = "退单佣金")
    @JSONField(name = "refund_commission")
    private BigDecimal refundCommission;

    /**
     * 退单明细信息
     */
    @ApiModelProperty("退单明细信息")
    @JSONField(name = "goods_list")
    private List<RefundOrderGoods> goodsList;

    /**
     * 退单支付方式
     */
    @ApiModelProperty("支付方式")
    @JSONField(name = "refund_pay_order_list")
    private List<RefundPayOrder> refundPayOrderList;

    /**
     * 支付方式  1.微信支付 2.支付宝支付 3.余额支付 4.积分抵扣 5.线下支付
     */
    @ApiModelProperty(value = "支付方式  1.微信支付 2.支付宝支付 3.余额支付 4.积分抵扣 5.线下支付")
    private Integer payType;

    /**
     * 开始时间
     */
    @JsonIgnore
    private String applyStartTime;

    /**
     * 结束时间
     */
    @JsonIgnore
    private String applyEndTime;

    /**
     * 备注/审核结果
     */
    @ApiModelProperty("备注/审核结果")
    private String remark;

    /**
     * 退单数据组装
     *
     * @param order
     */
    public void buildByOrder(Order order) {
        this.shopId = order.getShopId();
        this.orderNo = order.getOrderNo();
        this.refundSource = order.getOrderSource();
        this.stockShopId = order.getStockShopId();
        this.pickShopId = order.getPickupShopId();
    }

}
