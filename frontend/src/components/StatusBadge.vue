<script setup>
import { computed } from 'vue'

import { formatStatus } from '../utils/formatters'

const props = defineProps({
  status: {
    type: String,
    required: true
  },
  locale: {
    type: String,
    default: 'ca'
  }
})

const variantClass = computed(() => `status-badge--${props.status.toLowerCase()}`)
const label = computed(() => formatStatus(props.status, props.locale))
</script>

<template>
  <span :class="['status-badge', variantClass]">{{ label }}</span>
</template>

<style scoped>
.status-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 34px;
  padding: 6px 10px;
  border: 1px solid var(--line);
  border-radius: 6px;
  font-size: 0.82rem;
  font-weight: 700;
  letter-spacing: 0.14em;
  text-transform: uppercase;
  background: rgba(158, 255, 125, 0.04);
}

.status-badge--active {
  color: var(--success);
  border-color: rgba(158, 255, 125, 0.4);
  background: var(--success-soft);
}

.status-badge--frozen {
  color: var(--amber);
  border-color: rgba(224, 192, 107, 0.4);
  background: var(--amber-soft);
}

.status-badge--ended {
  color: var(--ink-soft);
  border-color: rgba(109, 192, 96, 0.2);
  background: rgba(109, 192, 96, 0.06);
}
</style>
