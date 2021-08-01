<template>
 <el-button
      @click="addLine"
      type="primary"
      icon="el-icon-edit"
      circle
      v-if="options.length === 0&&!dmDisabled"
    ></el-button>
  <el-space v-for="(v, index) in options" :key="index" wrap>
    <el-input
      style="width: 100px"
      autosize
      v-model="options[index].key"
      :placeholder="keyPlaceholder"
      :disabled="dmDisabled"
      @blur="change"
    />
    <span>：</span>
    <el-input
      style="width: 100px"
      autosize
      v-model="options[index].value"
      :placeholder="valuePlaceholder"
      :disabled="dmDisabled"
      @blur="$emit('change', options)"
    />
    <el-button
      @click="addLine"
      type="primary"
      icon="el-icon-edit"
      v-if="!dmDisabled"
      circle
    ></el-button>
    <el-button
      v-if="(options.length > 1 || (options.length === 1 && !required)) && !dmDisabled"
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
    dmDisabled: {
      type: Boolean,
      default: false,
    },
    keyPlaceholder: {
      type: String,
      default: "请输入key",
    },
    valuePlaceholder: {
      type: String,
      default: "请输入vlaue",
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
      this.options.push({ key: "", value: "" });
    },
     change(){
      this.watch =false;
      this.$emit('change' , this.options)
    },
  },
};
</script>

<style>
</style>