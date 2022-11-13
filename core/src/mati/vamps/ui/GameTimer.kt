package mati.vamps.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.kotcrab.vis.ui.widget.VisLabel
import com.badlogic.gdx.utils.Array as GdxArray

class GameTimer: VisLabel() {

    interface Listener {
        fun onMinutesChange(minutes: Int)
        fun onSecondChange(seconds: Int)
    }

    private var seconds = 0f

    private var minDec = 0
    private var minUnit = 0

    private var secDec = 0
    private var secUnit = 0

    private val listeners = GdxArray<Listener>()

    init {
        setColor(1f, 1f, 1f, 1f)
        setFontScale(2.5f)
        pack()
    }

    fun addListener(l: Listener) {
        listeners.add(l)
    }

    fun reset() {
        seconds = 0f

        minDec = 0
        minUnit = 0

        secDec = 0
        secUnit = 0
    }

    override fun act(delta: Float) {
        super.act(delta)

        this.setPosition(stage.viewport.worldWidth / 2f - 45, stage.viewport.worldHeight - 100f)

        val lastSeconds = MathUtils.floor(seconds)
        seconds += Gdx.graphics.deltaTime
        if(lastSeconds != MathUtils.floor(seconds)) {
            for(l in listeners) l.onSecondChange(getSeconds())
        }

        val lastMinUnit = minUnit

        val mins = (seconds / 60).toInt()
        minDec = mins / 10
        minUnit = mins % 10

        val secs = (seconds % 60).toInt()
        secDec = secs / 10
        secUnit = secs % 10

        setText("$minDec$minUnit:$secDec$secUnit")

        if(minUnit != lastMinUnit) {
            for(l in listeners) l.onMinutesChange(getMinutes())
        }
    }

    fun getSeconds() : Int { return secDec * 10 + secUnit }
    fun getMinutes() : Int { return minDec * 10  + minUnit }


}