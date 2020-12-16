package com.zz.framework.domain.order;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "le_order")
public class LeOrderOperateLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id ;
	//用户id
	int uid;
	//订单id
	int oid;
	//操作内容
	String content;
	LocalDateTime createTime;
	LocalDateTime updateTime;
}
