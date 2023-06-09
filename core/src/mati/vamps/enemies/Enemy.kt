package mati.vamps.enemies

import com.badlogic.gdx.graphics.g2d.Batch

import mati.vamps.Entity
import mati.vamps.utils.Utils
import mati.vamps.Vamps
import mati.vamps.enemies.behaviors.Behavior
import mati.vamps.enemies.behaviors.FollowPlayer
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

    private var mustApplyKnockback = false

    private var state = ScreenState.UNINITIALIZED

    private var myBehavior : Behavior = FollowPlayer()
    private var removed = false

    override fun initialize(i: Info) {
       super.initialize(i)

       EventManager.subscribeAsEnemy(this)
    }

    fun setBehavior(behavior: Behavior) : Enemy {
        myBehavior = behavior
        return this
    }

    override fun act(delta: Float) {
        super.act(delta)

        myBehavior.act(this)

        velocity.add(acceleration)

        x += velocity.x
        y += velocity.y

        acceleration.set(0f, 0f)

        checkIfOnScreen()
    }

    private fun checkIfOnScreen() {
        if(removed) return

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

    fun applyingKnockBack() : Boolean {
        return mustApplyKnockback
    }

    override fun onVEvent(event: VEvent, params: String) {
        myBehavior.onEvent(this, event, params)
    }

    fun getEnemyInfo() : EnemyInfo {
        return info as EnemyInfo
    }

    fun hasOnKillEvent() : Boolean {
        return getEnemyInfo().onKillEvent != VEvent.NONE
    }

    fun getDmgPerFrame() : Float {
        return (info as EnemyInfo).dmgPerFrame
    }

    fun applyKnockback(force: Float) {
        mustApplyKnockback = true;
    }

    fun stopKnockBack() {
        mustApplyKnockback = false
    }

    override fun remove(): Boolean {
        val inf = Utils.json.toJson(info)
        EventManager.announceNot2Enemies(VEvent.ENEMY_REMOVED, "$x$PARAM_SEP$y$PARAM_SEP${inf}$PARAM_SEP$entityId")
        removed = true
        return super.remove()
    }

    fun dealDmg(dmg: Float) {
        (info as EnemyInfo).health -= dmg
        if((info as EnemyInfo).health <= 0) {
            EventManager.announceNot2Enemies((info as EnemyInfo).onKillEvent, "$x$PARAM_SEP$y$PARAM_SEP${Utils.json.toJson(info)}")
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