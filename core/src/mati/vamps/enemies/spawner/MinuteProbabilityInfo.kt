package mati.vamps.enemies.spawner

import mati.vamps.enemies.EnemyType

data class MinuteProbabilityInfo (

    var minute: Int = 0,
    var bag : Array<MPI_Entry> = emptyArray()
)

data class MPI_Entry (var num: Int = 1, var type: EnemyType = EnemyType.NONE)