package mati.vamps.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.kotcrab.vis.ui.widget.VisLabel
import mati.vamps.Vamps
import mati.vamps.weapons.Holster

class StatsUI: Actor() {

    companion object {
        const val WEAPON_ICON_SIZE = 26F
        const val WEAPON_ICON_SEP = 4F
        const val YOFF = 75f
        const val XOFF = 100f
        const val GOLD_X_OFF = 800F
    }

    private lateinit var myHolster: Holster
    private val goldLabel = VisLabel("")

    fun initialize(holster: Holster) {
        x = XOFF
        y = Gdx.graphics.height - YOFF
        myHolster = holster
        goldLabel.setPosition(x + GOLD_X_OFF + 30, y + 6)
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)

        val weaponIconList = myHolster.getWeaponIconList()
        var count = 0
        for(icon in weaponIconList) {
            batch!!.draw(Vamps.atlas().findRegion(icon), x + count * (WEAPON_ICON_SEP + WEAPON_ICON_SIZE), y, WEAPON_ICON_SIZE, WEAPON_ICON_SIZE)
            count++
        }

        goldLabel.setText("${Vamps.goldMgr().earnedThisRound()}")
        batch!!.draw(Vamps.atlas().findRegion("items/coin"), x + GOLD_X_OFF, y)
        goldLabel.draw(batch, parentAlpha)

    }

}
