package mati.vamps.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Json
import java.lang.Float.max
import java.util.*
import kotlin.math.sign

object Utils {
    
    val TAG = Utils::class.java.getSimpleName()

    const val DEBUG = false
    const val ATLAS_PATH = "gfx/atlas.atlas"

    const val MAX_UPGRADES_OFFERED = 3


    val json = Json()
    val r = Random()

    const val DIAG_SPEED_MULTIPLIER = 0.707f

    private var currentId = 0
    fun getNewId() : Int { currentId++; return currentId }


    fun getRandomDirection() : Vector2 {
        var x = 0f
        var y = 0f
        do {
            x = r.nextInt(3) - 1f
            y = r.nextInt(3) - 1f
        } while(x == 0f && y == 0f)

        return Vector2(x, y)
    }

    fun getOffscreenPosForDirection(dir: Vector2, player: Vector2, worldW: Float, worldH: Float) : Vector2 {
        var xoff = worldW / 2 + 400f
        var yoff = worldH / 2 + 400f
        val res = Vector2(player)
        
        if(dir.x == 0f && dir.y == 0f) {
            Gdx.app.log(TAG, "Warning: calling gospfd with (0, 0")
            return res
        }

        // para que esté en diagonal
        if(dir.x != 0f && dir.y != 0f) {
            val m = max(xoff, yoff)
            xoff = m
            yoff = m
        }

        res.x -= dir.x * xoff
        res.y -= dir.y * yoff
        
        return res
    }



}