package com.kz.audit.constants.fanxing.order;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 订单状态枚举
 *
 * @author lb
 * @version 1.1
 * @date 2021-06-21 10:32
 */
public enum OrderStatusEnum {

    CANCEL_PAY(0, "支付取消"),
    UNPAID(1, "未支付"),
    WAIT_RECEIVE(2, "待接单"),
    RECEIVED(3, "已接单"),
    PREPARED(5, "备货完成"),
    TO_BE_DELIVERY(7, "等待取货"),
    DELIVERY(9, "配送中"),
    DELIVERY_CANCEL(11, "配送取消"),
    UNUSUAL_DELIVERY(13, "配送异常"),
    COMPLETED(15, "已完成"),
    CANCEL(17, "已取消");

    private static final Map<Integer, OrderStatusEnum> LOOKUP = new HashMap<>();

    static {
        Iterator i$ = EnumSet.allOf(OrderStatusEnum.class).iterator();

        while (i$.hasNext()) {
            OrderStatusEnum c = (OrderStatusEnum) i$.next();
            LOOKUP.put(c.status, c);
        }
    }

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 描述
     */
    private String description;

    OrderStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public static OrderStatusEnum getConstant(int status) {
        return LOOKUP.get(status);
    }

    /**
     * 获取状态码
     *
     * @return
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 获取描述
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

}
