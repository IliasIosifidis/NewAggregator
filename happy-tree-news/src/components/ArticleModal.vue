<script setup lang="ts">
import { nextTick, onBeforeUnmount, onMounted, useTemplateRef, watch } from 'vue'
import type { Article } from '@/types/article'
import { sourceLabel, sourceTheme } from '@/constants/sources'
import { formatDate } from '@/utils/format'

const props = defineProps<{
  article: Article | null
}>()

const emit = defineEmits<{
  close: []
}>()

function onKeydown(event: KeyboardEvent) {
  if (event.key === 'Escape' && props.article) emit('close')
}

onMounted(() => window.addEventListener('keydown', onKeydown))
onBeforeUnmount(() => window.removeEventListener('keydown', onKeydown))

const dialog = useTemplateRef<HTMLElement>('dialog')
let opener: HTMLElement | null = null

watch(
  () => props.article,
  async (article, previous) => {
    // Keep the page behind the dialog from scrolling while it is open.
    document.body.classList.toggle('overflow-hidden', article !== null)

    // Move focus into the dialog for keyboard and screen reader users, and hand it back on close.
    if (article && !previous) {
      opener = document.activeElement instanceof HTMLElement ? document.activeElement : null
      await nextTick()
      dialog.value?.focus()
    } else if (!article && previous) {
      opener?.focus()
      opener = null
    }
  },
)
</script>

<template>
  <Teleport to="body">
    <Transition
      enter-active-class="transition-opacity duration-200"
      leave-active-class="transition-opacity duration-200"
      enter-from-class="opacity-0"
      leave-to-class="opacity-0"
    >
      <div
        v-if="article"
        class="fixed inset-0 z-50 flex items-center justify-center bg-black/60 p-4"
        @click.self="emit('close')"
      >
        <div
          ref="dialog"
          role="dialog"
          aria-modal="true"
          aria-labelledby="article-modal-title"
          tabindex="-1"
          class="max-h-[85vh] w-full max-w-2xl overflow-y-auto rounded-2xl bg-white p-6 shadow-2xl focus:outline-none sm:p-8"
        >
          <div class="flex items-start justify-between gap-4">
            <p
              class="text-xs font-semibold tracking-wide uppercase"
              :class="sourceTheme(article.source).accent"
            >
              {{ sourceLabel(article.source) }} · {{ formatDate(article.publishedAt) }}
            </p>
            <button
              type="button"
              aria-label="Close"
              class="-mt-2 -mr-2 rounded-full p-2 text-slate-500 hover:bg-slate-100 hover:text-slate-900"
              @click="emit('close')"
            >
              <svg
                class="size-5"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                aria-hidden="true"
              >
                <path d="M6 6l12 12M18 6 6 18" />
              </svg>
            </button>
          </div>
          <h2 id="article-modal-title" class="mt-2 text-2xl leading-tight font-bold text-slate-900">
            {{ article.title }}
          </h2>
          <p
            class="mt-4 leading-relaxed"
            :class="article.summary ? 'text-slate-700' : 'text-slate-400 italic'"
          >
            {{ article.summary ?? 'No preview available for this article.' }}
          </p>
          <a
            :href="article.url"
            target="_blank"
            rel="noopener noreferrer"
            class="mt-6 inline-flex items-center gap-1.5 font-semibold text-forest-700 hover:text-forest-900 hover:underline"
          >
            Read the full article on {{ sourceLabel(article.source) }}
            <svg
              class="size-4"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
              aria-hidden="true"
            >
              <path d="M14 4h6v6M20 4l-9 9M18 14v5a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V7a1 1 0 0 1 1-1h5" />
            </svg>
          </a>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>
