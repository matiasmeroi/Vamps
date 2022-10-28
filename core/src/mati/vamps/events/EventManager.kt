package mati.vamps.events

import com.badlogic.gdx.utils.Array
import mati.vamps.enemies.Enemy

object EventManager {

    const val PARAM_SEP = "::::::::"

    interface VEventListener {
        fun onVEvent(event: VEvent, params: String)
    }

    private val listeners = Array<VEventListener>()
    private val enemyListeners = Array<Enemy>()

    fun announceNot2Enemies(globalEvent: VEvent, params: String) {
        for(l in listeners) {
            l.onVEvent(globalEvent, params)
        }
    }

    fun announce2Enemies(globalEvent: VEvent, params: String) {
        for(l in enemyListeners) {
            l.onVEvent(globalEvent, params)
        }
    }

    fun announceAll(globalEvent: VEvent, params: String) {
        announce2Enemies(globalEvent, params)
        announceNot2Enemies(globalEvent, params)
    }

    fun subscribe(l: VEventListener) {
        listeners.add(l)
    }

    fun subscribeAsEnemy(e: Enemy) {
        enemyListeners.add(e)
    }

    fun unsubscribeEnemy(e: Enemy) {
        enemyListeners.removeValue(e, true)
    }


}