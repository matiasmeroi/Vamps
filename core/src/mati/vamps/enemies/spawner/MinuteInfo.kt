package mati.vamps.enemies.spawner

import mati.vamps.enemies.EnemyType
import mati.vamps.enemies.spawner.waves.Waves.Type

data class MinuteInfo (
    var minute: Int = 0,
    var bag : Array<MPI_Entry> = emptyArray(),
    var enemyWaves : Array<Type> = emptyArray()
)

data class MPI_Entry (var num: Int = 1, var type: EnemyType = EnemyType.NONE)