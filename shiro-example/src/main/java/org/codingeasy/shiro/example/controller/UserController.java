package org.codingeasy.shiro.example.controller;

import org.codingeasy.shiro.authorize.metadata.AuthMetadataEvent;
import org.codingeasy.shiro.authorize.metadata.EventType;
import org.codingeasy.shiro.authorize.metadata.PermissionMetadata;
import org.codingeasy.shiro.springboot.AuthMetadataEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
*   
* @author : KangNing Hu
*/
@RequestMapping("user")
@RestController
public class UserController {


	@Autowired
	private AuthMetadataEventPublisher authMetadataEventPublisher;


	@PostMapping()
	public String add(){
		return "add 成功";
	}


	@DeleteMapping
	public String delete(){
		return "delete 成功";
	}


	@PutMapping()
	public String update(){
		return "update 成功";
	}


	@GetMapping
	public String get(){
		return "查看";
	}


	/**
	 * 发送权限元信息变更事件
	 * @param permissionMetadata
	 */
	@PutMapping("/publishEvent")
	public void publishEvent(@RequestBody PermissionMetadata permissionMetadata){
		authMetadataEventPublisher.publish(new AuthMetadataEvent(EventType.UPDATE, permissionMetadata));
	}
}
