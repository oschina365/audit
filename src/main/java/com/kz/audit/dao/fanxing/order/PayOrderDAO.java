package com.kz.audit.dao.fanxing.order;

import com.kz.audit.dao.CommonDao;
import com.kz.audit.entity.fanxing.order.PayOrder;

/**
 * @author kz
 * @since 2021-12-31 11:45:13
 */
public class PayOrderDAO extends CommonDao<PayOrder> {

    public static final PayOrderDAO ME = new PayOrderDAO();

    @Override
    protected String databaseName() {
        return "fanxing";
    }

    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }


}
