package mati.vamps.enemies.spawner

import mati.vamps.enemies.EnemyType
import mati.vamps.enemies.spawner.waves.WaveType

data class MinuteInfo (
    var minute: Int = 0,
    var enemyProbabilityBag : Array<MPI_Entry> = emptyArray(),
    var enemyWaves : Array<WaveType> = emptyArray()
)

data class MPI_Entry (var num: Int = 1, var type: EnemyType = EnemyType.NONE)