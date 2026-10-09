<template>
  <header class="sticky top-0 z-40 border-b border-white/50 bg-white/85 shadow-md backdrop-blur">
    <!-- The row is at least as tall as the search box, so the bar keeps its size on pages without one. -->
    <div
      class="mx-auto grid max-w-7xl grid-cols-[1fr_minmax(0,36rem)_1fr] grid-rows-[minmax(2.625rem,auto)] items-center gap-4 px-4 py-3"
    >
      <div>
        <slot name="start" />
      </div>
      <!-- The search box, centered. -->
      <div>
        <slot />
      </div>
      <div class="justify-self-end text-right">
        <slot name="end" />
      </div>
    </div>
  </header>
</template>
