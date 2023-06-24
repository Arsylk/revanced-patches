package app.revanced.patches.jakdojade.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.dexbacked.instruction.DexBackedInstruction35c

object SharedPrefsFingerprint : MethodFingerprint(
    accessFlags = AccessFlags.PUBLIC.value,
    parameters = listOf(),
    strings = listOf("marker-options"),
    customFingerprint = { methodDef, _ ->
        methodDef.implementation?.instructions?.any {
            // TODO BAD PLZ FIX
            (it as? DexBackedInstruction35c)?.reference?.toString()?.contains("(Ljava/lang/Class;)") == true
        } == true
    }
)