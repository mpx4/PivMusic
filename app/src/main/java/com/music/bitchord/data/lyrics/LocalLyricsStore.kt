package com.music.bitchord.data.lyrics

import android.content.Context
import com.music.bitchord.data.LocaleTags
import java.util.Locale

/** Small, device-local store for user-edited lyric sheets and translations. */
class LocalLyricsStore(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun lyrics(trackId: String): List<LyricLine>? = preferences.getString(key(LYRICS, trackId), null)
        ?.let { LrcLib.parseLrc(it).takeIf(List<LyricLine>::isNotEmpty) }

    /**
     * Reads the canonical language key first, then legacy/base aliases. Older
     * builds saved the app language as `en`, while the UI can ask for `en-US`;
     * accepting both keeps those translations visible after an upgrade.
     */
    fun translation(trackId: String, languageTag: String): List<LyricLine>? {
        languageCandidates(languageTag).forEach { candidate ->
            preferences.getString(key(TRANSLATIONS, "$trackId|$candidate") , null)
                ?.let { LrcLib.parseLrc(it).takeIf(List<LyricLine>::isNotEmpty) }
                ?.let { return it }
        }
        return null
    }

    fun save(trackId: String, lines: List<LyricLine>, translation: List<LyricLine>?, languageTag: String?) {
        preferences.edit().apply {
            putString(key(LYRICS, trackId), lines.toLrc())
            if (!languageTag.isNullOrBlank()) {
                // Store the canonical app-language key. Also retain the exact
                // requested tag for compatibility with pre-canonical callers.
                val canonical = canonicalLanguage(languageTag)
                val candidates = languageCandidates(languageTag)
                if (translation.isNullOrEmpty()) {
                    // An empty editor field is an explicit deletion, not a
                    // request to keep the previous cached translation.
                    candidates.forEach { candidate ->
                        remove(key(TRANSLATIONS, "$trackId|$candidate"))
                    }
                } else {
                    putString(key(TRANSLATIONS, "$trackId|$canonical"), translation.toLrc())
                    if (canonical != languageTag) {
                        putString(key(TRANSLATIONS, "$trackId|$languageTag"), translation.toLrc())
                    }
                }
            }
        }.apply()
    }

    private fun languageCandidates(raw: String): List<String> {
        val canonical = canonicalLanguage(raw)
        val normalized = raw.trim().replace('_', '-')
        val base = Locale.forLanguageTag(normalized).language.lowercase(Locale.ROOT)
        return listOf(canonical, normalized, base).filter { it.isNotBlank() }.distinct()
    }

    private fun canonicalLanguage(raw: String): String = LocaleTags.normalizeAppTag(raw)

    private fun key(namespace: String, id: String) = "$namespace:${id.hashCode()}:$id"

    private companion object {
        const val NAME = "local_lyrics"
        const val LYRICS = "lyrics"
        const val TRANSLATIONS = "translation"
    }
}
