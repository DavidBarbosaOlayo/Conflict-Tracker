<script setup>
import { computed, onMounted, watchEffect } from 'vue'
import { storeToRefs } from 'pinia'
import { RouterLink, RouterView, useRoute } from 'vue-router'

import { useMonitorStore } from './stores/monitorStore'
import { useUiStore } from './stores/uiStore'

const monitorStore = useMonitorStore()
const uiStore = useUiStore()
const route = useRoute()

const { bootstrapping } = storeToRefs(monitorStore)

const copy = computed(() => uiStore.copy)
const trackedCount = computed(() => uiStore.trackedConflictIds.length)
const currentConflict = computed(() =>
  route.name === 'conflict-detail' ? monitorStore.conflictById(route.params.id) : null
)

const locale = computed({
  get: () => uiStore.locale,
  set: (value) => uiStore.setLocale(value)
})

const pageLabel = computed(() =>
  route.name === 'conflict-detail' ? copy.value.detailLabel : copy.value.dashboardLabel
)

onMounted(() => {
  monitorStore.bootstrap().catch(() => {})
})

watchEffect(() => {
  document.documentElement.lang = uiStore.locale
  document.title = currentConflict.value
    ? `${currentConflict.value.name} | ${copy.value.appTitle}`
    : copy.value.appTitle
})
</script>

<template>
  <div class="app-shell">
    <header class="masthead">
      <RouterLink to="/" class="brand">
        <span class="brand__eyebrow">{{ pageLabel }}</span>
        <span class="brand__title">{{ copy.appTitle }}</span>
      </RouterLink>

      <div class="masthead__tools">
        <p class="masthead__subtitle">{{ copy.appSubtitle }}</p>

        <div class="toolbar">
          <span class="toolbar__tracked">
            {{ copy.trackedConflicts }}: <strong>{{ trackedCount }}</strong>
          </span>

          <label class="language-switch" for="locale-select">
            <span>{{ copy.languageLabel }}</span>
            <select id="locale-select" v-model="locale">
              <option value="ca">{{ copy.languageCa }}</option>
              <option value="en">{{ copy.languageEn }}</option>
            </select>
          </label>
        </div>
      </div>
    </header>

    <div v-if="bootstrapping" class="top-loader">{{ copy.loading }}</div>

    <main class="app-main">
      <RouterView />
    </main>
  </div>
</template>
