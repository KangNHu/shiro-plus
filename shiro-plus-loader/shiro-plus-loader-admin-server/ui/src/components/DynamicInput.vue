<template>
  <el-space v-for="(v, index) in options" :key="index" wrap>
    <el-input
      style="width: 230px"
      v-model="options[index]"
      @blur="change"
      :placeholder="placeholder"
      :disabled="diDisabled"
    />
    <el-button
      @click="addLine"
      type="primary"
      icon="el-icon-edit"
      circle
      v-if="!diDisabled"
    ></el-button>
    <el-button
      v-if="(index > 0) & !diDisabled"
      @click="delLine(index)"
      type="danger"
      icon="el-icon-delete"
      circle
    ></el-button>
  </el-space>
</template>

<script>
export default {
  data() {
    return {
      options: [],
      watch: true,
    };
  },
  props: {
    values: {
      type: Array,
      required: true,
    },
    diDisabled: {
      type: Boolean,
      default: false,
    },
    placeholder: {
      type: String,
      default: "请输入",
    },
  },
  created() {
    this.initData();
    this.$watch("values", (newVal) => {
      this.initData(newVal);
    });
  },

  emits: ["change"],
  methods: {
    //删除一行
    delLine(index) {
      this.options.splice(index, 1);
    },
    //添加一行
    addLine() {
      this.options.push("");
    },
    change() {
      this.watch = false;
      this.$emit("change", this.options);
    },
    //初始化数据
    initData(newVal) {
      if (!this.watch) {
        return;
      }
      let array = newVal;
      if (!this.diDisabled && (!array || array.length == 0)) {
        array = [""];
      }
      this.options = array;
    },
  },
};
</script>

<style>
</style>