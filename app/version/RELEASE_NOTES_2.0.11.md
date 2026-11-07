# PivMusic 2.0.11 — Playlists, Library & Lyrics Update

PivMusic 2.0.11 selectively incorporates upstream improvements from ChordPlay 2.0.5–2.0.7 while preserving PivMusic's local lyrics editor, offline translations, Discord Rich Presence, branding, and GitHub update checker.

## What's new

- Improved Playlist Picker with cleaner organization and smoother interactions for adding songs to playlists.
- Improved local music library organization and recent listening history.
- Optimistic playlist membership cache, making playlist changes appear faster in the interface.
- Music repository fixes for video shelves, radio loading, and content retrieval.
- Plain-text lyrics are now saved locally even when they do not contain timestamps.
- Enhanced LRC timing is preserved, including word-by-word timestamps when lyrics are edited or stored locally.
- Copying lyrics preserves detailed word-level timing instead of flattening it into ordinary line timestamps.
- Manual offline translations remain persistent line by line, including replacement and correction of existing translations.
- Online and local translation priority remains stable, so local translations can be restored after disabling online translation.
- Existing Discord RPC, automatic GitHub update detection, and offline lyric editing behavior remain preserved.

The upstream offline-lyrics storage and full download-flow rewrites were intentionally not copied wholesale because they would replace PivMusic's tested local lyrics implementation. PivMusic keeps its own compatible implementation for plain text, line timing, enhanced LRC, and manual translations.
