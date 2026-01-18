package com.spoof.cosa.common

import com.spoof.cosa.BuildConfig

/**
 * 伪装配置常量
 */
object SpoofConfig {

    const val targetPackageName: String = "com.oplus.cosa"

    const val systemPropertiesClassName: String = "android.os.SystemProperties"
    const val prjnamePropertyKey: String = "ro.boot.prjname"

    const val defaultFakePrjname: String = "24831"

    /**
     * LSPosed 对 New XSharedPreferences 的兼容通常依赖默认文件名：${applicationId}_preferences.xml
     */
    val prefsName: String = BuildConfig.APPLICATION_ID + "_preferences"

    /**
     * 旧版本配置文件名 - 用于兼容迁移
     */
    const val prefsNameLegacy: String = "cosa_spoof"
    const val prefsKeyFakePrjname: String = "fake_prjname"
}

