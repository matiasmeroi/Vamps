package mati.vamps.ui

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTextButton
import mati.vamps.Vamps
import mati.vamps.power_ups.PowerUps

class PowerUpUIEntry(val myPu: PowerUps.PowerUp) : Table() {

    private val descLabel = VisLabel("")
    private val ownedLabel = VisLabel("x0")
    private val priceLabel = VisLabel("")
    private val plusButton = VisTextButton("+")

    init {
        this.defaults().pad(20f)
        this.row()
        this.add(descLabel)
        this.add(ownedLabel)
        this.add(priceLabel)
        this.add(plusButton)
        refresh()
    }

    fun onPlusClicked(cl: ClickListener) {
        plusButton.addListener(cl)
    }

    private fun refresh() {
        descLabel.setText(myPu.description)
        ownedLabel.setText("" + Vamps.puMgr().getOwned(myPu) + "x")
        val price = Vamps.puMgr().getPrice(myPu)
        if(price == -1) priceLabel.setText("MAX")
        else priceLabel.setText("$price")
        descLabel.pack()
        ownedLabel.pack()
        priceLabel.pack()
        pack()
    }

    override fun act(delta: Float) {
        super.act(delta)
        refresh()
    }

}