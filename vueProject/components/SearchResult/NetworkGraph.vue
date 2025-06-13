<template>
  <div ref="chartRef" class="chart-container"></div>
</template>

<script setup>
import * as echarts from 'echarts'
import { ref, onMounted, onBeforeUnmount, watch,computed } from 'vue'

const props = defineProps({
  center: String,
})
const centerName = '中心节点'

/**
 * @type {Array<{name:string,lable:string,symbolSize:int,itemStyle:Object,draggable:bool}>}
 */
const nodes = ref([
  { name: '节点1', displayName:'hello',symbolSize: 40, itemStyle: { color: '#83bff6' }, draggable: true },
])

/**
 * @type {Array<{source:string,target:string,lineStyle:Object}>}
 */
const edges = ref([
  { source: centerName, target: '节点1', lineStyle: { color: '#a0c4ff', width: 2 } },
])

const graphData = computed(() => [
  {
    name: centerName,
    symbolSize: 60,
    itemStyle: {
      color: '#ff7f50',
      shadowBlur: 15,
      shadowColor: 'rgba(255, 127, 80, 0.8)'
    },
    label: {
      fontSize: 18,
      fontWeight: 'bold',
      color: '#ff5722'
    },
    draggable: true
  },
  ...nodes.value.filter(n => n.name !== centerName)
])


const chartRef = ref(null)
let chartInstance = null
const getChartOption = () => ({
  backgroundColor: '#f9f9f9',
  // title: {
  //   text: '论文关系网络',
  //   left: 'center',
  //   textStyle: {
  //     color: '#333',
  //     fontSize: 22,
  //     fontWeight: 'bold'
  //   }
  // },
  tooltip: {
    show: true,
    formatter: function (params) {
      if (params.dataType === 'node') {
        return `<strong>${params.data.name}</strong>`
      } else if (params.dataType === 'edge') {
        return `${params.data.source} → ${params.data.target}`
      }
      return ''
    }
  },
  series: [
    {
      type: 'graph',
      layout: 'force',
      roam: false,
      focusNodeAdjacency: true, // 鼠标悬停高亮节点和邻接边
      label: {
        show: true,
        position: 'bottom',
        fontSize: 14,
        color: '#555',
        fontWeight: '500',
        formatter: function(params) {
          // 优先显示 displayName，没有则显示 name
          return params.data.displayName || params.data.name;
        }
      },
      force: {
        repulsion: 4000,
        edgeLength: [50, 80],
        gravity: 0.1,
        layoutAnimation: true
      },
      edgeSymbol: ['none', 'arrow'],
      edgeSymbolSize: 8,
      edgeLabel: {
        show: false
      },
      data: graphData.value,
      links: edges.value,
      emphasis: {
        focus: 'adjacency',
        label: {
          show: true,
          fontSize: 16,
          fontWeight: 'bold',
          color: '#000'
        },
        itemStyle: {
          shadowBlur: 20,
          shadowColor: 'rgba(0, 0, 0, 0.3)'
        },
        lineStyle: {
          width: 3,
          color: '#ff7f50'
        }
      }
    }
  ]
})

const initChart = () => {
  if (chartRef.value) {
    chartInstance = echarts.init(chartRef.value)
    chartInstance.setOption(getChartOption())
    window.addEventListener('resize', resizeChart)
  }
}

const resizeChart = () => {
  chartInstance?.resize()
}

onMounted(() => {
  setTimeout(initChart, 100) // 确保dialog渲染完毕后初始化
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeChart)
  chartInstance?.dispose()
})

watch(() => props.center, (newCenter) => {
  if (chartInstance) {
    chartInstance.setOption(getChartOption(),{ resetLayout: true })
  }
})
</script>

<style scoped>
.chart-container {
  width: 100%;
  height: 100%;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 6px 12px rgba(0,0,0,0.1);
}
</style>
