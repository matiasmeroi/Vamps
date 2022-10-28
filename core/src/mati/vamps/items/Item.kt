package mati.vamps.items

import com.badlogic.gdx.graphics.g2d.Batch
import mati.vamps.Entity

class Item : Entity() {

    enum class Type {
        NONE,
        DIAMOND_BLUE_10
    }

    fun onPickUpEffect() : ItemEffect {
        return (info as ItemInfo).effect
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        drawCenteredAndScale(batch!!, texture)
    }

}