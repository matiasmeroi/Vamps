package mati.vamps.enemies.spawner

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import mati.vamps.enemies.Enemy
import mati.vamps.enemies.EnemyFactory
import mati.vamps.enemies.EnemyType
import mati.vamps.enemies.spawner.waves.Waves
import mati.vamps.utils.Utils
import mati.vamps.ui.GameTimer
import java.lang.Math.max

class Spawner(private val timer: GameTimer, private val factory: EnemyFactory, val stage: Stage): GameTimer.Listener {
    
    companion object {
        val TAG = Spawner::class.java.getSimpleName()
    }

    init {
        timer.addListener(this)
    }

    private var currentSeconds = 0
    private var currentMinute = 0

    private var waveCooldown = 0

    private val waves = Waves()

    fun update(playerPosition: Vector2) {
        spawnRandomEnemies(playerPosition)
        spawnWaves(playerPosition)
    }

    private fun spawnRandomEnemies(playerPosition: Vector2) {
        if((Gdx.graphics.frameId % 35).toInt() == 0 ) {
            val e = factory.create(SpawnerData.getRandomEnemyTypeForMinute(currentMinute))
            stage.addActor(e)
            setPositionOffScreen(playerPosition, e)
        }
    }

    private fun spawnWaves(playerPosition: Vector2) {
        if(waveCooldown > 0) {
            waveCooldown--
            return
        }

        if(SpawnerData.mustSpawnWave(currentSeconds, currentMinute)) {
            val type = SpawnerData.getWave(currentSeconds, currentMinute)
            Gdx.app.log(TAG, "Creating wave $type")
            stage.addActor(waves.getWaveFromType(type, playerPosition, factory, stage.viewport.worldWidth, stage.viewport.worldHeight ))
            waveCooldown = 120
        }

    }

    private fun setPositionOffScreen(pp: Vector2, enemy: Enemy) {
        val rads = MathUtils.map(0f, 1f, 0f, MathUtils.PI2, Utils.r.nextFloat())
        val radius = max(stage.viewport.worldWidth, stage.viewport.worldHeight)
        val pos = Vector2(MathUtils.cos(rads) *radius ,MathUtils.sin(rads) *radius).add(pp)
        enemy.setPosition(pos.x, pos.y)
    }

    fun spawnAround(playerPos:Vector2, radius: Float, num: Int, enemyType: EnemyType? = null) {
        var angle = 0f
        val dAngle = MathUtils.PI2 / num
        while(angle <= MathUtils.PI2) {
            val enemyOffsetX = MathUtils.cos(angle) * radius
            val enemyOffsetY = MathUtils.sin(angle) * radius

            val type : EnemyType = enemyType ?: SpawnerData.getRandomEnemyTypeForMinute(currentMinute)
            val enemy = factory.create(type)
            enemy.setPosition(playerPos.x + enemyOffsetX, playerPos.y + enemyOffsetY)

            stage.addActor(enemy)

            angle += dAngle
        }
    }

    override fun onMinutesChange(minutes: Int) {
        currentMinute = minutes
    }

    override fun onSecondChange(seconds: Int) {
        currentSeconds = seconds
    }

}