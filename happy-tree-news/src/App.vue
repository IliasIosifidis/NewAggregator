<script setup lang="ts">
import TopBar from '@/components/TopBar.vue'
import SearchBar from '@/components/SearchBar.vue'
import SourceList from '@/components/SourceList.vue'
import ArticleGrid from '@/components/ArticleGrid.vue'
import FeedStatus from '@/components/FeedStatus.vue'
import ArticleModal from '@/components/ArticleModal.vue'
import { SOURCES } from '@/constants/sources'
import { useArticlesStore } from '@/stores/articles'

const store = useArticlesStore()

store.loadArticles()
</script>

<template>
  <TopBar>
    <SearchBar :source="store.selectedSource" @search="store.search" @select="store.openArticle" />
  </TopBar>

  <main class="mx-auto max-w-7xl px-4 py-6">
    <SourceList :sources="SOURCES" :selected="store.selectedSource" @select="store.selectSource" />
    <ArticleGrid class="mt-6" :articles="store.articles" @read-more="store.openArticle" />
    <FeedStatus
      :loading="store.loading"
      :error="store.error"
      :is-empty="store.isEmpty"
      :has-more="store.hasMore"
      @retry="store.retry"
      @load-more="store.loadMore"
    />
  </main>

  <ArticleModal :article="store.activeArticle" @close="store.closeArticle" />
</template>
