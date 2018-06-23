package de.basedow.keno.spacerocks

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton

class SpaceRocksGame : BaseGame() {

    private lateinit var music: Music

    override fun create() {

        val uiFont = BitmapFont(Gdx.files.internal("cooper.fnt"))
        uiFont.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        skin.add("uiFont", uiFont)

        val uiLableStyle = Label.LabelStyle(uiFont, Color.GREEN)
        skin.add("uiLabelStyle", uiLableStyle)

        val uiTextButtonStyle = TextButton.TextButtonStyle()
        uiTextButtonStyle.font = uiFont
        uiTextButtonStyle.fontColor = Color.NAVY

        val buttonUpTex = Texture("ninepatch-1.png")
        skin.add("buttonUp", NinePatch(buttonUpTex, 26, 26, 16, 20))
        uiTextButtonStyle.up = skin.getDrawable("buttonUp")

        val buttonOverTex = Texture("ninepatch-2.png")
        skin.add("buttonOver", NinePatch(buttonOverTex, 26, 26, 16, 20))
        uiTextButtonStyle.over = skin.getDrawable("buttonOver")

        val buttonDownTex = Texture("ninepatch-3.png")
        skin.add("buttonDown", NinePatch(buttonDownTex, 26, 26, 16, 20))
        uiTextButtonStyle.down = skin.getDrawable("buttonDown")

        skin.add("uiTextButtonStyle", uiTextButtonStyle)

        music = Gdx.audio.newMusic(Gdx.files.internal("Disco con Tutti.mp3"))
        music.isLooping = true
        music.play()

        screen = LevelScreen(this)
    }

    override fun dispose() {
        super.dispose()
        music.dispose()
    }
}
