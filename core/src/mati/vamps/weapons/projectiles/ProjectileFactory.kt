package mati.vamps.weapons.projectiles

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ObjectMap
import mati.vamps.Utils
import mati.vamps.enemies.EnemyFactory
import mati.vamps.enemies.EnemyInfo
import mati.vamps.enemies.EnemyType

class ProjectileFactory {

    companion object {
        val TAG = ProjectileFactory::class.java.getSimpleName()
        const val INFO_FILE = "data/projectiles.json"
    }

    private val infoMap = ObjectMap<Projectile.Type, ProjectileInfo>()

    fun load() {
        val fileString = Gdx.files.internal(INFO_FILE).readString()
        val infoArray = Utils.json.fromJson(Array<ProjectileInfo>::class.java, fileString)
        for(i in infoArray) {
            infoMap.put(i.type, i)
            Gdx.app.log(TAG, "Loaded: ${i.type}")
        }

        Gdx.app.log(TAG, "${infoArray.size} projectiles loaded")
    }

    fun create(type: Projectile.Type, dir: Vector2) : Projectile{
        when(type) {
            Projectile.Type.NONE -> error("Invalid projectile type")
            Projectile.Type.KNIFE -> {
                val p = ThrowableProjectile(dir)
                val info = infoMap.get(type).copy()
                p.initialize(info)
                return p
            }
        }
    }
}