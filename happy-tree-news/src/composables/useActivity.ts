import { onActivated, onDeactivated, ref } from 'vue'
import { getActivity } from '@/api/activity'
import type { Activity } from '@/types/activity'

const REFRESH_MS = 10_000

// The services' latest activity, refreshed while the page is on screen. For pages inside the
// app's <KeepAlive>: refreshing starts when the page is shown and stops when you leave it.
export function useActivity() {
  /** Null until the first response arrives. */
  const activity = ref<Activity | null>(null)
  /** Whether the latest request failed; the last activity loaded stays in place. */
  const failed = ref(false)
  let timer: ReturnType<typeof setInterval> | undefined
  // Only the latest request may update the panel; a new one aborts the one in flight.
  let controller: AbortController | null = null

  async function refresh() {
    // Nobody is looking at a hidden tab.
    if (document.hidden) return
    controller?.abort()
    const current = new AbortController()
    controller = current
    try {
      activity.value = await getActivity(current.signal)
      failed.value = false
    } catch (e) {
      if (!current.signal.aborted) {
        console.error(e)
        failed.value = true
      }
    }
  }

  onActivated(() => {
    refresh()
    timer = setInterval(refresh, REFRESH_MS)
  })
  onDeactivated(() => {
    clearInterval(timer)
    controller?.abort()
  })

  return { activity, failed }
}
