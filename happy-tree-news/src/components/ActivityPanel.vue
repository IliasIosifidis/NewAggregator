<script setup lang="ts">
import { computed } from 'vue'
import type { Activity } from '@/types/activity'
import { formatLogTime } from '@/utils/format'

const props = defineProps<{
  /** Null until the first response arrives. */
  activity: Activity | null
  failed: boolean
}>()

// Top to bottom, in the order articles flow through them.
const SERVICES = ['news-ingestor', 'article-store', 'search-service']

const sections = computed(() =>
  SERVICES.map((service) => ({ service, entries: props.activity?.[service] ?? [] })),
)

function emptyText() {
  if (props.activity) return 'No activity yet.'
  return props.failed ? 'Could not load activity.' : 'Loading activity…'
}
</script>

<!-- On wide screens the panel is as tall as the page and scrolls if the messages don't fit. -->
<template>
  <section aria-label="Service activity" class="flex flex-col gap-3 md:overflow-y-auto">
    <p v-if="failed && activity" class="rounded-full bg-white/90 px-4 py-1 text-sm text-red-600 shadow">
      Could not refresh; showing the last activity loaded.
    </p>

    <article
      v-for="{ service, entries } in sections"
      :key="service"
      class="rounded-2xl bg-white/90 p-4 shadow-lg ring-1 ring-black/5 backdrop-blur"
    >
      <h2 class="text-lg font-bold text-forest-800">{{ service }}</h2>
      <ol v-if="entries.length > 0" class="mt-2 space-y-2">
        <li v-for="(entry, index) in entries" :key="`${entry.timestamp}-${index}`">
          <time :datetime="entry.timestamp" class="block text-xs text-slate-500 tabular-nums">
            {{ formatLogTime(entry.timestamp) }}
          </time>
          <p class="text-sm leading-snug text-slate-800">{{ entry.message }}</p>
        </li>
      </ol>
      <p v-else class="mt-2 text-sm text-slate-400 italic">{{ emptyText() }}</p>
    </article>
  </section>
</template>
