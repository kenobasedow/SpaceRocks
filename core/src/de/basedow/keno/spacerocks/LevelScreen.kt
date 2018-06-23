package de.basedow.keno.spacerocks

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.TextButton

class LevelScreen(game: BaseGame, val level: Int = 1) : BaseScreen(game) {

    init {
        val spaceTex = Texture("space.png")

        val startButton = TextButton("Level $level", game.skin, "uiTextButtonStyle")
        startButton.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                return true
            }

            override fun touchUp(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int) {
                game.screen = GameScreen(game, level)
            }
        })

        uiTable.add(startButton)
    }

    override fun update(delta: Float) {

    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.SPACE)
            game.screen = GameScreen(game, level)
        return false
    }
}
