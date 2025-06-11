<template>
  <div ref="chartRef" class="chart-container"></div>
</template>

<script setup>
import * as echarts from 'echarts'
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'

const props = defineProps({
  center: String
})

const chartRef = ref(null)
let chartInstance = null

const initChart = () => {
  if (chartRef.value) {
    chartInstance = echarts.init(chartRef.value)
    chartInstance.setOption(getChartOption(props.center))
    window.addEventListener('resize', resizeChart)
  }
}

const getChartOption = (center) => ({
  title: {
    text: '关键词关系图',
    left: 'center'
  },
  tooltip: {},
  series: [
    {
      type: 'graph',
      layout: 'force',
      roam: true,
      label: { show: true },
      force: { repulsion: 5000 },
      data: [
        { name: center, symbolSize: 50 },
        { name: '节点1', symbolSize: 30 },
        { name: '节点2', symbolSize: 30 },
        { name: '节点3', symbolSize: 30 },
        { name: '节点4', symbolSize: 30 }
      ],
      links: [
        { source: center, target: '节点1' },
        { source: center, target: '节点2' },
        { source: center, target: '节点3' },
        { source: center, target: '节点4' }
      ]
    }
  ]
})

const resizeChart = () => {
  chartInstance?.resize()
}

onMounted(() => {
  setTimeout(initChart, 100) // 确保 dialog 渲染完毕后再初始化
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeChart)
  chartInstance?.dispose()
})

watch(() => props.center, (newCenter) => {
  if (chartInstance) {
    chartInstance.setOption(getChartOption(newCenter))
  }
})
</script>

<style scoped>
.chart-container {
  width: 100%;
  height: 100%;
}
</style>
