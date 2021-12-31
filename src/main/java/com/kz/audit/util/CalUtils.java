package com.kz.audit.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

/**
 * 计算工具类
 *
 * @author jmy
 * @email jmyinjg@163.com
 * @date 2019/11/25 9:45
 */
@UtilityClass
public class CalUtils {

    /**
     * kg转g
     *
     * @param weight
     * @return
     */
    public BigDecimal kgToG(BigDecimal weight) {
        if (null != weight) {
            return multiply(weight, new BigDecimal(1000));
        }
        return BigDecimal.ZERO;
    }

    /**
     * g转kg
     *
     * @param weight
     * @return
     */
    public BigDecimal gToKg(BigDecimal weight) {
        if (null != weight) {
            return divide(weight, 1000);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 元转分
     *
     * @param money
     * @return
     */
    public BigDecimal yuanToCent(BigDecimal money) {
        if (null != money) {
            return multiply(money, new BigDecimal(100));
        }
        return BigDecimal.ZERO;
    }

    /**
     * 分转元
     *
     * @param money
     * @return
     */
    public BigDecimal centToYuan(BigDecimal money) {
        if (null != money) {
            return divide(money, 100);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 除法
     *
     * @param a
     * @param bs
     * @return
     */
    public BigDecimal divide(BigDecimal a, BigDecimal... bs) {
        if (a == null) {
            a = BigDecimal.ZERO;
        }
        BigDecimal result = a;
        for (BigDecimal decimal : bs) {
            if (decimal == null || decimal.compareTo(BigDecimal.ZERO) == 0) {
                decimal = BigDecimal.ONE;
            }
            result = result.divide(decimal, 2, BigDecimal.ROUND_HALF_DOWN);
        }
        return result;
    }

    /**
     * 除法数值型
     *
     * @param a
     * @param b
     * @return
     */
    public BigDecimal divide(BigDecimal a, Integer b) {
        return divide(a, new BigDecimal(b));
    }

    /**
     * 除法字符串型
     *
     * @param a
     * @param b
     * @return
     */
    public BigDecimal divide(BigDecimal a, String b) {
        return divide(a, new BigDecimal(b));
    }

    /**
     * 乘法运算
     *
     * @param a
     * @param bs
     * @return
     */
    public BigDecimal multiply(BigDecimal a, BigDecimal... bs) {
        if (a == null) {
            a = BigDecimal.ZERO;
        }
        BigDecimal result = a;
        for (BigDecimal decimal : bs) {
            if (decimal == null) {
                decimal = BigDecimal.ONE;
            }
            result = result.multiply(decimal);
        }
        return result.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    /**
     * 减法计算
     *
     * @param a
     * @param bs
     * @return
     */
    public BigDecimal sub(BigDecimal a, BigDecimal... bs) {
        if (a == null) {
            a = BigDecimal.ZERO;
        }
        BigDecimal result = a;
        for (BigDecimal b : bs) {
            if (b == null) {
                b = BigDecimal.ZERO;
            }
            result = result.subtract(b);
        }
        return result;
    }

    /**
     * 加法计算
     *
     * @param a
     * @param bs
     * @return
     */
    public BigDecimal add(BigDecimal a, BigDecimal... bs) {
        if (a == null) {
            a = BigDecimal.ZERO;
        }
        BigDecimal result = a;
        for (BigDecimal b : bs) {
            if (b == null) {
                b = BigDecimal.ZERO;
            }
            result = result.add(b);
        }
        return result;
    }

    /**
     * 比较价格差异
     *
     * @param a
     * @param b
     * @return
     */
    public Boolean comparePrice(BigDecimal a, BigDecimal b) {
        return yuanToCent(a).intValue() == yuanToCent(b).intValue();
    }

    /**
     * 比较库存差异
     *
     * @param a
     * @param b
     * @return
     */
    public Boolean compareStock(BigDecimal a, BigDecimal b) {
        if (null == a || null == b) {
            return Boolean.TRUE;
        }
        return a.intValue() == b.intValue();
    }

    /**
     * 比较两个值是否发生变化
     *
     * @param a
     * @param b
     * @return
     */
    public Boolean compareChange(BigDecimal a, BigDecimal b) {
        if (null == a || null == b) {
            return Boolean.TRUE;
        }
        return a.compareTo(b) != 0;
    }

    /**
     * 取反
     *
     * @param a
     * @return
     */
    public BigDecimal negate(BigDecimal a) {
        if (a == null) {
            return BigDecimal.ZERO;
        }
        return a.negate();
    }

    /**
     * 取绝对值
     *
     * @param a
     * @return
     */
    public BigDecimal abs(BigDecimal a) {
        if (a == null) {
            return BigDecimal.ZERO;
        }
        return a.abs();
    }

    /**
     * 取负数
     *
     * @param a
     * @return
     */
    public BigDecimal minus(BigDecimal a) {
        if (a == null) {
            return BigDecimal.ZERO;
        }
        return abs(a).negate();
    }

    /**
     * 判断是否为零
     *
     * @param a
     * @return
     */
    public Boolean isZero(BigDecimal a) {
        return equal(a, BigDecimal.ZERO);
    }

    /**
     * 判断是否为小于零
     *
     * @param a
     * @return
     */
    public Boolean isLessZero(BigDecimal a) {
        return less(a, BigDecimal.ZERO);
    }

    /**
     * 判断是否为小于等于零
     *
     * @param a
     * @return
     */
    public Boolean isLessEqualZero(BigDecimal a) {
        return lessEqual(a, BigDecimal.ZERO);
    }

    /**
     * 判断是否大于零
     *
     * @param a
     * @return
     */
    public Boolean isGreatZero(BigDecimal a) {
        return great(a, BigDecimal.ZERO);
    }

    /**
     * 判断是否大于等于零
     *
     * @param a
     * @return
     */
    public Boolean isGreatEqualZero(BigDecimal a) {
        return greatEqual(a, BigDecimal.ZERO);
    }

    /**
     * 判断是否相等
     *
     * @param a
     * @return
     */
    public Boolean equal(BigDecimal a, BigDecimal b) {
        if (a == null) {
            return Boolean.FALSE;
        }
        return a.compareTo(b) == 0;
    }

    /**
     * 判断是否不相等
     *
     * @param a
     * @return
     */
    public Boolean notEqual(BigDecimal a, BigDecimal b) {
        if (a == null) {
            return Boolean.TRUE;
        }
        return a.compareTo(b) != 0;
    }

    /**
     * 判断是否小于
     *
     * @param a
     * @param b
     * @return
     */
    public Boolean less(BigDecimal a, BigDecimal b) {
        if (a == null) {
            return Boolean.FALSE;
        }
        return a.compareTo(b) < 0;
    }

    /**
     * 判断是否为小于等于
     *
     * @param a
     * @param b
     * @return
     */
    public Boolean lessEqual(BigDecimal a, BigDecimal b) {
        if (a == null) {
            return Boolean.FALSE;
        }
        return a.compareTo(b) <= 0;
    }

    /**
     * 判断是否大于
     *
     * @param a
     * @param b
     * @return
     */
    public Boolean great(BigDecimal a, BigDecimal b) {
        if (a == null) {
            return Boolean.FALSE;
        }
        return a.compareTo(b) > 0;
    }

    /**
     * 判断是否大于
     *
     * @param a
     * @param b
     * @return
     */
    public Boolean greatEqual(BigDecimal a, BigDecimal b) {
        if (a == null) {
            return Boolean.FALSE;
        }
        return a.compareTo(b) >= 0;
    }

}
