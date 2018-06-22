package de.basedow.keno.spacerocks

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion

open class AnimatedActor : BaseActor() {

    private var elapsedTime = 0f
    private var activeAnim: Animation<TextureRegion>? = null
    var activeName: String? = null
        private set
    private var animationStorage = mutableMapOf<String, Animation<TextureRegion>>()

    fun storeAnimation(name: String, anim: Animation<TextureRegion>) {
        animationStorage.put(name, anim)
        if (activeName == null)
            setActiveAnimation(name)
    }

    fun storeAnimation(name: String, tex: Texture) {
        storeAnimation(name, Animation(1.0f, TextureRegion(tex)))
    }

    fun setActiveAnimation(name: String) {
        if (!animationStorage.containsKey(name))
            return

        if (activeName == name)
            return

        activeName = name
        activeAnim = animationStorage.get(name)
        elapsedTime = 0f

        val region = activeAnim?.getKeyFrame(0f)
        width = region?.regionWidth?.toFloat() ?: 0f
        height = region?.regionHeight?.toFloat() ?: 0f
    }

    override fun act(delta: Float) {
        super.act(delta)
        elapsedTime += delta
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        region.setRegion(activeAnim?.getKeyFrame(elapsedTime))
        super.draw(batch, parentAlpha)
    }

    fun copy(original: AnimatedActor) {
        super.copy(original)
        elapsedTime = 0f
        animationStorage = original.animationStorage
        val name = original.activeName
        if (name != null)
            setActiveAnimation(name)
    }

    override fun clone(): AnimatedActor {
        val newbie = AnimatedActor()
        newbie.copy(this)
        return newbie
    }
}
