import { onScopeDispose, ref, watch } from 'vue'
import { searchArticles } from '@/api/articles'
import type { Article } from '@/types/article'

const SUGGESTION_SIZE = 6
// Long enough to skip the keystrokes in a word, short enough to feel live.
const TYPING_PAUSE_MS = 300

// Search-as-you-type suggestions for a text box, kept current for both its text and the source filter.
export function useSuggestions(source: () => string | null) {
  const suggestions = ref<Article[]>([])
  let text = ''
  let timer: ReturnType<typeof setTimeout> | undefined
  // Only the latest request may update the list; a new one aborts the one in flight.
  let controller: AbortController | null = null

  function cancel() {
    clearTimeout(timer)
    controller?.abort()
  }

  async function load() {
    cancel()
    if (text === '') {
      suggestions.value = []
      return
    }

    const current = new AbortController()
    controller = current
    try {
      suggestions.value = await searchArticles(
        { text, source: source(), size: SUGGESTION_SIZE },
        current.signal,
      )
    } catch (e) {
      // Suggestions are optional: the list just empties, and Enter still runs a full search.
      if (!current.signal.aborted) {
        console.error(e)
        suggestions.value = []
      }
    }
  }

  function update(value: string) {
    const next = value.trim()
    if (next === text) return
    text = next
    // An emptied box clears the list at once; otherwise wait for a pause in typing.
    if (text === '') {
      load()
    } else {
      clearTimeout(timer)
      timer = setTimeout(load, TYPING_PAUSE_MS)
    }
  }

  watch(source, load)
  onScopeDispose(cancel)

  return { suggestions, update }
}
