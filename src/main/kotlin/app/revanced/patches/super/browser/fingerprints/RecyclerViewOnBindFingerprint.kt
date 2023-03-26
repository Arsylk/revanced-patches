package app.revanced.patches.`super`.browser.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import app.revanced.patches.`super`.browser.patch.SuperBrowserPatch
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.iface.instruction.Instruction
import org.jf.dexlib2.iface.instruction.ReferenceInstruction
import org.jf.dexlib2.iface.reference.TypeReference

object RecyclerViewOnBindFingerprint : MethodFingerprint(
    customFingerprint = fingerprint@{ methodDef ->
        if (methodDef.accessFlags and AccessFlags.STATIC.value != 0)
            return@fingerprint false
        for (instruction in methodDef.implementation!!.instructions) {
            if (RecyclerViewOnBindFingerprint.isVideoBeanCast(instruction)) {
                return@fingerprint true
            }
        }
        false
    }
) {

    fun isVideoBeanCast(instruction: Instruction): Boolean {
        if (instruction.opcode != Opcode.CHECK_CAST) return false
        val casted = instruction as? ReferenceInstruction ?: return false
        val typeRef = casted.reference as? TypeReference ?: return false
        return typeRef.type == SuperBrowserPatch.VideoDownloaderBean
    }
}