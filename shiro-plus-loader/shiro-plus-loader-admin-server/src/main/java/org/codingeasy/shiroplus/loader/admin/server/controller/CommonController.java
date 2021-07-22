package org.codingeasy.shiroplus.loader.admin.server.controller;

import org.codingeasy.shiroplus.loader.admin.server.service.CommonService;
import org.codingeasy.shiroplus.loader.admin.server.models.Dict;
import org.codingeasy.shiroplus.loader.admin.server.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
* 通用的接口  
* @author : KangNing Hu
*/
@RestController
@RequestMapping("/admin/common")
public class CommonController {


	@Autowired
	private CommonService commonService;
	/**
	 * 获取所以字典数据
	 * @return 返回字典数据 字典编码->[字典值:字典名称]
	 */
	@GetMapping("/dict/all")
	public Response<Map<String , List<Dict>>> getDict(){
		return Response.ok(commonService.getDict());
	}
}
