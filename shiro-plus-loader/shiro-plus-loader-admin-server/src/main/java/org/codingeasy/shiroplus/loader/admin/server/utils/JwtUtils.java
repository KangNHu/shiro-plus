package org.codingeasy.shiroplus.loader.admin.server.utils;

import com.hazelcast.util.MD5Util;
import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/**
*   jwt 工具类
* @author : KangNing Hu
*/
@Component
public class JwtUtils {

	private static final String JWT_ISSUER = "codingeasy";
	private static final String JWT_NAME_AGENT_ID = "agent_id";
	private static final String JWT_NAME_IP = "_ip";




	/**
	 * 创建密钥对
	 * @return 返回密钥对对象
	 */
	public static RSAKey createKeyPair(){
		try {
			//创建密钥对
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			//创建rsa key
			return new RSAKey.Builder((RSAPublicKey)keyPair.getPublic()).
					privateKey(keyPair.getPrivate()).
					keyUse(KeyUse.SIGNATURE).
					algorithm(JWSAlgorithm.RS512).
					keyID(UUID.randomUUID().toString()).
					build();
		}catch (Exception e){
			throw new IllegalStateException(e);
		}
	}

	/**
	 * 创建token
	 * @param userId 用户id
	 * @param request 请求上下文
	 * @return 返回jwt生成的token
	 */
	public static String createToken(@NotNull Long userId , @NotNull HttpServletRequest request ){
		try {
			String agentId = MD5Util.toMD5String(WebUtils.getUserAgent(request));
			//构建基础信息
			JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
					.jwtID(UUID.randomUUID().toString())
					.issuer(JWT_ISSUER)
					.claim(JWT_NAME_AGENT_ID,agentId )
					.claim(JWT_NAME_IP, WebUtils.getRemoteIp(request))
					.subject(userId + "");
			//设置有效期
			setValidTime(builder);
			//签名
			SignedJWT signedJWT = new SignedJWT(getJwsHeader(), builder.build());
			signedJWT.sign(new RSASSASigner(SystemUtils.getRSAKey()));
			//序列化 字符串
			return signedJWT.serialize();
		}catch (Exception e){
			throw new AuthenticationException("创建token错误" , e);
		}
	}


	/**
	 * 校验rsaKey
	 * @param rsaKey rsaKey json字符串
	 * @return 返回一个虚拟的token
	 */
	public static String validateRSAKey(String rsaKey) throws Exception {
		//构建基础信息
		JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
				.jwtID(UUID.randomUUID().toString())
				.issuer(JWT_ISSUER)
				.claim(JWT_NAME_AGENT_ID,"test" )
				.claim(JWT_NAME_IP, "test")
				.subject("test");
		//签名
		SignedJWT signedJWT = new SignedJWT(getJwsHeader(), builder.build());
		signedJWT.sign(new RSASSASigner(RSAKey.parse(rsaKey)));
		//序列化 字符串
		return signedJWT.serialize();
	}

	/**
	 * 获取主题并校验token
	 * @param token  token
	 * @return 如果校验成功 返回token主题
	 *
	 */
	public static Long getSubjectAndVerify(String token , HttpServletRequest request){
		try {
			if (StringUtils.isEmpty(token)){
				throw new AuthenticationException("不支持匿名访问");
			}
			SignedJWT parse = SignedJWT.parse(token);
			JWTClaimsSet jwtClaimsSet = parse.getJWTClaimsSet();
			//校验token的合法性
			JWSHeader jwsHeader = getJwsHeader();
			Algorithm alg =jwsHeader.getAlgorithm();
			JWSAlgorithm expectedJWSAlg = JWSAlgorithm.parse(alg.getName());
			ImmutableJWKSet<SecurityContext> immutableJWKSet = new ImmutableJWKSet<>(new JWKSet(SystemUtils.getRSAKey()));
			JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(expectedJWSAlg, immutableJWKSet);
			keySelector.selectJWSKeys(jwsHeader, null);
			ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
			jwtProcessor.setJWSKeySelector(keySelector);
			jwtProcessor.process(token, null);
			//校验客户端是否合法
			String agentId = jwtClaimsSet.getStringClaim(JWT_NAME_AGENT_ID);
			if (StringUtils.isEmpty(agentId) || !agentId.equals(MD5Util.toMD5String(WebUtils.getUserAgent(request)))){
				throw new AuthenticationException("客户端设备不合法");
			}
			String ip = jwtClaimsSet.getStringClaim(JWT_NAME_IP);
			if (StringUtils.isEmpty(ip) || !ip.equals(WebUtils.getRemoteIp(request))){
				throw new AuthenticationException("客户端设备不合法");
			}
			//校验token的颁发人
			if (!JWT_ISSUER.equals(jwtClaimsSet.getIssuer())){
				throw new AuthenticationException("非法的token");
			}
			return Long.valueOf(jwtClaimsSet.getSubject());
		}catch (AuthenticationException e){
			throw e;
		} catch (Exception e){
			throw new AuthenticationException("无效的token" ,e);
		}

	}


	/**
	 * 获取jwt头部
	 * @return 返回jwt头
	 */
	private static JWSHeader getJwsHeader() {
		return new JWSHeader(JWSAlgorithm.RS512);
	}




	private static void setValidTime(JWTClaimsSet.Builder builder){
		long loginValidTime = SystemUtils.getLoginValidTime();
		long startTime = System.currentTimeMillis();
		long endTime = startTime + loginValidTime * 60 * 1000;
		builder
				.issueTime(new Date(startTime))
				.expirationTime(new Date(endTime));
	}
}
