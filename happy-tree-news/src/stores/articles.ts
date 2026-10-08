import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { getArticles, searchArticles } from '@/api/articles'
import { articleKey, type Article } from '@/types/article'

const PAGE_SIZE = 24
// search-service returns at most 20 results and has no paging.
const SEARCH_SIZE = 20

export const useArticlesStore = defineStore('articles', () => {
  const articles = ref<Article[]>([])
  const selectedSource = ref<string | null>(null)
  const query = ref('')
  const page = ref(0)
  const totalPages = ref(0)
  const loading = ref(false)
  const error = ref<string | null>(null)
  const activeArticle = ref<Article | null>(null)

  const isSearching = computed(() => query.value !== '')
  const hasMore = computed(() => !isSearching.value && page.value + 1 < totalPages.value)
  const isEmpty = computed(() => !loading.value && !error.value && articles.value.length === 0)

  // Only the latest request may update state; a new one aborts the one in flight.
  let controller: AbortController | null = null

  async function request(load: (signal: AbortSignal) => Promise<void>, failure: string) {
    controller?.abort()
    const current = new AbortController()
    controller = current
    loading.value = true
    error.value = null

    try {
      await load(current.signal)
    } catch (e) {
      if (!current.signal.aborted) {
        console.error(e)
        error.value = failure
      }
    } finally {
      if (!current.signal.aborted) loading.value = false
    }
  }

  function loadPage(pageNumber: number) {
    return request(async (signal) => {
      const result = await getArticles(
        { source: selectedSource.value, page: pageNumber, size: PAGE_SIZE },
        signal,
      )
      if (pageNumber === 0) {
        articles.value = result.content
      } else {
        // New articles shift page offsets, so a later page can repeat ones already shown.
        const knownKeys = new Set(articles.value.map(articleKey))
        articles.value.push(...result.content.filter((article) => !knownKeys.has(articleKey(article))))
      }
      page.value = result.page.number
      totalPages.value = result.page.totalPages
    }, 'Could not load articles. Please try again.')
  }

  function loadResults() {
    return request(async (signal) => {
      articles.value = await searchArticles(
        { text: query.value, source: selectedSource.value, size: SEARCH_SIZE },
        signal,
      )
    }, 'Search failed. Please try again.')
  }

  function refresh() {
    return isSearching.value ? loadResults() : loadPage(0)
  }

  function loadArticles() {
    return loadPage(0)
  }

  function loadMore() {
    if (loading.value || !hasMore.value) return
    return loadPage(page.value + 1)
  }

  function retry() {
    if (isSearching.value) return loadResults()
    return loadPage(articles.value.length > 0 ? page.value + 1 : 0)
  }

  function selectSource(source: string | null) {
    if (source === selectedSource.value) return
    selectedSource.value = source
    articles.value = []
    totalPages.value = 0
    return refresh()
  }

  function search(text: string) {
    const next = text.trim()
    if (next === query.value) return
    // Switching between the feed and search results starts from an empty grid;
    // refining a search keeps the current results until the new ones arrive.
    if (next === '' || query.value === '') {
      articles.value = []
      totalPages.value = 0
    }
    query.value = next
    return refresh()
  }

  function openArticle(article: Article) {
    activeArticle.value = article
  }

  function closeArticle() {
    activeArticle.value = null
  }

  return {
    articles,
    selectedSource,
    query,
    loading,
    error,
    activeArticle,
    hasMore,
    isEmpty,
    loadArticles,
    loadMore,
    retry,
    selectSource,
    search,
    openArticle,
    closeArticle,
  }
})
