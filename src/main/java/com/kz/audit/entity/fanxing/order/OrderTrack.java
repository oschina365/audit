package com.kz.audit.entity.fanxing.order;


import com.kz.audit.entity.db.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单轨迹表
 * 
 * @author leibo
 * @version 1.0
 * @date 2021-06-21 14:15:59
 */
@Data
@Entity.Cache(region = "OrderTrack")
@ApiModel(value = "订单轨迹表")
public class OrderTrack extends OrderEntity implements Serializable {

	public static final OrderTrack ME = new OrderTrack();

	@Override
	public String tableName() {
		return "s_order_track";
	}

	/**
	 * 商户号
	 */
	@ApiModelProperty(value="商户号")
	private String tenantCode;

	/**
	 * 订单编码
	 */
	@ApiModelProperty(value="订单编码")
	private String orderNo;

	/**
	 * 变更前订单状态
	 */
	@ApiModelProperty(value="变更前订单状态")
	private Integer beforeStatus;

	/**
	 * 当前订单状态
	 */
	@ApiModelProperty(value="当前订单状态")
	private Integer orderStatus;

	/**
	 * 备注
	 */
	@ApiModelProperty(value="备注")
	private String remark;

}
