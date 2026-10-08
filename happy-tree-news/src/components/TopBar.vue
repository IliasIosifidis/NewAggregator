<template>
  <header class="sticky top-0 z-40 border-b border-white/50 bg-white/85 shadow-md backdrop-blur">
    <div class="mx-auto grid max-w-7xl grid-cols-[1fr_minmax(0,36rem)_1fr] items-center gap-4 px-4 py-3">
      <span class="hidden text-4xl font-bold tracking-tight text-forest-800 sm:block">
        FOREST NEWS
      </span>
      <!-- The search box, centered. -->
      <div class="col-start-2">
        <slot />
      </div>
    </div>
  </header>
</template>
