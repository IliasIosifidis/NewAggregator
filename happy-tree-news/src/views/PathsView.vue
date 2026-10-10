<script setup lang="ts">
import TopBar from '@/components/TopBar.vue'
import PageLink from '@/components/PageLink.vue'
import ActivityPanel from '@/components/ActivityPanel.vue'
import { useActivity } from '@/composables/useActivity'
import forestPaths from '@/assets/forest-paths.jpg'

const { activity, failed } = useActivity()
</script>

<template>
  <div>
    <TopBar>
      <template #start class="flex flex-row">
        <PageLink to="/">
          <span class="text-4xl text-forest-800" aria-hidden="true">&lt;&lt;</span>
          <span class="text-4xl text-forest-800"> FOREST NEWS </span>
        </PageLink>
      </template>

      <!-- This page's title, styled like the link to it on the news page. Phones only have room for the link back. -->
      <template #end>
        <span class="hidden text-4xl font-bold tracking-tight whitespace-nowrap text-bark-700 sm:block">
          FOREST PATHS
        </span>
      </template>
    </TopBar>

    <!-- On wider screens the map is the page's background, as wide as the page, and you scroll down it to follow the
         paths. The activity panel sits over its top-left corner, which is only trees. Phones have no room beside the
         panel, so the panel comes first and the map follows it. -->
    <main class="grid">
      <ActivityPanel
          :activity="activity"
          :failed="failed"
          class="relative m-4 md:col-start-1 md:row-start-1 md:w-80 md:self-start lg:w-96"
      />

      <img
          :src="forestPaths"
          width="1600"
          height="2478"
          alt="Map of the Forest News services as paths through a forest. The news sources lead to the news ingestor, which publishes to RabbitMQ. The article store and search service consume from RabbitMQ; the article store keeps articles in PostgreSQL and the search service in Elasticsearch. The Forest News front end calls both."
          class="w-full md:col-start-1 md:row-start-1"
      />
    </main>
  </div>
</template>
