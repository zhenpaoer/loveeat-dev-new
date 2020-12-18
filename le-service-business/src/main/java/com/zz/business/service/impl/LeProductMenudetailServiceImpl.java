package com.zz.business.service.impl;/**
 * Created by zhangzhen on 2020/5/18
 */

import com.alibaba.druid.util.StringUtils;
import com.zz.business.dao.LeProductMenudetailMapper;
import com.zz.business.service.LeProductMenudetailService;
import com.zz.framework.common.exception.ExceptionCast;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.business.LeProduct;
import com.zz.framework.domain.business.LeProductMenudetail;
import com.zz.framework.domain.business.ext.LeProductMenuNode;
import com.zz.framework.domain.business.response.ProductCode;
import com.zz.framework.domain.business.response.ProductMenuDetailCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName LeProductMenudetailServiceImpl
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/5/18 
 * @Version V1.0
 **/

@Service
public class LeProductMenudetailServiceImpl implements LeProductMenudetailService{
	@Autowired
	LeProductMenudetailMapper leProductMenudetailMapper;


	@Override
	public List<LeProductMenuNode> getProductMenuByPid(int pid) {
		return leProductMenudetailMapper.getProductMenuByPid(pid);
	}

	//保存菜单
	@Override
	public ResponseResult saveProductMenu(List<LeProductMenudetail> leProductMenudetails) {
		//取出title的
		List<LeProductMenudetail> titleList = leProductMenudetails.parallelStream().filter(s ->  s.getParentid() != null && s.getParentid()==0).collect(Collectors.toList());
		LeProductMenudetail title = titleList.get(0);
		//取出菜单下的条目
		List<LeProductMenudetail> items = leProductMenudetails.parallelStream().filter(item -> !titleList.contains(item)).collect(Collectors.toList());
		//新增
		if (title.getId() == null || title.getId() == 0){
			//先根据pid和title查有没有
			Example example = new Example(LeProductMenudetail.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("parentid",title.getParentid())
					.andEqualTo("title",title.getTitle());
			List<LeProductMenudetail> localLeProductMenudetails = leProductMenudetailMapper.selectByExample(example);
			if (localLeProductMenudetails.size()>0){
				//本地存在
				ExceptionCast.cast(ProductMenuDetailCode.PRODUCTMENU_NAME_EXIT);
			}

			int insert = leProductMenudetailMapper.insert(title);
			if (insert == 0){
				ExceptionCast.cast(ProductMenuDetailCode.PRODUCTMENU_CREATE_FALSE);
			}
			//获取父id
			Integer id = title.getId();
			//子条目设置父id
			items.parallelStream().forEach(item -> item.setParentid(id));
			//批量插入
			int i = leProductMenudetailMapper.insertList(items);
			if (i == 0){
				ExceptionCast.cast(ProductMenuDetailCode.PRODUCTMENU_CREATE_FALSE);
			}
			return new ResponseResult(CommonCode.SUCCESS);
		}else {
			boolean isIdExist = items.parallelStream().allMatch(item -> item.getId() > 0);
			//如果id都存在
			if (isIdExist){
				int update = leProductMenudetailMapper.updateByPrimaryKey(title);
				if (update == 0){
					ExceptionCast.cast(ProductMenuDetailCode.PRODUCTMENU_UPDATE_ERROR);
				}
				items.parallelStream().forEach(item -> {
					leProductMenudetailMapper.updateByPrimaryKey(item);
				});
				return new ResponseResult(CommonCode.SUCCESS);
			}else {
				ExceptionCast.cast(ProductMenuDetailCode.PRODUCTMENU_DARACHECK_ERROR);
			}
			return  new ResponseResult(CommonCode.FAIL);
		}
	}
}
