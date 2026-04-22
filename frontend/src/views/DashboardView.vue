<script setup>
import { computed, reactive, ref } from 'vue'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'

import ConflictCard from '../components/ConflictCard.vue'
import SectionPanel from '../components/SectionPanel.vue'
import StatusChart from '../components/StatusChart.vue'
import { useMonitorStore } from '../stores/monitorStore'
import { useUiStore } from '../stores/uiStore'
import { formatDate } from '../utils/formatters'

const monitorStore = useMonitorStore()
const uiStore = useUiStore()
const router = useRouter()

const { conflicts, countries, bootstrapping, loadingConflicts, createPending, deletingIds, error } =
  storeToRefs(monitorStore)

const search = ref('')
const selectedStatus = ref('ALL')
const selectedCountry = ref('ALL')
const formError = ref('')

const createForm = reactive({
  name: '',
  startDate: '',
  status: 'ACTIVE',
  description: '',
  countryCodes: []
})

const copy = computed(() => uiStore.copy)
const countryIndex = computed(() => monitorStore.countryIndex)

const statusOptions = computed(() => [
  { value: 'ALL', label: copy.value.allStatuses },
  { value: 'ACTIVE', label: copy.value.statusActive },
  { value: 'FROZEN', label: copy.value.statusFrozen },
  { value: 'ENDED', label: copy.value.statusEnded }
])

const countryOptions = computed(() => [
  { code: 'ALL', name: copy.value.allCountries },
  ...countries.value
])

const filteredConflicts = computed(() => {
  const query = search.value.trim().toLowerCase()

  return conflicts.value.filter((conflict) => {
    const matchesSearch =
      !query ||
      conflict.name.toLowerCase().includes(query) ||
      (conflict.description ?? '').toLowerCase().includes(query)

    const matchesStatus = selectedStatus.value === 'ALL' || conflict.status === selectedStatus.value
    const matchesCountry =
      selectedCountry.value === 'ALL' || conflict.countryCodes.includes(selectedCountry.value)

    return matchesSearch && matchesStatus && matchesCountry
  })
})

const activeCount = computed(
  () => conflicts.value.filter((conflict) => conflict.status === 'ACTIVE').length
)
const endedCount = computed(
  () => conflicts.value.filter((conflict) => conflict.status === 'ENDED').length
)
const trackedCount = computed(() => uiStore.trackedConflictIds.length)
const newestConflict = computed(() => conflicts.value[0] ?? null)
const leadConflict = computed(
  () => filteredConflicts.value.find((conflict) => conflict.status === 'ACTIVE') ?? filteredConflicts.value[0]
)

const statCards = computed(() => [
  {
    label: copy.value.totalConflicts,
    value: conflicts.value.length,
    hint: leadConflict.value ? leadConflict.value.name : copy.value.noDataYet
  },
  {
    label: copy.value.activeConflicts,
    value: activeCount.value,
    hint: `${copy.value.liveStatus}: ${copy.value.statusActive}`
  },
  {
    label: copy.value.endedConflicts,
    value: endedCount.value,
    hint: copy.value.stateDistribution
  },
  {
    label: copy.value.trackedConflicts,
    value: trackedCount.value,
    hint: copy.value.dashboardLabel
  }
])

const distribution = computed(() => {
  const total = conflicts.value.length || 1

  return [
    {
      key: 'active',
      label: copy.value.statusActive,
      value: activeCount.value
    },
    {
      key: 'frozen',
      label: copy.value.statusFrozen,
      value: conflicts.value.filter((conflict) => conflict.status === 'FROZEN').length
    },
    {
      key: 'ended',
      label: copy.value.statusEnded,
      value: endedCount.value
    }
  ].map((segment) => ({
    ...segment,
    percent: Math.round((segment.value / total) * 100)
  }))
})

function resetForm() {
  createForm.name = ''
  createForm.startDate = ''
  createForm.status = 'ACTIVE'
  createForm.description = ''
  createForm.countryCodes = []
}

