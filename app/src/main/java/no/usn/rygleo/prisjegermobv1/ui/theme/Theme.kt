package no.usn.rygleo.prisjegermobv1.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

/**
 * OBS : toppBar definert i values.colors (XML)
 */

private val DarkColorPalette = darkColors(
    primary = HandlelistePrimaryDark,
    primaryVariant = HandlelistePrimaryDark,
    secondary = HandlelisteSecondaryDark,
    secondaryVariant = HandlelisteHeader,
    background = HandlelisteSecondaryLight
// TODO: skal vi ha annen bakgrunn enn hvit?
)

private val LightColorPalette = lightColors(
    primary = HandlelistePrimaryLight,
    primaryVariant = HandlelistePrimaryDark,
    secondary = HandlelisteSecondaryLight,
    secondaryVariant = HandlelisteHeader,
    background = HandlelisteSecondaryLight

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
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