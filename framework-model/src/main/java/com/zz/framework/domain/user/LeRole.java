package com.zz.framework.domain.user;

import javax.persistence.*;
import java.util.Date;


public class LeRole {
    /**
     * 角色id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 是否有效 1是 0否
     */
    private Boolean valid;

    /**
     * 创建日期
     */
    private Date createtime;

    /**
     * 更新日期
     */

    private Date updatetime;

    /**
     * 获取角色id
     *
     * @return id - 角色id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置角色id
     *
     * @param id 角色id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取角色名称
     *
     * @return role_name - 角色名称
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置角色名称
     *
     * @param roleName 角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * 获取是否有效 1是 0否
     *
     * @return valid - 是否有效 1是 0否
     */
    public Boolean getValid() {
        return valid;
    }

    /**
     * 设置是否有效 1是 0否
     *
     * @param valid 是否有效 1是 0否
     */
    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    /**
     * 获取创建日期
     *
     * @return createTime - 创建日期
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置创建日期
     *
     * @param createtime 创建日期
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取更新日期
     *
     * @return updateTime - 更新日期
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 设置更新日期
     *
     * @param updatetime 更新日期
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}