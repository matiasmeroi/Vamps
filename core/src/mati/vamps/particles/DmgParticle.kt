package mati.vamps.particles

import com.kotcrab.vis.ui.widget.VisLabel
import mati.vamps.utils.Utils

class DmgParticle(val num: Int) : VisLabel("$num") {

    init {
        this.setColor(1f, 0f, 0f, 1f)
        this.setFontScale(1.5f)
    }

    private var timer = Utils.r.nextInt(10) + 30

    override fun act(delta: Float) {
        super.act(delta)
        timer --
        if(timer <= 0) remove()
    }




}