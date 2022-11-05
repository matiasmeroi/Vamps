package mati.vamps.events

enum class VEvent  {

    PLAYER_TYPE_SELECTED,

    PLAYER_POSITION,
    PLAYER_MOVED_BY,
    PLAYER_ENEMY_COLLISION,

    ENEMY_KILLED, // x, y, info, entityid
    ENEMY_HIT // x, y, dmg
    ,
    ITEM_EFFECT_ACTIVATED,
    NEXT_LEVEL,

    ENEMY_ON_SCREEN,
    ENEMY_OFF_SCREEN,

    GAME_START,

    BUY_POWER_UP,
    REFUND_POWER_UPS,
    POWER_UP_ACTIVATED

}