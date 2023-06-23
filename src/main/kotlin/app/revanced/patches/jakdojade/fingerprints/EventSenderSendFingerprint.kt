package app.revanced.patches.jakdojade.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.iface.instruction.formats.Instruction21c

object EventSenderSendFingerprint : MethodFingerprint(
    returnType = "V",
    parameters = listOf(
        "Ljava/lang/String;",
        "Ljava/lang/String;",
        "Ljava/lang/String;",
        "Ljava/lang/Float;",
        "Landroid/os/Bundle;"
    ),
    customFingerprint = { methodDef ->
        methodDef.implementation?.instructions?.any { inst ->
            inst.opcode == Opcode.NEW_INSTANCE &&
                    (inst as Instruction21c).reference.toString() ==
                        "Lcom/citynav/jakdojade/pl/android/common/analytics/EventNameTooLongException;"
        } == true
    }
)