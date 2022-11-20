package mati.vamps.enemies

import mati.vamps.Entity
import mati.vamps.events.VEvent

data class EnemyInfo (
        var health: Float = 1f,
        val moveSpeed: Float = Enemy.MOVE_SPEED,
        var dmgPerFrame: Float = 0.1f,
        var knockback: Float = 1f,
        var type: EnemyType = EnemyType.NONE,
        var onKillEvent: VEvent = VEvent.NONE,


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