package com.kz.audit.entity.fanxing.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author lb
 * @version 1.1
 * @date 2021-06-28 18:15
 */
@Data
@ApiModel("结算单")
public class AppSettleOrderVO implements Serializable {

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNo;

    /**
     * 订单来源
     */
    private Integer orderSource;

    /**
     * 门店id
     */
    @ApiModelProperty("门店id")
    private String shopId;

    /**
     * 库存门店id
     */
    @ApiModelProperty("库存门店id")
    private String stockShopId;

    /**
     * 门店名称
     */
    @ApiModelProperty("门店名称")
    private String shopName;

    /**
     * 商品原价金额
     */
    @ApiModelProperty("商品原价金额")
    private BigDecimal goodsAmount;

    /**
     * 订单实付金额
     */
    @ApiModelProperty("订单实付金额")
    private BigDecimal amount;

    /**
     * 商品总数量
     */
    @ApiModelProperty("商品总数量")
    private BigDecimal goodsCount;

    /**
     * 商品总重量
     */
    @ApiModelProperty("商品总重量")
    private BigDecimal goodsWeight;

    /**
     * 订单类型 1.普通订单 2.预定订单 3.预售订单
     */
    @ApiModelProperty(value = "订单类型 1.普通订单 2.预定订单 3.预售订单")
    private Integer OrderType;

    /**
     * 预售活动编码
     */
    @ApiModelProperty(value = "预售活动编码")
    private String activityCode;

    /**
     * 订单商品信息
     */
    @ApiModelProperty("订单商品信息")
    private List<AppSettleOrderGoodsVO> goodsList;

}
