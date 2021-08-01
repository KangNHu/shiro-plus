<template>
  <el-button
    @click="addLine"
    type="primary"
    icon="el-icon-edit"
    circle
    v-if="options.length === 0&&!diDisabled"
  ></el-button>
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
      v-if="
        (options.length > 1 || (options.length === 1 && !required)) &&
        !diDisabled
      "
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
    required: {
      type: Boolean,
      default: false,
    },
    placeholder: {
      type: String,
      default: "请输入",
    },
  },
  created() {
    this.options = this.values;
    this.$watch("values", (newVal) => {
      this.options = newVal;
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
  },
};
</script>

<style>
</style>