package app.revanced.patches.taxi.android.client.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.removeInstruction
import app.revanced.patcher.extensions.replaceInstructions
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.jakdojade.annotations.JakDojadeCompatibility
import app.revanced.patches.taxi.android.client.annotations.FreeNowCompatibility
import app.revanced.patches.taxi.android.client.fingerprints.RootDetectorFingerprint
import org.jf.dexlib2.Opcode

@Patch
@Name("freenow-root-bypass")
@Description("Disable root checker in FREE NOW")
@FreeNowCompatibility
@Version("0.0.1")
class FreeNowRootBypassPatch : BytecodePatch(
    listOf(
        RootDetectorFingerprint,
    )
) {

    override fun execute(context: BytecodeContext): PatchResult {
        val result = RootDetectorFingerprint.result
        val method = result!!.mutableMethod

        val newArrayIndex = method.implementation!!.instructions.indexOfFirst { instruction ->
            instruction.opcode == Opcode.FILLED_NEW_ARRAY_RANGE
        }
        require(newArrayIndex >= 0)

        method.replaceInstructions(
            newArrayIndex,
            """
            const/4 v0, 0x0
        """)
        method.replaceInstructions(
            newArrayIndex + 1,
            """
            new-array v0, v0, [Ljava/lang/String;
        """)

        return PatchResultSuccess()
    }
}