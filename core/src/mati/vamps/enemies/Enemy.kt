package mati.vamps.enemies

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import mati.vamps.Entity
import mati.vamps.Utils
import mati.vamps.Vamps
import mati.vamps.events.EventManager
import mati.vamps.events.EventManager.PARAM_SEP
import mati.vamps.events.VEvent
import kotlin.math.sin

class Enemy : Entity(), EventManager.VEventListener{

    companion object {
        const val MOVE_SPEED = 1.3f
    }

    override fun initialize(i: Info) {
       super.initialize(i)

       EventManager.subscribe(this)
    }

    override fun act(delta: Float) {
        super.act(delta)

        velocity.add(acceleration)

        x += velocity.x
        y += velocity.y

        acceleration.set(0f, 0f)
    }

    fun stop() {
        velocity.set(0f, 0f)
    }

    fun addToAcceleration(dx: Float, dy: Float) {
        acceleration.add(dx, dy)
    }

    override fun onVEvent(event: VEvent, params: String) {
        when(event) {
            VEvent.PLAYER_POSITION -> {
                val p = params.split(EventManager.PARAM_SEP)
                val px = Utils.json.fromJson(Float::class.java, p[0])
                val py = Utils.json.fromJson(Float::class.java, p[1])
                val angle = MathUtils.atan2(py - y, px - x)
                this.velocity.set(MathUtils.cos(angle) * (info as EnemyInfo).moveSpeed,
                    MathUtils.sin(angle) * (info as EnemyInfo).moveSpeed)
            }
        }
    }

    fun getEnemyInfo() : EnemyInfo {
        return info as EnemyInfo
    }

    fun getDmgPerFrame() : Float {
        return (info as EnemyInfo).dmgPerFrame
    }

    fun dealDmg(dmg: Float) {
        (info as EnemyInfo).health -= dmg
        if((info as EnemyInfo).health <= 0) {
            val js = Utils.json
            val params = js.toJson(x) +
                        PARAM_SEP +
                        js.toJson(y) +
                        PARAM_SEP +
                        js.toJson(info as EnemyInfo)
            EventManager.announce(VEvent.ENEMY_KILLED, params)

            remove()
        }
    }

    fun isDead() : Boolean {
        return (info as EnemyInfo).health <= 0
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        drawCentered(batch, Vamps.atlas().findRegion(info.textureName))
    }


}