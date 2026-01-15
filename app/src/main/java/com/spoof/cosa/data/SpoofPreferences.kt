package com.spoof.cosa.data

import android.content.Context
import com.spoof.cosa.common.SpoofConfig

/**
 * 模块自身进程使用的配置存取
 */
class SpoofPreferences(context: Context) {

    private val prefs = context.getSharedPreferences(SpoofConfig.prefsName, Context.MODE_PRIVATE)

    fun getFakePrjname(): String {
        val value = prefs.getString(SpoofConfig.prefsKeyFakePrjname, null)?.trim().orEmpty()
        return value.ifEmpty { SpoofConfig.defaultFakePrjname }
    }

    fun setFakePrjname(value: String) {
        val normalized = value.trim()
        val editor = prefs.edit()
        if (normalized.isEmpty()) editor.remove(SpoofConfig.prefsKeyFakePrjname)
        else editor.putString(SpoofConfig.prefsKeyFakePrjname, normalized)
        editor.commit()
    }
}

