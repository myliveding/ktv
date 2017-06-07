/**
 * 
 */
package com.st.service.impl;

import com.st.utils.Constant;
import com.st.utils.date.DateUtil;
import com.st.utils.PropertiesUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ArticleHandle
 * @Description: 
 * @author dingzr
 * @date 2016-6-1 下午5:02:41 2016
 */

@Service("articleRedisHandleService")
public class ArticleRedisHandleServiceImpl {

		@Autowired
		public RedisTemplate<String,JSONObject> redisTemplate;
		
		private Logger logger = LoggerFactory.getLogger(this.getClass());

		public void save(String key,JSONObject result,Integer type) {
			/*redisTemplate.opsForList(); 
	        redisTemplate.opsForSet(); 
	        redisTemplate.opsForHash()*/
			ValueOperations<String, JSONObject> valueOper = redisTemplate.opsForValue();
			valueOper.set(key, result);

			Integer intervalTime=1;
			Integer timeType=DateUtil.DATE_INTERVAL_HOUR;

			//设置这个key的到期时间，后面一个参数就是该key过期的确切的时间点
			redisTemplate.expireAt(key, DateUtil.dateAdd(timeType,DateUtil.getNowTime(),intervalTime));
		}

		public JSONObject read(String key) {
			ValueOperations<String, JSONObject> valueOper = redisTemplate.opsForValue();
			logger.info("Radis缓存中读取的key：" + key + ",的有效时间还剩余：" + redisTemplate.getExpire(key) + "s");
			return valueOper.get(key);
		}

		public void saveVCode(String key,JSONObject result,Integer leftTime) {
			ValueOperations<String, JSONObject> valueOper = redisTemplate.opsForValue();
			valueOper.set(key, result);
			Integer timeType=DateUtil.DATE_INTERVAL_SECOND;
					//设置这个key的到期时间，后面一个参数就是该key过期的确切的时间点
			redisTemplate.expireAt(key, DateUtil.dateAdd(timeType,DateUtil.getNowTime(),leftTime));
		}
		public JSONObject readVCode(String key) {
			ValueOperations<String, JSONObject> valueOper = redisTemplate.opsForValue();
			logger.info("Radis缓存中读取的key：" + key + ",的有效时间还剩余：" + redisTemplate.getExpire(key) + "s " + valueOper.get(key));
			JSONObject result= null;
			if (redisTemplate.getExpire(key)>0){
				result=valueOper.get(key);
				result.put("redisTimeleft",redisTemplate.getExpire(key));
			}
			return result;
		}

		public void delete(String key) {
			ValueOperations<String, JSONObject> valueOper = redisTemplate.opsForValue();
			RedisOperations<String,JSONObject>  redisOperations  = valueOper.getOperations();
			redisOperations.delete(key);
		}

}
