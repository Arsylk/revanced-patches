package app.revanced.patches.jakdojade.fingerprints

import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.AccessFlags

object GooglePurchaseConstructorFingerprint : MethodFingerprint(
    returnType = "V",
    access = AccessFlags.CONSTRUCTOR or AccessFlags.PUBLIC,
    parameters = listOf(
        "Lcom/citynav/jakdojade/pl/android/billing/output/GoogleProduct;",
        "J",
        "Ljava/lang/String;",
        "Ljava/util/Date;",
        "Ljava/util/Date;",
    )
)