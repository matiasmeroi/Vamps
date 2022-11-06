package mati.vamps.players

import mati.vamps.Entity
import mati.vamps.weapons.WeaponType

enum class PlayerUpgradeType(val description: String, val applyFun: (PlayerInfo)->Unit) {
    INCREASE_MAX_HEALTH_20(description = "Increase max health by 20%", applyFun = { i: PlayerInfo -> i.maxHealth *= 1.2f }),
//    INCREASE_MAX_HEALTH_30(description = "Increase max health by 30%"),
    INCREASE_ARMOR_10(description = "Reduce dmg taken by 10%", applyFun = { i: PlayerInfo -> i.armor *= 1.1f }),
//    INCREASE_ARMOR_20(description = "Reduce dmg taken by 20%"),
//    INCREASE_ARMOR_30(description = "Reduce dmg taken by 30%"),
    INCREASE_PICKUP_RADIUS(description = "Increase pick-up area", applyFun = { i: PlayerInfo -> i.pickupRadius *= 1.5f }),
    INCREASE_RECOVERY_02(description = "Increase health recovery by 0.2/sec", applyFun = { i: PlayerInfo -> i.recovery += 0.2f }),
    INCREASE_MIGHT_10(description = "Increase might by 10%", applyFun = { i: PlayerInfo -> i.might *= 1.1f })
}

data class PlayerInfo (
    var name: String = "Unnamed",
    var description: String = "Empty Description",
    var initialWeapon: WeaponType = WeaponType.NONE,
    var maxHealth: Float = 100f,
    var might: Float = 1f,
    var armor: Float = 0f,
    var moveSpeed: Float = 1f,
    var recovery: Float = 0f,
    var pickupRadius: Float = 60f,
    var weaponCooldownMultiplier: Float = 0f,
    var type: PlayerType = PlayerType.NONE,


    var texture: String = DEFAULT_TEXT,
    var w: Float = DEF_W,
    var h: Float = DEF_H,
    var cw: Float = DEF_CW,
    var ch: Float = DEF_CH,
    var cxo: Float = DEF_CXO,
    var cyo: Float = DEF_CYO
) : Entity.Info(
    textureName = texture,
    _w = w,
    _h = h,
    _cw = cw,
    _ch = ch,
    _cxo = cxo,
    _cyo = cyo)