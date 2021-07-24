<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades">用户列表</i>
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-button
          type="primary"
          icon="el-icon-edit"
          class="handle-del mr10"
          @click="handlingAdd"
          >新增</el-button
        >
        <el-input
          v-model="query.username"
          placeholder="用户名"
          class="handle-input mr10"
        ></el-input>
        <el-input
          v-model="query.nickname"
          placeholder="用户昵称"
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
        <el-table-column prop="nickname" label="用户昵称"></el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          :formatter="formatterStatus"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="createTm"
          :formatter="formatterDate"
          label="创建时间"
        ></el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button
              type="text"
              icon="el-icon-edit"
              @click.stop="handleRestPassword(scope.$index, scope.row)"
              >重置密码</el-button
            >
            <el-button
              type="text"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.$index, scope.row)"
              >编辑</el-button
            >
            <el-button
              type="text"
              icon="el-icon-delete"
              class="red"
              @click.stop="handleDelete(scope.$index, scope.row)"
              >删除</el-button
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

    <add-or-update
      :open="addOrUpdate.modalOpen"
      :action-sate="addOrUpdate.actionSate"
      :title-describe="addOrUpdate.titleDescribe"
      :user-id="addOrUpdate.userId"
      @operation-complete="handleSearch"
      @cancel="cancel"
    />
  </div>
</template>

<script>
import userApi from "../../api/UserApi";
import addOrUpdate from "./AddOrUpdate.vue";
export default {
  name: "user",
  data() {
    return {
      query: {
        username: "", //登录名称
        nickname: "", //用户昵称
        pageNo: 1,
        pageSize: 10,
      },
      //用户 查看 新增 编辑 组件属性
      addOrUpdate: {
        modalOpen: false,
        actionSate: 1,
        titleDescribe: "新增",
        userId: "",
      },
      tableData: [],
      pageTotal: 0,
    };
  },
  created() {
    this.handleSearch();
  },
  methods: {
    getData() {
      userApi.getPageList(this.query).then((page) => {
        if (page) {
          console.log(page);
          this.tableData = page.list;
          this.pageTotal = page.total || 0;
        }
      });
    },
    //新增操作
    handlingAdd() {
      this.addOrUpdate.actionSate = 1;
      this.addOrUpdate.titleDescribe = "新增";
      this.addOrUpdate.modalOpen = true;
    },
    // 编辑操作
    handleEdit(index, row) {
      this.addOrUpdate.actionSate = 3;
      this.addOrUpdate.userId = row.id;
      this.addOrUpdate.titleDescribe = "编辑";
      this.addOrUpdate.modalOpen = true;
    },
    // 删除操作
    handleDelete(index, row) {
      // 二次确认删除
      this.$confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          userApi.delete(row.id).then(() => {
            this.$message.success("删除成功");
            //刷新列表
            this.handleSearch();
          });
        })
        .catch(() => {});
    },
    //重置密码
    handleRestPassword(index, row) {
      // 二次确认删除
      this.$confirm("确定要重置吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          userApi.restPassword(row.id).then(() => {
            this.$message.success("重置成功");
            //刷新列表
            this.handleSearch();
          });
        })
        .catch(() => {});
    },
    //查看
    look(row) {
      this.addOrUpdate.actionSate = 2;
      this.addOrUpdate.userId = row.id;
      this.addOrUpdate.titleDescribe = "查看";
      this.addOrUpdate.modalOpen = true;
    },
    //取消模态框
    cancel() {
      this.addOrUpdate.modalOpen = false;
      this.addOrUpdate.userId = "";
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
    },
    /**
     * 格式化相关
     */
    formatterDate(row, column, cellValue) {
      if (!cellValue) {
        return cellValue;
      }
      return this.dayutil(cellValue).format("YYYY-MM-DD HH:mm:ss");
    },
    formatterStatus(row, column, cellValue) {
      return cellValue ? (cellValue === 1 ? "正常" : "禁用") : cellValue;
    },
  },
  inject: ["dayutil"],
  components: {
    addOrUpdate,
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
