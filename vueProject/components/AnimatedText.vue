<script setup>
import { ref, onMounted, withCtx } from 'vue'

const fullText = '欢迎来到论文管理系统！';
const displayText = ref('');
const isShowing = ref(true);

let index = 0;
let isAppearing = true;

const animateText = async () => {
  while (isAppearing) {
    if (index < fullText.length) {
      displayText.value += fullText[index++];
      await new Promise(resolve => setTimeout(resolve, 500));
    } else {
      await new Promise(resolve => setTimeout(resolve, 500));
      isAppearing = false;
    }
  }
}

onMounted(() => {
  animateText();
});
</script>

<template>
  <div class="animated-text">
    <h2>{{ displayText}}</h2>
  </div>
</template>

<style scoped>
.animated-text {
  text-align: center;
  font-size: 30px;
  font-weight:lighter;
  font-family: 'Arial';
  z-index: 999;
  color: #060606;
}
.animated-text >h2{
  height: 1.5em;
}
</style>
