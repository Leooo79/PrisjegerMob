package no.usn.rygleo.prisjegermobv1.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

/**
 * OBS : toppBar definert i values.colors (XML)
 */

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = PrimaryDark,
    primaryVariant = PrimaryDarkVariant,
    secondary = SecondaryDark,
    secondaryVariant = SecondaryDarkVariant,
    background = SecondaryDark,
    onPrimary = PrimaryFontDark,
    onSecondary = PrimaryFontDark
// TODO: skal vi ha annen bakgrunn enn hvit?
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = HandlelistePrimaryLight,
    primaryVariant = HandlelistePrimaryDark,
    secondary = HandlelisteSecondaryLight,
    secondaryVariant = HandlelisteHeader,
    background = HandlelisteSecondaryLight,
    onPrimary = PrimaryFontLightYellow,
    onSecondary = PrimaryFontLightBrown
)


@Composable
fun PrisjegerMobV1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}