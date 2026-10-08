<script setup lang="ts">
import { articleKey, type Article } from '@/types/article'
import ArticleCard from './ArticleCard.vue'

defineProps<{
  articles: Article[]
}>()

const emit = defineEmits<{
  readMore: [article: Article]
}>()
</script>

<template>
  <!-- items-start lets each card keep its own height instead of stretching to the tallest in the row. -->
  <section class="grid items-start gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
    <ArticleCard
      v-for="article in articles"
      :key="articleKey(article)"
      :article="article"
      @read-more="emit('readMore', $event)"
    />
  </section>
</template>
