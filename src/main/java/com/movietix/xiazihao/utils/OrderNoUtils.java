package com.movietix.xiazihao.utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 订单号生成工具类
 */
public class OrderNoUtils {
    // 前缀部分
    private static final String ORDER_PREFIX = "ORD";

    // 序列号部分（原子操作保证线程安全）
    private static final AtomicInteger sequence = new AtomicInteger(0);

    // 最大序列号（6位数）
    private static final int MAX_SEQUENCE = 999999;

    // 日期格式
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    /**
     * 生成订单号
     * 格式: ORD + 年月日(8位) + 序列号(4位，不足补零)
     * 示例: ORD202304110001
     *
     * @return 生成的订单号
     */
    public static synchronized String generateOrderNo() {
        // 获取当前日期部分
        String datePart = dateFormat.format(new Date());

        // 获取并递增序列号，达到最大值后重置
        int seq = sequence.incrementAndGet();
        if (seq > MAX_SEQUENCE) {
            sequence.set(0);
            seq = sequence.incrementAndGet();
        }

        // 格式化序列号为6位数字，不足补零
        String sequencePart = String.format("%06d", seq);

        return ORDER_PREFIX + datePart + sequencePart;
    }

    /**
     * 生成带年份的订单号
     * 格式: ORD + 年(2位) + 序列号(6位，不足补零)
     * 示例: ORD230001
     *
     * @return 生成的订单号
     */
    public static synchronized String generateShortOrderNo() {
        // 获取当前年份后两位
        String yearPart = new SimpleDateFormat("yy").format(new Date());

        // 获取并递增序列号
        int seq = sequence.incrementAndGet();
        if (seq > MAX_SEQUENCE) {
            sequence.set(0);
            seq = sequence.incrementAndGet();
        }

        // 格式化序列号为6位数字，不足补零
        String sequencePart = String.format("%06d", seq);

        return ORDER_PREFIX + yearPart + sequencePart;
    }

    /**
     * 根据用户ID生成订单号（适用于分布式系统）
     * 格式: ORD + 年月日(8位) + 用户ID后4位 + 序列号(2位)
     *
     * @param userId 用户ID
     * @return 生成的订单号
     */
    public static synchronized String generateOrderNoByUserId(long userId) {
        String datePart = dateFormat.format(new Date());

        // 获取用户ID后4位，不足补零
        String userIdPart = String.format("%04d", userId % 10000);

        // 获取并递增序列号
        int seq = sequence.incrementAndGet();
        if (seq > 99) {
            sequence.set(0);
            seq = sequence.incrementAndGet();
        }

        // 格式化序列号为2位数字
        String sequencePart = String.format("%02d", seq);

        return ORDER_PREFIX + datePart + userIdPart + sequencePart;
    }
}
