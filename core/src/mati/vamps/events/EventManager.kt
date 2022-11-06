package mati.vamps.events

import com.badlogic.gdx.utils.Array
import mati.vamps.enemies.Enemy

object EventManager {

    const val PARAM_SEP = "::::::::"

    interface VEventListener {
        fun onVEvent(event: VEvent, params: String)
    }

    private val notEnemyBuffer = EventQueue()
    private val enemyBuffer = EventQueue()

    fun announceNot2Enemies(event: VEvent, params: String) {
        notEnemyBuffer.addMsg(event, params)
    }

    fun announce2Enemies(event: VEvent, params: String) {
        enemyBuffer.addMsg(event, params)
    }

    fun announceAll(event: VEvent, params: String) {
        announce2Enemies(event, params)
        announceNot2Enemies(event, params)
    }

    fun subscribe(l: VEventListener) {
        notEnemyBuffer.subscribe(l)
    }

    fun subscribeAsEnemy(e: Enemy) {
        enemyBuffer.subscribe(e)
    }

    fun unsubscribeEnemy(e: Enemy) {
        enemyBuffer.unsubscribe(e)
    }


}