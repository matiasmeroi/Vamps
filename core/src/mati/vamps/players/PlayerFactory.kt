package mati.vamps.players

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import mati.vamps.utils.Utils
import com.badlogic.gdx.utils.ObjectMap as GdxMap

class PlayerFactory {

    companion object {
        val TAG = PlayerFactory::class.java.simpleName
        const val INFO_FILE = "data/players.json"

        private var infoMap = GdxMap<PlayerType, PlayerInfo>()

        fun getDescriptionForType(type: PlayerType, addName: Boolean = true) : String {
            val info = infoMap.get(type)
            var str = if(addName) "${info.name} -> " else ""
            str += info.description + " -> " + info.initialWeapon.name
            return str
        }

    }



    fun load() {
        val fileString = Gdx.files.internal(INFO_FILE).readString()
        val infoArray = Utils.json.fromJson(Array<PlayerInfo>::class.java, fileString)
        for(i in infoArray) {
            infoMap.put(i.type, i)
            Gdx.app.log(TAG, "Loaded: ${i.type}")
        }

        Gdx.app.log(TAG, "${infoArray.size} players loaded")
    }


    fun create(stage: Stage, type: PlayerType) : Player {
        val player = Player(stage)
        val info = infoMap.get(type).copy()
        player.initialize(info)
        return player
    }

}