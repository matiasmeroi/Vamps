package mati.vamps.players

import mati.vamps.Entity
import mati.vamps.weapons.WeaponType

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
    var weaponCooldownMultiplier: Float = 1f,
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