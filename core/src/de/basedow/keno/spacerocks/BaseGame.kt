package de.basedow.keno.spacerocks

import com.badlogic.gdx.Game
import com.badlogic.gdx.scenes.scene2d.ui.Skin

abstract class BaseGame : Game() {

    val skin = Skin()

    abstract override fun create()

    override fun dispose() {
        skin.dispose()
    }
}
