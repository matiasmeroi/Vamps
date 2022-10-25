package mati.vamps.events

enum class VEvent  {

    PLAYER_POSITION,
    PLAYER_MOVED_BY,
    PLAYER_ENEMY_COLLISION,

    ENEMY_KILLED, // x, y, info
    ENEMY_REMOVED,
    ENEMY_HIT // x, y, dmg

}