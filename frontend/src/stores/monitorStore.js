import { defineStore } from 'pinia'

import { api } from '../services/api'
import { sortConflicts } from '../utils/formatters'

export const useMonitorStore = defineStore('monitor', {
  state: () => ({
    conflicts: [],
    countries: [],
    factions: [],
    events: [],
    bootstrapped: false,
    bootstrapping: false,
    loadingConflicts: false,
    loadingDetail: false,
    createPending: false,
    deletingIds: [],
    error: ''
  }),
  getters: {
    countryIndex: (state) =>
      state.countries.reduce((index, country) => {
        index[country.code] = country
        return index
      }, {}),
    conflictById: (state) => (conflictId) =>
      state.conflicts.find((conflict) => conflict.id === Number(conflictId)),
    factionsByConflict: (state) => (conflictId) =>
      state.factions
        .filter((faction) => faction.conflictId === Number(conflictId))
        .sort((left, right) => left.name.localeCompare(right.name)),
    eventsByConflict: (state) => (conflictId) =>
      state.events
        .filter((event) => event.conflictId === Number(conflictId))
        .sort((left, right) => String(right.eventDate).localeCompare(String(left.eventDate)))
  },
  actions: {
    async bootstrap(force = false) {
      if (this.bootstrapped && !force) {
        return
      }

      this.bootstrapping = true
      this.error = ''

      try {
        await Promise.all([
          this.fetchCountries(),
          this.fetchConflicts(),
          this.fetchFactions(),
          this.fetchEvents()
        ])
        this.bootstrapped = true
      } catch (error) {
        this.error = error.message || 'Unable to load monitor data.'
        throw error
      } finally {
        this.bootstrapping = false
      }
    },
    async fetchCountries() {
      this.countries = await api.getCountries()
    },
    async fetchConflicts() {
      this.loadingConflicts = true

      try {
        this.conflicts = sortConflicts(await api.getConflicts())
      } finally {
        this.loadingConflicts = false
      }
    },
    async fetchFactions() {
      this.factions = await api.getFactions()
    },
    async fetchEvents() {
      this.events = await api.getEvents()
    },
    async fetchConflict(conflictId) {
      this.loadingDetail = true

      try {
        const conflict = await api.getConflict(conflictId)
        this.upsertConflict(conflict)
        return conflict
      } finally {
        this.loadingDetail = false
      }
    },
    async createConflict(payload) {
      this.createPending = true
      this.error = ''

      try {
        const createdConflict = await api.createConflict(payload)
        this.upsertConflict(createdConflict)
        return createdConflict
      } catch (error) {
        this.error = error.message || 'Unable to create conflict.'
        throw error
      } finally {
        this.createPending = false
      }
    },
    async deleteConflict(conflictId) {
      const id = Number(conflictId)
      this.deletingIds = [...this.deletingIds, id]
      this.error = ''

      try {
        await api.deleteConflict(id)
        this.conflicts = this.conflicts.filter((conflict) => conflict.id !== id)
        this.factions = this.factions.filter((faction) => faction.conflictId !== id)
        this.events = this.events.filter((event) => event.conflictId !== id)
      } catch (error) {
        this.error = error.message || 'Unable to delete conflict.'
        throw error
      } finally {
        this.deletingIds = this.deletingIds.filter((trackedId) => trackedId !== id)
      }
    },
    upsertConflict(conflict) {
      const index = this.conflicts.findIndex((entry) => entry.id === conflict.id)

      if (index >= 0) {
        this.conflicts.splice(index, 1, conflict)
      } else {
        this.conflicts.unshift(conflict)
      }

      this.conflicts = sortConflicts(this.conflicts)
    }
  }
})
