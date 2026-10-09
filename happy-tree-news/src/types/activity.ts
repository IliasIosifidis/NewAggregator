export interface ActivityEntry {
  timestamp: string
  message: string
}

/** The latest activity messages for each service, by service name, newest first. */
export type Activity = Record<string, ActivityEntry[]>
