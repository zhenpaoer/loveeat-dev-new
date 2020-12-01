package com.zz.framework.domain.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "le_bargain_log")
public class LeBargainLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id ;
	int pid;
	int uid;
	BigDecimal price; //砍价金额
	LocalDateTime createtime;
	LocalDateTime updatetime;
}
