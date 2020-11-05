package com.zz.framework.domain.area;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LeArea {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int parentId; //父id
	private String area;  //区域 一级城市 二级行政区 三级 商圈
	private int searchcount; //热值
	private LocalDateTime createtime;
	private LocalDateTime updatetime;
}
