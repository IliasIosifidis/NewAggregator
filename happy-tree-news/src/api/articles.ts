import type { Article, ArticlePage } from '@/types/article'
import { toPlainText } from '@/utils/format'

export interface ArticleQuery {
  source: string | null
  page: number
  size: number
}

export interface SearchQuery {
  text: string
  source: string | null
  size: number
}

// Requests go to `/api` on the same origin; in development Vite proxies them to the backend (see vite.config.ts).
export async function getArticles(
  { source, page, size }: ArticleQuery,
  signal?: AbortSignal,
): Promise<ArticlePage> {
  const params = new URLSearchParams({
    page: String(page),
    size: String(size),
    sort: 'publishedAt,desc',
  })
  if (source) params.set('source', source)

  const response = await fetch(`/api/articles?${params}`, { signal })
  if (!response.ok) {
    throw new Error(`GET /api/articles failed with HTTP ${response.status}`)
  }

  const result: ArticlePage = await response.json()
  return { ...result, content: result.content.map(cleanSummary) }
}

// Served by search-service, ranked by relevance rather than date.
export async function searchArticles(
  { text, source, size }: SearchQuery,
  signal?: AbortSignal,
): Promise<Article[]> {
  const params = new URLSearchParams({ q: text, size: String(size) })
  if (source) params.set('source', source)

  const response = await fetch(`/api/search?${params}`, { signal })
  if (!response.ok) {
    throw new Error(`GET /api/search failed with HTTP ${response.status}`)
  }

  const results: Article[] = await response.json()
  return results.map(cleanSummary)
}

// Some feeds (e.g. Guardian editorials) put HTML tags and entities in the summary.
function cleanSummary(article: Article): Article {
  return {
    ...article,
    summary: article.summary ? toPlainText(article.summary) || null : null,
  }
}
