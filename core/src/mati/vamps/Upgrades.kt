package mati.vamps

import mati.vamps.players.PlayerFactory
import mati.vamps.players.PlayerUpgradeType
import mati.vamps.weapons.Holster
import mati.vamps.weapons.WeaponType
import com.badlogic.gdx.utils.Array as GdxArray

open class Upgrade(val type: UpgradeType, val description: String)

data class WeaponUpgradeInfo (val weaponType: WeaponType, val upgradeType: WeaponUpgradeType, val _desc: String) : Upgrade(UpgradeType.WEAPON_UPGRADE, _desc)
data class PlayerUpgradeInfo (val upgradeType: PlayerUpgradeType, val _desc: String) : Upgrade(UpgradeType.PLAYER_UPGRADE, _desc)

enum class UpgradeType { PLAYER_UPGRADE, WEAPON_UPGRADE }
enum class WeaponUpgradeType { ADD_WEAPON, LEVEL_UP_WEAPON }

fun generateUpgrades(currentLevel: Int, holster: Holster) :  GdxArray<Upgrade> {
    if(currentLevel % 5 == 0) {
        // generate player upgrades
        return PlayerFactory.getUpgradesAvailable()
    } else {
        // generate weapon upgrades
        return holster.getWeaponUpgradesAvailable()
    }
}

fun generatePresents(holster: Holster) : GdxArray<Upgrade> {
    val player = PlayerFactory.getUpgradesAvailable()
    val holsterUpgrades = holster.getWeaponUpgradesAvailable(true)
    val result = GdxArray<Upgrade>()
    result.addAll(player)
    result.addAll(holsterUpgrades)
    return result
}