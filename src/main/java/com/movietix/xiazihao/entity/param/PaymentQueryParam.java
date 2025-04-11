package com.movietix.xiazihao.entity.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentQueryParam {
    private Integer orderId;                // 关联订单ID
    private String transactionId;        // 交易流水号
    private String paymentMethod;        // 支付方式(alipay/wechat/unionpay)
    private Integer status;              // 支付状态(0-未支付 1-成功 2-失败)
    private String startDate;            // 支付开始日期(YYYY-MM-DD)
    private String endDate;              // 支付结束日期
    private BigDecimal minAmount;        // 最小金额
    private BigDecimal maxAmount;        // 最大金额
    private Integer page = 1;            // 页码(默认1)
    private Integer pageSize = 10;       // 每页条数(默认10)
}
