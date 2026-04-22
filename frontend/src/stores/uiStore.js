import { defineStore } from 'pinia'

import { translations } from '../utils/translations'

const LOCALE_KEY = 'gcm-locale'
const TRACKED_KEY = 'gcm-tracked-conflicts'

function readLocale() {
  const saved = localStorage.getItem(LOCALE_KEY)
  return saved === 'en' ? 'en' : 'ca'
}

function readTrackedIds() {
  try {
    const raw = localStorage.getItem(TRACKED_KEY)
    return raw ? JSON.parse(raw) : []
  } catch {
    return []
  }
}

export const useUiStore = defineStore('ui', {
  state: () => ({
    locale: readLocale(),
    trackedConflictIds: readTrackedIds()
  }),
  getters: {
    copy: (state) => translations[state.locale],
    isTracked: (state) => (conflictId) => state.trackedConflictIds.includes(Number(conflictId))
  },
  actions: {
    setLocale(locale) {
      this.locale = locale === 'en' ? 'en' : 'ca'
      localStorage.setItem(LOCALE_KEY, this.locale)
    },
    toggleTracked(conflictId) {
      const id = Number(conflictId)

      if (this.trackedConflictIds.includes(id)) {
        this.trackedConflictIds = this.trackedConflictIds.filter((trackedId) => trackedId !== id)
      } else {
        this.trackedConflictIds = [...this.trackedConflictIds, id]
      }

      localStorage.setItem(TRACKED_KEY, JSON.stringify(this.trackedConflictIds))
    }
  }
})
