package com.zz.auth.service;

import com.zz.auth.feign.UserClient;
import com.zz.framework.domain.user.LePermission;
import com.zz.framework.domain.user.LeRole;
import com.zz.framework.domain.user.ext.LeUserExt;
import com.zz.framework.domain.user.response.GetUserExtResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
	ClientDetailsService clientDetailsService;
    @Autowired
    UserClient userClient;

    /**认证服务调用spring security接口申请令牌，
     * spring security接口会调用UserDetailsServiceImpl从数据库查询用户，
     * 如果查询不到则返回 NULL，表示不存在；
     * 在UserDetailsServiceImpl中将正确的密码返回，
     * spring security 会自动去比对输入密码的正确性。
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if(authentication==null){
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if(clientDetails!=null){
                //密码
                String clientSecret = clientDetails.getClientSecret();
                return new User(username,clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        //调用User服务的查询用户接口
        GetUserExtResult result = userClient.getUserext(username);
        if (result.getCode() !=  10000){
            return null;
        }
        LeUserExt userext = result.getLeUserExt();
        if (userext == null){
            //返回NULL表示用户不存在，Spring Security会抛出异常
            return null;
        }
        //判断权限是否为空，避免报空指针
        List<LePermission> permissions = userext.getPermissions();
        if (permissions== null){
            permissions = new ArrayList<>();
        }
        //取出正确密码（hash值）
        //从数据库查询用户正确的密码，Spring Security会去比对输入密码的正确性
        String password = userext.getPassword();
      /*  //从数据库获取权限
        List<String> user_permission = new ArrayList<>();

        permissions.forEach(item-> user_permission.add(item.getUri()));
        String user_permission_string  = StringUtils.join(user_permission.toArray(), ",");
        UserJwt userDetails = new UserJwt(username,
                password,
//                AuthorityUtils.commaSeparatedStringToAuthorityList(user_permission_string));*/
        List<String> userRoles = new ArrayList<>();
        List<LeRole> roles = userext.getRoles();
        if (roles== null){
            roles = new ArrayList<>();
        }
        roles.stream().forEach(role -> userRoles.add(role.getRoleName()));
        String userRoleString  = StringUtils.join(userRoles.toArray(), ",");
        UserJwt userDetails = new UserJwt(username, password, AuthorityUtils.createAuthorityList(userRoleString));

        userDetails.setId(userext.getId().toString());
        userDetails.setName(userext.getUsername());//用户名称
        userDetails.setUserpic(userext.getUserpic());//用户头像.
       /* UserDetails userDetails = new org.springframework.security.core.userdetails.User(username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(""));*/
//                AuthorityUtils.createAuthorityList("course_get_baseinfo","course_get_list"));
        return userDetails;
    }
}
