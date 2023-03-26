package app.revanced.patches.jakdojade.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.dexbacked.instruction.DexBackedInstruction35c

object SharedPrefsFingerprint : MethodFingerprint(
    access = AccessFlags.PUBLIC.value,
    parameters = listOf(),
    strings = listOf("marker-options"),
    customFingerprint = { methodDef ->
        methodDef.implementation?.instructions?.any {
            (it as? DexBackedInstruction35c)?.reference?.toString()== "Lcom/google/gson/Gson;->fromJson(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/Object;"
        } == true
    }
)