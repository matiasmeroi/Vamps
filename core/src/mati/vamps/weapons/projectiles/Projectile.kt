package mati.vamps.weapons.projectiles

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import mati.vamps.Entity
import mati.vamps.enemies.Enemy
import org.w3c.dom.css.Rect

abstract class Projectile : Entity() {

    enum class Type {
        NONE,
        KNIFE
    }

    companion object {
        val TAG = Projectile::class.java.getSimpleName()
    }

    override fun getRect() : Rectangle {
//        Gdx.app.log(TAG, "Warning: calling getRect without area multiplier")
        return getColRect(1f)
    }

    override fun getColRect() : Rectangle {
//        Gdx.app.log(TAG, "Warning: calling getColRect without area multiplier")
        return getColRect(1f)
    }

    fun getRect(areaMultiplier: Float) : Rectangle {
        return getColRect(areaMultiplier)
    }
    fun getColRect(areaMultiplier: Float) : Rectangle {
        return Rectangle(x - (collisionWidth / 2) * areaMultiplier + collisionOffsetX,
            y - (collisionHeight / 2) * areaMultiplier+ collisionOffsetY,
            collisionWidth * areaMultiplier,
            collisionHeight * areaMultiplier)
    }

    abstract fun isDealingDmg() : Boolean
    abstract fun toRemove() : Boolean
    abstract fun onEnemyHit(enemy: Enemy)
    abstract fun update(areaMultiplier: Float)
    abstract fun draw(areaMultiplier: Float, batch: Batch)


}