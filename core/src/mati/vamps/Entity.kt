package mati.vamps

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor

abstract class Entity: Actor() {

    open class Info(
        var textureName: String = DEFAULT_TEXT,

        // sprite dimensions
        var _w: Float = DEF_W,
        var _h: Float = DEF_H,

        // collision rectangle dimensions
        var _cw: Float = DEF_CW,
        var _ch: Float = DEF_CH,

        // collision rectangle offset
        var _cxo: Float = DEF_CXO,
        var _cyo: Float = DEF_CYO
    ) {
        companion object {
            const val DEFAULT_TEXT = "players/greg"
            const val DEF_W = 52F
            const val DEF_H = 52F
            const val DEF_CW = 32F
            const val DEF_CH = 32F
            const val DEF_CXO = 0F
            const val DEF_CYO = 0F
        }
    }

    protected val velocity = Vector2()
    protected val acceleration = Vector2()

    protected var collisionWidth: Float = 0f
    protected var collisionHeight: Float = 0f
    protected var collisionOffsetX: Float = 0f
    protected var collisionOffsetY: Float = 0f

    protected lateinit var info: Info
    protected lateinit var texture: TextureRegion

    open fun initialize(info: Info) {
        this.info = info

        texture = Vamps.atlas().findRegion(info.textureName)

        if(info._w == 0f || info._h == 0f) {
            setSize(texture.regionWidth + 0f, texture.regionHeight + 0f)
        } else {
            setSize(info._w, info._h)
        }

        if(info._cw == 0f || info._ch == 0f) {
            setCollisionSize(width - 5, height - 5)
        } else {
            setCollisionSize(info._cw, info._ch)
        }

        setCollisionOffset(info._cxo, info._cyo)
    }

    protected fun setCollisionSize(w: Float, h: Float) {
        collisionWidth = w
        collisionHeight = h
    }

    protected fun setCollisionOffset(w: Float, h: Float) {
        collisionOffsetX = w
        collisionOffsetY = h
    }

    fun getPosition(): Vector2 {
        return Vector2(x, y)
    }

    fun getRect() : Rectangle {
        return Rectangle(x - width / 2, y - height / 2, width, height)
    }

    fun getColRect() : Rectangle {
        return Rectangle(x - collisionWidth / 2 + collisionOffsetX, y - collisionHeight / 2 + collisionOffsetY, collisionWidth, collisionHeight)
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