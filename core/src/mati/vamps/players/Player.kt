package mati.vamps.players

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import mati.vamps.Vamps

class Player : Actor() {

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch!!.draw(Vamps.atlas().findRegion("players/p1/p1"), 0f, 0f)
    }
}