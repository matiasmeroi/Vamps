package mati.vamps.enemies

import com.badlogic.gdx.Gdx
import mati.vamps.Utils
import mati.vamps.events.EventManager
import mati.vamps.events.VEvent
import com.badlogic.gdx.utils.ObjectMap as GdxMap
import com.badlogic.gdx.utils.Array as GdxArray

class EnemyFactory : EventManager.VEventListener {

    companion object {
        val TAG = EnemyFactory::class.java.getSimpleName()
        const val INFO_FILE = "data/enemies.json"
    }

    private val infoMap = GdxMap<EnemyType, EnemyInfo>()
    private val enemyList = GdxArray<Enemy>()

    init {
        EventManager.subscribe(this)
    }

    fun load() {
        val fileString = Gdx.files.internal(INFO_FILE).readString()
        val infoArray = Utils.json.fromJson(Array<EnemyInfo>::class.java, fileString)
        for(i in infoArray) {
            infoMap.put(i.type, i)
            Gdx.app.log(TAG, "Loaded: ${i.type}")
        }

        Gdx.app.log(TAG, "${infoArray.size} enemies loaded")
    }

    fun create(type: EnemyType) : Enemy {
        val enemy = Enemy()
        val info = infoMap.get(type).copy()
        enemy.initialize(info)
        enemyList.add(enemy)
        return enemy
    }

    fun getList(): GdxArray<Enemy> {
        return enemyList
    }

    override fun onVEvent(event: VEvent, params: String) {
        when(event) {
            VEvent.ENEMY_KILLED -> {

            }

            VEvent.ENEMY_REMOVED -> {

            }
        }
    }
}