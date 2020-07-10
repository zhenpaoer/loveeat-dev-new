package com.zz.framework.domain.business.ext;/**
 * Created by zhangzhen on 2020/5/17
 */

import com.zz.framework.domain.business.LeProductMenudetail;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName LeProductMenuNode
 * @Description: TODO  菜单节 树形节点
 * @Author zhangzhen
 * @Date 2020/5/17 
 * @Version V1.0
 **/
@Data
@ToString
public class LeProductMenuNode extends LeProductMenudetail {
	List<LeProductMenuNode> children;
}
