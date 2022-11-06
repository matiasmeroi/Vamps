package mati.vamps.utils

import mati.vamps.events.EventManager
import mati.vamps.events.VEvent

class KillCounter: EventManager.VEventListener {

    private var count = 0

    init {
        EventManager.subscribe(this)
    }

    fun reset() {
        count = 0
    }

    fun getCount() : Int { return count }

    override fun onVEvent(event: VEvent, params: String) {
        when(event) {
            VEvent.ENEMY_KILLED -> {
                count++
            }
        }
    }
}