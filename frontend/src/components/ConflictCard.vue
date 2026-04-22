<script setup>
import { computed } from 'vue'

import CountryFlag from './CountryFlag.vue'
import StatusBadge from './StatusBadge.vue'
import { daysSince, formatDate } from '../utils/formatters'
import { translations } from '../utils/translations'

const props = defineProps({
  conflict: {
    type: Object,
    required: true
  },
  countries: {
    type: Object,
    required: true
  },
  locale: {
    type: String,
    default: 'ca'
  },
  tracked: {
    type: Boolean,
    default: false
  },
  deleting: {
    type: Boolean,
    default: false
  }
})

defineEmits(['open', 'delete', 'toggle-track'])

const copy = computed(() => translations[props.locale])
const countryEntries = computed(() =>
  props.conflict.countryCodes.map((code) => props.countries[code] ?? { code, name: code })
)
const runningDays = computed(() => daysSince(props.conflict.startDate))
const truncatedDescription = computed(() =>
  props.conflict.description?.trim() ? props.conflict.description : copy.value.noDescription
)
</script>

<template>
  <article :class="['conflict-card', 'fade-up', { 'conflict-card--tracked': tracked }]">
    <div class="conflict-card__top">
      <StatusBadge :status="conflict.status" :locale="locale" />

      <button class="conflict-card__track" type="button" @click="$emit('toggle-track', conflict.id)">
        {{ tracked ? copy.untrack : copy.track }}
      </button>
    </div>

    <div class="conflict-card__body" @click="$emit('open', conflict.id)">
      <h3 class="conflict-card__title">{{ conflict.name }}</h3>
      <p class="conflict-card__description">{{ truncatedDescription }}</p>

      <div class="conflict-card__meta">
        <span>{{ copy.startedOn }} {{ formatDate(conflict.startDate, locale) }}</span>
        <span>{{ runningDays }} {{ copy.daysRunning }}</span>
      </div>
    </div>

    <div class="chip-list">
      <span v-if="!countryEntries.length" class="country-chip">{{ copy.noCountries }}</span>
      <span v-for="country in countryEntries" :key="country.code" class="country-chip">
        <CountryFlag :code="country.code" :name="country.name" />
        <span>{{ country.name }}</span>
      </span>
    </div>

    <div class="button-row">
      <button class="button button--secondary" type="button" @click="$emit('open', conflict.id)">
        {{ copy.openDetail }}
      </button>
      <button
        class="button button--danger"
        type="button"
        :disabled="deleting"
        @click="$emit('delete', conflict.id)"
      >
        {{ deleting ? copy.deleting : copy.delete }}
      </button>
    </div>
  </article>
</template>

<style scoped>
.conflict-card {
  display: grid;
  gap: 14px;
  padding: 16px;
  border-radius: var(--radius-lg);
  background: linear-gradient(180deg, rgba(17, 31, 17, 0.98), rgba(9, 18, 9, 0.98));
  border: 1px solid var(--line);
  box-shadow: var(--shadow);
}

.conflict-card--tracked {
  border-color: var(--line-strong);
}

.conflict-card__top {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: center;
}

.conflict-card__track {
  min-height: 38px;
  padding: 8px 12px;
  border: 1px solid var(--line);
  border-radius: var(--radius-md);
  background: rgba(158, 255, 125, 0.06);
  color: var(--accent);
}

.conflict-card__body {
  display: grid;
  gap: 10px;
  cursor: pointer;
}

.conflict-card__title {
  margin: 0;
  font-size: 1.25rem;
  line-height: 1.2;
  color: var(--accent);
}

.conflict-card__description {
  margin: 0;
  color: var(--ink-soft);
  line-height: 1.6;
}

.conflict-card__meta {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
  color: var(--ink-dim);
  font-size: 0.86rem;
  letter-spacing: 0.03em;
}

@media (max-width: 720px) {
  .conflict-card {
    padding: 14px;
  }

  .conflict-card__top {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
