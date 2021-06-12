package org.codingeasy.shiroplus.core.utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
*   
* @author : kangning <a>2035711178@qq.com</a>
*/
public class AnnotationUtilsTest {


	@Test
	public void instantiateAnnotation(){
		Map<String ,Object> map = new HashMap<>();
		map.put("value" , new String[]{"111"});
		assert AnnotationUtils.call(
				an -> an.value().length == 1 && an.value()[0].equals("111"),
				RequiresPermissions.class,
				map
		);
	}
}
