package mati.vamps.enemies.spawner

import com.badlogic.gdx.Gdx
import mati.vamps.enemies.EnemyType
import mati.vamps.utils.Utils
import com.badlogic.gdx.utils.Array as GdxArray
class SpawnerData {

    companion object {

        const val INFO_FILE = "data/spawner_data.json"

        private val minutesMap = HashMap<Int, GdxArray<EnemyType>>()

        fun load() {
            val fileString = Gdx.files.internal(INFO_FILE).readString()
            val loadedArray = Utils.json.fromJson(Array<MinuteProbabilityInfo>::class.java, fileString)
            for(minProInfo in loadedArray) {
                minutesMap.put(minProInfo.minute, GdxArray<EnemyType>())

                for(entry in minProInfo.bag) {

                    val amount = entry.num
                    for(i in 0 until amount) {
                        minutesMap.get(minProInfo.minute)!!.add(entry.type)
                    }

                }

                println("Loaded minute ${minProInfo.minute} for enemy spawner")
            }

        }

        fun getRandomEnemyTypeForMinute(min: Int) : EnemyType{
            var key = min
            if(!(key in minutesMap.keys)) key = getLastMinuteLoaded()

            val typeArray = minutesMap[key]
            return typeArray!!.get(Utils.r.nextInt(typeArray.size))!!
        }

        private fun getLastMinuteLoaded() : Int {
            var max = -1
            for(k in minutesMap.keys) {
                if(k > max) max = k
            }
            return max
        }

    }
}