package mati.vamps.map

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import mati.vamps.Vamps

class Background {

    companion object {
        const val TEXTURE = "map/bg/bg_molise"
    }

    private var x = 0f
    private var y = 0f

    private var textureWidth: Int
    private var textureHeight: Int
    private val off = 500f

    private var texture: TextureRegion

    init {
        texture = Vamps.atlas().findRegion(TEXTURE)
        textureWidth = texture.regionWidth
        textureHeight = texture.regionHeight
    }

    fun moveBy(dx: Float, dy: Float) {
        x += dx
        y += dy

        if(x >= textureWidth) {
            x -= textureWidth
        }
        if(y >= textureHeight) {
            y -= textureHeight
        }
        if(x <= - textureWidth) {
            x += textureWidth
        }
        if(y <= -textureHeight) {
            y += textureHeight
        }

//        println("$x, $y")
    }

    fun draw() {
        Vamps.batch().begin()
        Vamps.batch().draw(texture, x, y)

        val right = x < 0 + off
        val up = y < 0 + off
        val left = x > 0 - off
        val down = y > 0 - off

        if(right) Vamps.batch().draw(texture, x + textureWidth, y)
        if(left) Vamps.batch().draw(texture, x - textureWidth, y)
        if(up) Vamps.batch().draw(texture, x, y + textureHeight)
        if(down) Vamps.batch().draw(texture, x, y - textureHeight)

        if(right && up) Vamps.batch().draw(texture, x + textureWidth, y + textureHeight)
        if(right && down) Vamps.batch().draw(texture, x + textureWidth, y - textureHeight)
        if(left && up) Vamps.batch().draw(texture, x - textureWidth, y + textureHeight)
        if(left && down) Vamps.batch().draw(texture, x - textureWidth, y - textureHeight)

//        println("$right, $up, $left, $down")

        Vamps.batch().end()
    }
}