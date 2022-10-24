package mati.vamps.players

data class PlayerInfo (
    var name: String = "Unnamed",
    var description: String = "Empty Description",
    var maxHealth: Float = 100f,
    var might: Float = 1f,
    var armor: Float = 0f,
    var moveSpeed: Float = 1f,
    var recovery: Float = 0f,
    var pickupRadius: Float = 30f,
    var weaponCooldownMultiplier: Float = 1f,
    var type: PlayerType = PlayerType.NONE,
    var textureName: String = "None",

    // sprite dimensions
    var _w: Float = 52f,
    var _h: Float = 52f,

    // collision rectangle dimensions
    var _cw: Float = 32f,
    var _ch: Float = 32f
)