import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangzhen on 2019/7/10
 */
@SpringBootTest(classes = testJwt.class)
@RunWith(SpringRunner.class)
public class testJwt {

	@Test
	public void testCreateJwt(){
		//证书文件
		String key_location = "/le.keystore";
		//密钥库密码
		String keystore_password = "loveeatkeystore";
		//访问证书路径
		ClassPathResource resource = new ClassPathResource(key_location);
		//密钥的密码，此密码和别名要匹配
		String keypassword = "loveeat";
		//密钥别名
		String alias = "loveeat";
		// 密钥工厂
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource,keystore_password.toCharArray());
		//密钥对（密钥和公钥）
		KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias,keypassword.toCharArray());
		//私钥
		RSAPrivateKey aPrivate =(RSAPrivateKey) keyPair.getPrivate();
		//定义payload信息
		Map<String, Object> tokenMap = new HashMap<>();
		tokenMap.put("id", "123");
		tokenMap.put("name", "mrt");
		tokenMap.put("roles", "r01,r02");
		tokenMap.put("ext", "1");
		//生成jwt令牌
		Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(aPrivate));
		//取出jwt令牌
		String claims = jwt.getClaims();
		String encoded = jwt.getEncoded();
		System.out.println("claims==="+claims);
		System.out.println("encoded==="+encoded);

	}

	@Test
	public void testVerify(){
		//jwt令牌
		String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHQiOiIxIiwicm9sZXMiOiJyMDEscjAyIiwibmFtZSI6Im1ydCIsImlkIjoiMTIzIn0.uYiyfKSpwReHctvGhAOAIhozAGSf81qgSpdiwoDrNwTuuutOJNDf8wgmQXAhMsZRfb2mhlKc1-IpaYrBD7gOcTOnZU-62I7KpWu0-oQDSF3YGSWe0_uP9Z1s89YI9jGPSNXft7frLpQW2N2XK3QyMrpTiJwwxgJ2oKPPyTA_smMR6BbBUaP7k3LsuHsjyoGccSgSO3KclIOkUI9x4A7zu_OOavqYyfB3yvl6RqBAPLxCF_09fYCIlttdMiOdFmWThp7pGXVEOZkJyccBk9aoawEJ-Y-njJiYZQFOffcM5mnefrVxLcrdJQSrffZUnm9XrD6fgBBHN9sR1sfOLCku0Q";
		//公钥
		String publickey ="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6Sa1uK+mE+vSqN6sZGuyyuWsngiP1V29z9dgDaPOK8NDOjloY9qCwsT4LglTgOg8usgzjn0gaySqSYv5aoB6J6onXnXUSWpisofR4QLdPD3l/AieXr4AR0N9NSaYOy2L9xOyJHnkcGE6glBrylave/c5IqUGN0BD3lZ5LBWmGkiU2lkBlIUpWZyYIzcDv6U2Fwbq+ePx1qmJbNKqMzyKpzrRUauxVEdBxXXRO6dNmaSzYAjvtgbJyWKrrHPer3wPMfoVUxovmNq4WNHo4QvHwzEHjUE5q/HYHt6SbcUPttH3BG/mp/5XGYnzFa25YhTYhehsZTnXNAwInGqjJvz8AwIDAQAB-----END PUBLIC KEY-----";
		//校验jwt
		Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));
		//获取jwt原始内容
		String claims = jwt.getClaims();
		//jwt令牌
		String encoded = jwt.getEncoded();
		System.out.println("claims==="+claims);
		System.out.println("encoded==="+encoded);
	}
	@Test
	public void testEncode(){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println("encode==="+encoder.encode("LeWebapp"));
	}
}
