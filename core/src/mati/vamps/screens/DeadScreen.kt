package mati.vamps.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTextButton
import mati.vamps.ProfileManager
import mati.vamps.Vamps
import mati.vamps.events.EventManager
import mati.vamps.events.EventManager.PARAM_SEP
import mati.vamps.events.VEvent

class DeadScreen : Screen, EventManager.VEventListener {

    val stage = Stage(
        FitViewport(
            Gdx.graphics.width + 0f, Gdx.graphics.height + 0f,
            OrthographicCamera()
        )
    )

    private val table = Table()
    private val timeLabel = VisLabel("Time: 00:00")
    private val goldLabel = VisLabel("Gold: ${Vamps.goldMgr().earnedThisRound()}")
    private val killLabels = VisLabel("Kills: 0")
    private val okButton = VisTextButton("Ok")

    init {
        okButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                EventManager.announceNot2Enemies(VEvent.RESET_GAME, "")
                Vamps.changeScreen(Vamps.ScreenType.MAIN_MENU_SCEEN)
            }
        })

        table.setFillParent(true)
        table.defaults().pad(30f)

        table.add(timeLabel)
        table.row()
        table.add(goldLabel)
        table.row()
        table.add(killLabels)
        table.row()
        table.add(okButton)

        stage.addActor(table)

        EventManager.subscribe(this)
    }

    override fun show() {
        Vamps.multiplexer().addProcessor(stage)
        ProfileManager.save()
        EventManager.announceNot2Enemies(VEvent.RESET_GAME, "")
        goldLabel.setText("Gold: ${Vamps.goldMgr().earnedThisRound()}")
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.BLACK)

        Vamps.batch().begin()
        val reg = Vamps.atlas().findRegion("items/game_over_msg")
        val tw = reg.regionWidth
        Vamps.batch().draw(reg, stage.viewport.worldWidth / 2 - tw  / 2f, stage.viewport.worldHeight - stage.viewport.worldHeight/ 4f)
        Vamps.batch().end()

        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
        Vamps.multiplexer().removeProcessor(stage)
    }

    override fun dispose() {
        stage.dispose()
    }

    override fun onVEvent(event: VEvent, params: String) {
        when(event) {
            VEvent.ROUND_TIME -> {
                val p = params.split(PARAM_SEP)
                val minutes = p[0]
                val seconds = p[1]
                timeLabel.setText("Time: $minutes:$seconds")
            }
            VEvent.ROUND_KILLS -> {
                killLabels.setText("Kills: $params")
            }
        }
    }
}