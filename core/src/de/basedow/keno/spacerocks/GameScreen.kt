package de.basedow.keno.spacerocks

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Label

class GameScreen(game: BaseGame, val level: Int) : BaseScreen(game) {

    private val spaceship = PhysicsActor()
    private val rocketfire = BaseActor()
    private val baseLaser = PhysicsActor()
    private val baseExplosion = AnimatedActor()
    private val spaceshipExplosion: AnimatedActor

    private var gameOver = false
    private val gameOverLabel: Label

    private val laserList = mutableListOf<PhysicsActor>()
    private val rockList = mutableListOf<PhysicsActor>()

    private val laserSound: Sound

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
        spaceship.setPosition(mapWidth / 2f, mapHeight / 2f)
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

        val laserTex = Texture("laser.png")
        laserTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        baseLaser.storeAnimation("default", laserTex)
        baseLaser.maxSpeed = 400f
        baseLaser.deceleration = 0f
        baseLaser.setEllipseBoundary()
        baseLaser.setOriginCenter()
        baseLaser.autoAngle = true

        for (n in 1..level) {
            val rock = PhysicsActor()
            val rockTex = Texture("rock${n % 4}.png")
            rockTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            rock.storeAnimation("default", rockTex)
            if (MathUtils.randomBoolean())
                rock.x = MathUtils.random(0f, mapWidth / 4f)
            else
                rock.x = MathUtils.random(mapWidth * 3f / 4f, mapWidth)
            if (MathUtils.randomBoolean())
                rock.y = MathUtils.random(0f, mapHeight / 4f)
            else
                rock.y = MathUtils.random(mapHeight * 3f / 4f, mapHeight)
            rock.setOriginCenter()
            rock.setEllipseBoundary()
            rock.autoAngle = false
            val speedUp = MathUtils.random(0f, 1f)
            rock.setVelocityAS(360f * MathUtils.random(), 75f + 50f * speedUp)
            rock.addAction(Actions.forever(Actions.rotateBy(360f, 2f - speedUp)))
            mainStage.addActor(rock)
            rockList.add(rock)
        }

        baseExplosion.storeAnimation("default",
                GameUtils.parseSpriteSheet("explosion.png", 6, 6,
                        0.03f, Animation.PlayMode.NORMAL))
        baseExplosion.setOriginCenter()

        spaceshipExplosion = baseExplosion.clone()

        laserSound = Gdx.audio.newSound(Gdx.files.internal("laser.wav"))

        gameOverLabel = Label("Game Over", game.skin, "uiLabelStyle")
        gameOverLabel.isVisible = false
        uiTable.add(gameOverLabel)
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

        val itLaser = laserList.iterator()
        while (itLaser.hasNext()) {
            val laser = itLaser.next()
            for (rock in rockList) {
                if (laser.overlaps(rock, false)) {
                    laser.isVisible = false
                    rock.isVisible = false
                    val explosion = baseExplosion.clone()
                    explosion.moveToOrigin(rock)
                    mainStage.addActor(explosion)
                    explosion.addAction(Actions.sequence(Actions.delay(1.08f), Actions.removeActor()))
                }
            }
            wraparound(laser)
            if (!laser.isVisible) {
                itLaser.remove()
                laser.remove()
            }
        }

        val itRock = rockList.iterator()
        while (itRock.hasNext()) {
            val rock = itRock.next()
            if (rock.overlaps(spaceship, false)) {
                gameOver = true
                gameOverLabel.isVisible = true
                spaceshipExplosion.moveToOrigin(spaceship)
                mainStage.addActor(spaceshipExplosion)
                spaceshipExplosion.addAction(Actions.sequence(Actions.delay(1.08f), Actions.visible(false)))
                spaceship.remove()
            }
            wraparound(rock)
            if (!rock.isVisible) {
                itRock.remove()
                rock.remove()
            }
        }

        if (rockList.isEmpty()) {
            game.screen = LevelScreen(game, level + 1)
        }

        if (!spaceshipExplosion.isVisible) {
            isPaused = true
        }
    }

    override fun dispose() {
        super.dispose()
        laserSound.dispose()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.SPACE) {
            if (gameOver) {
                game.screen = LevelScreen(game)
                return false
            }
            val laser = baseLaser.clone()
            laser.moveToOrigin(spaceship)
            laser.setVelocityAS(spaceship.rotation, 400f)
            laserList.add(laser)
            mainStage.addActor(laser)
            laser.addAction(Actions.sequence(
                    Actions.delay(2f),
                    Actions.fadeOut(0.5f),
                    Actions.visible(false)
            ))
            laserSound.play()
        }
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (gameOver)
            game.screen = LevelScreen(game)
        return false
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