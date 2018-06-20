package de.basedow.keno.spacerocks

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture

class GameScreen(game: BaseGame) : BaseScreen(game) {

    private val spaceship = PhysicsActor()
    private val rocketfire = BaseActor()

    private val mapWidth = 800f
    private val mapHeight = 600f

    init {
        val background = BaseActor()
        background.texture = Texture("space.png")
        background.setPosition(0f, 0f)
        mainStage.addActor(background)

        val shipTex = Texture("spaceship.png")
        shipTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        spaceship.storeAnimation("default", shipTex)
        spaceship.setPosition(400f, 300f)
        spaceship.setOriginCenter()
        spaceship.maxSpeed = 200f
        spaceship.deceleration = 20f
        spaceship.setEllipseBoundary()
        mainStage.addActor(spaceship)

        rocketfire.setPosition(-28f, 24f)
        val fireTex = Texture("fire.png")
        fireTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        rocketfire.texture = fireTex
        spaceship.addActor(rocketfire)
    }

    override fun update(delta: Float) {
        spaceship.setAccelerationXY(0f, 0f)

        when {
            Gdx.input.isKeyPressed(Input.Keys.LEFT) -> spaceship.rotateBy(180f * delta)
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) -> spaceship.rotateBy(-180f * delta)
            Gdx.input.isKeyPressed(Input.Keys.UP) -> spaceship.addAccelerationAS(spaceship.rotation, 100f)
        }

        rocketfire.isVisible = Gdx.input.isKeyPressed(Input.Keys.UP)

        wraparound(spaceship)
    }

    fun wraparound(ba: BaseActor) {
        when {
            ba.x + ba.width < 0 -> ba.x = mapWidth
            ba.x > mapWidth -> ba.x = -ba.width
            ba.y + ba.height < 0 -> ba.y = mapHeight
            ba.y > mapHeight -> ba.y = -ba.height
        }
    }
}