package de.basedow.keno.spacerocks

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label

class SpaceRocksGame : BaseGame() {

    override fun create() {

        val uiFont = BitmapFont(Gdx.files.internal("cooper.fnt"))
        uiFont.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        skin.add("uiFont", uiFont)

        val uiLableStyle = Label.LabelStyle(uiFont, Color.GREEN)
        skin.add("uiLabelStyle", uiLableStyle)

        screen = GameScreen(this)
    }
}
