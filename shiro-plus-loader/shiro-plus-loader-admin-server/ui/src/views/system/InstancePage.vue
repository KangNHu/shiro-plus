<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>系统设置</el-breadcrumb-item>
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades">客户端实例管理</i>
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="handle-box">
      <el-input
        v-model="query.name"
        placeholder="应用名称"
        class="handle-input mr10"
      ></el-input>
      <el-input
        v-model="query.ip"
        placeholder="IP"
        class="handle-input mr10"
      ></el-input>
      <el-button type="primary" icon="el-icon-search" @click="handleSearch"
        >搜索</el-button
      >
    </div>
    <div class="container">
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
        <el-table-column prop="name" label="实例应用名称"></el-table-column>
        <el-table-column prop="code" label="实例编码"></el-table-column>
        <el-table-column prop="ip" label="实例IP"></el-table-column>
        <el-table-column
          prop="lastPingTime"
          :formatter="formatterDate"
          label="最后活跃时间"
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
  </div>
</template>

<script>
import sa from "../../api/SystemApi";
export default {
  name: "user",
  data() {
    return {
      query: {
        name: "", //实例的应用名称
        ip: "", //实例的ip
        pageNo: 1,
        pageSize: 10,
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
      sa.instancePage(this.query).then((page) => {
        if (page) {
          this.tableData = page.list;
          this.pageTotal = page.total || 0;
        }
      });
    },
    // 删除操作
    handleDelete(index, row) {
      // 二次确认删除
      this.$confirm("确定要删除吗？", "提示", {
        type: "warning",
      })
        .then(() => {
          sa.deleteInstance(row.id).then(() => {
            this.$message.success("删除成功");
            //刷新列表
            this.handleSearch();
          });
        })
        .catch(() => {});
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
  },
  inject: ["dayutil"],
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
