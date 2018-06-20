package de.basedow.keno.spacerocks

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.scenes.scene2d.Group

open class BaseActor : Group() {

    var region = TextureRegion()
    var boundingPolygon: Polygon = Polygon()
        get() {
            field.setPosition(x, y)
            field.rotation = rotation
            return field
        }

    var texture: Texture? = null
        set(value) {
            field = value
            region.setRegion(value)
            if (value == null) return;
            width = value.width.toFloat()
            height = value.height.toFloat()
        }

    fun setRectangleBoundary() {
        boundingPolygon = Polygon(floatArrayOf(0f, 0f, width, 0f, width, height, 0f, height))
        boundingPolygon.setOrigin(originX, originY)
    }

    fun setEllipseBoundary() {
        val n = 8
        val vertices = FloatArray(n * 2)
        for (i in 0 until n) {
            val t = i.toFloat() * 6.28f / n.toFloat()
            vertices[2 * i] = width / 2 * MathUtils.cos(t) + width / 2
            vertices[2 * i + 1] = height / 2 * MathUtils.sin(t) + height / 2

        }
        boundingPolygon = Polygon(vertices)
        boundingPolygon.setOrigin(originX, originY)
    }

    fun overlaps(other: BaseActor, resolve: Boolean): Boolean {
        if (!boundingPolygon.boundingRectangle.overlaps(other.boundingPolygon.boundingRectangle))
            return false

        val mtv = Intersector.MinimumTranslationVector()
        val polyOverlap = Intersector.overlapConvexPolygons(boundingPolygon, other.boundingPolygon, mtv)

        if (polyOverlap && resolve)
            moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth)

        val significant = 0.5f
        return (polyOverlap && mtv.depth > significant)
    }

    fun copy(original: BaseActor) {
        region = TextureRegion(original.region)
        if (original.boundingPolygon.vertices.size > 0)
            boundingPolygon = Polygon(original.boundingPolygon.vertices)
        boundingPolygon.setOrigin(original.originX, original.originY)
        setPosition(original.x, original.y)
        originX = original.originX
        originY = original.originY
        width = original.width
        height = original.height
        color = original.color
        isVisible = original.isVisible
    }

    fun clone(): BaseActor {
        val newbie = BaseActor()
        newbie.copy(this)
        return newbie
    }

    override fun act(delta: Float) {
        super.act(delta)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch?.setColor(color.r, color.g, color.b, color.a)
        if (isVisible)
            batch?.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation)
        super.draw(batch, parentAlpha)
    }

    fun setOriginCenter() {
        setOrigin(width / 2f, height / 2f)
    }
}
