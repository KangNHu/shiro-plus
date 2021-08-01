<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades">API管理</i>
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="handle-box">
      <el-input
        v-model="query.path"
        placeholder="接口路径"
        class="handle-input mr10"
      ></el-input>
     <dict-select
          hint="请求方式"
          v-model="query.method"
          dict-code="method_code"
          class="mr10"
        />
      <el-button
        type="primary"
        class="mr10"
        icon="el-icon-search"
        @click="handleSearch"
        >搜索</el-button
      >
      <el-upload
      class="upload-demo"
        action="#"
        :multiple="false"
        :limit="1"
        :before-upload="beforeUpload"
        :file-list="fileList"
        :beforeUpload="beforeUpload"
        :http-request="uploading"
        :show-file-list="false"
      >
        <el-button type="primary">导入API</el-button>
      </el-upload>
    </div>
    <div class="container">
      <el-table
        :data="tableData"
        border
        class="table"
        ref="multipleTable"
        header-cell-class-name="table-header"
      >
        <el-table-column
          prop="id"
          label="ID"
          width="55"
          align="center"
        ></el-table-column>
        <el-table-column prop="path" label="接口URl"></el-table-column>
        <el-table-column label="请求方法" prop="method">
          <template #default="scope">
            <el-tag>{{
              du.getDictValue("method_code", scope.row.method)
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="summary" label="创建时间"></el-table-column>
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
import ca from "../../../api/ConfigApi";
import du from "../../../utils/DictUtils";
import DictSelect from "../../../components/DictSelect.vue";
export default {
  name: "user",
  data() {
    return {
      query: {
        path: "", //接口路径
        method:''//请求方式
      },
      tableData: [],
      pageTotal: 0,
      fileList: [],
      du: du,
    };
  },
  created() {
    this.handleSearch();
  },
  components:{
    DictSelect
  },
  methods: {
    getData() {
      ca.openApiPage(this.query).then((page) => {
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
          ca.deleteOpenApi(row.id).then(() => {
            this.$message.success("删除成功");
            //刷新列表
            this.handleSearch();
          });
        })
        .catch(() => {});
    },
    //上传
    uploading(data) {
      ca.importOpenApi(data.file).then(() => {
        this.fileList = [];
        this.$message.success("上传成功");
        this.handleSearch();
      });
    },
    //上传前
    beforeUpload(file) {
      //必须为json 且不能超过30m
      if (!file.name.endsWith(".json") || file.size > 1024 * 1024 * 30) {
        this.$message.error("文件必须为.json类型 ,且文件大小不能超过30m");
        return false;
      }
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
  },
};
</script>

<style  lang="scss" scoped>
.handle-box {
  margin-bottom: 20px;
    margin:0 auto;
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
.upload-demo {
   display: inline-block;
   height:40px;
   width:89px;
   line-height:40px;
   margin: 0;
   padding: 0;
   float: left;
   margin-right: 10px;
}
.upload-demo ::v-deep .el-upload--text{
  height:40px;
  width:89px;
  line-height:40px;
  display: inline-block;
  margin: 0;
  padding: 0;
  border:0ch
}
</style>
