package com.zz.framework.domain.area.ext;

import com.zz.framework.domain.area.LeArea;
import com.zz.framework.domain.business.ext.LeProductMenuNode;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName LeAreaNode
 * @Description: TODO  地区 节树形节点
 * @Author zhangzhen
 * @Date 2020/11/5
 * @Version V1.0
 **/
@Data
@ToString
public class LeAreaNode extends LeArea {
	List<LeAreaNode> children;
}
