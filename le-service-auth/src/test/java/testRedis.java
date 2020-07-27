import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangzhen on 2019/7/10
 */
@SpringBootTest(classes = testRedis.class)
@RunWith(SpringRunner.class)
public class testRedis {
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	/**
	 * {
	 *     "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6IlhjV2ViQXBwIiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOm51bGwsInV0eXBlIjpudWxsLCJpZCI6bnVsbCwiZXhwIjoxNTYyODc0OTU5LCJqdGkiOiIwNmFmNjg0MC1iODQ3LTQ0NTEtODM3Ni1lNzkxNmQ3Mjc3YTQiLCJjbGllbnRfaWQiOiJYY1dlYkFwcCJ9.tupGXPOmInUcyeGx0HaX7CezVuVghETyhP3MF8H-CQ1i9X67eCa5FiG1x39uOT1RirzfxmhkspqUrGrUzhH9ed7-DrXNj8oVmjfys0XiCrwHxFeoSKRQcMnVeJrgc3t9XE8VLU4j195VU9ih6ApTn5P3ulT2tvCqA2cDk2OlKBV7FriMwtpKImuabOylgoeoJOrmn8zo2qEs6zrhAB_k_5ICoraq9H7lLe8Up11RBVl0Gr_uyszvJVVdO5-I0Ftd5obCTmFMuFan8Mg9bEWNYaGntJ04I1OS8LMoATPieZSZKzEgtOSgheTtBRZKRGORlHaBMXiSQBYZvxRpJ8aZow",
	 *     "token_type": "bearer",
	 *     "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6IlhjV2ViQXBwIiwic2NvcGUiOlsiYXBwIl0sImF0aSI6IjA2YWY2ODQwLWI4NDctNDQ1MS04Mzc2LWU3OTE2ZDcyNzdhNCIsIm5hbWUiOm51bGwsInV0eXBlIjpudWxsLCJpZCI6bnVsbCwiZXhwIjoxNTYyODc0OTU5LCJqdGkiOiJlNTI3YmQ4Ny0wMWUxLTRkN2MtODk1Yi01ZTlmY2FmZDUxYmQiLCJjbGllbnRfaWQiOiJYY1dlYkFwcCJ9.A75YiWvZ-_20c7v17kaKR21bB6yZJFXTtfl6BpY-kCd_j7oNMyEqIkmuTSGV423JfJDH31V5O7i70QWJn_IRU9YoyARShYEav-Mr6p6QcGLSF3emCWJ5bfXi6a7p4JeQ4wFPfBxJcCb7i5hhskP7ecHib296WjAqCROOlW30S7drha-VVqLT43Xou8heTPEiUC_Om-aLwK4QcReam8rfptQEolNEGu44N5xkyoPtWXs6Ri2mFQOKlM5Eo8nhpRpdFbpTci99Uxj0uVaYHGwr6JH1RcZCfrITYXmWv-3EDueDc-wUJqLu39jEmK4v5fXqf28tAN7xkmuSLTpmP6mydQ",
	 *     "expires_in": 43199,
	 *     "scope": "app",
	 *     "jti": "06af6840-b847-4451-8376-e7916d7277a4"
	 * }
	 */
	@Test
	public void testRedis() {
		String key = "user_token:06af6840-b847-4451-8376-e7916d7277a4";
		Map<String,String> valueMap = new HashMap<>();
		valueMap.put("access_token","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6IlhjV2ViQXBwIiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOm51bGwsInV0eXBlIjpudWxsLCJpZCI6bnVsbCwiZXhwIjoxNTYyODc0OTU5LCJqdGkiOiIwNmFmNjg0MC1iODQ3LTQ0NTEtODM3Ni1lNzkxNmQ3Mjc3YTQiLCJjbGllbnRfaWQiOiJYY1dlYkFwcCJ9.tupGXPOmInUcyeGx0HaX7CezVuVghETyhP3MF8H-CQ1i9X67eCa5FiG1x39uOT1RirzfxmhkspqUrGrUzhH9ed7-DrXNj8oVmjfys0XiCrwHxFeoSKRQcMnVeJrgc3t9XE8VLU4j195VU9ih6ApTn5P3ulT2tvCqA2cDk2OlKBV7FriMwtpKImuabOylgoeoJOrmn8zo2qEs6zrhAB_k_5ICoraq9H7lLe8Up11RBVl0Gr_uyszvJVVdO5-I0Ftd5obCTmFMuFan8Mg9bEWNYaGntJ04I1OS8LMoATPieZSZKzEgtOSgheTtBRZKRGORlHaBMXiSQBYZvxRpJ8aZow");
		valueMap.put("refresh_token","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6IlhjV2ViQXBwIiwic2NvcGUiOlsiYXBwIl0sImF0aSI6IjA2YWY2ODQwLWI4NDctNDQ1MS04Mzc2LWU3OTE2ZDcyNzdhNCIsIm5hbWUiOm51bGwsInV0eXBlIjpudWxsLCJpZCI6bnVsbCwiZXhwIjoxNTYyODc0OTU5LCJqdGkiOiJlNTI3YmQ4Ny0wMWUxLTRkN2MtODk1Yi01ZTlmY2FmZDUxYmQiLCJjbGllbnRfaWQiOiJYY1dlYkFwcCJ9.A75YiWvZ-_20c7v17kaKR21bB6yZJFXTtfl6BpY-kCd_j7oNMyEqIkmuTSGV423JfJDH31V5O7i70QWJn_IRU9YoyARShYEav-Mr6p6QcGLSF3emCWJ5bfXi6a7p4JeQ4wFPfBxJcCb7i5hhskP7ecHib296WjAqCROOlW30S7drha-VVqLT43Xou8heTPEiUC_Om-aLwK4QcReam8rfptQEolNEGu44N5xkyoPtWXs6Ri2mFQOKlM5Eo8nhpRpdFbpTci99Uxj0uVaYHGwr6JH1RcZCfrITYXmWv-3EDueDc-wUJqLu39jEmK4v5fXqf28tAN7xkmuSLTpmP6mydQ");
		String value = JSON.toJSONString(valueMap);
		//向redis中存储字符串
		stringRedisTemplate.boundValueOps(key).set(value,30, TimeUnit.SECONDS);

		//读取过期时间，已过期返回-2
		Long expire = stringRedisTemplate.getExpire(key);

		//从redis根据key获取value的值
		String s = stringRedisTemplate.opsForValue().get(key);
		System.out.println(s);


	}
}
