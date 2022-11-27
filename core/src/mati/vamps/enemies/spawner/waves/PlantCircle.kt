package mati.vamps.enemies.spawner.waves

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import mati.vamps.enemies.Enemy
import mati.vamps.enemies.EnemyFactory
import mati.vamps.enemies.EnemyType
import mati.vamps.enemies.behaviors.GotoPosition
import com.badlogic.gdx.utils.Array as GdxArray

class PlantCircle(pp: Vector2, fact: EnemyFactory, ww: Float, wh: Float) : Waves.EnemyWave(pp, fact, ww, wh) {

    companion object {
        const val NUM_ENEMIES = 70
        const val RADIUS = 800
        const val ENEMY_SPEED = 0.1F
        const val ALIVE_TIME = 1900
    }

    private var added = false
    private val enemyList = GdxArray<Enemy>()
    private var timer = ALIVE_TIME

    override fun act(delta: Float) {
        super.act(delta)

        if(!added) {
            addEnemies()
        } else {
            timer--
            if(timer == 0) {
                removeEnemies()
                remove()
            }
        }
    }

    private fun addEnemies() {
        var angle = 0f
        val dAngle = MathUtils.PI2 / NUM_ENEMIES.toFloat()
        for(i in 0 until NUM_ENEMIES) {
            val enemy = factory.create(EnemyType.PLANT)
            enemy.setPosition(MathUtils.cos(angle) * RADIUS, MathUtils.sin(angle) * RADIUS)
            enemy.setBehavior(GotoPosition(playerPosition, ENEMY_SPEED))
            stage.addActor(enemy)
            enemyList.add(enemy)
            angle += dAngle
        }
        added = true
    }

    private fun removeEnemies() {
        for(enemy in enemyList) {
            enemy.remove()
        }
    }

}