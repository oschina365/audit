package com.kz.audit.entity.fanxing.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.kz.audit.constants.fanxing.order.OrderStatusEnum;
import com.kz.audit.entity.db.Entity;
import com.kz.audit.entity.fanxing.order.vo.AppSettleOrderVO;
import com.kz.audit.entity.fanxing.order.vo.RefundOrderInfoVO;
import com.kz.audit.util.CalUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单头表
 *
 * @author dingchengyu
 * @date 2021-06-18 17:05:52
 */
@Data
@Entity.Cache(region = "Order")
@ApiModel(value = "订单头表")
public class Order extends OrderEntity implements Serializable {

    public static final Order ME = new Order();

    @Override
    public String tableName() {
        return "s_order";
    }

    /**
     * 商户号
     */
    @ApiModelProperty(value = "商户号", hidden = true)
    private String tenantCode;

    /**
     * 下单渠道
     */
    @ApiModelProperty(value = "订单来源")
    @JSONField(name = "order_source")
    private Integer orderSource;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    @JSONField(name = "order_no")
    private String orderNo;

    /**
     * 门店id(下单门店)
     */
    @ApiModelProperty(value = "门店id(下单门店)")
    @JSONField(name = "shop_id")
    private String shopId;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    @JSONField(name = "user_id")
    private String userId;

    /**
     * 是否可以整单退 1 可以退货 0不能退货
     */
    @JSONField(name = "can_return")
    private Integer canReturn;

    /**
     * 是否可以部分退 1 可以退货 0不能退货
     */
    @JSONField(name = "can_part_return")
    private Integer canPartReturn;

    /**
     * 下单门店名称
     */
    @ApiModelProperty("下单门店名称")
    @JSONField(name = "shop_name")
    private String shopName;

    /**
     * 订单类型 1.普通订单 2.预定订单 3.预售订单
     */
    @ApiModelProperty(value = "订单类型 1.普通订单 2.预定订单 3.预售订单")
    @JSONField(name = "order_type")
    private Integer orderType;

    /**
     * 履约方式 1.自提 2.商家自送 3.三方配送 4.物流
     */
    @ApiModelProperty(value = "履约方式 1.自提 2.商家自送 3.三方配送 4.物流")
    @JSONField(name = "performance_type")
    private Integer performanceType;

    /**
     * 订单序号
     */
    @ApiModelProperty(value = "订单序号")
    @JSONField(name = "order_seq")
    private Integer orderSeq;

    /**
     * 库存门店id
     */
    @ApiModelProperty(value = "库存门店id")
    private String stockShopId;

    /**
     * 库存门店名称
     */
    @ApiModelProperty("库存门店名称")
    private String stockShopName;

    /**
     * 履约门店id
     */
    @ApiModelProperty(value = "履约门店id")
    private String pickupShopId;

    /**
     * 履约门店名称
     */
    @ApiModelProperty("履约门店名称")
    private String pickupShopName;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    @JSONField(name = "order_status")
    private Integer orderStatus;

    /**
     * 订单备注信息
     */
    @ApiModelProperty(value = "订单备注信息")
    @JSONField(name = "order_remark")
    private String orderRemark;

    /**
     * 期望送达开始时间
     */
    @ApiModelProperty(value = "期望送达开始时间")
    @JSONField(name = "expect_start_time")
    private Date expectStartTime;

    /**
     * 期望送达结束时间
     */
    @ApiModelProperty(value = "期望送达结束时间")
    @JSONField(name = "expect_end_time")
    private Date expectEndTime;

    /**
     * SKU总数量
     */
    @ApiModelProperty(value = "SKU总数量")
    @JSONField(name = "sku_count")
    private Integer skuCount;

    /**
     * 商品总数量
     */
    @ApiModelProperty(value = "商品总数量")
    @JSONField(name = "goods_count")
    private BigDecimal goodsCount;

    /**
     * 商品总重量
     */
    @ApiModelProperty(value = "商品总重量")
    @JSONField(name = "goods_weight")
    private BigDecimal goodsWeight;

    /**
     * 商品总价
     */
    @ApiModelProperty(value = "商品总价")
    @JSONField(name = "goods_amount")
    private BigDecimal goodsAmount;

    /**
     * 商品预售金额
     */
    @ApiModelProperty(value = "商品预售金额")
    @JSONField(name = "pre_sale_amount")
    private BigDecimal preSaleAmount;

    /**
     * 商品预售尾款
     */
    @ApiModelProperty(value = "商品预售尾款")
    @JSONField(name = "pre_sale_balance")
    private BigDecimal preSaleBalance;

