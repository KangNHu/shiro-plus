package org.codingeasy.shiroplus.loader.admin.server.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
* 加密工具类  
* @author : KangNing Hu
*/
public class EncryptionUtils {

	private static final String ALGORITHM_NAME = "md5";
	private static final int HASH_ITERATIONS = 5;

	/**
	 * 密码加密
	 * @param password 密码
	 * @param username 登录名
	 * @return 返回密文
	 */
	public static String encrypt(String password ,String username){
		SimpleHash simpleHash = new SimpleHash(ALGORITHM_NAME, password, username, HASH_ITERATIONS);
		return simpleHash.toBase64();
	}


	/**
	 * 校验密码
	 * @param cipherText 密文
	 * @param password 密码 明文
	 * @param username 用户名
	 * @return 如果明文正确返回true 否则false
	 */
	public static boolean verify(String cipherText , String password , String username){
		if (StringUtils.isEmpty(password)){
			return false;
		}
		return encrypt(password , username).equals(cipherText);
	}
}
