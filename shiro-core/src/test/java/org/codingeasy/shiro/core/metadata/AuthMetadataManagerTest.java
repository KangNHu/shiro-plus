package org.codingeasy.shiro.core.metadata;

import org.codingeasy.shiro.core.event.AuthMetadataEvent;
import org.codingeasy.shiro.core.event.EventManager;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
*   
* @author : KangNing Hu
*/
public class AuthMetadataManagerTest {


	private AuthMetadataManager authMetadataManager;

	@Before
	public void init(){
		authMetadataManager = new AuthMetadataManager(new MyMetadataLoader());
		authMetadataManager.init();
	}



	@Test
	public void getPermissionMetadata(){
		PermissionMetadata permissionMetadata = authMetadataManager.getPermissionMetadata("/user:POST" , PermiModel.PRINCIPAL);
		System.out.println(permissionMetadata);
		assert permissionMetadata != null;
		permissionMetadata = authMetadataManager.getPermissionMetadata("/user:GET" , PermiModel.PRINCIPAL);
		System.out.println(permissionMetadata);
		assert permissionMetadata != null;
		permissionMetadata = authMetadataManager.getPermissionMetadata("/role:GET" , PermiModel.ROLE);
		System.out.println(permissionMetadata);
		assert permissionMetadata != null;
		permissionMetadata = authMetadataManager.getPermissionMetadata("/role:GET" , PermiModel.ROLE);
		System.out.println(permissionMetadata);
		assert permissionMetadata != null;
	}




	@Test
	public void  getGlobalMetadata(){
		GlobalMetadata globalMetadata = authMetadataManager.getGlobalMetadata(AuthMetadataManager.DEFAULT_TENANT_ID);
		System.out.println(globalMetadata);
		assert globalMetadata != null;
		globalMetadata = authMetadataManager.getGlobalMetadata("tenantId");
		System.out.println(globalMetadata);
		assert globalMetadata != null;
	}



	@Test
	public void permissionMetadataEvent() throws InterruptedException {
		EventManager eventManager = new EventManager();
		eventManager.register(this.authMetadataManager);

		eventManager.publish(new AuthMetadataEvent( AuthMetadataEvent.EventType.UPDATE , new PermissionMetadata("/user" , RequestMethod.POST , Arrays.asList("user:add") , Logical.AND , PermiModel.ROLE)));
		eventManager.publish(new AuthMetadataEvent( AuthMetadataEvent.EventType.UPDATE , new PermissionMetadata("/user" , RequestMethod.POST , Arrays.asList("user:add:event") , Logical.AND , PermiModel.PRINCIPAL)));
		eventManager.asyncPublish(new AuthMetadataEvent(AuthMetadataEvent.EventType.UPDATE , new GlobalMetadata(null , Arrays.asList("/swagger") , false , true) ));

		Thread.sleep(1000L);

		GlobalMetadata globalMetadata = this.authMetadataManager.getGlobalMetadata(AuthMetadataManager.DEFAULT_TENANT_ID);
		System.out.println(globalMetadata);
		assert globalMetadata != null && !globalMetadata.getEnableAuthentication();

		PermissionMetadata permissionMetadata = this.authMetadataManager.getPermissionMetadata("/user:POST", PermiModel.ROLE);
		System.out.println(permissionMetadata);
		assert  permissionMetadata != null;
		permissionMetadata = this.authMetadataManager.getPermissionMetadata("/user:POST", PermiModel.PRINCIPAL);
		System.out.println(permissionMetadata);
		assert  permissionMetadata != null && permissionMetadata.getPermis().get(0).equals("user:add:event");
	}



	static class MyMetadataLoader implements MetadataLoader{

		@Override
		public List<PermissionMetadata> load() {
			return Arrays.asList(
					new PermissionMetadata("/user" , RequestMethod.POST , Arrays.asList("user:add") , Logical.AND , PermiModel.PRINCIPAL),
					new PermissionMetadata("/user" , RequestMethod.GET , Arrays.asList("user:get") ,  Logical.AND , PermiModel.PRINCIPAL),
					new PermissionMetadata("/role" , RequestMethod.POST , Arrays.asList("role:add") , Logical.AND , PermiModel.ROLE),
					new PermissionMetadata("/role" , RequestMethod.GET , Arrays.asList("role:get") ,  Logical.AND , PermiModel.ROLE)
			);
		}

		@Override
		public List<GlobalMetadata> loadGlobal() {
			return Arrays.asList(
					new GlobalMetadata(null , Arrays.asList("/swagger") , true , true),
					new GlobalMetadata("tenantId" , Arrays.asList("/swagger") , true , true)
			);
		}
	}

}
