<script setup lang="ts">
defineProps<{
  loading: boolean
  error: string | null
  isEmpty: boolean
  hasMore: boolean
}>()

const emit = defineEmits<{
  retry: []
  loadMore: []
}>()
</script>

<template>
  <div class="mt-8 flex justify-center">
    <div v-if="error" class="rounded-2xl bg-white/95 px-6 py-4 text-center shadow-lg">
      <p class="text-red-600">{{ error }}</p>
      <button
        type="button"
        class="mt-2 font-semibold text-forest-700 hover:text-forest-900 hover:underline"
        @click="emit('retry')"
      >
        Try again
      </button>
    </div>
    <p v-else-if="loading" class="rounded-full bg-white/90 px-5 py-2 text-slate-600 shadow">
      Loading articles…
    </p>
    <p v-else-if="isEmpty" class="rounded-full bg-white/90 px-5 py-2 text-slate-600 shadow">
      No articles found.
    </p>
    <button
      v-else-if="hasMore"
      type="button"
      class="rounded-full bg-forest-700 px-6 py-2 font-semibold text-white shadow-lg hover:bg-forest-800"
      @click="emit('loadMore')"
    >
      Load more
    </button>
  </div>
</template>
