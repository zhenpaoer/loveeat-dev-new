package com.zz.framework.domain.order;

import lombok.*;
import sun.util.resources.LocaleData;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "le_order")
public class LeOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id ;
	//交易号
	String tradeNo;
	//商家id
	int bid;
	//商品id
	int pid;
	//用户id
	int uid;
	//状态 订单状态 0：下单，1：待支付，2：待使用，3：待评价，4：已完成，5：已过期，6：其他异常
	int status;
	//订单金额
	BigDecimal price;
	//创建日期
	LocalDate createDate;
	//创建时间
	LocalDateTime createTime;

	LocalDateTime updateTime;
}
