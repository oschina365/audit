package com.kz.audit.entity.fanxing.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.kz.audit.entity.db.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单附加信息表
 *
 * @author leibo
 * @version 1.1
 * @date 2021-06-22 14:06:53
 */
@Data
@Entity.Cache(region = "Order")
@ApiModel(value = "订单附加信息表")
public class OrderExtra extends OrderEntity implements Serializable {

    public static final OrderExtra ME = new OrderExtra();

    @Override
    public String tableName() {
        return "s_order_extra";
    }

    /**
     * 商户号
     */
    @ApiModelProperty(value = "商户号")
    private String tenantCode;

    /**
     * 订单编码
     */
    @ApiModelProperty(value = "订单编码")
    @JSONField(name = "order_no")
    private String orderNo;

    /**
     * 小蜜蜂id
     */
    @ApiModelProperty(value = "小蜜蜂id")
    private String beeId;

    /**
     * 团长id
     */
    @ApiModelProperty(value = "团长id")
    private String commanderId;

    /**
     * 收货人姓名
     */
    @ApiModelProperty(value = "收货人姓名")
    @JSONField(name = "client_name")
    private String clientName;

    /**
     * 收货人电话
     */
    @ApiModelProperty(value = "收货人电话")
    @JSONField(name = "client_phone")
    private String clientPhone;

    /**
     * 收货人地址
     */
    @ApiModelProperty(value = "收货人地址")
    @JSONField(name = "client_address")
    private String clientAddress;

    /**
     * 客户备注信息
     */
    @ApiModelProperty(value = "客户备注信息")
    @JSONField(name = "client_remark")
    private String clientRemark;

    /**
     * 是否开发票 0.否 1.是
     */
    @ApiModelProperty(value = "是否开发票 0.否 1.是")
    @JSONField(name = "is_invoice")
    private Integer isInvoice;

    /**
     * 发票抬头类型  0.个人 1.企业普票 2.企业专票
     */
    @ApiModelProperty(value = "发票抬头类型  0.个人 1.企业普票 2.企业专票")
    @JSONField(name = "invoice_type")
    private Integer invoiceType;

    /**
     * 发票抬头
     */
    @ApiModelProperty(value = "发票抬头")
    @JSONField(name = "invoice_title")
    private String invoiceTitle;

    /**
     * 发票税号
     */
    @ApiModelProperty(value = "发票税号")
    @JSONField(name = "invoice_duty_no")
    private String invoiceDutyNo;

}
