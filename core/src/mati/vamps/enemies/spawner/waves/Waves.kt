package mati.vamps.enemies.spawner.waves

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import mati.vamps.enemies.EnemyFactory

class Waves {

    abstract class EnemyWave(val playerPosition: Vector2, val factory: EnemyFactory
    , val worldWidth: Float, val worldHeight: Float) : Actor()

    enum class Type {
        BAT_CROSS_SCREEN
    }

    fun getWaveFromType(t: Type, playerPosition: Vector2, enemyFactory: EnemyFactory,
                worldWidth: Float, worldHeight: Float) : EnemyWave {
        return when(t) {
            Type.BAT_CROSS_SCREEN -> BatCrossScreenWave(playerPosition, enemyFactory, worldWidth, worldHeight)
        }
    }

}