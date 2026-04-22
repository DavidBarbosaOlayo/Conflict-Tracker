<script setup>
import { computed, ref, watch } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { storeToRefs } from 'pinia'

import ConflictMap from '../components/ConflictMap.vue'
import CountryFlag from '../components/CountryFlag.vue'
import SectionPanel from '../components/SectionPanel.vue'
import StatusBadge from '../components/StatusBadge.vue'
import { useMonitorStore } from '../stores/monitorStore'
import { useUiStore } from '../stores/uiStore'
import { daysSince, formatDate } from '../utils/formatters'

const route = useRoute()
const monitorStore = useMonitorStore()
const uiStore = useUiStore()

const { loadingDetail } = storeToRefs(monitorStore)

const copy = computed(() => uiStore.copy)
const loadError = ref('')
const conflictId = computed(() => Number(route.params.id))
const conflict = computed(() => monitorStore.conflictById(conflictId.value))
const relatedFactions = computed(() => monitorStore.factionsByConflict(conflictId.value))
const timeline = computed(() => monitorStore.eventsByConflict(conflictId.value))
const countryEntries = computed(() =>
  conflict.value
    ? conflict.value.countryCodes.map((code) => monitorStore.countryIndex[code] ?? { code, name: code })
    : []
)

const factCards = computed(() => {
  if (!conflict.value) {
    return []
  }

  return [
    {
      label: copy.value.startedOn,
      value: formatDate(conflict.value.startDate, uiStore.locale)
    },
    {
      label: copy.value.duration,
      value: `${daysSince(conflict.value.startDate)} ${copy.value.daysRunning}`
    },
    {
      label: copy.value.countriesCount,
      value: String(countryEntries.value.length)
    },
    {
      label: copy.value.eventsCount,
      value: String(timeline.value.length)
    }
  ]
})

async function loadConflict() {
  loadError.value = ''

  try {
    await Promise.all([monitorStore.bootstrap(), monitorStore.fetchConflict(conflictId.value)])
  } catch (error) {
    loadError.value = error.message || copy.value.conflictNotFound
  }
}

watch(
  conflictId,
  (nextId) => {
    if (Number.isFinite(nextId)) {
      loadConflict()
    }
  },
  { immediate: true }
)
</script>

<template>
  <div v-if="loadingDetail && !conflict" class="state-card">{{ copy.loading }}</div>

  <div v-else-if="loadError && !conflict" class="state-card state-card--error">
    {{ loadError }}
  </div>

  <template v-else-if="conflict">
    <section class="detail-hero">
      <div class="detail-summary fade-up">
        <RouterLink to="/" class="detail-summary__back">
          {{ copy.backToDashboard }}
        </RouterLink>

        <p class="detail-summary__eyebrow">{{ copy.detailEyebrow }}</p>
        <h1 class="detail-summary__title">{{ conflict.name }}</h1>
        <p class="detail-summary__description">
          {{ conflict.description || copy.noDescription }}
        </p>
      </div>

      <aside class="detail-meta fade-up">
        <div class="detail-meta__top">
          <StatusBadge :status="conflict.status" :locale="uiStore.locale" />
          <button class="button button--secondary" type="button" @click="uiStore.toggleTracked(conflict.id)">
            {{ uiStore.isTracked(conflict.id) ? copy.untrack : copy.track }}
          </button>
        </div>

        <article class="fact-card">
          <span class="fact-card__label">{{ copy.sourceLabel }}</span>
          <strong class="fact-card__value">{{ copy.sourceValue }}</strong>
          <div class="fact-card__subline">
            {{ copy.conflictCode }} #{{ conflict.id }}
          </div>
        </article>

        <article class="fact-card">
          <span class="fact-card__label">{{ copy.liveStatus }}</span>
          <strong class="fact-card__value">{{ copy.summaryLabel }}</strong>
          <div class="fact-card__subline">
            {{ conflict.description || copy.noDescription }}
          </div>
        </article>
      </aside>
    </section>

    <section class="detail-grid detail-grid--summary">
      <SectionPanel :title="copy.factsTitle" :description="copy.factsDescription">
        <template #eyebrow>{{ copy.summaryLabel }}</template>

        <div class="fact-grid">
          <article v-for="fact in factCards" :key="fact.label" class="fact-card">
            <span class="fact-card__label">{{ fact.label }}</span>
            <strong class="fact-card__value">{{ fact.value }}</strong>
          </article>
        </div>
      </SectionPanel>

      <SectionPanel
        :title="copy.relatedCountriesTitle"
        :description="copy.relatedCountriesDescription"
        tone="accent"
      >
        <template #eyebrow>{{ copy.countriesCount }}</template>

        <div v-if="!countryEntries.length" class="state-card">
          {{ copy.noCountries }}
        </div>

        <div v-else class="chip-list">
          <span v-for="country in countryEntries" :key="country.code" class="country-chip">
            <CountryFlag :code="country.code" :name="country.name" />
            <span>{{ country.name }} ({{ country.code }})</span>
          </span>
        </div>
      </SectionPanel>

      <SectionPanel :title="copy.mapTitle" :description="copy.mapDescription" tone="accent">
        <template #eyebrow>{{ copy.mapTitle }}</template>

        <ConflictMap :countries="countryEntries" :empty-label="copy.mapUnavailable" />
      </SectionPanel>

      <SectionPanel
        :title="copy.factionsTitle"
        :description="copy.factionsDescription"
        tone="accent"
      >
        <template #eyebrow>{{ copy.factionsCount }}</template>

        <div v-if="!relatedFactions.length" class="state-card">
          {{ copy.noFactions }}
        </div>

        <div v-else class="stack">
          <article v-for="faction in relatedFactions" :key="faction.id" class="fact-card">
            <span class="fact-card__label">{{ copy.factionsTitle }}</span>
            <strong class="fact-card__value">{{ faction.name }}</strong>
            <div class="fact-card__subline">
              {{ copy.supportedBy }}:
              {{
                faction.supportedCountryCodes.length
                  ? faction.supportedCountryCodes
                      .map((code) => monitorStore.countryIndex[code]?.name ?? code)
                      .join(', ')
                  : copy.noCountries
              }}
            </div>
          </article>
        </div>
      </SectionPanel>
    </section>

    <section class="detail-timeline">
      <SectionPanel :title="copy.timelineTitle" :description="copy.timelineDescription">
        <template #eyebrow>{{ copy.eventsCount }}</template>

        <div v-if="!timeline.length" class="state-card">
          {{ copy.noEvents }}
        </div>

        <div v-else class="timeline">
          <article v-for="event in timeline" :key="event.id" class="timeline-item">
            <span class="timeline-item__date">{{ formatDate(event.eventDate, uiStore.locale) }}</span>
            <strong class="timeline-item__title">
              {{ copy.timelineLocation }}: {{ event.location }}
            </strong>
            <div class="timeline-item__description">{{ event.description }}</div>
          </article>
        </div>
      </SectionPanel>
    </section>
  </template>
</template>
