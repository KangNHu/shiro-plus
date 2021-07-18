# shiro plus admin server

## 角色权限说明

| 角色                 | 角色标识                        | 说明                              |
| -------------------- | ------------------------------- | --------------------------------- |
| 用户管理(读)         | admin:user:read                 | 只有用户管理查看权限              |
| 用户管理(读写)       | admin:user:read:write           | 用户管理的查看和编辑权限          |
| 系统配置(读)         | admin:systemSetting:read        | 只有系统设置-全局配置的查看权限   |
| 系统配置(读写)       | admin:systemSetting:read:write  | 系统设置-全局配置的产看和编辑权限 |
| 权限管理(读)         | admin:role:read                 | 只有权限管理的查看权限            |
| 权限管理(读写)       | admin:role:read:write           | 权限管理的查看和编辑权限          |
| 全局元数据管理(读)   | admin:globalMetadata:read       | 只有全局元数据的查看权限          |
| 全局元数据管理(读写) | admin:globalMetadata:read:write | 只有全局元数据的查看和编辑的权限  |
| 权限元数据管理(读)   | admin:permiMetadata:read        | 权限元数据管理的查看权限          |
| 权限元数据管理(读写) | admin:permiMetadata:read:write  | 权限元数据管理的查看和编辑权限    |

