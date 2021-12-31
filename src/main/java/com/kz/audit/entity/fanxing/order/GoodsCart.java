package com.kz.audit.entity.fanxing.order;

import com.kz.audit.entity.db.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车表
 *
 * @author leibo
 * @version 1.1
 * @date 2021-06-22 15:54:00
 */
@Data
@Entity.Cache(region = "GoodsCart")
@ApiModel(value = "购物车表")
public class GoodsCart extends OrderEntity implements Serializable {


    public static final GoodsCart ME = new GoodsCart();

    @Override
    public String tableName() {
        return "s_goods_cart";
    }

    /**
     * 商户号
     */
    @ApiModelProperty(value = "商户号")
    private String tenantCode;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 门店id
     */
    @ApiModelProperty(value = "门店id")
    private String shopId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private String goodsId;

    /**
     * 业态类型 1、商场 2、百货 3、云店
     */
    @ApiModelProperty(value = "业态类型 1、商场 2、百货 3、云店")
    private Integer type;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private BigDecimal quantity;

    /**
     * 商品选中状态
     */
    @ApiModelProperty(value = "商品选中状态 0 未选中  1 选中")
    private Integer checkStatus;

    /**
     * 库存门店id
     */
    @ApiModelProperty(value = "库存门店id")
    private String stockShopId;

    /**
     * 商家sku编码
     */
    @ApiModelProperty(value = "商家sku编码")
    private String skuCode;

    /**
     * 是否加锁 0 否 1 是
     */
    @ApiModelProperty(value = "是否加锁 0 否 1 是")
    private Integer isLock;

    /**
     * 是否过期 0 否 1 是（过期的要去掉选中状态）
     */
    @ApiModelProperty(value = "是否过期 0 否 1 是")
    private Integer isInvalid;

}
