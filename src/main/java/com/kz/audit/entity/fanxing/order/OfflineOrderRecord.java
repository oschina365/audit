package com.kz.audit.entity.fanxing.order;

import com.kz.audit.entity.db.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 线下订单明细
 *
 * @author wangxx
 * @email jmyinjg@163.com
 * @date 2021-12-18 14:28:38
 */
@Data
@Entity.Cache(region = "OfflineOrderRecord")
@ApiModel(value = "线下订单明细")
public class OfflineOrderRecord extends OrderEntity implements Serializable {

    public static final OfflineOrderRecord ME = new OfflineOrderRecord();

    @Override
    public String tableName() {
        return "s_offline_order_record";
    }

    /**
     * 商户号
     */
    @ApiModelProperty(value = "商户号", hidden = true)
    private String tenantCode;

    /**
     * 支付单号
     */
    @ApiModelProperty(value = "支付单号")
    private String payNumber;

    /**
     * 商品Id
     */
    @ApiModelProperty(value = "商品Id")
    private String goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private BigDecimal quantity;

    /**
     * 商品单价
     */
    @ApiModelProperty(value = "商品单价")
    private BigDecimal goodsPrice;

    /**
     * 商品总价
     */
    @ApiModelProperty(value = "商品总价")
    private BigDecimal goodsAmount;

}
