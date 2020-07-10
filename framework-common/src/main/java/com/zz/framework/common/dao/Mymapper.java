package com.zz.framework.common.dao;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by zhangzhen on 2020/3/15
 */
public interface Mymapper<T> extends Mapper<T> , MySqlMapper<T>  {
}
