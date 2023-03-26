package app.revanced.patches.`super`.browser.annotations

import app.revanced.patcher.annotation.Compatibility
import app.revanced.patcher.annotation.Package

@Compatibility(
    compatiblePackages = [
        Package("com.bharat.browser")
    ]
)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class SuperBrowserCompatibility