package com.spoof.cosa.hook

import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.spoof.cosa.common.SpoofConfig
import java.util.HashMap

@InjectYukiHookWithXposed
class HookEntry : IYukiHookXposedInit {

    override fun onInit() = configs {
        isDebug = false
        isEnableHookSharedPreferences = true
    }

    override fun onHook() = encase {
        loadApp(name = SpoofConfig.targetPackageName) {
            val systemPropertiesClass = SpoofConfig.systemPropertiesClassName.toClass(loader = null)
            systemPropertiesClass.resolve().apply {
                firstMethod {
                    name = "get"
                    parameters(String::class)
                }.hook {
                    replaceAny {
                        val key = args().first().string()
                        if (key == SpoofConfig.prjnamePropertyKey) HookPreferences.getFakePrjname()
                        else callOriginal()
                    }
                }
                firstMethod {
                    name = "get"
                    parameters(String::class, String::class)
                }.hook {
                    replaceAny {
                        val key = args().first().string()
                        if (key == SpoofConfig.prjnamePropertyKey) HookPreferences.getFakePrjname()
                        else callOriginal()
                    }
                }
            }

            /**
             * 兼容：com.oplus.cosa 可能把系统属性缓存到自身的 SharedPreferences
             * 如 /data/data/com.oplus.cosa/shared_prefs/prop_and_oplus_feature_sp.xml
             * 因此额外拦截 SharedPreferences 读取，确保即使已缓存也能返回伪装值
             */
            "android.app.SharedPreferencesImpl".toClassOrNull(loader = null)?.resolve()?.apply {
                firstMethod {
                    name = "getString"
                    parameters(String::class, String::class)
                }.hook {
                    replaceAny {
                        val key = args().first().string()
                        if (key == SpoofConfig.prjnamePropertyKey) HookPreferences.getFakePrjname()
                        else callOriginal()
                    }
                }
                firstMethod {
                    name = "getAll"
                    emptyParameters()
                }.hook {
                    replaceAny {
                        val original = callOriginal<Map<*, *>>()
                        if (original != null && original.containsKey(SpoofConfig.prjnamePropertyKey)) {
                            val copy = HashMap(original)
                            copy[SpoofConfig.prjnamePropertyKey] = HookPreferences.getFakePrjname()
                            copy
                        } else original
                    }
                }
            }
        }
    }
}
