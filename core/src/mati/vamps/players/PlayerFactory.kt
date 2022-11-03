package mati.vamps.players

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import mati.vamps.utils.Utils
import mati.vamps.PlayerUpgradeInfo
import mati.vamps.Upgrade
import com.badlogic.gdx.utils.ObjectMap as GdxMap
import com.badlogic.gdx.utils.Array as GdxArray

class PlayerFactory {

    companion object {
        val TAG: String = PlayerFactory::class.java.simpleName
        const val INFO_FILE = "data/players.json"

        private var infoMap = GdxMap<PlayerType, PlayerInfo>()

        fun getDescriptionForType(type: PlayerType, addName: Boolean = true) : String {
            val info = infoMap.get(type)
            var str = if(addName) "${info.name} -> " else ""
            str += info.description + " -> " + info.initialWeapon.name
            return str
        }

        fun applyUpgrade(upgType: PlayerUpgradeType, info: PlayerInfo) {
            Gdx.app.log(TAG, "Applying <${upgType}> to ${info.name}")
            when(upgType) {
                PlayerUpgradeType.INCREASE_MAX_HEALTH_20 ->
                    info.maxHealth *= 1.2f
//                PlayerUpgradeType.INCREASE_MAX_HEALTH_30 ->
//                    info.maxHealth *= 1.3f
                PlayerUpgradeType.INCREASE_ARMOR_10 ->
                    info.armor *= 1.1f
//                PlayerUpgradeType.INCREASE_ARMOR_20 ->
//                    info.armor *= 1.2f
//                PlayerUpgradeType.INCREASE_ARMOR_30 ->
//                    info.armor *= 1.3f
                PlayerUpgradeType.INCREASE_PICKUP_RADIUS ->
                    info.pickupRadius *= 1.5f
                PlayerUpgradeType.INCREASE_RECOVERY_02 ->
                    info.recovery += 0.2f
                PlayerUpgradeType.INCREASE_MIGHT_10 ->
                    info.might *= 1.1f
            }
        }

        fun getUpgradesAvailable(): GdxArray<Upgrade> {
            val res = GdxArray<Upgrade>()
            for(type in PlayerUpgradeType.values()) {
                res.add(PlayerUpgradeInfo(type, type.description))
            }
            return res
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