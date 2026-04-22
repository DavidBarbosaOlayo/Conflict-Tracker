<script setup>
defineProps({
  title: {
    type: String,
    required: true
  },
  description: {
    type: String,
    default: ''
  },
  tone: {
    type: String,
    default: 'default'
  }
})
</script>

<template>
  <section :class="['panel', tone !== 'default' ? `panel--${tone}` : '']">
    <header class="panel__header">
      <div>
        <p v-if="$slots.eyebrow" class="panel__eyebrow">
          <slot name="eyebrow" />
        </p>
        <h2 class="panel__title">{{ title }}</h2>
        <p v-if="description" class="panel__description">{{ description }}</p>
      </div>

      <div v-if="$slots.actions">
        <slot name="actions" />
      </div>
    </header>

    <div class="panel__body">
      <slot />
    </div>
  </section>
</template>
