package com.movietix.xiazihao.utils;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单号生成工具类
 */
public class OrderNoUtils {
    // 前缀部分
    private static final String ORDER_PREFIX = "ORD";

    // 日期格式
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    /**
     * 生成订单号
     * 格式: ORD + 年月日(8位) + 数据库的订单自增id(8位，不足补零)
     * 示例: ORD202304110001
     *
     * @return 生成的订单号
     */
    public static synchronized String generateOrderNo(Integer id) {
        // 获取当前日期部分
        String datePart = dateFormat.format(new Date());

        return ORDER_PREFIX + datePart + id;
    }

}
