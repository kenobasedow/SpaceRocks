package de.basedow.keno.spacerocks

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

class PhysicsActor : AnimatedActor() {

    private val velocity = Vector2()
    private val acceleration = Vector2()

    var maxSpeed = 9999f
    var deceleration = 0f
    var autoAngle = false

    var speed: Float
        get() = velocity.len()
        set(value) { velocity.setLength(value) }

    val motionAngle: Float
        get() = MathUtils.atan2(velocity.x, velocity.y) * MathUtils.radiansToDegrees

    fun setVelocityXY(vx: Float, vy: Float) { velocity.set(vx, vy) }
    fun addVelocityXY(vx: Float, vy: Float) { velocity.add(vx, vy) }
    fun setVelocityAS(angleDeg: Float, speed: Float) {
        velocity.x = speed * MathUtils.cosDeg(angleDeg)
        velocity.y = speed * MathUtils.sinDeg(angleDeg)
    }

    fun setAccelerationXY(ax: Float, ay: Float) { acceleration.set(ax, ay) }
    fun addAccelerationXY(ax: Float, ay: Float) { acceleration.add(ax, ay) }
    fun setAccelerationAS(angleDeg: Float, speed: Float) {
        acceleration.x = speed * MathUtils.cosDeg(angleDeg)
        acceleration.y = speed * MathUtils.sinDeg(angleDeg)
    }
    fun addAccelerationAS(angleDeg: Float, amount: Float) {
        acceleration.add(amount * MathUtils.cosDeg(angleDeg), amount * MathUtils.sinDeg(angleDeg))
    }

    fun accelerationForward(forwardSpeed: Float) {
        setAccelerationAS(rotation, forwardSpeed)
    }

    override fun act(delta: Float) {
        super.act(delta)

        velocity.add(acceleration.x * delta, acceleration.y * delta)

        if (acceleration.len() < 0.01f) {
            if (speed < (deceleration * delta))
                speed = 0f
            else
                speed -= deceleration * delta
        }

        if (speed > maxSpeed)
            speed = maxSpeed

        moveBy(velocity.x * delta, velocity.y * delta)

        if (autoAngle && speed > 0.1f)
            rotation = motionAngle
    }
}