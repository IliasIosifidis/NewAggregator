<script setup lang="ts">
import type { Article } from '@/types/article'
import { sourceLabel, sourceTheme } from '@/constants/sources'
import { formatDate } from '@/utils/format'

defineProps<{
  article: Article
}>()

const emit = defineEmits<{
  readMore: [article: Article]
}>()
</script>

<template>
  <!-- Height follows the content; long titles and summaries are clamped so a card never grows past its maximum. -->
  <article
    class="flex flex-col rounded-2xl p-5 shadow-lg ring-1"
    :class="sourceTheme(article.source).card"
  >
    <p
      class="text-xs font-semibold tracking-wide uppercase"
      :class="sourceTheme(article.source).accent"
    >
      {{ sourceLabel(article.source) }} · {{ formatDate(article.publishedAt) }}
    </p>
    <h2 class="mt-2 line-clamp-3 text-lg leading-snug font-bold text-slate-900">
      {{ article.title }}
    </h2>
    <p
      class="mt-2 line-clamp-5 text-sm leading-relaxed"
      :class="article.summary ? 'text-slate-600' : 'text-slate-500 italic'"
    >
      {{ article.summary ?? 'No preview available for this article.' }}
    </p>
    <button
      type="button"
      class="mt-3 self-start font-semibold text-forest-700 hover:text-forest-900 hover:underline"
      @click="emit('readMore', article)"
    >
      Read more...
    </button>
  </article>
</template>
