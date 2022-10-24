package mati.vamps.players

import com.badlogic.gdx.Gdx
import mati.vamps.Utils
import com.badlogic.gdx.utils.ObjectMap as GdxMap

class PlayerFactory {

    companion object {
        val TAG = PlayerFactory::class.java.getSimpleName()
        const val INFO_FILE = "data/players.json"
    }

    private var infoMap = GdxMap<PlayerType, PlayerInfo>()

    fun load() {
        val fileString = Gdx.files.internal(INFO_FILE).readString()
        val infoArray = Utils.json.fromJson(Array<PlayerInfo>::class.java, fileString)
        for(i in infoArray) {
            infoMap.put(i.type, i)
            Gdx.app.log(TAG, "Loaded: ${i.type}")
        }

        Gdx.app.log(TAG, "${infoArray.size} players loaded")
    }

    fun create(type: PlayerType) : Player {
        val player = Player()
        val info = infoMap.get(type).copy()
        player.initialize(info)
        return player
    }

}