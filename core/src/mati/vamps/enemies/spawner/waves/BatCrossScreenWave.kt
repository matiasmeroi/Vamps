package mati.vamps.enemies.spawner.waves

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import mati.vamps.enemies.EnemyFactory
import mati.vamps.enemies.EnemyType
import mati.vamps.enemies.behaviors.CrossScreen
import mati.vamps.events.EventManager
import mati.vamps.events.EventManager.PARAM_SEP
import mati.vamps.events.VEvent
import mati.vamps.utils.Utils

class BatCrossScreenWave(pp: Vector2, fact: EnemyFactory, ww: Float, wh: Float) : Waves.EnemyWave(pp, fact, ww, wh),
    EventManager.VEventListener{

    companion object {
        const val NUM_WAVES = 3
        const val WAVE_SEP = 300
        const val NUM_ENEMIES = 22
    }

    private var timer = 0
    private var added = 0

    init {
        EventManager.subscribe(this)
    }

    private fun addNewWave() {
//        addInRectangleFormation()
        addInCircularFormation()
        added++
    }

    private fun addInRectangleFormation() {
        val waveDirection = Utils.getRandomDirection()
        val centerPos = Utils.getOffscreenPosForDirection(waveDirection, playerPosition, worldWidth, worldHeight)

        val sep = 40f
        val offset = 100f
        for(i in 0 until NUM_ENEMIES) {
            val bat = factory.create(EnemyType.BAT_MEDIUM)
            bat.setPosition(centerPos.x + (i % 4) * sep - offset, centerPos.y + (i / 4) * sep - offset)
            this.stage.addActor(bat)
            bat.setBehavior(CrossScreen(waveDirection))
        }

    }

    private fun addInCircularFormation() {
        val waveDirection = Utils.getRandomDirection()
        val centerPos = Utils.getOffscreenPosForDirection(waveDirection, playerPosition, worldWidth, worldHeight)

        var angle = 0f
        var dAngle = 1.5f
        var radius = 50f
        var dRadius = 45f
        var added = 0

        while(added < NUM_ENEMIES) {
            var accum = 0f
            angle = MathUtils.random(MathUtils.PI2)
            while(accum < MathUtils.PI2 && added < NUM_ENEMIES) {
                val bat = factory.create(EnemyType.BAT_MEDIUM)
                bat.setPosition(centerPos.x + MathUtils.cos(angle) * radius, centerPos.y + MathUtils.sin(angle) * radius)
                this.stage.addActor(bat)
                bat.setBehavior(CrossScreen(waveDirection))
                angle += dAngle
                accum += dAngle
                added ++
            }
            radius +=dRadius
            dAngle *= 0.8f
        }
    }

    override fun act(delta: Float) {
        super.act(delta)

        if(added >= NUM_WAVES) remove()

        timer--
        if(timer <= 0 ) {
            if(added < NUM_WAVES) addNewWave()
            timer = WAVE_SEP
        }
    }

    override fun onVEvent(event: VEvent, params: String) {
        when(event) {
            VEvent.PLAYER_POSITION -> {
                val p = params.split(PARAM_SEP)
                val x = p[0].toFloat()
                val y = p[1].toFloat()
                playerPosition.set(x, y)
            }
        }
    }


}