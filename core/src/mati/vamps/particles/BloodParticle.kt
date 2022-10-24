package mati.vamps.particles

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import mati.vamps.Utils
import mati.vamps.Vamps

class BloodParticle : Actor() {

    private var timer = 2

    init {
        this.toFront()
    }

    override fun act(delta: Float) {
        super.act(delta)
        timer--
        if(timer == 0) remove()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        val n = Utils.r.nextInt(3) + 1
        batch!!.draw(Vamps.atlas().findRegion("blood/blood3"), x - 22, y - 22)
    }
}