package com.zz.framework.domain.user;

import lombok.Data;

import javax.persistence.*;


@Data
@Table(name = "le_user_role")
public class LeUserRole {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 会员id
     */

    private Integer userId;

    /**
     * 角色id
     */

    private Integer roleId;

}