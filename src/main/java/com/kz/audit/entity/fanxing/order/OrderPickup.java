package com.kz.audit.entity.fanxing.order;

import com.kz.audit.entity.db.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单自提信息表
 * 
 * @author leibo
 * @version 1.1
 * @date 2021-06-22 14:06:53
 */
@Data
@Entity.Cache(region = "OrderPickup")
@ApiModel(value = "订单自提信息表")
public class OrderPickup extends OrderEntity implements Serializable {

	public static final OrderPickup ME = new OrderPickup();

	@Override
	public String tableName() {
		return "s_order_pickup";
	}

	/**
	 * 商户号
	 */
	@ApiModelProperty(value="商户号", hidden = true)
	private String tenantCode;

	/**
	 * 订单编码
	 */
	@ApiModelProperty(value="订单编码")
	private String orderNo;

	/**
	 * 自提点id
	 */
	@ApiModelProperty(value="自提点id")
	private String pickupCenterId;

	/**
	 * 自提点名称
	 */
	@ApiModelProperty(value="自提点名称")
	private String pickupCenterName;

	/**
	 * 自提点地址
	 */
	@ApiModelProperty(value="自提点地址")
	private String pickupCenterAddress;

	/**
	 * 核验码
	 */
	@ApiModelProperty(value="核验码")
	private String verifyCode;

	/**
	 * 取货开始时间
	 */
	@ApiModelProperty(value="取货开始时间")
	private Date pickupStartTime;

	/**
	 * 取货结束时间
	 */
	@ApiModelProperty(value="取货结束时间")
	private Date pickupEndTime;

	/**
	 * 提取状态 0.未自提 1.已自提
	 */
	@ApiModelProperty(value="提取状态 0.未自提 1.已自提")
	private Integer status;

	/**
	 * 自提截止时间
	 */
	@ApiModelProperty(value="自提截止时间")
	private Date deadlineTime;

}
