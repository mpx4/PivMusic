# PivMusic 2.0.12 — Share Links & Addon Quality

PivMusic 2.0.12 keeps the local lyrics editor, Enhanced LRC and word-synced timing preservation, offline translations, Discord Rich Presence, branding, and automatic GitHub update detection from 2.0.11.

### What's new

- Added PivMusic share links using `https://music.piv.lol/t/<video-id>`.
- Added Android App Links for `music.piv.lol`, so supported links can open PivMusic directly when the domain is verified.
- Added a rich web fallback for shared tracks, including YouTube Music metadata and Discord Open Graph previews.
- Added addon quality selection when an addon exposes multiple quality options.
- Persisted the selected addon quality and restored it when the addon is reopened.
- Kept addon download and lossless-output policies visible while the source is unavailable.
- Preserved the existing PivMusic lyrics, translation, Discord, and update-checker behavior instead of importing the upstream UI redesign wholesale.

### Upstream integration note

This release selectively incorporates the BitChord addon-quality protocol improvements. The larger ChordPlay 2.1.3–2.1.4 UI and playback redesigns were not imported because they would replace PivMusic-specific lyrics, branding, playback, and sharing changes.
