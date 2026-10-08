// Shared by the article store feed and search-service results, so both render with the same components.
export interface Article {
  source: string
  externalId: string
  title: string
  url: string
  summary: string | null
  publishedAt: string
}

// Unique across feeds; search-service uses the same `source:externalId` as its document id.
export function articleKey(article: Article): string {
  return `${article.source}:${article.externalId}`
}

export interface PageMetadata {
  size: number
  number: number
  totalElements: number
  totalPages: number
}

export interface ArticlePage {
  content: Article[]
  page: PageMetadata
}
