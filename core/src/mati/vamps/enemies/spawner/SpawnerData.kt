package mati.vamps.enemies.spawner

import com.badlogic.gdx.Gdx
import mati.vamps.enemies.EnemyType
import mati.vamps.enemies.spawner.waves.Waves
import mati.vamps.utils.Utils
import com.badlogic.gdx.utils.Array as GdxArray
class SpawnerData {

    companion object {

        val TAG = SpawnerData::class.java.getSimpleName()

        const val INFO_FILE = "data/spawner_data.json"

        private val wavesMap = HashMap<Int, GdxArray<Waves.Type>>()
        private val minutesMap = HashMap<Int, GdxArray<EnemyType>>()

        fun load() {
            val fileString = Gdx.files.internal(INFO_FILE).readString()
            val loadedArray = Utils.json.fromJson(Array<MinuteInfo>::class.java, fileString)
            for(minuteInfo in loadedArray) {
                minutesMap.put(minuteInfo.minute, GdxArray<EnemyType>())

                for(entry in minuteInfo.bag) {

                    val amount = entry.num
                    for(i in 0 until amount) {
                        minutesMap.get(minuteInfo.minute)!!.add(entry.type)
                    }

                }

                wavesMap.put(minuteInfo.minute, GdxArray<Waves.Type>())
                if(minuteInfo.enemyWaves.isNotEmpty()) {
                    for(wave in minuteInfo.enemyWaves) {
                        wavesMap[minuteInfo.minute]!!.add(wave)
                        Gdx.app.log(TAG, "Wave $wave in minute ${minuteInfo.minute}" )
                    }
                }

                Gdx.app.log(TAG, "Loaded minute ${minuteInfo.minute} for enemy spawner")
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

        fun mustSpawnWave(seconds: Int, minutes: Int) : Boolean {
            if(!(minutes in wavesMap.keys)) return false

            val info = wavesMap[minutes]!!

            if(info.isEmpty) return false


            val total = info.size
            if(seconds == 0 && total == 1) return true
            val timeBetweenWaves = 60 / total


            return seconds % timeBetweenWaves == 0
        }

        fun getWave(seconds: Int, minutes: Int) : Waves.Type {
            assert(mustSpawnWave(seconds, minutes))

            val info = wavesMap[minutes]!!

            val total = info.size
            val timeBetweenWaves = 60 / total
            val current = seconds / timeBetweenWaves

            return info[current]
        }

    }
}