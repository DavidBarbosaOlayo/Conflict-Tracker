<script setup>
import { computed } from 'vue'

import { getFlagUrl } from '../utils/flags'

const props = defineProps({
  code: {
    type: String,
    required: true
  },
  name: {
    type: String,
    default: ''
  }
})

const flagUrl = computed(() => getFlagUrl(props.code))
const fallbackLabel = computed(() => props.code.slice(0, 2).toUpperCase())
</script>

<template>
  <img
    v-if="flagUrl"
    :src="flagUrl"
    :alt="name ? `${name} flag` : `${code} flag`"
    class="flag-icon"
    loading="lazy"
  />
  <span v-else class="flag-fallback">{{ fallbackLabel }}</span>
</template>
