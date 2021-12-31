package com.kz.audit.entity.fanxing.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 退单对象,订单详情页查询退单信息
 *
 * @author Xiekui
 * @version 1.1
 * @date 2021-07-01 18:13
 */
@Data
@ApiModel("退单对象,订单详情页查询退单信息")
public class RefundOrderInfoVO {

    /**
     * 退单号
     */
    @ApiModelProperty(value="退单号")
    private String refundOrderNo;

    /**
     * 申请金额
     */
    @ApiModelProperty(value="申请金额")
    private BigDecimal goodsAmount;

    /**
     * 实付退单金额
     */
    @ApiModelProperty(value="实付退单金额")
    private BigDecimal refundAmount;

    /**
     * 退单状态
     */
    @ApiModelProperty(value="退单状态")
    private Integer refundStatus;
}
