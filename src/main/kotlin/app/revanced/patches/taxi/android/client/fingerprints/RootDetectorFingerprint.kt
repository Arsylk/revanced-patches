package app.revanced.patches.taxi.android.client.fingerprints

import app.revanced.patcher.extensions.or
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.iface.instruction.ReferenceInstruction
import org.jf.dexlib2.iface.reference.StringReference

object RootDetectorFingerprint : MethodFingerprint(
    returnType = "V",
    access = AccessFlags.PUBLIC or AccessFlags.STATIC or AccessFlags.CONSTRUCTOR,
    parameters = listOf(),
    customFingerprint = customFingerprint@{ methodDef ->
        if (methodDef.name == "<clinit>") {
            var isStringFound = false

            val impl = methodDef.implementation!!
            impl.instructions.forEachIndexed { _, instruction ->
                if (instruction.opcode.ordinal != Opcode.CONST_STRING.ordinal) return@forEachIndexed

                val string = ((instruction as ReferenceInstruction).reference as StringReference).string
                if (string == "/system/app/Superuser.apk") {
                    isStringFound = true
                }
            }

            if (isStringFound) {
                return@customFingerprint true
            }
        }
        false
    }
)