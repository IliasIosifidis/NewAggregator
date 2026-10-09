<template>
  <!-- Kept alive so the news page keeps its search text, results and source when you come back. -->
  <RouterView v-slot="{ Component }">
    <KeepAlive>
      <component :is="Component" />
    </KeepAlive>
  </RouterView>
</template>
