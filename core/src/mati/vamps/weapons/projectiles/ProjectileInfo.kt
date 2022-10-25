package mati.vamps.weapons.projectiles

import mati.vamps.Entity

data class ProjectileInfo (
    var type: Projectile.Type = Projectile.Type.NONE,
    var timeOnScreen: Int = 120,
    var speed: Float = 1f,

    var texture: String = "items/knife",
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