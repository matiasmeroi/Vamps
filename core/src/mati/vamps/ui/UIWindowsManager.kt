package mati.vamps.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.widget.VisWindow
import mati.vamps.PlayerUpgradeInfo
import mati.vamps.Upgrade
import mati.vamps.WeaponUpgradeInfo
import mati.vamps.generateUpgrades
import mati.vamps.players.PlayerFactory
import mati.vamps.utils.Utils
import mati.vamps.weapons.Holster
import com.badlogic.gdx.utils.Array as GdxArray

class UIWindowsManager(val stage: Stage) {

    interface UIWindow {
        fun update()
    }

    val upgradeSelectionUI = GenericListSelector<Upgrade>("Select Upgrade")
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

    fun showUpgradeWindow(currentLevel: Int, holster: Holster) : Boolean {
//        val upgradesAvailable = holster.getWeaponUpgradesAvailable()
//
//        for(playerUpgrade in PlayerFactory.getUpgradesAvailable()) {
//            upgradesAvailable.add(playerUpgrade)
//        }

        val upgradesAvailable = generateUpgrades(currentLevel, holster)

        if(upgradesAvailable.size == 0) return false

        val givenOptions = GdxArray<GenericListSelector.Option<Upgrade>>()

        var added = 0
        while(!upgradesAvailable.isEmpty && added < Utils.MAX_UPGRADES_OFFERED) {
            val idx = Utils.r.nextInt(upgradesAvailable.size)
            val uv = upgradesAvailable[idx]

            val opt = GenericListSelector.Option(uv.description, uv)

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


    private fun centerWindow(window: VisWindow) {
        window.setPosition(Gdx.graphics.width / 2  - window.width / 2, Gdx.graphics.height / 2  - window.height / 2)
    }


}