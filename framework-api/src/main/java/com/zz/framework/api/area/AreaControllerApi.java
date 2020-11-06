package com.zz.framework.api.area;

import com.zz.framework.common.model.response.ResponseResult;
import com.zz.framework.common.model.response.ResponseResultWithData;
import com.zz.framework.domain.area.response.GetLeAreaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api(value = "区域服务",description = "区域服务接口",tags = {"区域服务"})
public interface AreaControllerApi {
	//增加区域
	@ApiOperation("增加区域")
	public ResponseResult insertArea(int parentId, String area);

	//查询区域
	@ApiOperation("查询区域")
	public GetLeAreaResult getLeAreasById(int id);

	//根据城市查询全部商圈及三个热点商圈
	@ApiOperation("根据城市查询全部商圈及三个热点商圈")
	public ResponseResultWithData getAllAndHotAreasByCittId(int id);

	//查询所有城市
	@ApiOperation("查询所有城市")
	public ResponseResultWithData getAllCitys();

}
