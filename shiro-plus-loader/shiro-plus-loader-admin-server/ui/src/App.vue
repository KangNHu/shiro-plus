<template>
    <router-view />
</template>

<script>
import dayjs from "dayjs"

export default {
    provide:{
        dayutil:dayjs
    }
};
</script>

<style>
@import "./assets/css/main.css";
@import "./assets/css/color-dark.css";
</style>
