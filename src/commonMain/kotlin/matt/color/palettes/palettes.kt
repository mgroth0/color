package matt.color.palettes

import kotlinx.serialization.Serializable
import matt.color.colors.Colors
import matt.color.common.IntColor


@Serializable
data class FigColorScheme(
    val background: IntColor,
    val foreground: IntColor
) {
    companion object {
        val DEFAULT by lazy { DARK }
        val DARK = FigColorScheme(background = Colors.Black, foreground = Colors.White)
        val LIGHT = FigColorScheme(background = Colors.White, foreground = Colors.Black)
    }
}
