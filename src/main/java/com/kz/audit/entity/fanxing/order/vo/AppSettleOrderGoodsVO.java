package com.kz.audit.entity.fanxing.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lb
 * @version 1.1
 * @date 2021-07-10 15:47
 */
@Data
@NoArgsConstructor
@ApiModel("结算单商品信息")
public class AppSettleOrderGoodsVO implements Serializable {

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private String goodsId;

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
     * 商家商品编码
     */
    @ApiModelProperty("商家商品编码")
    private String skuCode;

    /**
     * 商品条码
     */
    @ApiModelProperty("商品条码")
    private String barcode;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String name;

    /**
     * 商品数量
     */
    @ApiModelProperty("商品数量")
    private BigDecimal quantity;

    /**
     * 商品总重量
     */
    @ApiModelProperty("商品重量")
    private BigDecimal weight;

    /**
     * 商品主图
     */
    @ApiModelProperty("商品主图")
    private String primaryPicUrl;

    /**
     * 商品原价
     */
    @ApiModelProperty("商品原价")
    private BigDecimal salePrice;

    /**
     * 商品促销价 0表示没有促销
     */
    @ApiModelProperty("商品促销价 0表示没有促销")
    private BigDecimal promotePrice;

    /**
     * 促销信息
     */
    @ApiModelProperty("促销信息")
    private String promoteId;

    /**
     * 会员价
     */
    @ApiModelProperty("会员价")
    private BigDecimal vipPrice;

    /**
     * 商品总售价
     */
    @ApiModelProperty
    private BigDecimal sellAmount;

    /*public AppSettleOrderGoodsVO(Goods goods, BigDecimal quantity) {
        this.goodsId = goods.getId();
        this.shopId = goods.getShopId();
        this.skuCode = goods.getSkuCode();
        this.name = goods.getName();
        this.quantity = quantity;
        this.primaryPicUrl = goods.getPrimaryPicUrl();
        this.stockShopId = goods.getStockShopId();
        this.weight = goods.getWeight();
        this.barcode = goods.getBarcode();
        this.salePrice = goods.getSalePrice();
        this.promotePrice = goods.getPromotePrice();
        this.promoteId = goods.getPromoteId();
    }*/

}
