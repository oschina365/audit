package com.kz.audit.entity.fanxing.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.kz.audit.entity.db.Entity;
import com.kz.audit.entity.fanxing.order.vo.AppSettleOrderGoodsVO;
import com.kz.audit.util.CalUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单商品表
 *
 * @author dingchengyu
 * @email jmyinjg@163.com
 * @date 2021-06-19 14:12:42
 */
@Data
@Entity.Cache(region = "OrderGoods")
@NoArgsConstructor
@ApiModel(value = "订单商品表")
public class OrderGoods extends OrderEntity implements Serializable {

    public static final OrderGoods ME = new OrderGoods();

    @Override
    public String tableName() {
        return "s_order_goods";
    }

    /**
     * 商户号
     */
    @ApiModelProperty(value = "商户号")
    private String tenantCode;

    /**
     * 门店id(下单门店)
     */
    @ApiModelProperty(value = "门店id(下单门店)")
    private String shopId;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private String orderId;

    /**
     * 订单编码
     */
    @ApiModelProperty(value = "订单编码")
    @JSONField(name = "order_no")
    private String orderNo;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    @JSONField(name = "goods_id")
    private String goodsId;

    /**
     * 商家商品编码
     */
    @ApiModelProperty(value = "商家商品编码")
    @JSONField(name = "sku_code")
    private String skuCode;

    /**
     * 商品条码
     */
    @ApiModelProperty(value = "商品条码")
    private String barcode;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    @JSONField(name = "goods_name")
    private String goodsName;

    /**
     * 商品主图
     */
    @ApiModelProperty(value = "商品主图")
    private String photo;

    /**
     * 商品总重量
     */
    @ApiModelProperty(value = "商品总重量")
    private BigDecimal weight;

    /**
     * 售卖数量
     */
    @ApiModelProperty(value = "售卖数量")
    private BigDecimal quantity;

    /**
     * 商品原价
     */
    @ApiModelProperty(value = "商品原价")
    @JSONField(name = "original_price")
    private BigDecimal originalPrice;

    /**
     * 商品售价
     */
    @ApiModelProperty(value = "商品售价")
    @JSONField(name = "sell_price")
    private BigDecimal sellPrice;

    /**
     * 商品原价总额
     */
    @ApiModelProperty(value = "商品原价总额")
    @JSONField(name = "original_amount")
    private BigDecimal originalAmount;

    /**
     * 商品售价总额
     */
    @ApiModelProperty(value = "商品售价总额")
    @JSONField(name = "sell_amount")
    private BigDecimal sellAmount;

    /**
     * 优惠总金额
     */
    @ApiModelProperty(value = "优惠总金额")
    @JSONField(name = "disc_amount")
    private BigDecimal discAmount;

    /**
     * 商品优惠金额
     */
    @ApiModelProperty(value = "商品优惠金额")
    @JSONField(name = "goods_disc_amount")
    private BigDecimal goodsDiscAmount;

    /**
     * 整单优惠分摊
     */
    @ApiModelProperty(value = "整单优惠分摊")
    @JSONField(name = "whole_disc_amount")
    private BigDecimal wholeDiscAmount;

    /**
     * 已退货数量
     */
    @ApiModelProperty(value = "已退货数量")
    @JSONField(name = "refund_quantity")
    private BigDecimal refundQuantity;

    /**
     * 订单商品对应的活动分摊数组
     */
    @ApiModelProperty("订单商品对应的活动分摊数组")
    private List<OrderGoodsDiscount> discountList;

    public OrderGoods(AppSettleOrderGoodsVO appGoods) {
        this.shopId = appGoods.getShopId();
        this.goodsId = appGoods.getGoodsId();
        this.skuCode = appGoods.getSkuCode();
        this.barcode = appGoods.getBarcode();
        this.goodsName = appGoods.getName();
        this.photo = appGoods.getPrimaryPicUrl();
        this.weight = appGoods.getWeight();
        this.quantity = appGoods.getQuantity();
        this.originalPrice = CalUtils.yuanToCent(appGoods.getSalePrice());
        this.originalAmount = CalUtils.multiply(this.originalPrice, this.quantity);
        if (CalUtils.isZero(appGoods.getPromotePrice())) {
            this.sellPrice = this.originalPrice;
        } else {
            this.sellPrice = CalUtils.yuanToCent(appGoods.getPromotePrice());
        }
        this.sellAmount = CalUtils.multiply(this.sellPrice, this.quantity);
        this.refundQuantity = BigDecimal.ZERO;
    }

}
