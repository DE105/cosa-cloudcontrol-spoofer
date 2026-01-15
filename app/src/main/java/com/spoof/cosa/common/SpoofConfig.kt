package com.spoof.cosa.common

/**
 * 伪装配置常量
 */
object SpoofConfig {

    const val targetPackageName: String = "com.oplus.cosa"

    const val systemPropertiesClassName: String = "android.os.SystemProperties"
    const val prjnamePropertyKey: String = "ro.boot.prjname"

    const val defaultFakePrjname: String = "24831"

    const val prefsName: String = "cosa_spoof"
    const val prefsKeyFakePrjname: String = "fake_prjname"
}

