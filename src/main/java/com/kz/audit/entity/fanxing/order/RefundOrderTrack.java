package com.kz.audit.entity.fanxing.order;

import com.alibaba.fastjson.annotation.JSONField;
import com.kz.audit.entity.db.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 退单轨迹表
 * 
 * @author leibo
 * @version 1.0
 * @date 2021-06-21 14:15:59
 */
@Data
@Entity.Cache(region = "RefundOrderTrack")
@ApiModel(value = "退单轨迹表")
public class RefundOrderTrack extends OrderEntity implements Serializable {

	public static final RefundOrderTrack ME = new RefundOrderTrack();

	@Override
	public String tableName() {
		return "s_refund_order_track";
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
	 * 变更前订单状态
	 */
	@ApiModelProperty(value="变更前订单状态")
	@JSONField(name = "before_status")
	private Integer beforeStatus;

	/**
	 * 当前订单状态
	 */
	@ApiModelProperty(value="当前订单状态")
	@JSONField(name = "refund_status")
	private Integer refundStatus;

	/**
	 * 备注
	 */
	@ApiModelProperty(value="备注")
	private String remark;

}
