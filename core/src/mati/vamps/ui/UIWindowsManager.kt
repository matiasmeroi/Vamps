package mati.vamps.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.widget.VisWindow
import mati.vamps.players.Player
import mati.vamps.players.WeaponUpgradeInfo
import mati.vamps.utils.Utils
import mati.vamps.weapons.Holster
import com.badlogic.gdx.utils.Array as GdxArray

class UIWindowsManager(val stage: Stage) {

    interface UIWindow {
        fun update()
    }

    val upgradeSelectionUI = GenericListSelector<WeaponUpgradeInfo>("Select Upgrade")
    private val allWindows = arrayOf(upgradeSelectionUI)

    fun initialize() {
        stage.addActor(upgradeSelectionUI)
    }

    fun update() {
        for(w in allWindows) if(w.isVisible) w.update()
    }

    fun isWindowOpen() : Boolean {
        for(w in allWindows) if(w.isVisible) return true
        return false
    }

    fun showUpgradeWindow(holster: Holster) : Boolean {
        val upgradesAvailable = holster.getWeaponUpgradesAvailable()

        if(upgradesAvailable.size == 0) return false

        val givenOptions = GdxArray<GenericListSelector.Option<WeaponUpgradeInfo>>()

        var added = 0
        while(!upgradesAvailable.isEmpty && added < Utils.MAX_WEAPON_UPGRADES_OFFERED) {
            val idx = Utils.r.nextInt(upgradesAvailable.size)
            val uv = upgradesAvailable[idx]
            val opt = GenericListSelector.Option("${uv.upgradeType} -> ${uv.weaponType} -> ${uv.description}", uv)
            givenOptions.add(opt)
            upgradesAvailable.removeIndex(idx)
            added++
        }

        upgradeSelectionUI.setOptions(givenOptions)
        upgradeSelectionUI.toFront()
        upgradeSelectionUI.isVisible = true

        centerWindow(upgradeSelectionUI)
        return true
    }

//    fun showUpgradeWindow(holster: Holster) {
//        upgradeSelectionUI.createOptions(holster)
//        upgradeSelectionUI.toFront()
//        upgradeSelectionUI.isVisible = true
//
//        centerWindow(upgradeSelectionUI)
//    }

    fun centerWindow(window: VisWindow) {
        window.setPosition(Gdx.graphics.width / 2  - window.width / 2, Gdx.graphics.height / 2  - window.height / 2)
    }


}