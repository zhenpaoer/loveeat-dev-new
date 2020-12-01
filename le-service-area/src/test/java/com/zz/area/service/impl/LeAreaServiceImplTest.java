package com.zz.area.service.impl;

import com.zz.area.dao.LeAreaMapper;
import com.zz.area.service.LeAreaService;
import com.zz.framework.domain.area.LeArea;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class LeAreaServiceImplTest extends TestCase {
	@Autowired
	LeAreaService service;
	@Autowired
	LeAreaMapper mapper;

	@Test
	public void testInsertArea() {
		LeArea leArea = new LeArea();
		leArea.setParentId(1);
		leArea.setArea("丰台区");
		int i = mapper.insertArea(leArea);
		System.out.println(i);
	}

	@Test
	public void testGetAreaById() {

	}
}