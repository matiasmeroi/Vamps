package mati.vamps.enemies.spawner

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import mati.vamps.enemies.Enemy
import mati.vamps.enemies.EnemyFactory
import mati.vamps.enemies.EnemyType
import mati.vamps.enemies.behaviors.DoNothing
import mati.vamps.enemies.spawner.waves.Waves
import mati.vamps.events.EventManager
import mati.vamps.events.VEvent
import mati.vamps.utils.Utils
import mati.vamps.ui.GameTimer
import java.lang.Math.max
import java.lang.Math.min
import com.badlogic.gdx.utils.Array as GdxArray

class Spawner(private val timer: GameTimer, private val factory: EnemyFactory, val stage: Stage): GameTimer.Listener,
    EventManager.VEventListener {
    
    companion object {
        val TAG = Spawner::class.java.getSimpleName()
        const val MIN_SPAWN_DELAY = 40
        const val DEATH_TO_SPAWN_DELAY_OFFSET = 0
    }

    init {
        EventManager.subscribe(this)
        timer.addListener(this)
    }

    private val deathFrameDelays = GdxArray<Int>()
    private var enemyKilledThisFrame = false
    private var framesWithoutEnemiesDeaths = 0
    private var spawnEnemiesEveryXFrames = MIN_SPAWN_DELAY

    private var currentSeconds = 0
    private var currentMinute = 0

    private var waveCooldown = 0

    private val waves = Waves()

    fun reset() {
        currentSeconds = 0
        currentMinute = 0

        waveCooldown = 0

        spawnEnemiesEveryXFrames = MIN_SPAWN_DELAY
        framesWithoutEnemiesDeaths = 0
        enemyKilledThisFrame = false
        deathFrameDelays.clear()
    }

    fun update(playerPosition: Vector2) {
        spawnRandomEnemies(playerPosition)
        spawnWaves(playerPosition)
        spawnFires(playerPosition)

        if(enemyKilledThisFrame) {
            framesWithoutEnemiesDeaths = 0
            enemyKilledThisFrame = false
        } else {
            framesWithoutEnemiesDeaths++
            if(framesWithoutEnemiesDeaths >= MIN_SPAWN_DELAY) {
                deathFrameDelays.add(MIN_SPAWN_DELAY)
                framesWithoutEnemiesDeaths = 0
                println("I")
            }
        }
    }

    private fun spawnRandomEnemies(playerPosition: Vector2) {
        calcSpawnRate()

        // Gdx.app.log(TAG, "$spawnEnemiesEveryXFrames")
        if((Gdx.graphics.frameId % spawnEnemiesEveryXFrames).toInt() == 0 ) {
            if(Gdx.graphics.frameId.toInt() % 10 == 0)
                spawnMiniHorde(playerPosition)
            else
                spawnSingleEnemy(playerPosition)

        }

        capDeathList()
    }

    private fun spawnSingleEnemy(playerPosition: Vector2) {
        val e = factory.create(SpawnerData.getRandomEnemyTypeForMinute(currentMinute))
        stage.addActor(e)
        setPositionOffScreen(playerPosition, e)
    }

    private fun spawnMiniHorde(playerPosition: Vector2) {
        val radius = 200f
        val centerPos = getPostionOffScreen(playerPosition, radius)
        val numEnemies = Utils.r.nextInt(5) + 5
        val enemyType = SpawnerData.getRandomEnemyTypeForMinute(currentMinute)
        for(i in 0 until numEnemies) {
            val ang = Utils.getRandomAngleRad()
            val r = Utils.r.nextFloat() * radius
            val pos = Vector2(MathUtils.cos(ang) * r, MathUtils.sin(ang) * r).add(centerPos)
            val enemy = factory.create(enemyType)
            enemy.setPosition(pos.x, pos.y)
            stage.addActor(enemy)
        }
    }

    private fun calcSpawnRate() {
        val deathEveryXFrames = Utils.calcAvg(deathFrameDelays)
        val dexf = max(deathEveryXFrames.toInt() - DEATH_TO_SPAWN_DELAY_OFFSET, DEATH_TO_SPAWN_DELAY_OFFSET + 1)
        spawnEnemiesEveryXFrames = min(MIN_SPAWN_DELAY, dexf)
    }

    private fun capDeathList() {
        if(deathFrameDelays.size > 100) {
            deathFrameDelays.removeRange(0, 30)
        }
    }

    private fun onEnemyKilled() {
        deathFrameDelays.add(min(framesWithoutEnemiesDeaths, MIN_SPAWN_DELAY))
        enemyKilledThisFrame = true
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

    private fun spawnFires(playerPosition: Vector2) {
        val r = Utils.r.nextInt(1000)
        if(r == 1) {
            Gdx.app.log(TAG, "Spawning fire")
            val fire = factory.create(EnemyType.FIRE)
            val pos = Utils.getOffscreenPosForDirection(Utils.getRandomDirection(),
                playerPosition, stage.viewport.worldWidth, stage.viewport.worldHeight)
            fire.setBehavior(DoNothing())
            fire.setPosition(pos.x, pos.y)
            stage.addActor(fire)
        }
    }

    private fun setPositionOffScreen(pp: Vector2, enemy: Enemy) {
        val rads = Utils.getRandomAngleRad()
        val radius = max(stage.viewport.worldWidth, stage.viewport.worldHeight)
        val pos = Vector2(MathUtils.cos(rads) *radius ,MathUtils.sin(rads) *radius).add(pp)
        enemy.setPosition(pos.x, pos.y)
    }

    private fun getPostionOffScreen(playerPosition: Vector2, offset: Float) : Vector2 {
        val rads = Utils.getRandomAngleRad()
        val radius = max(stage.viewport.worldWidth, stage.viewport.worldHeight) + offset
        return Vector2(MathUtils.cos(rads) * radius, MathUtils.sin(rads) * radius).add(playerPosition)
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

    override fun onVEvent(event: VEvent, params: String) {
        when(event) {
            VEvent.GAME_START -> {
                spawnAround(Vector2(0f, 0f), 700f, 10)
            }
            VEvent.ENEMY_KILLED -> {
                onEnemyKilled()
            }
        }
    }

}