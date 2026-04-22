<script setup>
import { onMounted, onUnmounted, ref, watch } from 'vue'
import Chart from 'chart.js/auto'

const props = defineProps({
  segments: {
    type: Array,
    required: true
  }
})

const canvasEl = ref(null)
let chart = null

function buildChart() {
  if (!canvasEl.value) {
    return
  }

  chart?.destroy()

  chart = new Chart(canvasEl.value, {
    type: 'doughnut',
    data: {
      labels: props.segments.map((segment) => segment.label),
      datasets: [
        {
          data: props.segments.map((segment) => segment.value),
          backgroundColor: ['#9eff7d', '#e0c06b', '#587a55'],
          borderColor: '#08100a',
          borderWidth: 2,
          hoverOffset: 4
        }
      ]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      cutout: '66%',
      plugins: {
        legend: {
          display: false
        },
        tooltip: {
          backgroundColor: '#102012',
          titleColor: '#c8ff8f',
          bodyColor: '#9eff7d',
          borderColor: 'rgba(158, 255, 125, 0.28)',
          borderWidth: 1
        }
      }
    }
  })
}

onMounted(buildChart)
onUnmounted(() => chart?.destroy())

watch(
  () => props.segments,
  () => buildChart(),
  { deep: true }
)
</script>

<template>
  <div class="status-chart">
    <canvas ref="canvasEl"></canvas>
  </div>
</template>

<style scoped>
.status-chart {
  position: relative;
  height: 220px;
}
</style>
