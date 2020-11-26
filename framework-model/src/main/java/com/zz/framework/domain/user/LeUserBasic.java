package com.zz.framework.domain.user;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Table(name = "le_user_basic")
public class LeUserBasic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nickname;

    private String avatar;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    private String username;

    private String password;

    private String phone;

    private LocalDateTime updatetime;

    private String openid;
    private String sessionkey;

    /**
     * 头像
     */
    private String userpic;
    /**
     * 经度
     */
    private String lon;
    /**
     * 纬度
     */
    private String lat;
    /**
     * 地址
     */
    private String address;

}