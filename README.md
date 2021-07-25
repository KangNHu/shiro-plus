# shiro-plus

### spring boot

#### 介绍
##### 一款shiro的加强版框架，它提供了如下功能
- 动态授权 （根据远程或本地配置对访问接口进行权限动态调整）
- 对shiro 原生授权方式进行了完美的适配
- 支持 web 和 aop动态授权的场景同事也提供了shiro原生的授权方式
- 可以对授权类型进行高度的扩展
- 提供了shiro filter容器和spring filter容器的隔离，同时也支持类似 spring bean 的注册注册方式
- 和spring boot完美的集成，真正实现"零配置"
- 提供了多租户的支持
- 当动态授权都不开启时，可以单纯当成一个shiro框架，并且对shiro本身并没有任何侵入
- 事件处理
- 支持第三方元数据中心




#### 软件架构

![shiro-plus](image/shiro-plus.png)

#### 安装说明

1. 

```bash
git clone https://gitee.com/kiangning/shiro-plus.git
mvn clean package
```

2. 将jar包引入自己的项目

#### 项目结构

- shiro-plus-core：核心工程 包括 事件处理 ，元信息加载，授权处理（静态和动态）
- shiro-plus-pom: shirt plus的依赖版本管理
- shiro-plus-loader shiro plus的元数据加载器实现
  - shiro-plus-loader-nacos 基于nacos的元数据中心
  - shiro-plus-loader-admin-server shiro plus admin 元数据中心
  - shiro-plus-loader-admin-client shiro plus admin元数据中的客服端
- shiro-plus-example：示例工程
  - spring-cloud-gateway 集成 spring cloud gateway 例子 （naocs作为元数据中心）
  - spring-cloud-gateway-provider spring-cloud-gateway项目的辅助工程
  - springboot  集成spring boot 列子工程
  - spring-cloud-gateway-admin  集成 spring cloud gateway 例子 （shiro plus admin 作为元数据中心）
- shiro-plus-gateway: spring cloud gateway的集成
- shiro-plus-springboot： spring boot的集成，模块化开发

#### 依赖说明

| 项目                 | 依赖版本       |
| -------------------- | -------------- |
| spring framework     | 5.2.14.RELEASE |
| spring boot          | 2.2.13.RELEASE |
| shiro                | 1.5.1          |
| servlet-api          | 4.0.0          |
| spring cloud         | Hoxton.SR11    |
| nacos-spring-context | 1.1.0          |
| junit                | 4.12           |

使用说明

#### 使用说明

##### 快速开始

1. 启动类

```java
@EnableShiroPlus
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class , args);
	}
}
```

2. 定义一个AuthProcessor

```java
@Component
public class SimpleAuthProcessor extends HttpServletAuthProcessor {

	private String userToken = "abc123456";

	private String userId = "123456";

	private static Map<String , Set<String>> map = new LinkedHashMap<>();


	static {
		map.put("123456:permission" , new HashSet<String>(Arrays.asList("test" ,"admin")));
		map.put("123456:role" , new HashSet<String>(Arrays.asList("add" ,"update" , "delete")));
	}



	//获取请求中的token
	@Override
	public String getToken(HttpServletRequest request) {
		return request.getHeader("token");
	}

  //校验token 并返回token主体
	@Override
	public Object validate(RequestToken<HttpServletRequest> requestToken) {
		String token = (String) requestToken.getCredentials();
		if (userToken.equals(token)){
			return userId;
		}
		return null;
	}
	//返回用户的权限列表 用于授权
	@Override
	public Set<String> getPermissions(Object primaryPrincipal) {
		return map.get(primaryPrincipal + ":permission");
	}
}
```

3. application.yml文件配置

   ```yml
   shiroplus:
     plus:
       filter-chain-definition:
         auth2: {path_rule} #path_rule是你需要进行权限控制的的路径规则 如 /** 符合ant匹配模式
   ```

   

##### 注解说明

1. @EnableShiroPlus 激活shiro plus

   | 属性          | 说明                                          | 默认值 |
   | ------------- | --------------------------------------------- | ------ |
   | dynamicAuthor | 是否开启动态授权                              | true   |
   | originAuthor  | 是否开启shiro原生的授权方式（基于注解的方式） | true   |
   | model         | 授权模型 可选 web,aop                         | web    |

