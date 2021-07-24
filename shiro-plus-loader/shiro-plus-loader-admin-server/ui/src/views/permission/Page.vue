<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades">系统设置</i>
        </el-breadcrumb-item>
        <el-breadcrumb-item>权限管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-input
          v-model="query.username"
          placeholder="用户名"
          class="handle-input mr10"
        ></el-input>
        <el-button type="primary" icon="el-icon-search" @click="handleSearch"
          >搜索</el-button
        >
      </div>
      <el-table
        :data="tableData"
        border
        class="table"
        ref="multipleTable"
        header-cell-class-name="table-header"
        @row-click="look"
      >
        <el-table-column
          prop="id"
          label="ID"
          width="55"
          align="center"
        ></el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column label="角色集" show-overflow-tooltip = "true">
            <template #default="scope">
                <div v-if="scope.row.roles">
                  <el-tag v-for="role in scope.row.roles" :key="role.id" >{{role.name}}</el-tag>
                </div>
            </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button
              type="text"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.$index, scope.row)"
              >编辑</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :current-page="query.pageNo"
          :page-size="query.pageSize"
          :total="pageTotal"
          @current-change="handlePageChange"
        ></el-pagination>
      </div>
    </div>

   <update
      :open="addOrUpdate.modalOpen"
      :action-sate="addOrUpdate.actionSate"
      :title-describe="addOrUpdate.titleDescribe"
      :user-id ="addOrUpdate.userId"
      @operation-complete ="handleSearch"
      @cancel = "cancel"
    />
  </div>
</template>

<script>
import pa from "../../api/PermissionApi";
import Update from "./Update.vue";
export default {
  name: "user",
  data() {
    return {
      query: {
        username: "", //登录名称
        pageNo: 1,
        pageSize: 10,
      },
      //用户 查看 新增 编辑 组件属性
      addOrUpdate: {
        modalOpen: false,
        actionSate: 1,
        titleDescribe: "新增",
        userId:''
      },
      tableData: [],
      pageTotal: 0,
      form: {},
      idx: -1,
      id: -1,
    };
  },
  created() {
    this.handleSearch();
  },
  methods: {
    getData() {
      pa.page(this.query).then((page) => {
        if (page) {
          console.log(page);
          this.tableData = page.list;
          this.pageTotal = page.total || 0;
        }
      });
    },
    // 编辑操作
    handleEdit(index, row) {
      this.addOrUpdate.actionSate = 2;
      this.addOrUpdate.userId = row.id;
      this.addOrUpdate.titleDescribe = "编辑";
      this.addOrUpdate.modalOpen = true;
    },
    //查看
    look(row){
       this.addOrUpdate.actionSate = 1;
       this.addOrUpdate.userId = row.id;
       this.addOrUpdate.titleDescribe = "查看";
       this.addOrUpdate.modalOpen = true;
    },
    //取消模态框
    cancel(){
        this.addOrUpdate.modalOpen = false;
        this.addOrUpdate.userId ='';
    },
    // 触发搜索按钮
    handleSearch() {
      this.query.pageNo = 1;
      this.getData();
    },
    // 分页导航
    handlePageChange(val) {
      this.query.pageNo = val;
      this.getData();
    }
  },
  inject:['dayutil'],
  components: {
    Update,
  },
};
</script>

<style scoped>
.handle-box {
  margin-bottom: 20px;
}

.handle-select {
  width: 120px;
}

.handle-input {
  width: 300px;
  display: inline-block;
}
.table {
  width: 100%;
  font-size: 14px;
}
.red {
  color: #ff0000;
}
.mr10 {
  margin-right: 10px;
}
.table-td-thumb {
  display: block;
  margin: auto;
  width: 40px;
  height: 40px;
}
</style>
