package app.revanced.patches.jakdojade.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint

object IsUserTrackingEnabledFingerprint : MethodFingerprint(
    returnType = "Z",
    parameters = emptyList(),
    customFingerprint = { methodDef ->
        methodDef.definingClass =="Lcom/gemius/sdk/Config;"
                && methodDef.name == "isUserTrackingEnabled"
    }
)