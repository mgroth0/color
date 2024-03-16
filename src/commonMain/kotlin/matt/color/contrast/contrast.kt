package matt.color.contrast

import matt.color.common.IntColor
import matt.color.common.rgb

fun IntColor.calculateContrastingColor(contrastor: ContrastAlgorithm): IntColor = contrastor.contrastingColorOf(this)
interface ContrastAlgorithm {
    fun contrastingColorOf(input: IntColor): IntColor
}

object LetsTrySomethingMoreAdvanced : ContrastAlgorithm {
    override fun contrastingColorOf(input: IntColor): IntColor =
        input.run {
            require(hasDefaultAlpha) {
                "not ready"
            }
            val yellow = (red + green) / 2u
            return rgb(
                r =
                    when {
                        (red >= 128u) ->
                            when {
                                red <= 192u ->
                                    when {
                                        green >= 128u -> 255
                                        else          -> 0
                                    }

                                else        -> 0
                            }

                        else          -> 255
                    },
                g =
                    when {
                        (green >= 128u) ->
                            when {
                                green <= 192u ->
                                    when {
                                        red >= 128u -> 255
                                        else        -> 0
                                    }

                                else          -> 0
                            }

                        else            -> 255
                    },
                b =
                    when {
                        (blue >= 128u) ->
                            when {
                                blue <= 192u ->
                                    when {
                                        yellow >= 128u -> 255
                                        else           -> 0
                                    }

                                else         -> 0
                            }

                        else           -> 255
                    }
            )
        }
}

object SimpleButOftenStupid : ContrastAlgorithm {
    override fun contrastingColorOf(input: IntColor) =
        input.run {


            require(hasDefaultAlpha) {
                "not ready"
            }
            rgb(
                r =
                    when {
                        (red >= 128u) -> 0
                        else          -> 255
                    },
                g =
                    when {
                        (green >= 128u) -> 0
                        else            -> 255
                    },
                b =
                    when {
                        (blue >= 128u) -> 0
                        else           -> 255
                    }
            )
        }
}