2. @ShiroFilter 声明shiro filter

   | 属性  | 说明                                          | 默认值 |
   | ----- | --------------------------------------------- | ------ |
   | value | filter名称 用于配置过滤链使用 `/** ->{value}` | 无     |

3. @DynamicAuthorization 声明基于aop方式的动态授权

##### 授权注解说明

基于Aop模式的动态授权，必须标记`@DynamicAuthorization`,`@RequiresPermissions`,`@RequiresUser`,`@RequiresGuest`,`RequiresRoles`,`@RequiresAuthentication` 注解在方法上或类上

##### 元信息

###### Metadata

1. PermissionMetadata 权限元信息，用于动态授权 

   | 属性       | 说明                                                         | 默认值 |
   | ---------- | ------------------------------------------------------------ | ------ |
   | path       | 请求路径                                                     |        |
   | method     | 生效的请求方式 如 `get,put,...`,如果是基于aop授权模式，该字段可以为空 |        |
   | permis     | 访问该请求路径所需要的权限列表                               |        |
   | logical    | 权限校验逻辑，如果权限列表有多个值，是使用and还是or进行判断  | And    |
   | permiModel | 授权类型                                                     |        |

2. PermiModel 授权类型<a id = "permi_model"></a>

   | 枚举           | 说明                                       | Shiro 注解             |
   | -------------- | ------------------------------------------ | ---------------------- |
   | ROLE           | 基于角色标识授权                           | RequiresRoles          |
   | PERMISSION     | 基于权限标识授权                           | RequiresPermissions    |
   | AUTHENTICATION | 鉴权                                       | RequiresAuthentication |
   | PRINCIPAL      | 当前请求是否含有用户标识（是否非匿名访问） | RequiresGuest          |
   | USER           | 访问请求的用户是否存在                     | RequiresUser           |

3. GlobalMetadata

   | 属性                 | 说明                            | 默认值            |
   | -------------------- | ------------------------------- | ----------------- |
   | tenantId             | 租户id                          | default_tenant_Id |
   | anons                | 需要匿名访问的请求列表 支持 ant |                   |
   | enableAuthentication | 是否开启鉴权                    | true              |
   | enableAuthorization  | 是否开启授权                    | true              |

###### MetadataLoader 

- 元数据加载器

- 使用方式

  ```java
  public class SimpleMetadataLoader implements MetadataLoader {
  
  	private static final List<PermissionMetadata> ermissionMetadataList = new ArrayList<PermissionMetadata>();
  
  	private static final List<GlobalMetadata> globalMetadata = new ArrayList<GlobalMetadata>();
  
  	static {
  		// 权限配置
  		ermissionMetadataList.add(new PermissionMetadata("/user" , RequestMethod.POST , Arrays.asList("permi:add"), null , PermiModel.PERMISSION));
  		ermissionMetadataList.add(new PermissionMetadata("/user" , RequestMethod.DELETE , Arrays.asList("permi:delete"), null , PermiModel.PERMISSION));
  		ermissionMetadataList.add(new PermissionMetadata("/user" , RequestMethod.PUT , Arrays.asList("permi:put"), null , PermiModel.PERMISSION));
  		ermissionMetadataList.add(new PermissionMetadata("/user" , RequestMethod.GET , Arrays.asList("permi:get"), null , PermiModel.PERMISSION));
  		//全局配置
  		globalMetadata.add(new GlobalMetadata(null , Arrays.asList("") , true , true));
  	}
  
  
  	public List<PermissionMetadata> load() {
  		return ermissionMetadataList;
  	}
  
  	public List<GlobalMetadata> loadGlobal() {
  		return globalMetadata;
  	}
  }
  ```


##### 事件

###### 事件管理器(EventManager)

- EventManager#createDefaultEventBus() : 创建一个默认的事件总线
- EventManager#publish(EventObject):发布一个事件
- EventManager#asyncPublish(EventObject):发布一个异步事件
- EventManager#asyncPublish(EventObject event , Executor executor)：发布一个异步事件 ，需要指定一个线程池
- EventManager#register(EventListener)：注册一个事件监听器
- EventManager#register(Object)：注册一个或多个事件监听器，可以是 EventListener也可以是 @org.apache.shiro.event.Subscribe
- EventManager#unRegister(EventListener) : 移除一个事件监听器

###### 内置事件

