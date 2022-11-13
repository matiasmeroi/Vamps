package mati.vamps.enemies.spawner.waves

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import mati.vamps.enemies.EnemyFactory
import mati.vamps.enemies.EnemyType
import mati.vamps.enemies.behaviors.CrossScreen
import mati.vamps.events.EventManager
import mati.vamps.events.EventManager.PARAM_SEP
import mati.vamps.events.VEvent
import mati.vamps.utils.Utils
import mati.vamps.weapons.CooldownTimer

class BatCrossScreenWave(pp: Vector2, fact: EnemyFactory, ww: Float, wh: Float) : Waves.EnemyWave(pp, fact, ww, wh),
    EventManager.VEventListener{

    companion object {
        const val NUM_WAVES = 3
        const val WAVE_SEP = 300
        const val NUM_ENEMIES = 16
    }

    private var timer = 0
    private var added = 0

    init {
        EventManager.subscribe(this)
    }

    private fun addNewWave() {
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
        added++
    }

    override fun act(delta: Float) {
        super.act(delta)

        if(added == NUM_WAVES) remove()

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