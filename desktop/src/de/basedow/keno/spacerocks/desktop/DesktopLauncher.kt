package de.basedow.keno.spacerocks.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import de.basedow.keno.spacerocks.SpaceRocksGame

fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
    config.title = "Space Rocks"
    config.width = 800
    config.height = 600
    LwjglApplication(SpaceRocksGame(), config)
}
