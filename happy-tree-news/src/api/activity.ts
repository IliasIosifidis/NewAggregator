import type { Activity } from '@/types/activity'

// Served by search-service, which reads the services' activity logs back from Elasticsearch.
// Lists can be empty right after a restart.
export async function getActivity(signal?: AbortSignal): Promise<Activity> {
  const response = await fetch('/api/activity', { signal })
  if (!response.ok) {
    throw new Error(`GET /api/activity failed with HTTP ${response.status}`)
  }
  return response.json()
}
