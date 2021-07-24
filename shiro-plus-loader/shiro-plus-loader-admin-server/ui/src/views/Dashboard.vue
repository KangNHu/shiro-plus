<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="mgb20 box-card">
          <div class="user-info">
            <img src="../assets/img/img.jpg" class="user-avator" alt />
            <div class="user-info-cont">
              <div>{{ nickname }}</div>
            </div>
          </div>
        </el-card>
        <el-card shadow="hover" class="box-card">
          <template #header>
            <div class="card-heade">
              <span>登录日志</span>
            </div>
          </template>
          <login-log style="height: 205px"></login-log>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="hover" class="xxx" style="height: 507px">
          <template #header>
            <div class="card-heade">
              <span>系统操作日志</span>
              <el-select
                class="left_interval"
                v-model="logsQuery.operateId"
                filterable
                remote
                reserve-keyword
                placeholder="操作人"
                :remote-method="userSelectData"
                :loading="userSelectLoading"
              >
                <el-option
                  class="left_interval"
                  v-for="item in userSelectOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
              <dict-select dict-code="business_code" v-model="logsQuery.businessCode"/>
              <el-button
                class="left_interval"
                type="primary"
                icon="el-icon-search"
                @click="handleLogsSearch"
                >搜索</el-button
              >
            </div>
          </template>
          <el-table :show-header="false" :data="logs" style="width: 100%">
            <el-table-column show-overflow-tooltip = "true">
              <template #default="scope" >
                <div class="todo-item">
                  {{ scope.row.context }}
                </div>
              </template>
            </el-table-column>
            <el-table-column width="120" prop="operateName"></el-table-column>
            <el-table-column width="200" prop="createTime" :formatter = "formatterDate"></el-table-column>
          </el-table>
          <el-pagination
            :page-size="logsQuery.pageSize"
            :current-page="logsQuery.pageNo"
            layout="prev, pager, next"
            :total="total"
            @current-change="handleLogsPageChange"
          >
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
          <schart
            ref="bar"
            class="schart"
            canvasId="bar"
            :options="options"
          ></schart>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <schart
            ref="line"
            class="schart"
            canvasId="line"
            :options="options2"
          ></schart>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import DictSelect from "../components/DictSelect.vue"
import Schart from "vue-schart";
import ut from "../utils/userUtils";
import ua from "../api/UserApi";
import LoginLog from "../views/user/LoginLog.vue";
import bc from "../api/BusinessCode";
import sa from "../api/SystemApi";
export default {
  name: "dashboard",
  data() {
    return {
      logsQuery: {
        pageNo: 1,
        pageSize: 8,
        businessCode: undefined,
        operateId: undefined,
      },
      total: 1,
      userSelectLoading: false,
      userSelectOptions: [],
      businessCodeSelectOptions: bc.dict,
      name: localStorage.getItem("ms_username"),
      logs: [],
      data: [
        {
          name: "2018/09/04",
          value: 1083,
        },
        {
          name: "2018/09/05",
          value: 941,
        },
        {
          name: "2018/09/06",
          value: 1139,
        },
        {
          name: "2018/09/07",
          value: 816,
        },
        {
          name: "2018/09/08",
          value: 327,
        },
        {
          name: "2018/09/09",
          value: 228,
        },
        {
          name: "2018/09/10",
          value: 1065,
        },
      ],
      options: {
        type: "bar",
        title: {
          text: "最近一周各品类销售图",
        },
        xRorate: 25,
        labels: ["周一", "周二", "周三", "周四", "周五"],
        datasets: [
          {
            label: "家电",
            data: [234, 278, 270, 190, 230],
          },
          {
            label: "百货",
            data: [164, 178, 190, 135, 160],
          },
          {
            label: "食品",
            data: [144, 198, 150, 235, 120],
          },
        ],
      },
      options2: {
        type: "line",
        title: {
          text: "最近几个月各品类销售趋势图",
        },
        labels: ["6月", "7月", "8月", "9月", "10月"],
        datasets: [
          {
            label: "家电",
            data: [234, 278, 270, 190, 230],
          },
          {
            label: "百货",
            data: [164, 178, 150, 135, 160],
          },
          {
            label: "食品",
            data: [74, 118, 200, 235, 90],
          },
        ],
      },
    };
  },
  components: {
    Schart,
    LoginLog,
    DictSelect
  },
  created() {
    this.handleLogsSearch();
  },
  computed: {
    nickname() {
      let userInfo = ut.getCurrentUser();
      return userInfo ? userInfo.nickname : "未知";
    },
  },
  inject: ["dayutil"],
  methods: {
    changeDate() {
      const now = new Date().getTime();
      this.data.forEach((item, index) => {
        const date = new Date(now - (6 - index) * 86400000);
        item.name = `${date.getFullYear()}/${
          date.getMonth() + 1
        }/${date.getDate()}`;
      });
    },
    //用户下拉数据获取方法
    userSelectData(query) {
      this.userSelectLoading = true;
      ua.getUserSelectData(query).then((list) => {
        this.userSelectLoading = false;
        if (list) {
          this.userSelectOptions = list.map((item) => {
            return { value: item.id, label: item.username };
          });
        }
      });
    },
    //搜索日志列表
    handleLogsSearch() {
      this.logsQuery.pageNo = 1;
      this.getLogsData()
    },
    //获取分页数据
    getLogsData() {
      sa.logsPage(this.logsQuery).then((page) => {
        if (page) {
          this.total = page.total;
          this.logs = page.list;
        }
      });
    },
    // 分页导航
    handleLogsPageChange(val) {
      this.logsQuery.pageNo = val;
      this.getLogsData();
    },
    //时间格式化
    dateFormat(date){
          if(!date){
               return; 
          }  
          return this.dayutil(date).format("YYYY-MM-DD HH:mm:ss");
    },
    //列的时间格式化
    formatterDate(row, column, cellValue){
        return this.dateFormat(cellValue);
    }
  },
};
</script>

<style lang="scss" scoped>
.el-row {
  margin-bottom: 20px;
}
.left_interval {
  margin-left: 10px;
}

.grid-con-icon {
  font-size: 50px;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
  color: #fff;
}

.user-avator {
  width: 120px;
  height: 120px;
  border-radius: 50%;
}
.user-info {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #ccc;
  margin-bottom: 20px;
}

.user-info-cont {
  padding-left: 50px;
  flex: 1;
  font-size: 14px;
  color: #999;
}

.user-info-cont div:first-child {
  font-size: 30px;
  color: #222;
}

.mgb20 {
  margin-bottom: 20px;
}

.todo-item {
  font-size: 14px;
}

.logs_header .el-card .el-card__header {
  height: 50px;
  margin: 0;
  padding: 0;
}
.schart {
  width: 100%;
  height: 300px;
}
</style>
