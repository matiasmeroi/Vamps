package mati.vamps.enemies

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import mati.vamps.Entity
import mati.vamps.utils.Utils
import mati.vamps.Vamps
import mati.vamps.events.EventManager
import mati.vamps.events.EventManager.PARAM_SEP
import mati.vamps.events.VEvent

class Enemy : Entity(), EventManager.VEventListener{

    private enum class ScreenState {
        UNINITIALIZED, ON, OFF
    }

    companion object {
        const val MOVE_SPEED = 1f
    }

    private var lastPlayerInfo: Vector2 = Vector2(0f, 0f)
    private var mustApplyKnockback = false

    private var state = ScreenState.UNINITIALIZED

    override fun initialize(i: Info) {
       super.initialize(i)

       EventManager.subscribeAsEnemy(this)
    }

    override fun act(delta: Float) {
        super.act(delta)

        velocity.add(acceleration)


        x += velocity.x
        y += velocity.y

        acceleration.set(0f, 0f)

        checkIfOnScreen()
    }

    private fun checkIfOnScreen() {
        var newState = ScreenState.UNINITIALIZED
        if(this.isOnScreen()) {
            newState = ScreenState.ON
        } else {
            newState = ScreenState.OFF
        }

        val changed = newState != state
        state = newState
        if(changed) {
            if(state == ScreenState.ON) EventManager.announceNot2Enemies(VEvent.ENEMY_ON_SCREEN, Utils.json.toJson(this.entityId))
            if(state == ScreenState.OFF) EventManager.announceNot2Enemies(VEvent.ENEMY_OFF_SCREEN, Utils.json.toJson(this.entityId))
        }
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
                if(mustApplyKnockback) {
                    this.velocity.add(MathUtils.cos(angle) * (info as EnemyInfo).moveSpeed,
                        MathUtils.sin(angle) * (info as EnemyInfo).moveSpeed)
                    mustApplyKnockback = false
                }
                else this.velocity.set(MathUtils.cos(angle) * (info as EnemyInfo).moveSpeed,
                    MathUtils.sin(angle) * (info as EnemyInfo).moveSpeed)
                lastPlayerInfo.set(px, py)
            }
        }
    }

    fun getEnemyInfo() : EnemyInfo {
        return info as EnemyInfo
    }

    fun getDmgPerFrame() : Float {
        return (info as EnemyInfo).dmgPerFrame
    }

    fun applyKnockback(force: Float) {
//        val dir = Vector2(x, y).sub(lastPlayerInfo).nor()
//        velocity.set(dir.x * force, dir.y * force)
        mustApplyKnockback = true;
    }

    fun dealDmg(dmg: Float) {
        (info as EnemyInfo).health -= dmg
        if((info as EnemyInfo).health <= 0) {
            remove()
        }
    }

    fun isDead() : Boolean {
        return (info as EnemyInfo).health <= 0
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        drawCenteredAndScale(batch, Vamps.atlas().findRegion(info.textureName))
    }


}