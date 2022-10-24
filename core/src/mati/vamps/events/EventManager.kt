package mati.vamps.events

import com.badlogic.gdx.utils.Array

object EventManager {

    const val PARAM_SEP = "::::::::"

    interface VEventListener {
        fun onVEvent(event: VEvent, params: String)
    }

    private val listeners = Array<VEventListener>()

    fun announce(globalEvent: VEvent, params: String) {
        for(l in listeners) {
            l.onVEvent(globalEvent, params)
        }
    }

    fun subscribe(l: VEventListener) {
        listeners.add(l)
    }


}