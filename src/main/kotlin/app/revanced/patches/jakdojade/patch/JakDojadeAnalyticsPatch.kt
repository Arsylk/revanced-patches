package app.revanced.patches.jakdojade.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.*
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint.Companion.resolve
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.patch.annotations.RequiresIntegrations
import app.revanced.patcher.util.smali.ExternalLabel
import app.revanced.patches.jakdojade.annotations.JakDojadeCompatibility
import app.revanced.patches.jakdojade.fingerprints.EventSenderSendFingerprint
import app.revanced.patches.jakdojade.fingerprints.FirebaseRemoteGetValueFingerprint
import app.revanced.patches.jakdojade.fingerprints.IsUserTrackingEnabledFingerprint
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.iface.instruction.formats.Instruction21c
import org.jf.dexlib2.iface.reference.StringReference
import org.jf.dexlib2.writer.builder.BuilderClassDef

@Patch
@Name("jakdojade-analytics")
@Description("Disable analytics features in JakDojade")
@JakDojadeCompatibility
@RequiresIntegrations
@Version("0.0.1")
class JakDojadeAnalyticsPatch : BytecodePatch(
    listOf(
        IsUserTrackingEnabledFingerprint,
        FirebaseRemoteGetValueFingerprint,
        EventSenderSendFingerprint,
    )
) {

    companion object {
        const val IntegrationPatch = "Lapp/revanced/integrations/patches/JakDojadeAnalyticsPatch;"
        const val TrackerImpl = "Lcom/citynav/jakdojade/pl/android/common/externallibraries/GemiusAudienceImpressionsTracker;"
    }


    override fun execute(context: BytecodeContext): PatchResult {
        IsUserTrackingEnabledFingerprint.result?.mutableMethod?.run {
            removeInstructions(0, implementation!!.instructions.size - 1)
            addInstructions("""
                const/4 v0, 0x0
                return v0
            """.trimIndent())
        }

        FirebaseRemoteGetValueFingerprint.result?.mutableMethod?.run {
            addInstructions(0, """
                invoke-static {p1}, $IntegrationPatch->getValue(Ljava/lang/String;)Ljava/lang/Boolean;
                move-result-object v0
                if-eqz v0, :continue_original
                return-object v0
            """.trimIndent(),
            listOf(ExternalLabel("continue_original", instruction(0)))
            )
        }

        EventSenderSendFingerprint.result?.mutableMethod?.run {
            removeInstructions(0, implementation!!.instructions.size - 1)
            addInstructions("""
                return-void
            """.trimIndent())
        }

        findAndOverrideAnalyticsTracker(context)


        return PatchResultSuccess()
    }


    private fun findAndOverrideAnalyticsTracker(context: BytecodeContext) {
        val implClass = context.classes.first {
            it.type == TrackerImpl &&
                    it.interfaces.size == 1 /* unnecessary check ? */
        }
        val iClass = implClass.interfaces.first()
        context.findClass find@{ classDef ->
            val methods = classDef.methods.toList()
            if (methods.size != 2) return@find false
            val method= methods.firstOrNull { it.returnType == iClass } ?: return@find false
            val impl = method.implementation ?: return@find false
            for (instruction in impl.instructions) {
                if (instruction.opcode == Opcode.NEW_INSTANCE) {
                    val arg = (instruction as Instruction21c).reference.toString()
                    if (arg == TrackerImpl)
                        return@find true
                }
            }

            return@find false
        }?.mutableClass?.apply {
            methods.firstOrNull { it.returnType == iClass }?.apply {
                val iClassStr = iClass.run {
                    substring(1, length - 1)
                        .replace("/", ".")
                }
                removeInstructions(0, implementation!!.instructions.size - 1)
                addInstructions("""
                    const-string v0, "$iClassStr"
                    invoke-static {v0}, $IntegrationPatch->magicProxy(Ljava/lang/String;)Ljava/lang/Object;
                    move-result-object v0
                    check-cast v0, $iClass
                    return-object v0
                """.trimIndent())
            }
        }
    }
}