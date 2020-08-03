package com.zz.user.service.impl;

import com.zz.framework.domain.user.response.GetUserExtResult;
import com.zz.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zhangzhen on 2020/7/15
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

	@Autowired
	UserService userService;


	@Test
	public void getUserExt() {
		GetUserExtResult zhangsan = userService.getUserExt("zhangsan");
		System.out.println(zhangsan);
	}


}
