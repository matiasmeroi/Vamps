package mati.vamps.items

import mati.vamps.Entity

data class ItemInfo (

    var type: Item.Type = Item.Type.NONE,
    var effect: ItemEffect = ItemEffect.NONE,

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