package app.revanced.patches.jakdojade.fingerprints

import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.AccessFlags

object PremiumManagerConstructorFingerprint : MethodFingerprint(
    returnType = "V",
    access = AccessFlags.PUBLIC or AccessFlags.CONSTRUCTOR,
    customFingerprint = customFingerprint@{ methodDef ->
        val firstArg = methodDef.parameterTypes.getOrNull(0)
        if (firstArg != "Lcom/citynav/jakdojade/pl/android/billing/GooglePlayPurchaseManager;") {
            return@customFingerprint false
        }
        if ("Lcom/citynav/jakdojade/pl/android/common/analytics/AnalyticsPropertiesManager;" !in methodDef.parameterTypes) {
            return@customFingerprint false
        }
        if (methodDef.parameterTypes.none { it.startsWith("Lcom/citynav/jakdojade/pl/android/common/tools") }) {
            return@customFingerprint false
        }
        true
    }
)