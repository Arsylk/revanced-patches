package app.revanced.patches.`super`.browser.fingerprints

import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.dexbacked.instruction.DexBackedInstruction31i
import org.jf.dexlib2.iface.instruction.Instruction
import org.jf.dexlib2.iface.instruction.NarrowLiteralInstruction

class MediaDetectorRecyclerViewFingerprint(private val layoutId: Long = -1) : MethodFingerprint(
    opcodes = listOf(
        Opcode.CONST,
        Opcode.CONST_4,
    ),
    customFingerprint = fingerprint@{ methodDef ->
        val instructions: Iterable<Instruction> = methodDef.implementation?.instructions
            ?: return@fingerprint false

        for (instruction in instructions) {
            if (instruction.opcode != Opcode.CONST) continue
            val casted = instruction as? NarrowLiteralInstruction ?: continue
            if (casted.wideLiteral == layoutId)
                return@fingerprint true
        }

        false
    }
)