function openConflict(conflictId) {
  router.push({ name: 'conflict-detail', params: { id: conflictId } })
}

async function submitForm() {
  formError.value = ''

  if (!createForm.name || !createForm.startDate || !createForm.status) {
    formError.value = copy.value.formValidation
    return
  }

  try {
    const createdConflict = await monitorStore.createConflict({
      ...createForm,
      countryCodes: [...new Set(createForm.countryCodes)]
    })

    resetForm()
    router.push({ name: 'conflict-detail', params: { id: createdConflict.id } })
  } catch (submissionError) {
    formError.value = submissionError.message || copy.value.formValidation
  }
}

async function removeConflict(conflictId) {
  if (!window.confirm(copy.value.deleteConfirm)) {
    return
  }

  try {
    await monitorStore.deleteConflict(conflictId)
  } catch (deleteError) {
    formError.value = deleteError.message || copy.value.delete
  }
}
</script>

<template>
  <section class="hero">
    <div class="hero__copy fade-up">
      <p class="hero__eyebrow">{{ copy.dashboardLabel }}</p>
      <h1 class="hero__title">{{ copy.homeTitle }}</h1>
      <p class="hero__intro">{{ copy.homeIntro }}</p>
    </div>
  </section>

  <section class="metric-strip">
    <article v-for="card in statCards" :key="card.label" class="metric-card fade-up">
      <span class="metric-card__label">{{ card.label }}</span>
      <strong class="metric-card__value">{{ card.value }}</strong>
      <div class="metric-card__hint">{{ card.hint }}</div>
    </article>
  </section>

  <section class="dashboard-grid">
    <div class="dashboard-main">
      <SectionPanel :title="copy.conflictsLabel" :description="copy.conflictsDescription">
        <template #eyebrow>{{ copy.dashboardLabel }}</template>

        <template #actions>
          <div class="filters">
            <div class="control-group control-group--search">
              <label for="search">{{ copy.searchLabel }}</label>
              <input
                id="search"
                v-model.trim="search"
                class="input"
                :placeholder="copy.searchPlaceholder"
                type="search"
              />
            </div>

            <div class="control-group control-group--compact">
              <label for="status-filter">{{ copy.statusFilterLabel }}</label>
              <select id="status-filter" v-model="selectedStatus" class="select">
                <option v-for="option in statusOptions" :key="option.value" :value="option.value">
                  {{ option.label }}
                </option>
              </select>
            </div>

            <div class="control-group control-group--compact">
              <label for="country-filter">{{ copy.countryFilterLabel }}</label>
              <select id="country-filter" v-model="selectedCountry" class="select">
                <option v-for="option in countryOptions" :key="option.code" :value="option.code">
                  {{ option.name }}
                </option>
              </select>
            </div>
          </div>
        </template>

        <div class="panel-summary">
          <span class="panel-counter">{{ filteredConflicts.length }} / {{ conflicts.length }}</span>
          <span v-if="leadConflict" class="panel-summary__item">{{ leadConflict.name }}</span>
          <span v-if="newestConflict" class="panel-summary__item">
            {{ copy.startedOn }} {{ formatDate(newestConflict.startDate, uiStore.locale) }}
          </span>
        </div>

        <div v-if="bootstrapping || loadingConflicts" class="state-card">
          {{ copy.loading }}
        </div>

        <div v-else-if="error" class="state-card state-card--error">
          {{ error }}
        </div>

        <div v-else-if="!filteredConflicts.length" class="state-card">
          {{ copy.emptyFiltered }}
        </div>

        <div v-else class="conflict-list">
          <ConflictCard
            v-for="conflict in filteredConflicts"
            :key="conflict.id"
            :conflict="conflict"
            :countries="countryIndex"
            :locale="uiStore.locale"
            :tracked="uiStore.isTracked(conflict.id)"
            :deleting="deletingIds.includes(conflict.id)"
            @open="openConflict"
            @delete="removeConflict"
            @toggle-track="uiStore.toggleTracked"
          />
        </div>
      </SectionPanel>
    </div>

    <aside class="dashboard-side">
      <SectionPanel
        :title="copy.overviewTitle"
        :description="copy.overviewDescription"
        tone="accent"
      >
        <template #eyebrow>{{ copy.stateDistribution }}</template>

        <div class="stack">
          <article class="insight-card">
            <span class="insight-card__label">{{ copy.newestConflict }}</span>
            <strong class="insight-card__headline">
              {{ newestConflict ? newestConflict.name : copy.noDataYet }}
            </strong>
            <div class="insight-card__subline">
              {{
                newestConflict
                  ? `${copy.startedOn} ${formatDate(newestConflict.startDate, uiStore.locale)}`
                  : copy.noDescription
              }}
            </div>
          </article>

          <article v-if="leadConflict" class="insight-card">
            <span class="insight-card__label">{{ copy.summaryLabel }}</span>
            <strong class="insight-card__headline">{{ leadConflict.name }}</strong>
            <div class="insight-card__subline">
              {{ leadConflict.description || copy.noDescription }}
            </div>
          </article>

          <div class="distribution">
            <div v-for="segment in distribution" :key="segment.key" class="distribution__row">
              <div class="distribution__label">
                <span>{{ segment.label }}</span>
                <span>{{ segment.value }}</span>
              </div>
              <div class="distribution__track">
                <span
                  :class="['distribution__fill', `distribution__fill--${segment.key}`]"
                  :style="{ width: `${segment.percent}%` }"
                ></span>
              </div>
            </div>
          </div>
        </div>
      </SectionPanel>

      <SectionPanel :title="copy.chartTitle" :description="copy.chartDescription" tone="accent">
        <template #eyebrow>{{ copy.stateDistribution }}</template>

        <StatusChart :segments="distribution" />
      </SectionPanel>

      <SectionPanel
        :title="copy.createTitle"
        :description="copy.createDescription"
        tone="accent"
      >
        <template #eyebrow>{{ copy.createConflict }}</template>

        <form class="create-form" @submit.prevent="submitForm">
          <div class="control-group">
            <label for="conflict-name">{{ copy.nameField }}</label>
            <input id="conflict-name" v-model.trim="createForm.name" class="input" type="text" />
          </div>

          <div class="control-group">
            <label for="conflict-date">{{ copy.dateField }}</label>
            <input id="conflict-date" v-model="createForm.startDate" class="input" type="date" />
          </div>

          <div class="control-group">
            <label for="conflict-status">{{ copy.statusFilterLabel }}</label>
            <select id="conflict-status" v-model="createForm.status" class="select">
              <option
                v-for="option in statusOptions.filter((option) => option.value !== 'ALL')"
                :key="option.value"
                :value="option.value"
              >
                {{ option.label }}
              </option>
            </select>
          </div>

          <div class="control-group">
            <label for="conflict-description">{{ copy.descriptionField }}</label>
            <textarea
              id="conflict-description"
              v-model.trim="createForm.description"
              class="textarea"
            ></textarea>
          </div>

          <div class="control-group">
            <label>{{ copy.countriesField }}</label>
            <div class="country-selector">
              <label v-for="country in countries" :key="country.code" class="country-option">
                <input v-model="createForm.countryCodes" :value="country.code" type="checkbox" />
                <span>{{ country.name }}</span>
              </label>
            </div>
          </div>

          <p v-if="formError" class="form-error state-card">{{ formError }}</p>

          <div class="button-row">
            <button class="button button--accent" type="submit" :disabled="createPending">
              {{ createPending ? copy.creating : copy.createConflict }}
            </button>
          </div>
        </form>
      </SectionPanel>
    </aside>
  </section>
</template>
