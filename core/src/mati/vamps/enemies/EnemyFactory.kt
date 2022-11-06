package mati.vamps.enemies

import com.badlogic.gdx.Gdx
import mati.vamps.utils.Utils
import mati.vamps.events.EventManager
import mati.vamps.events.EventManager.PARAM_SEP
import mati.vamps.events.VEvent
import com.badlogic.gdx.utils.ObjectMap as GdxMap
import com.badlogic.gdx.utils.Array as GdxArray

class EnemyFactory : EventManager.VEventListener {

    companion object {
        val TAG = EnemyFactory::class.java.getSimpleName()
        const val INFO_FILE = "data/enemies.json"
    }

    private val infoMap = GdxMap<EnemyType, EnemyInfo>()
    private val enemyMap = GdxMap<Int, Enemy>()
    private val enemiesOnScreen = GdxArray<Enemy>()
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

    fun clear() {
        enemyMap.clear()
        enemiesOnScreen.clear()
    }

    fun create(type: EnemyType) : Enemy {
        val enemy = Enemy()
        val info = infoMap.get(type).copy()
        enemy.initialize(info)
        enemyMap.put(enemy.entityId, enemy)
        return enemy
    }

    fun getList(): GdxArray<Enemy> {
        return enemyMap.values().toArray()
    }

    fun getOnScreenList(): GdxArray<Enemy> {
        return enemiesOnScreen
    }

    override fun onVEvent(event: VEvent, params: String) {
        when(event) {
            VEvent.ENEMY_KILLED -> {
                val p = params.split(PARAM_SEP)
                val targetId = p[3].toInt()
                enemiesOnScreen.removeAll { enemy -> enemy.entityId == targetId }
                enemyMap.remove(targetId)
            }


            VEvent.ENEMY_OFF_SCREEN -> {
                val targetId = params.toInt()
                enemiesOnScreen.removeAll { enemy -> enemy.entityId == targetId }
            }
            VEvent.ENEMY_ON_SCREEN -> {
                val id = params.toInt()
                enemiesOnScreen.add(enemyMap.get(id))
            }
        }
    }
}