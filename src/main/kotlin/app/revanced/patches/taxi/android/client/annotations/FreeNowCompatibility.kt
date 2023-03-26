package app.revanced.patches.taxi.android.client.annotations

import app.revanced.patcher.annotation.Compatibility
import app.revanced.patcher.annotation.Package

@Compatibility(
    compatiblePackages = [
        Package("taxi.android.client")
    ]
)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class FreeNowCompatibility