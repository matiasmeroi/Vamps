package mati.vamps.weapons.projectiles

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ObjectMap
import mati.vamps.enemies.Enemy
import mati.vamps.players.Player
import mati.vamps.utils.Utils
import com.badlogic.gdx.utils.Array as GdxArray

class ProjectileFactory() {

    companion object {
        val TAG = ProjectileFactory::class.java.getSimpleName()
        const val INFO_FILE = "data/projectiles.json"
    }


    private val infoMap = ObjectMap<Projectile.Type, ProjectileInfo>()

    private lateinit var stage: Stage
    private lateinit var player: Player
    private lateinit var enemyList: com.badlogic.gdx.utils.Array<Enemy>

    fun initialize(stage: Stage, player: Player, enemyList: GdxArray<Enemy>) {
        this.stage = stage
        this.player = player
        this.enemyList = enemyList
    }

    fun load() {
        val fileString = Gdx.files.internal(INFO_FILE).readString()
        val infoArray = Utils.json.fromJson(Array<ProjectileInfo>::class.java, fileString)
        for(i in infoArray) {
            infoMap.put(i.type, i)
            Gdx.app.log(TAG, "Loaded: ${i.type}")
        }

        Gdx.app.log(TAG, "${infoArray.size} projectiles loaded")
    }

    private fun getSomeEnemyOnScreen() : Enemy? {
        var result: Enemy? = null
        if(enemyList.notEmpty()) {
            val r = Utils.r.nextInt(enemyList.size)
            result = enemyList.get(r)
        }
        return result
    }

    private fun getClosestEnemy() : Enemy? {
        var result: Enemy? = null
        var minDist = 10000000f
        for(i in 0 until enemyList.size) {
            val d = enemyList.get(i).getPosition().sub(player.getPosition()).len2()
            if(d < minDist) {
                result = enemyList.get(i)
                minDist = d
            }
        }

        return result
    }

    private fun generateRandomTarget(): Vector2 {
        return Vector2(player.x + Utils.r.nextInt(Gdx.graphics.width) - Gdx.graphics.width / 2,
            player.y + Utils.r.nextInt(Gdx.graphics.height) - Gdx.graphics.height / 2)
    }

    fun create(type: Projectile.Type, dir: Vector2 = Vector2(0f, 0f)) : Projectile{
        when(type) {
            Projectile.Type.NONE -> error("Invalid projectile type")
            Projectile.Type.KNIFE -> {
                val p = ThrowableProjectile(dir)
                val info = infoMap.get(type).copy()
                p.initialize(info)
                p.setPosition(player.x, player.y)
                stage.addActor(p)
                return p
            }

            Projectile.Type.GARLIC -> {
                val p = GarlicArea()
                val info = infoMap.get(type).copy()
                p.initialize(info)
                p.setPosition(player.x, player.y)
                stage.addActor(p)
                return p
            }

            Projectile.Type.WHIP -> {
                val p = WhipStrike()
                val info = infoMap.get(type).copy()
                p.initialize(info)
                stage.addActor(p)
                return p
            }
            Projectile.Type.HOLY_WATER -> {
                val target = getSomeEnemyOnScreen()
                val targetPosition = Vector2()

                if(target == null) {
                    targetPosition.set(generateRandomTarget())
                } else targetPosition.set(target.getPosition())

                val p = HolyWaterBottle(player.getPosition(), targetPosition)
                p.setPosition(targetPosition.x, targetPosition.y)
                val info = infoMap.get(type).copy()
                p.initialize(info)
                stage.addActor(p)
                return p
            }

            Projectile.Type.MAGIC_BULLET -> {
                val closestEnemyPosition = getClosestEnemy()?.getPosition() ?: generateRandomTarget()
                val dir = closestEnemyPosition.sub(player.x, player.y).nor()
                val p = ThrowableProjectile(dir, true)
                val info = infoMap.get(type).copy()
                p.initialize(info)
                p.setPosition(player.x, player.y)
                stage.addActor(p)
                return p
            }
        }
    }
}