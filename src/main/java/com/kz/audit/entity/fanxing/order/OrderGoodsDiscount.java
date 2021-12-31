package com.kz.audit.entity.fanxing.order;

import com.kz.audit.entity.db.Entity;
import com.kz.audit.entity.fanxing.order.vo.AppSettleOrderGoodsVO;
import com.kz.audit.util.CalUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单优惠详情表
 * 
 * @author leibo
 * @email leibo
 * @date 2021-08-30 11:37:44
 */
@Data
@Entity.Cache(region = "OrderGoodsDiscount")
@ApiModel(value = "订单优惠详情表")
public class OrderGoodsDiscount extends OrderEntity implements Serializable {

	public static final OrderGoodsDiscount ME = new OrderGoodsDiscount();

	@Override
	public String tableName() {
		return "s_order_goods_discount";
	}

	/**
	 * 商户号
	 */
	@ApiModelProperty(value = "商户号", hidden = true)
	private String tenantCode;

	/**
	 * 门店id
	 */
	@ApiModelProperty(value = "门店id")
	private String shopId;

	/**
	 * 下单用户id
	 */
	@ApiModelProperty(value = "下单用户id")
	private String userId;

	/**
	 * 订单号
	 */
	@ApiModelProperty(value = "订单号")
	private String orderNo;

	/**
	 * 活动id
	 */
	@ApiModelProperty(value = "活动id")
	private String activityId;

	/**
	 * 活动类型
	 */
	@ApiModelProperty(value = "活动类型")
	private Integer activityType;

	/**
	 * 活动商品id
	 */
	@ApiModelProperty(value = "活动商品id")
	private String activityGoodsId;

	/**
	 * 商品id
	 */
	@ApiModelProperty(value = "商品id")
	private String goodsId;

	/**
	 * 商品编码
	 */
	@ApiModelProperty(value = "商品编码")
	private String skuCode;

	/**
	 * 商品原价
	 */
	@ApiModelProperty(value = "商品原价")
	private BigDecimal salePrice;

	/**
	 * 商品促销价
	 */
	@ApiModelProperty(value = "商品促销价")
	private BigDecimal promotePrice;

	/**
	 * 参与促销的商品数量
	 */
	@ApiModelProperty(value = "参与促销的商品数量")
	private BigDecimal quantity;

	/**
	 * 已退的商品数量
	 */
	@ApiModelProperty(value = "已退的商品数量")
	private BigDecimal refundQuantity;

	/**
	 * 活动优惠总金额
	 */
	@ApiModelProperty(value = "活动优惠总金额")
	private BigDecimal discAmount;

	/**
	 * 商家承担总金额
	 */
	@ApiModelProperty(value = "商家承担总金额")
	private BigDecimal shopRate;

	/**
	 * 平台承担金额
	 */
	@ApiModelProperty(value = "平台承担金额")
	private BigDecimal platformRate;

	/**
	 * 物流承担费用
	 */
	@ApiModelProperty(value = "物流承担费用")
	private BigDecimal logisticsRate;

	/**
	 * 券码
	 */
	@ApiModelProperty(value = "券码")
	private String couponId;

	/**
	 * 构建对象
	 *
	 * @param goods
	 */
	public void buildByGoods(AppSettleOrderGoodsVO goods) {
		this.shopId = goods.getShopId();
		this.goodsId = goods.getGoodsId();
		this.skuCode = goods.getSkuCode();
		this.quantity = goods.getQuantity();
		this.salePrice = CalUtils.yuanToCent(goods.getSalePrice());
		if (CalUtils.isZero(goods.getPromotePrice())) {
			this.promotePrice = this.salePrice;
		} else {
			this.promotePrice = CalUtils.yuanToCent(goods.getPromotePrice());
		}
	}

}
