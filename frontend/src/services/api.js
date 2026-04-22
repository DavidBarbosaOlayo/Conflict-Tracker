const API_BASE = '/api/v1'

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers ?? {})
    },
    ...options
  })

  const contentType = response.headers.get('content-type') ?? ''
  const hasBody = response.status !== 204
  const payload = hasBody
    ? contentType.includes('application/json')
      ? await response.json()
      : await response.text()
    : null

  if (!response.ok) {
    const message =
      typeof payload === 'string'
        ? payload
        : payload?.message || response.statusText || 'Request failed'
    throw new Error(message)
  }

  return payload
}

export const api = {
  getConflicts() {
    return request('/conflicts')
  },
  getConflict(id) {
    return request(`/conflicts/${id}`)
  },
  createConflict(payload) {
    return request('/conflicts', {
      method: 'POST',
      body: JSON.stringify(payload)
    })
  },
  deleteConflict(id) {
    return request(`/conflicts/${id}`, {
      method: 'DELETE'
    })
  },
  getCountries() {
    return request('/countries')
  },
  getEvents() {
    return request('/events')
  },
  getFactions() {
    return request('/factions')
  }
}
