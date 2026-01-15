package com.spoof.cosa.hook

import com.spoof.cosa.BuildConfig
import com.spoof.cosa.common.SpoofConfig
import de.robv.android.xposed.XSharedPreferences

/**
 * Hook 进程使用的配置读取
 */
internal object HookPreferences {

    private val prefs by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        XSharedPreferences(BuildConfig.APPLICATION_ID, SpoofConfig.prefsName)
    }

    fun getFakePrjname(): String {
        prefs.reload()
        val value = prefs.getString(SpoofConfig.prefsKeyFakePrjname, null)?.trim().orEmpty()
        return value.ifEmpty { SpoofConfig.defaultFakePrjname }
    }
}

