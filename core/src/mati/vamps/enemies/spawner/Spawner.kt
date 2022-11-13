package mati.vamps.enemies.spawner

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import mati.vamps.enemies.Enemy
import mati.vamps.enemies.EnemyFactory
import mati.vamps.enemies.EnemyType
import mati.vamps.utils.Utils
import mati.vamps.ui.GameTimer
import java.lang.Math.max

class Spawner(val timer: GameTimer, val factory: EnemyFactory, val stage: Stage): GameTimer.Listener {

    init {
        timer.addListener(this)
    }

    private var currentMinute = 0

    fun update(playerPosition: Vector2) {
        if((Gdx.graphics.frameId % 15).toInt() == 0 ) {
            val e = factory.create(SpawnerData.getRandomEnemyTypeForMinute(currentMinute))
            stage.addActor(e)
            setPositionOffScreen(playerPosition, e)
        }
    }

    private fun setPositionOffScreen(pp: Vector2, enemy: Enemy) {
        val rads = MathUtils.map(0f, 1f, 0f, MathUtils.PI2, Utils.r.nextFloat())
        val radius = max(Gdx.graphics.width, Gdx.graphics.height)
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

}