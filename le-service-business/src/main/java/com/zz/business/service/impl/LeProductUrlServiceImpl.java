package com.zz.business.service.impl;/**
 * Created by zhangzhen on 2020/5/18
 */

import com.zz.business.dao.LeProductPicurlMapper;
import com.zz.business.service.LeProductUrlService;
import com.zz.framework.common.model.response.CommonCode;
import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.domain.business.LeProductPicurl;
import com.zz.framework.domain.business.response.ProductUrlCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName LeProductUrlServiceImpl
 * @Description: TODO
 * @Author zhangzhen
 * @Date 2020/5/18 
 * @Version V1.0
 **/

@Service
public class LeProductUrlServiceImpl implements LeProductUrlService {

	@Autowired
	LeProductPicurlMapper leProductPicurlMapper;

	//根据商品id查找图片
	@Override
	public List<LeProductPicurl> getProductUrlByPid(int pid) {
		return leProductPicurlMapper.getProductUrlByPid(pid);
	}

	@Override
	public ResponseResult saveLeProductPicurls(List<LeProductPicurl> leProductPicurls) {

		if (leProductPicurls.size() == 0){
			return new ResponseResult(ProductUrlCode.PRODUCTURL_DATACHECK_ERROR);
		}
		List<LeProductPicurl> localProductUrlByPid = leProductPicurlMapper.getProductUrlByPid(leProductPicurls.get(0).getPid());
		//新list与老list对比 ，交集是更新的，新list的补给是新增的，老list的补给是删除的

		List<Integer> localIds = localProductUrlByPid.parallelStream().map(LeProductPicurl::getId).collect(Collectors.toList());
		//交集
		List<LeProductPicurl> needUpdate = leProductPicurls.parallelStream().filter(item -> localIds.contains(item.getId())).collect(Collectors.toList());
		if (needUpdate.size() > 0){
			needUpdate.parallelStream().forEach(item -> {
				leProductPicurlMapper.updateByPrimaryKey(item);
			});
		}
		List<Integer> updateIds = needUpdate.parallelStream().map(LeProductPicurl::getId).collect(Collectors.toList());
		// 差集 需要删除的
		List<Integer> needDel = localIds.stream().filter(item -> !updateIds.contains(item)).collect(Collectors.toList());
		if (needDel.size()>0){
			needDel.parallelStream().forEach(item -> {
				leProductPicurlMapper.deleteByPrimaryKey(item);
			});
		}

		// 差集 需新增的
		List<LeProductPicurl> needInsert = leProductPicurls.stream().filter(item -> !updateIds.contains(item.getId())).collect(Collectors.toList());
		if (needInsert.size()>0){
			int i = leProductPicurlMapper.insertList(needInsert);
		}
		return new ResponseResult(CommonCode.SUCCESS);

		/*boolean paramCheck = leProductPicurls.parallelStream().allMatch(item -> item.getId() != null && item.getPid() != null && item.getPicurl() != null);
		//有为空的
		int i = 0;
		if (paramCheck){
			//需要新增的
			List<LeProductPicurl> needInsert = leProductPicurls.parallelStream().filter(item -> item.getId() == null).collect(Collectors.toList());
			//需要更新的
			List<LeProductPicurl> needUpdate = leProductPicurls.parallelStream().filter(item -> item.getId() != null && item.getId() > 0).collect(Collectors.toList());
			if (needInsert.size() > 0){
				i = leProductPicurlMapper.insertList(needInsert);
				if (needUpdate.size() > 0){
					needUpdate.parallelStream().forEach(item -> {
						leProductPicurlMapper.updateByPrimaryKey(item);
					});
				}
				if (i > 0 ){
					return new ResponseResult(CommonCode.SUCCESS);
				}
			}
			return new ResponseResult(ProductUrlCode.PRODUCTURL_SAVE_ERROR);
		}else {
			//全部不为空 需要更新


		}*/

	}

	@Override
	public ResponseResult saveLeProductPicurl(LeProductPicurl leProductPicurl) {
		Example example = new Example(LeProductPicurl.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("picurl",leProductPicurl.getPicurl());
		if (leProductPicurl.getId() == null){
			//先根据picurl查有没有
			List<LeProductPicurl> leProductPicurls = leProductPicurlMapper.selectByExample(example);
			if (leProductPicurls.size() == 0){
				int insert = leProductPicurlMapper.insert(leProductPicurl);
				if (insert > 0) new ResponseResult(CommonCode.SUCCESS);
				else new ResponseResult(ProductUrlCode.PRODUCTURL_CREATE_FALSE);
			}else return new ResponseResult(ProductUrlCode.PRODUCTURL_UPDATE_ERROR);
		}
		int update = leProductPicurlMapper.updateByPrimaryKey(leProductPicurl);
		if (update > 0) return new ResponseResult(CommonCode.SUCCESS);
		else  return new ResponseResult(ProductUrlCode.PRODUCTURL_UPDATE_ERROR);
	}

}
