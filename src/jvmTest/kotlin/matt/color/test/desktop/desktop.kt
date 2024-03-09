package matt.color.test.desktop

import matt.color.common.rgb
import matt.color.toAwtColor
import java.awt.Color
import kotlin.test.Test
import kotlin.test.assertEquals


class DesktopColorTests() {
    @Test
    fun awtColor() {
        assertEquals(
            rgb(255, 255, 255).toAwtColor(),
            Color.WHITE
        )
    }
}
