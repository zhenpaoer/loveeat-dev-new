package com.zz.framework.domain.user;

import javax.persistence.*;
import java.util.Date;


public class LePermission {
    /**
     * 权限id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 方法类型
     */
    private String method;

    /**
     * 网关前缀
     */

    private String zuulPrefix;

    /**
     * 服务前缀
     */

    private String servicePrefix;

    /**
     * 请求路径
     */
    private String uri;

    /**
     * 创建日期
     */

    private Date createtime;

    /**
     * 更新日期
     */

    private Date updatetime;

    /**
     * 获取权限id
     *
     * @return id - 权限id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置权限id
     *
     * @param id 权限id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取方法类型
     *
     * @return method - 方法类型
     */
    public String getMethod() {
        return method;
    }

    /**
     * 设置方法类型
     *
     * @param method 方法类型
     */
    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    /**
     * 获取网关前缀
     *
     * @return zuul_prefix - 网关前缀
     */
    public String getZuulPrefix() {
        return zuulPrefix;
    }

    /**
     * 设置网关前缀
     *
     * @param zuulPrefix 网关前缀
     */
    public void setZuulPrefix(String zuulPrefix) {
        this.zuulPrefix = zuulPrefix == null ? null : zuulPrefix.trim();
    }

    /**
     * 获取服务前缀
     *
     * @return service_prefix - 服务前缀
     */
    public String getServicePrefix() {
        return servicePrefix;
    }

    /**
     * 设置服务前缀
     *
     * @param servicePrefix 服务前缀
     */
    public void setServicePrefix(String servicePrefix) {
        this.servicePrefix = servicePrefix == null ? null : servicePrefix.trim();
    }

    /**
     * 获取请求路径
     *
     * @return uri - 请求路径
     */
    public String getUri() {
        return uri;
    }

    /**
     * 设置请求路径
     *
     * @param uri 请求路径
     */
    public void setUri(String uri) {
        this.uri = uri == null ? null : uri.trim();
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