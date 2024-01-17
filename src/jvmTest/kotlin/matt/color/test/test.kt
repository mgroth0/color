package matt.color.test


import matt.color.rgb
import matt.color.toAwtColor
import matt.test.Tests
import java.awt.Color
import kotlin.test.Test
import kotlin.test.assertEquals

class ColorTests: Tests() {
    @Test
    fun placeholderTest() = assertRunsInOneSecond {
        assertEquals(
            rgb(255, 255, 255).toAwtColor(),
            Color.WHITE
        )
    }
}