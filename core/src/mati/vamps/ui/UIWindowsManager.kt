package mati.vamps.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.JsonValue.PrettyPrintSettings
import com.kotcrab.vis.ui.widget.VisWindow
import mati.vamps.*
import mati.vamps.players.PlayerFactory
import mati.vamps.utils.Utils
import mati.vamps.weapons.Holster
import com.badlogic.gdx.utils.Array as GdxArray

class UIWindowsManager(val stage: Stage) {

    interface UIWindow {
        fun update()
    }

    val upgradeSelectionUI = GenericListSelector<Upgrade>("Select Upgrade")
    val presentUI = GenericListSelector<Upgrade>("Present")
    private val allWindows = arrayOf(upgradeSelectionUI, presentUI)

    fun initialize() {
        stage.addActor(upgradeSelectionUI)
        stage.addActor(presentUI)
    }

    fun update() {
        for(w in allWindows) if(w.isVisible) w.update()
    }

    fun isWindowOpen() : Boolean {
        for(w in allWindows) if(w.isVisible) return true
        return false
    }

    fun showUpgradeWindow(currentLevel: Int, holster: Holster) : Boolean {
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

    fun showPresentWindow(holster: Holster) : Boolean {
        val options = generatePresents(holster)

        if(options.isEmpty) return false

        val givenOptions = GdxArray<GenericListSelector.Option<Upgrade>>()

        for(option  in options) {
            val opt = GenericListSelector.Option(option.description, option)
            givenOptions.add(opt)
        }

        presentUI.setOptions(givenOptions)
        presentUI.setInputHandler(RandomListElementInputHandler())
        presentUI.isVisible = true

        centerWindow(presentUI)

        return true
    }


    private fun centerWindow(window: VisWindow) {
        window.setPosition(Gdx.graphics.width / 2  - window.width / 2, Gdx.graphics.height / 2  - window.height / 2)
    }


}