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
     * 是否已售  1未砍价  2已砍价 3已抢购
     */
    private Integer issale;

    //首页图片
    private String homepicture;

    //商家自定义规则
    private String rule;

    //商品使用时间
    private String usetime;

    //经度
    private String lon;
    //纬度
    private String lat;
    //距离 米
    private String distance;
}