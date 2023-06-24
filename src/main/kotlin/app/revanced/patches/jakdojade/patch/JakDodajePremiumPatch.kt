package app.revanced.patches.jakdojade.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.replaceInstructions
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint.Companion.resolve
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.annotations.RequiresIntegrations
import app.revanced.patches.jakdojade.annotations.JakDojadeCompatibility
import app.revanced.patches.jakdojade.fingerprints.GooglePurchaseConstructorFingerprint
import app.revanced.patches.jakdojade.fingerprints.PremiumManagerConstructorFingerprint
import app.revanced.patches.jakdojade.fingerprints.PremiumManagerIsPremiumFingerprint
import app.revanced.patches.jakdojade.fingerprints.SharedPrefsFingerprint
import java.util.Date

@Patch
@Name("jakdojade-premium")
@Description("Unlock premium features in JakDojade")
@JakDojadeCompatibility
@RequiresIntegrations
@Version("0.0.1")
class JakDodajePremiumPatch : BytecodePatch(
    listOf(
        PremiumManagerConstructorFingerprint,
        SharedPrefsFingerprint,
        GooglePurchaseConstructorFingerprint,
    )
) {

    companion object {
        const val GoogleProduct = "Lcom/citynav/jakdojade/pl/android/billing/output/GoogleProduct;"
        const val Date = "Ljava/util/Date;"
        const val Thread = "Ljava/lang/Thread;"

        const val IntegrationPatch = "Lapp/revanced/integrations/patches/JakDojadePremiumPatch;"
    }

    override fun execute(context: BytecodeContext): PatchResult {
        val result = GooglePurchaseConstructorFingerprint.result
        val className = result!!.classDef.toString()
        val classNameStr = className.run {
            substring(1, length - 1)
                .replace("/", ".")
        }

        SharedPrefsFingerprint.result!!.mutableMethod.replaceInstructions(
            0,
            """
                const-string v0, "$classNameStr"
                invoke-static {v0}, $IntegrationPatch->getMockPurchase(Ljava/lang/String;)Ljava/lang/Object;
                move-result-object v0
                check-cast v0, $className
                check-cast v0, $className
                return-object v0
            """
        )

        PremiumManagerIsPremiumFingerprint.resolve(context, PremiumManagerConstructorFingerprint.result!!.classDef)
        PremiumManagerIsPremiumFingerprint.result!!.mutableMethod.replaceInstructions(
            0,
            """
                const/4 v0, 0x1
                return v0
            """
        )

        return PatchResultSuccess()
    }
}