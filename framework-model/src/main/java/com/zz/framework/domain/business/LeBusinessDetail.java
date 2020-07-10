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
@Table(name = "le_business_detail")
public class LeBusinessDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 商家id
     */
    private Integer bid;

    /**
     * 是否分店 1是总店，2是分店
     */
    private Integer isbranch;

    /**
     * 分店名称
     */
    private String branchname;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    /**
     * 修改时间
     */
    private LocalDateTime modifitime;

    private String location;

    private String phone;

    /**
     * 商家描述
     */
    private String describetion;

    /**
     * 审核状态 1审核中 2审核通过 3审核不通过
     */
    private String status;

    /**
     * 状态 1:启用 2:禁用
     */
    private Integer state;

    /**
     * 商家图片url
     */
    private String url;
}