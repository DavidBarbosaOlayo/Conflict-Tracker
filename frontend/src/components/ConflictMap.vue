<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import L from 'leaflet'

import { getCountryCoordinate } from '../utils/countryCoordinates'

const props = defineProps({
  countries: {
    type: Array,
    required: true
  },
  emptyLabel: {
    type: String,
    required: true
  }
})

const mapEl = ref(null)
const mappedCountries = computed(() =>
  props.countries
    .map((country) => ({
      ...country,
      coordinate: getCountryCoordinate(country.code)
    }))
    .filter((country) => country.coordinate)
)

let map = null
let layerGroup = null

function renderMap() {
  if (!mapEl.value || !mappedCountries.value.length) {
    return
  }

  if (!map) {
    map = L.map(mapEl.value, {
      zoomControl: true,
      attributionControl: true
    })

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map)

    layerGroup = L.layerGroup().addTo(map)
  }

  layerGroup.clearLayers()

  const bounds = []

  mappedCountries.value.forEach((country) => {
    bounds.push(country.coordinate)

    L.circleMarker(country.coordinate, {
      radius: 8,
      color: '#c8ff8f',
      weight: 2,
      fillColor: '#9eff7d',
      fillOpacity: 0.55
    })
      .bindTooltip(`${country.name} (${country.code})`)
      .addTo(layerGroup)
  })

  if (bounds.length === 1) {
    map.setView(bounds[0], 4)
  } else {
    map.fitBounds(bounds, { padding: [30, 30] })
  }

  setTimeout(() => map.invalidateSize(), 0)
}

onMounted(renderMap)
onUnmounted(() => map?.remove())

watch(mappedCountries, () => renderMap(), { deep: true })
</script>

<template>
  <div v-if="mappedCountries.length" ref="mapEl" class="conflict-map"></div>
  <div v-else class="state-card">{{ emptyLabel }}</div>
</template>

<style scoped>
.conflict-map {
  height: 300px;
  border: 1px solid var(--line);
  border-radius: var(--radius-md);
  overflow: hidden;
}
</style>
