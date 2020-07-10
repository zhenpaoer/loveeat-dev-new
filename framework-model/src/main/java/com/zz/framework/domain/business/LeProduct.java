package com.zz.framework.domain.business;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "le_product")
public class LeProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 商家分店id
     */
    private Integer bid;

    private String businessname;//分店名称

    private String productname; //商品名称

    private Integer version;//版本号

    private LocalDateTime createtime;

    private LocalDateTime updatetime;

    /**
     * 状态 1启用 2禁用 3下架
     */
    private Integer state;

    /**
     * 状态  1审核中 2审核通过 3审核不通过
     */
    private String status;

    private Integer foodtype;

    /**
     * 商品描述
     */
    private String describ;

    /**
     * 原价
     */
    private BigDecimal originalprice;

    /**
     * 砍价之后的价格
     */
    private BigDecimal bargainprice;

    private Integer bargainpersonsum;

    /**
     * 是否已售 1已抢购 2未抢购
     */
    private Integer issale;


}