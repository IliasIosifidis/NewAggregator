<script setup lang="ts">
import type { RouteLocationRaw } from 'vue-router'

// A link to the other page in the top bar. Smaller on phones, where it shares the row with the search box.
defineProps<{
  to: RouteLocationRaw
}>()
</script>

<template>
  <RouterLink
    :to="to"
    class="text-sm font-bold tracking-tight whitespace-nowrap text-bark-700 hover:text-bark-900 sm:text-2xl"
  >
    <slot />
  </RouterLink>
</template>
