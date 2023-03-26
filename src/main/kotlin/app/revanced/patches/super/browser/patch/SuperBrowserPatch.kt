package app.revanced.patches.`super`.browser.patch

import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.fingerprint.method.impl.MethodFingerprint.Companion.resolve
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.DependsOn
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod.Companion.toMutable
import app.revanced.patches.`super`.browser.annotations.SuperBrowserCompatibility
import app.revanced.patches.`super`.browser.fingerprints.MediaDetectorRecyclerViewFingerprint
import app.revanced.patches.`super`.browser.fingerprints.RecyclerViewOnBindFingerprint
import app.revanced.patches.shared.mapping.misc.patch.ResourceMappingPatch
import org.jf.dexlib2.AccessFlags
import org.jf.dexlib2.Opcode
import org.jf.dexlib2.iface.ClassDef
import org.jf.dexlib2.iface.Method
import org.jf.dexlib2.iface.instruction.ReferenceInstruction
import org.jf.dexlib2.iface.reference.TypeReference
import app.revanced.patcher.patch.annotations.RequiresIntegrations

@Patch
@Name("super-browser-share-video")
@Description("Make video urls shareable")
@SuperBrowserCompatibility
@Version("0.0.1")
@RequiresIntegrations
@DependsOn([ResourceMappingPatch::class])
class SuperBrowserPatch : BytecodePatch() {

    companion object {
        internal const val IntegrationPatch = "Lapp/revanced/integrations/patches/SuperBrowserOnBindPatch;"
        internal const val VideoDownloaderBean = "Lcom/bluesky/browser/videodownloader/VideoDownloaderBean;"
    }

    override fun execute(context: BytecodeContext): PatchResult {
        val mId = ResourceMappingPatch.resourceMappings.single {
            it.type == "layout" && it.name == "adapter_download_multi_items"
        }.id
        val custom = MediaDetectorRecyclerViewFingerprint(mId)
        for (classDef in context.classes) {
            if (custom.resolve(context, classDef))
                break
        }
        val recyclerViewClass = custom.result!!.classDef
        println(recyclerViewClass)

        RecyclerViewOnBindFingerprint.resolve(context, recyclerViewClass)
        println("result: "+RecyclerViewOnBindFingerprint.result)


        val onBindMethod = RecyclerViewOnBindFingerprint.result!!.mutableMethod
        println(onBindMethod)

        val index = onBindMethod.implementation!!.instructions
            .indexOfFirst(RecyclerViewOnBindFingerprint::isVideoBeanCast)
        println(index)

        onBindMethod.addInstructions(
            index + 1,
            """
                invoke-static {v2, v3}, $IntegrationPatch->attach(Ljava/lang/Object;$VideoDownloaderBean)V
            """.trimIndent()
        )

        return PatchResultSuccess()
    }
}