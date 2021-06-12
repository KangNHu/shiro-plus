package org.codingeasy.shiroplus.example.controller;

import org.codingeasy.shiroplus.core.event.AuthMetadataEvent;
import org.codingeasy.shiroplus.core.event.EventManager;
import org.codingeasy.shiroplus.core.metadata.PermissionMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
*   
* @author : kangning <a>2035711178@qq.com</a>
*/
@RequestMapping("user")
@RestController
public class UserController {


	@Autowired
	private EventManager eventManager;


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
		return "get";
	}


	/**
	 * 发送权限元信息变更事件
	 * @param permissionMetadata
	 */
	@PutMapping("/publishEvent")
	public void publishEvent(@RequestBody PermissionMetadata permissionMetadata){
		eventManager.publish(new AuthMetadataEvent(AuthMetadataEvent.EventType.UPDATE, permissionMetadata));
	}
}
