package mati.vamps.events
import com.badlogic.gdx.utils.Queue as GdxQueue
import com.badlogic.gdx.utils.Array as GdxArray

class EventQueue {

    private data class Info(val event: VEvent, val params: String)

    private val queue = GdxQueue<Info>()
    private val listeners = GdxArray<EventManager.VEventListener>()
    private var sending = false

    fun addMsg(event: VEvent, params: String) {
        queue.addLast(Info(event, params))
        if(!sending) send()
    }

    private fun send() {
        if(queue.isEmpty)  {
            sending = false
            return
        }

        sending = true

        val current = queue.first()
        for(l in listeners) {
            l.onVEvent(current.event, current.params)
        }
        queue.removeFirst()

        send()
    }

    fun subscribe(l: EventManager.VEventListener) {
        listeners.add(l)
    }

    fun unsubscribe(l: EventManager.VEventListener) {
        listeners.removeValue(l, true)
    }

}