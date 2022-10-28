package mati.vamps.utils

import com.badlogic.gdx.utils.Json
import java.util.*

object Utils {

    const val DEBUG = false
    const val ATLAS_PATH = "gfx/atlas.atlas"


    val json = Json()
    val r = Random()


    const val DIAG_SPEED_MULTIPLIER = 0.707f

    private var currentId = 0
    fun getNewId() : Int { currentId++; return currentId }


}