<script setup>
import { ref, onMounted } from 'vue'

const fullText = '欢迎来到论文管理系统!';
const displayText = ref('');
const isShowing = ref(true);

let index = 0;
let isAppearing = true;

const animateText = async () => {
  while (true) {
    if (isAppearing) {
      if (index < fullText.length) {
        displayText.value += fullText[index++];
        await new Promise(resolve => setTimeout(resolve, 500));
      } else {
        await new Promise(resolve => setTimeout(resolve, 500));
        isAppearing = false;
      }
    } else {
      if (index > 0) {
        index--;
        displayText.value = fullText.substring(0, index);
        await new Promise(resolve => setTimeout(resolve, 500));
      } else {
        isAppearing = true;
      }
    }
  }
}

onMounted(() => {
  animateText();
});
</script>

<template>
  <div class="animated-text">
    <h2>{{ displayText }}</h2>
  </div>
</template>

<style scoped>
.animated-text {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #060606;
  margin-top: 500px; 
}
</style>
