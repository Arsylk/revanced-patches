package app.revanced.patches.jakdojade.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.iface.instruction.formats.Instruction21c
import org.jf.dexlib2.iface.reference.StringReference

object FirebaseRemoteGetValueFingerprint : MethodFingerprint(
    returnType = "Ljava/lang/Boolean;",
    parameters = listOf("Ljava/lang/String;"),
    customFingerprint = fingerprint@{ methodDef, _ ->
        if (!methodDef.definingClass.startsWith("Lcom/google/android/gms/measurement/internal"))
            return@fingerprint false
        val impl = methodDef.implementation ?: return@fingerprint false
        impl.instructions.forEach { instruction ->
            if (instruction.opcode != Opcode.CONST_STRING) return@forEach
            val str = ((instruction as Instruction21c).reference as StringReference).string
            if (str == "Failed to load metadata: Metadata bundle is null") {
                return@fingerprint true
            }
        }

        return@fingerprint false
    }
)