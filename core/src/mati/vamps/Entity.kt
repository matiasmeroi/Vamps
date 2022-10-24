package mati.vamps

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor

open class Entity: Actor() {

    protected val velocity = Vector2()
    protected val acceleration = Vector2()

    protected var collisionWidth: Float = 0f
    protected var collisionHeight: Float = 0f

    fun setCollisionSize(w: Float, h: Float) {
        collisionWidth = w
        collisionHeight = h
    }

    fun getRect() : Rectangle {
        return Rectangle(x - width / 2, y - height / 2, width, height)
    }

    fun getColRect() : Rectangle {
        return Rectangle(x - collisionWidth / 2, y - collisionHeight / 2, collisionWidth, collisionHeight)
    }

    fun drawRect() {
        val r = getRect()
        Vamps.sr().color = Color.BLUE
        Vamps.sr().rect(r.x, r.y,r.width, r.height)
    }

    fun drawColRect() {
        val r = getColRect()
        Vamps.sr().color = Color.RED
        Vamps.sr().rect(r.x, r.y,r.width, r.height)
    }

    fun drawCentered(batch: Batch?, texture: TextureRegion) {
        batch!!.draw(texture, x - width / 2, y - height / 2, width, height)
    }
}