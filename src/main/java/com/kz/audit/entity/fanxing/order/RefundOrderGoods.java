package com.kz.audit.entity.fanxing.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.kz.audit.entity.db.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退单商品表
 * 
 * @author leibo
 * @version 1.0
 * @date 2021-06-21 14:15:59
 */
@Data
@Entity.Cache(region = "RefundOrderGoods")
@ApiModel(value = "退单商品表")
public class RefundOrderGoods extends OrderEntity implements Serializable {

	public static final RefundOrderGoods ME = new RefundOrderGoods();

	@Override
	public String tableName() {
		return "s_refund_order_goods";
	}

	/**
	 * 商户号
	 */
	@ApiModelProperty(value="商户号")
	private String tenantCode;

	/**
	 * 退单编码
	 */
	@ApiModelProperty(value="退单编码")
	@JSONField(name = "refund_order_no")
	private String refundOrderNo;

	/**
	 * 商品id
	 */
	@ApiModelProperty(value="商品id")
	@JSONField(name = "goods_id")
	private String goodsId;

	/**
	 * 商家商品编码
	 */
	@ApiModelProperty(value="商家商品编码")
	@JSONField(name = "sku_code")
	private String skuCode;

	/**
	 * 商品条码
	 */
	@ApiModelProperty(value="商品条码")
	private String barcode;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value="商品名称")
	@JSONField(name = "goods_mame")
	private String goodsName;

	/**
	 * 商品主图
	 */
	@ApiModelProperty(value="商品主图")
	private String photo;

	/**
	 * 退商品总重量
	 */
	@ApiModelProperty(value="退商品总重量")
	private BigDecimal weight;

	/**
	 * 退商品数量
	 */
	@ApiModelProperty(value="退商品数量")
	private BigDecimal quantity;

	/**
	 * 商品原价
	 */
	@ApiModelProperty(value="商品原价")
	@JSONField(name = "original_price")
	private BigDecimal originalPrice;

	/**
	 * 退商品单价
	 */
	@ApiModelProperty(value="退商品单价")
	@JSONField(name = "refund_price")
	private BigDecimal refundPrice;

	/**
	 * 商品原价总额
	 */
	@ApiModelProperty(value="商品原价总额")
	@JSONField(name = "original_amount")
	private BigDecimal originalAmount;

	/**
	 * 退商品总金额
	 */
	@ApiModelProperty(value="退商品总金额")
	@JSONField(name = "refund_amount")
	private BigDecimal refundAmount;

	/**
	 * 退商品总优惠
	 */
	@ApiModelProperty(value="退商品总优惠")
	@JSONField(name = "disc_amount")
	private BigDecimal discAmount;

	/**
	 * 退商品优惠金额
	 */
	@ApiModelProperty(value="退商品优惠金额")
	@JSONField(name = "goods_disc_amount")
	private BigDecimal goodsDiscAmount;

	/**
	 * 退整单优惠金额
	 */
	@ApiModelProperty(value="退整单优惠金额")
	@JSONField(name = "whole_disc_amount")
	private BigDecimal wholeDiscAmount;

	/**
	 * 退单商品数据组装
	 *
	 * @param orderGoods
	 */
	public void buildByOrderGoods(OrderGoods orderGoods, String refundOrderNo){
		this.refundOrderNo = refundOrderNo;
		this.goodsId = orderGoods.getGoodsId();
		this.skuCode = orderGoods.getSkuCode();
		this.barcode = orderGoods.getBarcode();
		this.goodsName = orderGoods.getGoodsName();
		this.photo = orderGoods.getPhoto();
		this.originalPrice = orderGoods.getOriginalPrice();
		this.refundPrice = orderGoods.getSellPrice();
	}

	/**
	 * 组装整单退的商品数据组装
	 *
	 * @param orderGoods
	 * @param refundOrderNo
	 */
	public void buildCancelGoods(OrderGoods orderGoods, String refundOrderNo){
		this.buildByOrderGoods(orderGoods, refundOrderNo);
		this.weight = orderGoods.getWeight();
		this.quantity = orderGoods.getQuantity();
		this.originalAmount = orderGoods.getOriginalAmount();
		this.refundAmount = orderGoods.getSellAmount();
		this.discAmount = orderGoods.getDiscAmount();
		this.goodsDiscAmount = orderGoods.getGoodsDiscAmount();
	    this.wholeDiscAmount = orderGoods.getWholeDiscAmount();
	}

}
