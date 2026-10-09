<script setup lang="ts">
import TopBar from '@/components/TopBar.vue'
import PageLink from '@/components/PageLink.vue'
import ActivityPanel from '@/components/ActivityPanel.vue'
import { useActivity } from '@/composables/useActivity'
import forestPaths from '@/assets/forest-paths.jpg'

const { activity, failed } = useActivity()
</script>

<template>
  <!-- The forest fills the whole page below the top bar. Wide screens fit everything without scrolling. -->
  <div class="flex min-h-svh flex-col md:h-svh">
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

    <!-- Activity on the left, the map on the right. Phones stack them and scroll the page. -->
    <main class="relative flex flex-1 flex-col gap-4 overflow-hidden p-4 md:min-h-0 md:flex-row">
      <!-- The map is taller than most screens are wide, so a blurred copy fills the space around it. -->
      <img
          :src="forestPaths"
          alt=""
          class="absolute inset-0 size-full scale-110 object-cover blur-2xl brightness-50"
      />

      <ActivityPanel :activity="activity" :failed="failed" class="relative md:w-80 md:shrink-0 lg:w-96" />

      <div class="relative aspect-[1600/2478] md:aspect-auto md:flex-1">
        <img
            :src="forestPaths"
            width="1600"
            height="2478"
            alt="Map of the Forest News services as paths through a forest. The news sources lead to the news ingestor, which publishes to RabbitMQ. The article store and search service consume from RabbitMQ; the article store keeps articles in PostgreSQL and the search service in Elasticsearch. The Forest News front end calls both."
            class="absolute inset-0 size-full object-contain drop-shadow-2xl"
        />
      </div>
    </main>
  </div>
</template>
