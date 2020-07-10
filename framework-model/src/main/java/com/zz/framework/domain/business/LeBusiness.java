package com.zz.framework.domain.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "le_business")
public class LeBusiness {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 商家全称
     */
    private String businessname;

    /**
     * 商家简称
     */
    private String businessnickname;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    private LocalDateTime updatetime;

}