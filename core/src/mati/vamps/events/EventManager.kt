package mati.vamps.events

import com.badlogic.gdx.utils.Array

object EventManager {

    const val PARAM_SEP = "::::::::"

    interface GlobalEventListener {
        fun onGlobalEvent(event: VEvent, params: String)
    }

    private val listeners = Array<GlobalEventListener>()

    fun announce(globalEvent: VEvent, params: String) {
        for(l in listeners) {
            l.onGlobalEvent(globalEvent, params)
        }
    }

    fun subscribe(l: GlobalEventListener) {
        listeners.add(l)
    }


}