- AuthMetadataEvent ：权限元信息变更事件
- AuthorizeEvent：授权操作事件

###### 自定义事件

```java
public class MyEvent implements java.util.EventObject{
  public MyEvent(Object source) {
		super(source);
	}

}
```

###### 发布事件

```java
private EventManager em

public static void main(String[] p){
  em.publish(new MyEvent("自定义的事件"))
}

```

###### 注册事件监听器

- 实现接口方式

  ```java
  @Component
  public class AuthEventListener implements EventListener {
  
  	//判断是否是当前事件监听器监听的事件
  	@Override
  	public boolean accepts(Object event) {
  		return false;
  	}
  	//事件处理
  	@Override
  	public void onEvent(Object o) {
  
  	}
  }
  ```

- 注解实现

  ```java
  @Component
  public class AuthEventListener {
  
  	//事件处理 入参为监听的事件类型 ，注解为shiro 包下的
  	@Subscribe
  	public void onAuthorSucceed(AuthorizeEvent authorizeEvent){
  		...
  	}
  
  }
  ```

  

##### 组件的注册方式

###### 注册shiro filter

```java
@ShiroFilter("login")
public class LoginAuthenticatingFilter extends AuthenticatingFilter {
  ...
}
```

###### 其他组件注册

通过spring bean的方式进行注册 [点击前往](#shiro_components) 如：

```
@Component
public class SimpleMetadataLoader implements MetadataLoader {
...
}
```

##### 配置说明

```yaml
shiroplus: 
  plus:
    filter-chain-definition: #filter 链定义。{filterName}(@ShiroFilter#value) ->  path
      login : /login # 这里的配置顺序会影响到请求的过滤顺序
    login-url: /login # 登录url
    success-url: /user # 登录成功回调的url
    unauthorized-url: /xxx #授权不成功回调url 该参数赞无使用 授权处理 可以使用授权异常处理器进行处理
    definitions: #定义配置文件路径 ，不推荐使用 具体可参考shiro官网
```

##### 内置过滤器

- 名称：auth2

- 实现类：`HttpServletAuthFilter`

- 使用方式

  ```yml
  shiroplus: 
    plus:
      filter-chain-definition: 
        auth2 : /** # 后面路径是你项目需要进行权限控制的路径
  ```

  



##### 扩展

###### 授权处理器

- AuthorizationHandler

- 缺省情况下支持shiro框架所有的授权实现

- 使用方式

  ```java
  @Component
  public class PermissionAuthorizationHandler implements AuthorizationHandler{
  
  
  	@Override
  	public void authorize(PermissionMetadata permissionMetadata) {
      // 授权处理
  		...
  	}
  
  	@Override
  	public boolean support(PermissionMetadata permissionMetadata) {
      //是否支持当前处理器
  		...
  	}
  
  }
  ```


###### 元数据加载器

- MetadataLoader

- 使用方式

  ```java
  @Component
  public class SimpleMetadataLoader implements MetadataLoader {
  
  
  
  	public List<PermissionMetadata> load() {
  		//加载权限元信息
  		...
  	}
  
  	public List<GlobalMetadata> loadGlobal() {
  		//加载全局元信息
  		...
  	}
  }
  
  ```

###### 授权异常处理器

- AuthProcessor<R ,S>

- 使用方式

  ```java
  @Component
  public class DefaultAuthExceptionHandler implements AuthProcessor<R ,S>  {
  
   		...
  
  		/**
  	 * 授权失败处理
  	 * @param request 请求对象
  	 * @param response 响应对象
  	 */
  	public void authorizationFailure(R request ,S response, AuthorizationException e){
  		throw e;
  	}
  
  
  	/**
  	 * 鉴权失败处理
  	 * @param request 请求对象
  	 * @param response  响应对象
  	 */
  	public void authenticationFailure(R request ,S response, AuthenticationException e){
  		throw e;
  	}
  	}
  ```

###### 多租户

- 自定义实现TenantIdGenerator

  ```
  public class DefaultTenantIdGenerator implements AuthProcessor<R ,S> {
  
  	...
  
  	/**
  	 * 生成租户id
  	 * @param r 请求对象
  	 * @return 返回租户id
  	 */
  	default String getTenantId(R r){
  		
  		return getDefaultTenantId();
  	}
  
  }
  ```

- 在`GlobalMetadata`中设置TenantId的属性值即可

######  AuthProcessor接口说明

**提示：shiro plus容器中有且只有一个实例**

```java
public interface AuthProcessor<R ,S> {

	/**
	 * 获取token
	 * <p>获取当前请求的token</p>
	 * @return 返回token
	 */
	String getToken(R request);

	/**
	 * 校验token 并返回token主体
	 * @param requestToken token
	 * @return 返回token主体 用于后续权限查询
	 */
	Object validate(RequestToken<R> requestToken);

	/**
	 * 获取权限列表
	 * <p>一般为权限标识</p>
	 * @param primaryPrincipal token 主体
	 * @return 返回权限列表 ，可以返回空
	 */
	Set<String> getPermissions(Object primaryPrincipal);

	/**
	 * 获取角色列表
	 * @param primaryPrincipal  token 主体
	 * @return 返回角色列表 ，可以返回空
	 */
	default Set<String> getRoles(Object primaryPrincipal){
		throw new UnsupportedOperationException();
	}


	/**
	 * 生成租户id
	 * @param r 请求对象
	 * @return 返回租户id
	 */
	default String getTenantId(R r){
		return getDefaultTenantId();
	}

	/**
	 * 授权失败处理
	 * @param request 请求对象
	 * @param response 响应对象
	 */
	default void authorizationFailure(R request ,S response, AuthorizationException e){
		throw e;
	}


	/**
	 * 鉴权失败处理
	 * @param request 请求对象
	 * @param response  响应对象
	 */
	default void authenticationFailure(R request ,S response, AuthenticationException e){
		throw e;
	}


	/**
	 * 返回默认的租户id
	 * @return 租户id
	 */
	default String getDefaultTenantId(){
		return AuthMetadataManager.DEFAULT_TENANT_ID;
	}
}
```

###### shiro 原生组件扩展明细<a id ="shiro_components"></a>

| 接口名称               | 说明                                                         |
| ---------------------- | ------------------------------------------------------------ |
| Realm                  | 一般用于处理授权和鉴权，shiro plus已做了相应实现，如非特殊场景不推荐 |
| CacheManager           | 缓存管理器                                                   |
| RememberMeManager      | 记住我功能的管理器                                           |
| SessionManager         | 会话管理器                                                   |
| Authorizer             | 授权处理器 不推荐自行扩展                                    |
| SubjectFactory         | 主体工厂 不推荐自行扩展                                      |
| Authenticator          | 鉴权处理器 不推荐自行扩展                                    |
| RolePermissionResolver | 角色权限标识的解析器用于实现不同的`Permission`接口实现       |
| PermissionResolver     | 权限标识的解析器 用于实现不同的`Permission`接口实现          |
| EventBus               | 事件主线 ，shirt plus 已做了处理 不推荐自行扩展              |
| AuthenticationStrategy | 鉴权策略 用于处理整个鉴权过程的生命周期                      |

### spring cloud gateway

##### 快速开始

- 激活shiro plus gateway

  ```java
  @SpringBootApplication
  @EnableShiroPlusAdminClient
  @EnableDiscoveryClient
  public class GatewayApplication {
  
  
  	public static void main(String[] args) {
  		SpringApplication.run(GatewayApplication.class , args);
  	}
  
  
  }
  ```

- 实现自己的权限统一处理器

  ```java
  @Component
  public class ExampleHttpGatewayAuthProcessor extends HttpGatewayAuthProcessor {
  
  
  	private static Map<Object , Set<String>> map = new HashMap<>();
  
  	private String userToken = "abc123456";
  	static {
  		map.put("123456" + ":roles" , new HashSet<String>(Arrays.asList("test" ,"admin")));
  		map.put("123456" + ":permi" , new HashSet<String>(Arrays.asList("add" ,"update" , "delete","get")));
  	}
  
  	@Override
  	public String getToken(ServerHttpRequest request) {
  		return request.getQueryParams().getFirst("token");
  	}
  
  	@Override
  	public Object validate(RequestToken<ServerHttpRequest> requestToken) {
  		String token = (String) requestToken.getPrincipal();
  		if (userToken.equals(token)){
  			return "123456";
  		}
  		return null;
  	}
  
  
  	@Override
  	public Set<String> getPermissions(Object primaryPrincipal) {
  		return map.get(primaryPrincipal + ":permi");
  	}
  
  	@Override
  	public Set<String> getRoles(Object primaryPrincipal) {
  		return map.get(primaryPrincipal + ":roles");
  	}
  }
  ```

- 配置说明

  ```yaml
  spring:
      gateway:
        routes:
            filters:
             # shiro plus 权限控制内置过滤器名称
              - name: Auth
                args:
                	# 指定租户Id 的参数名称 用于从请求中获取租户id
                  tenantName: tenantId 
                  # 指定获取租户id的策略 可选枚举值 COOKIE ,HEAD ,QUERY(?tenantId=xxx的形式),PATH(/shiro/{tenantId}的形式)
                  tenantStrategy: PATH 
                  #指定token 的参数名称 用于从请求中获取token
                  tokenName: token 
                  #指定获取租户id的策略 可选枚举值 COOKIE ,HEAD ,QUERY,PATH (QUERY,PATH 都是以?token=xxx的形式 )
                  tokenStrategy: PATH
                  # 如果租户的获取策略为 path 则 获取第第几层的路径作为租户id  如 /api/shiroplus/xxx  如果值为1则租户id为 shiroplus
                  tenantPathIndex: 1
                  # 接口授权的请求路径从哪来开始进行匹配 如 /api/shiroplus/user 如果值为 /api 则实际接口授权的路径为/shiroplus/user
                  authorizationPathPrefix: /api 
  ```

##### 扩展

如果上述功能都无法满足你的需求 则推荐继承`HttpGatewayAuthProcessor`来进行方法重写

### 元数据中心-nacos

##### 快速开始

- 激活shiro plus nacos 元数据中心

  ```
  @EnableShiroPlus
  //激活nacos的支持
  @EnableShiroPlusNacos(@NacosProperties(serverAddr = "localhost:8848"))
  public class Application {
  
  	public static void main(String[] args) {
  		SpringApplication.run(Application.class , args);
  	}
  }
  ```

- 配置

  1. 通过`@EnableShiroPlusNacos#value`的方式进行指定nacos的配置

  2. 通过yaml文件的方式指定nacos的配置

     `application.yml`

     ```yaml
     nacos:
     	serverAddr: localhost:8848
     ```

- 和nacos配置的通用之处

  - 如果你的项目已经使用了nacos那么shiro plus nacos会共享你的全局配置
  - 如果你想单独指定nacos那么执行使用nacos的原生注解`@NacosProperties`指定给`@EnableShiroPlusNacos#value`即可

##### 配置说明

- 本地配置

```yaml
shiroplus:
  nacos:
    config-type: yaml # nacos的配置格式
    timeout: 1000 # nacos的超时时长
    group: SHIR_PLUS_METADATA #元数据所属nacos分组
```

- 远程配置（nacos server）

  - permi-model 可以参考 [点击前往](#permi_model)

  - 如果远程想使用`json`,`xml`,`properties`则只需要保证如下配置规则即可

  - 权限元数据配置

    - dataId: org.codingeasy.shiroplus.permission.metadata <u>**不可改变为常量**</u>
    - group: SHIR_PLUS_METADATA

    ```yaml
    /user: # 请求path
        get: # 如果是web请求 则为请求方法 
            permis: permi:get1 # 该请求所需要的权限，多个用逗号隔开
            logical: and # 权限的校验逻辑  可选 and 和 or
            permi-model: permission # 校验模式 可选 permission ,user ,
        post:
            permis: permi:add
            logical: and
            permi-model: permission
        delete:   
            permis: permi:delete
            logical: and
            permi-model: permission 
        put:   
            permis: permi:put
            logical: and
            permi-model: permission   
    #如果 有多个path配置则在下面追加上面格式配置即可 
    ```

  - 全局元数据配置

    - dataId:org.codingeasy.shiroplus.global.metadata <u>**不可改变为常量**</u>
    - group:SHIR_PLUS_METADATA

    ```yaml
    default_tenant_Id: #租户id
        anons: /u** # 需要忽略的请求 多个逗号隔开 
        enable-authentication: true # 鉴权总开关
        enable-authorization: true #授权总开关
    default_tenant_Id2: #租户2
		anons: /u** # 需要忽略的请求 多个逗号隔开 
        enable-authentication: true # 鉴权总开关
        enable-authorization: true #授权总开关
    ```
    
    

### 元数据中心-shiroplus admin

待完成

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
