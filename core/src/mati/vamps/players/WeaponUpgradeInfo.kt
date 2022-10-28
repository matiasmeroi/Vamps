package mati.vamps.players

import mati.vamps.weapons.WeaponType

data class WeaponUpgradeInfo (val weaponType: WeaponType, val upgradeType: UpgradeType, val description: String)

enum class UpgradeType { ADD_WEAPON, LEVEL_UP_WEAPON }