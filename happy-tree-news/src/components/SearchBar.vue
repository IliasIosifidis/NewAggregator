<script setup lang="ts">
import { computed, ref, useId, watch } from 'vue'
import { articleKey, type Article } from '@/types/article'
import { sourceLabel, sourceTheme } from '@/constants/sources'
import { useSuggestions } from '@/composables/useSuggestions'

const props = defineProps<{
  /** Suggestions are limited to the selected source, like the full results. */
  source: string | null
}>()

const emit = defineEmits<{
  /** Full results for the text; an empty string returns to the feed. */
  search: [text: string]
  select: [article: Article]
}>()

const listboxId = useId()
const optionId = (index: number) => `${listboxId}-option-${index}`

const text = ref('')
const { suggestions, update } = useSuggestions(() => props.source)

// Whether the user wants the list; it only shows once there is something in it.
const expanded = ref(false)
// -1 means nothing is highlighted, so Enter runs a full search.
const activeIndex = ref(-1)
const isOpen = computed(() => expanded.value && suggestions.value.length > 0)

function close() {
  expanded.value = false
  activeIndex.value = -1
}

watch(text, (value) => {
  update(value)
  if (value.trim() === '') {
    close()
    emit('search', '')
  } else {
    expanded.value = true
    activeIndex.value = -1
  }
})

// A new list makes the highlighted position meaningless.
watch(suggestions, () => {
  activeIndex.value = -1
})

// Arrow keys also reopen a dismissed list. Position -1 is the text box itself,
// so moving past either end returns to what was typed.
function move(step: 1 | -1) {
  if (suggestions.value.length === 0) return
  expanded.value = true
  const positions = suggestions.value.length + 1
  activeIndex.value = ((activeIndex.value + 1 + step + positions) % positions) - 1
}

function choose(article: Article) {
  close()
  emit('select', article)
}

function onEnter() {
  const highlighted = isOpen.value ? suggestions.value[activeIndex.value] : undefined
  if (highlighted) {
    choose(highlighted)
  } else {
    close()
    emit('search', text.value)
  }
}

function onEscape(event: KeyboardEvent) {
  // With the list closed, let the browser clear the box as usual.
  if (!isOpen.value) return
  event.preventDefault()
  close()
}

// Covers clicking outside and tabbing away. Clicks on the list keep focus in the box (see mousedown below).
function onFocusOut(event: FocusEvent) {
  const root = event.currentTarget as HTMLElement
  if (!root.contains(event.relatedTarget as Node | null)) close()
}
</script>

<template>
  <form role="search" class="relative w-full" @submit.prevent="onEnter" @focusout="onFocusOut">
    <label class="relative block">
      <span class="sr-only">Search articles</span>
      <svg
        class="pointer-events-none absolute top-1/2 left-3.5 size-5 -translate-y-1/2 text-slate-400"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
        stroke-linecap="round"
        aria-hidden="true"
      >
        <circle cx="11" cy="11" r="7" />
        <path d="m20 20-3.5-3.5" />
      </svg>
      <input
        v-model="text"
        type="search"
        role="combobox"
        aria-autocomplete="list"
        :aria-expanded="isOpen"
        :aria-controls="listboxId"
        :aria-activedescendant="activeIndex >= 0 ? optionId(activeIndex) : undefined"
        autocomplete="off"
        placeholder="Search articles"
        class="w-full rounded-full border border-slate-200 bg-white py-2 pr-4 pl-11 text-slate-900 shadow-sm placeholder:text-slate-400 focus:border-forest-600 focus:ring-2 focus:ring-forest-600/30 focus:outline-none focus:placeholder:text-transparent"
        @keydown.down.prevent="move(1)"
        @keydown.up.prevent="move(-1)"
        @keydown.esc="onEscape"
      />
    </label>

    <!-- v-show keeps the listbox in the DOM so aria-controls always points at it. -->
    <ul
      v-show="isOpen"
      :id="listboxId"
      role="listbox"
      aria-label="Suggested articles"
      class="absolute inset-x-0 top-full z-10 mt-2 overflow-hidden rounded-2xl bg-white py-1.5 shadow-xl ring-1 ring-black/10"
      @mousedown.prevent
    >
      <li
        v-for="(article, index) in suggestions"
        :id="optionId(index)"
        :key="articleKey(article)"
        role="option"
        :aria-selected="index === activeIndex"
        class="cursor-pointer px-4 py-2"
        :class="{ 'bg-forest-100': index === activeIndex }"
        @mousemove="activeIndex = index"
        @click="choose(article)"
      >
        <span class="line-clamp-2 leading-snug font-semibold text-slate-900">{{ article.title }}</span>
        <span
          class="mt-0.5 block text-xs font-semibold tracking-wide uppercase"
          :class="sourceTheme(article.source).accent"
        >
          {{ sourceLabel(article.source) }}
        </span>
      </li>
    </ul>
  </form>
</template>
