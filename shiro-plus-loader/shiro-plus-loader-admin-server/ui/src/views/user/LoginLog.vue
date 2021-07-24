<template>
  <div class="infinite-list-wrapper" style="overflow: auto">
    <ul class="list" v-infinite-scroll="load" infinite-scroll-disabled="noMore">
      <li
        v-for="log in logs"
        :key="log.id"
        style="box-shadow: 0 1px 1px 0 rgba(0, 0, 0, 0.1)"
        class="font"
      >
        {{ handLogTime(log.createTm) }} {{ log.context }}
      </li>
      <el-row
        style="height: 27.3px"
        v-if="!noMore"
        v-loading="!noMore"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.3)"
      ></el-row>
      <p class="font" style="text-align: center;" v-if="noMore">没有更多了</p>
    </ul>
  </div>
</template>

<script>
import ua from "../../api/UserApi";
import day from "dayjs";
import isToday from "dayjs/plugin/isToday";
import customParseFormat from "dayjs/plugin/customParseFormat"
day.extend(customParseFormat);
day.extend(isToday);
export default {
  data() {
    return {
      page: {
        pageNo: 1,
        pageSize: 10,
      },
      logs: [],
      pages: 2,
    };
  },
  computed: {
    noMore() {
      return this.page.pageNo > this.pages;
    },
  },
  methods: {
    //滑块滑动加载
    load() {
      this.getData();
    },
    //获取数据
    getData() {
      ua.getCurrentUserloginLog(this.page).then((page) => {
        this.page.pageNo++;
        this.pages = Math.ceil(page.total / this.page.pageSize);
        page.list.forEach((value) => {
          this.logs.push(value);
        });
      });
    },
    //处理登录时间
    handLogTime(time) {
      let date = day(time);
      if (date.isToday()) {
        return "今天" + "      " + date.format("HH:mm:ss");
      }
      return date.format("YYYY-MM-DD HH:mm:ss");
    },
  },
};
</script>

<style>
.font {
  font-size: 14px;
  color: rgb(96, 98, 102);
}

.infinite-list-wrapper {
  width: 100%;
  height: 100%;
}
.list {
  width: 100%;
}
.list li {
  height: 27.3px;
  margin: 5px 0;
  list-style: decimal;
}
</style>