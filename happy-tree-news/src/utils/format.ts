const dateFormatter = new Intl.DateTimeFormat(undefined, {
  dateStyle: 'medium',
  timeStyle: 'short',
})

export function formatDate(iso: string): string {
  return dateFormatter.format(new Date(iso))
}

const timeFormatter = new Intl.DateTimeFormat(undefined, { timeStyle: 'medium' })

// Today's entries only need the time; older ones also need the date.
export function formatLogTime(iso: string): string {
  const date = new Date(iso)
  const today = date.toDateString() === new Date().toDateString()
  return today ? timeFormatter.format(date) : dateFormatter.format(date)
}

// Parsed into an inert document, so markup from the feed is never executed or rendered.
export function toPlainText(html: string): string {
  return new DOMParser().parseFromString(html, 'text/html').body.textContent?.trim() ?? ''
}
