package de.basedow.keno.spacerocks

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array

object GameUtils {

    fun parseSpriteSheet(fileName: String, frameCols: Int, frameRows: Int,
                         frameDuration: Float, mode: Animation.PlayMode): Animation<TextureRegion>
    {
        val t = Texture(Gdx.files.internal(fileName), true)
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        val frameWidth = t.width / frameCols
        val frameHeight = t.height / frameRows

        val temp = TextureRegion.split(t, frameWidth, frameHeight)
        val frames = Array<TextureRegion>(frameCols * frameRows)

        for (row in 0 until frameRows) {
            for (col in 0 until frameCols) {
                frames.add(temp[row][col])
            }
        }

        return Animation(frameDuration, frames, mode)
    }
}