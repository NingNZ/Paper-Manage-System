<template>
  <div ref="chartRef" style="width: 1000px; height: 500px" class="chart-container"></div>
</template>

<script setup>
import * as echarts from 'echarts'
import { ref, onMounted, onBeforeUnmount, watch, nextTick } from 'vue'
import { userNetUtils } from '../../scripts/userNet'

// 父组件传入 userId
const props = defineProps({
  userId: {
    type: String,
    required: true
  },
  coAuthors: {
    type: Array,
    default: () => []
  }
})

// 所有数据准备好以后再渲染
const chartRef = ref(null)
let chartInstance = null

// 数据源
const centerNode = ref(null)
const nodes = ref([])
const edges = ref([])
const showGraph = ref(false)

// 初始化图表
const initChart = () => {
  if (!chartRef.value) return
  chartInstance = echarts.init(chartRef.value)
  window.addEventListener('resize', resizeChart)
}

// 渲染图表（完整清空重绘）
const renderChart = () => {
  if (!chartInstance) return

  const data = [
    {
      name: centerNode.value.name,
      displayName: centerNode.value.displayName,
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
    ...nodes.value
  ]

  const option = {
    backgroundColor: '#f9f9f9',
    tooltip: {
      show: true,
      formatter: function (params) {
        if (params.dataType === 'node') {
          return `<strong>${params.data.displayName || params.data.name}</strong>`
        } else if (params.dataType === 'edge') {
          const papers = params.data.papers
          if (papers?.length > 0) {
            return `共同发表论文：<br/>${papers.join('<br/>')}`
          }
          return '无共同论文'
        }
        return ''
      }
    },
    series: [
      {
        type: 'graph',
        layout: 'force',
        roam: true,  // 开启缩放与拖拽，体验更好
        focusNodeAdjacency: true,
        label: {
          show: true,
          position: 'bottom',
          fontSize: 14,
          color: '#555',
          fontWeight: '500',
          formatter: function (params) {
            return params.data.displayName || params.data.name
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
        edgeLabel: { show: false },
        data: data,
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
  }

  chartInstance.clear()
  chartInstance.setOption(option)
}

// 获取数据
const fetchCoAuthor = async () => {
  console.log("传入的 userId：[" + props.userId + "]");
  try {
    const res = await userNetUtils.getCoAuthor(props.userId)
    if (res.code === 200) {
      const data = res.data

      centerNode.value = {
        name: data[0].name,
        displayName: data[0].displayName
      }

      nodes.value = data.slice(1).map(item => ({
        name: item.name,
        displayName: item.displayName,
        symbolSize: 40,
        itemStyle: { color: '#83bff6' },
        draggable: true
      }))

      edges.value = data.slice(1).map(item => ({
        source: centerNode.value.name,
        target: item.name,
        papers: item.papers || [],
        lineStyle: { color: '#a0c4ff', width: 2 }
      }))

      showGraph.value = false
      nextTick(() => {
        showGraph.value = true
      })
      renderChart()
    } else {
      console.error('数据返回错误：', res.msg)
    }
  } catch (err) {
    console.error('请求失败：', err)
  }
}

const resizeChart = () => {
  chartInstance?.resize()
}

onMounted(() => {
  initChart()
  fetchCoAuthor()
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeChart)
  chartInstance?.dispose()
})

watch(
  () => props.userId,
  (newUserId) => {
    if (newUserId) {
      fetchCoAuthor()
    }
  },
  { immediate: true }
)

watch(
  () => props.coAuthors,
  (newCoAuthors) => {
    console.log('watch coAuthors:', newCoAuthors)
    nodes.value = newCoAuthors.map(user => ({
      name: user.userName,
      displayName: user.displayName || user.userName,
      ...user
    }))
  },
  { immediate: true }
)
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
