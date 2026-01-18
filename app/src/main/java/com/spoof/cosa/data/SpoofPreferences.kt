package com.spoof.cosa.data

import android.content.Context
import com.spoof.cosa.common.SpoofConfig

/**
 * 模块自身进程使用的配置存取
 */
class SpoofPreferences(context: Context) {

    private val prefs = context.getSharedPreferences(SpoofConfig.prefsName, Context.MODE_PRIVATE)
    private val legacyPrefs = context.getSharedPreferences(SpoofConfig.prefsNameLegacy, Context.MODE_PRIVATE)

    fun getFakePrjname(): String {
        val value = prefs.getString(SpoofConfig.prefsKeyFakePrjname, null)?.trim().orEmpty()
        if (value.isNotEmpty()) return value

        val legacyValue = legacyPrefs.getString(SpoofConfig.prefsKeyFakePrjname, null)?.trim().orEmpty()
        if (legacyValue.isNotEmpty()) {
            prefs.edit().putString(SpoofConfig.prefsKeyFakePrjname, legacyValue).commit()
            return legacyValue
        }

        return SpoofConfig.defaultFakePrjname
    }

    fun setFakePrjname(value: String) {
        val normalized = value.trim()
        val editor = prefs.edit()
        if (normalized.isEmpty()) editor.remove(SpoofConfig.prefsKeyFakePrjname)
        else editor.putString(SpoofConfig.prefsKeyFakePrjname, normalized)
        editor.commit()
    }
}

