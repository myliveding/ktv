package com.st.service.impl;

import com.st.core.util.date.DateUtil;
import com.st.javabean.pojo.wxtour.TBAttribute;
import com.st.mapper.wxtour.TBAttributeMapper;
import com.st.service.TBAttributeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Transactional
@Service("attributeService")
public class TBAttributeServiceImpl implements TBAttributeService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired TBAttributeMapper attributeMapper;
	
	/**
     * @Title selectByKey
     * @Description 根据key查询对象信息
     * @return TBAttribute
     */
	public TBAttribute selectByKey(String key) {
		return attributeMapper.selectByKey(key);
	}

	/**
     * @Title updateUserNum
     * @Description 根据key更新
     */
	public void updateUserNum() {
		TBAttribute  attribute = attributeMapper.selectByKey("userNum");
		int num = Integer.valueOf(attribute.getValue());
    	Random r = new Random();
    	int n2 = r.nextInt(5) + 1;
    	logger.info("定时器更新用户统计数量,获取到的随机数为：" + n2 + ",时间为：" + DateUtil.getNowStringTime());
		TBAttribute record = new TBAttribute();
		record.setKey("userNum");
		record.setValue(String.valueOf(num + n2));
		attributeMapper.updateByKey(record);
	}

}
