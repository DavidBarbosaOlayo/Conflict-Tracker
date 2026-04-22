export function formatDate(value, locale = 'ca') {
  if (!value) {
    return '—'
  }

  const formatter = new Intl.DateTimeFormat(locale === 'ca' ? 'ca-ES' : 'en-GB', {
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  })

  return formatter.format(new Date(`${value}T00:00:00`))
}

export function formatStatus(status, locale = 'ca') {
  const labels = {
    ACTIVE: locale === 'ca' ? 'Actiu' : 'Active',
    FROZEN: locale === 'ca' ? 'Congelat' : 'Frozen',
    ENDED: locale === 'ca' ? 'Finalitzat' : 'Ended'
  }

  return labels[status] ?? status
}

export function daysSince(value) {
  if (!value) {
    return 0
  }

  const start = new Date(`${value}T00:00:00`)
  const today = new Date()
  const diff = today.getTime() - start.getTime()
  return Math.max(0, Math.floor(diff / (1000 * 60 * 60 * 24)))
}

export function sortConflicts(conflicts) {
  const statusOrder = {
    ACTIVE: 0,
    FROZEN: 1,
    ENDED: 2
  }

  return [...conflicts].sort((left, right) => {
    const statusDiff = (statusOrder[left.status] ?? 99) - (statusOrder[right.status] ?? 99)

    if (statusDiff !== 0) {
      return statusDiff
    }

    return String(right.startDate).localeCompare(String(left.startDate))
  })
}