    /**
     * 商品分销总金额
     */
    @ApiModelProperty(value = "商品分销总金额")
    @JSONField(name = "goods_retail_amount")
    private BigDecimal goodsRetailAmount;

    /**
     * 打包费
     */
    @ApiModelProperty(value = "打包费")
    @JSONField(name = "package_amount")
    private BigDecimal packageAmount;

    /**
     * 配送费
     */
    @ApiModelProperty(value = "配送费")
    @JSONField(name = "delivery_amount")
    private BigDecimal deliveryAmount;

    /**
     * 总优惠金额
     */
    @ApiModelProperty(value = "总优惠金额")
    @JSONField(name = "discount_amount")
    private BigDecimal discountAmount;

    /**
     * 配送优惠金额
     */
    @ApiModelProperty(value = "配送优惠金额")
    @JSONField(name = "delivery_dis_amount")
    private BigDecimal deliveryDiscAmount;

    /**
     * 打包优惠金额
     */
    @ApiModelProperty(value = "打包优惠金额")
    @JSONField(name = "package_disc_amount")
    private BigDecimal packageDiscAmount;

    /**
     * 商品优惠金额
     */
    @ApiModelProperty(value = "商品优惠金额")
    @JSONField(name = "goods_disc_amount")
    private BigDecimal goodsDiscAmount;

    /**
     * 整单优惠金额
     */
    @ApiModelProperty(value = "整单优惠金额")
    @JSONField(name = "whole_disc_amount")
    private BigDecimal wholeDiscAmount;

    /**
     * 实付金额
     */
    @ApiModelProperty(value = "实付金额")
    @JSONField(name = "pay_amount")
    private BigDecimal payAmount;

    /**
     * 佣金
     */
    @ApiModelProperty(value = "佣金")
    private BigDecimal commission;

    /**
     * 订单效期
     */
    @ApiModelProperty(value = "订单效期")
    private Date orderValidity;

    /**
     * 售后效期
     */
    @ApiModelProperty(value = "售后效期")
    private Date salesValidity;

    /**
     * 删除标记  0.正常(默认)  1.删除(app端不展示)
     */
    @ApiModelProperty(value = "删除标记  0.正常(默认)  1.删除(app端不展示)")
    private Integer isDelete;

    /**
     * 退单状态是否展示正向单  0.展示  1.不展示
     */
    @ApiModelProperty(value = "退单状态是否展示正向单  0.展示  1.不展示")
    private Integer refundStatus;

    /**
     * 版本控制字段
     */
    @ApiModelProperty("版本控制字段")
    private Integer version;

    /**
     * 订单商品信息
     */
    @ApiModelProperty("订单商品信息")
    @JSONField(name = "goods_list")
    private List<OrderGoods> goodsList;

    /**
     * 支付方式
     */
    @ApiModelProperty("支付方式")
    @JSONField(name = "pay_order_list")
    private List<PayOrder> payOrderList;

    /**
     * 订单扩展信息
     */
    @ApiModelProperty("订单扩展信息")
    private OrderExtra extra;

    /**
     * 订单轨迹列表
     */
    @ApiModelProperty("订单轨迹列表")
    private List<OrderTrack> orderTrackList;

    /**
     * 订单对应的退单列表
     */
    @ApiModelProperty("订单对应的退单列表")
    private List<RefundOrderInfoVO> refundOrders;

    /**
     * 实际退款合计金额
     */
    @ApiModelProperty(value = "实际退款合计金额")
    private BigDecimal refundAmountTotal;

    /**
     * 下单时间
     */
    @ApiModelProperty("下单时间")
    @JSONField(name = "create_time")
    private Date createTime;

    public Order() {
        this.refundAmountTotal = BigDecimal.ZERO;
    }

    public Order(AppSettleOrderVO appOrder) {
        this.orderNo = appOrder.getOrderNo();
        this.shopId = appOrder.getShopId();
        this.stockShopId = appOrder.getStockShopId();
        this.pickupShopId = appOrder.getShopId();
        this.orderStatus = OrderStatusEnum.UNPAID.getStatus();
        this.skuCount = appOrder.getGoodsList().size();
        this.goodsCount = appOrder.getGoodsCount();
        this.goodsWeight = appOrder.getGoodsWeight();
        this.goodsAmount = CalUtils.yuanToCent(appOrder.getGoodsAmount());
        this.preSaleAmount = BigDecimal.ZERO;
        this.preSaleBalance = BigDecimal.ZERO;
        this.goodsRetailAmount = BigDecimal.ZERO;
    }

}
