package app.revanced.patches.jakdojade.annotations

import app.revanced.patcher.annotation.Compatibility
import app.revanced.patcher.annotation.Package

@Compatibility(
    compatiblePackages = [
        Package("com.citynav.jakdojade.pl.android")
    ]
)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class JakDojadeCompatibility