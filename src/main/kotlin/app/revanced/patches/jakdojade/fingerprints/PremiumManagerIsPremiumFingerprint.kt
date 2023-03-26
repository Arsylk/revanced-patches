package app.revanced.patches.jakdojade.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode

object PremiumManagerIsPremiumFingerprint : MethodFingerprint(
    returnType = "Z",
    access = AccessFlags.PUBLIC.value,
    parameters = listOf(),
    customFingerprint = { methodDef ->
        val int = methodDef.implementation!!.instructions
        int.count { it.opcode == Opcode.IGET_OBJECT } == 3 &&
                int.count { it.opcode == Opcode.IF_EQZ } == 2 &&
                int.count { it.opcode == Opcode.IF_NEZ } == 1
    }
)