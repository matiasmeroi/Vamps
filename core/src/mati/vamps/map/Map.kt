package mati.vamps.map

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import mati.vamps.Utils
import mati.vamps.events.EventManager
import mati.vamps.events.VEvent

class Map(val stage: Stage): EventManager.VEventListener {

    private var background = Background()

    init {
        EventManager.subscribe(this)
    }

    fun update() {

    }

    fun draw() {
        background.draw()
    }

    override fun onVEvent(event: VEvent, params: String) {
        when(event) {
            VEvent.PLAYER_MOVED_BY -> {
                val p = params.split(EventManager.PARAM_SEP)
                val dx = Utils.json.fromJson(Float::class.java, p[0])
                val dy = Utils.json.fromJson(Float::class.java, p[1])
                background.moveBy(-dx, -dy)
            }
        }
    }

}