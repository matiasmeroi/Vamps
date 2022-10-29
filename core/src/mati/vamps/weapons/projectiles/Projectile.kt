package mati.vamps.weapons.projectiles

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Rectangle
import mati.vamps.Entity
import mati.vamps.enemies.Enemy
import com.badlogic.gdx.utils.IntMap

abstract class Projectile : Entity() {

    enum class Type {
        NONE,
        KNIFE,
        GARLIC,
        WHIP,
        HOLY_WATER,
        MAGIC_BULLET
    }

    companion object {
        val TAG = Projectile::class.java.getSimpleName()
        const val ENTITY_COLLISION_TIMEOUT = 30
    }


    // las claves son los id de las entidades a las que no se les aplica daño
    // los valores son timers
    private val timeOuts = IntMap<Int>()

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


    fun timeOutEntity(entity: Entity) {
        timeOuts.put(entity.entityId, ENTITY_COLLISION_TIMEOUT)
    }

    fun isEnityOnTimeOut(entity: Entity) : Boolean {
        val i = timeOuts.keys()
        while(i.hasNext) {
            if(entity.entityId == i.next()) {
                return true
            }
        }
        return false
    }

    fun update(areaMultiplier: Float) {
        val iter = timeOuts.keys()
        while(iter.hasNext) {
            val key = iter.next()

            var t = timeOuts.get(key)
            if(t == null) continue
            t--
            if(t <= 0) timeOuts.remove(key)
            else timeOuts.put(key, t)
        }

        onUpdate(areaMultiplier)
    }
    abstract fun onUpdate(areaMultiplier: Float)
    abstract fun draw(areaMultiplier: Float, batch: Batch)


}