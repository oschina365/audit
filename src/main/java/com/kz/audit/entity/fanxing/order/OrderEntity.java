package com.kz.audit.entity.fanxing.order;

import com.kz.audit.entity.db.Entity;
import lombok.Data;

import java.io.Serializable;

/**
 * 支付订单
 */
@Data
@Entity.Cache(region = "PayOrder")
public class OrderEntity extends Entity implements Serializable {

    public static final OrderEntity ME = new OrderEntity();

    @Override
    public String databaseName() {
        return "fanxing";
    }

    @Override
    protected String schemaName() {
        return "fanxing_order";
    }


}
