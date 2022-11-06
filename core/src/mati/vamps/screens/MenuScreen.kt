package mati.vamps.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTextButton
import mati.vamps.Vamps
import mati.vamps.events.EventManager
import mati.vamps.events.VEvent
import mati.vamps.players.PlayerFactory
import mati.vamps.players.PlayerType
import mati.vamps.ui.GenericListSelector
import mati.vamps.utils.Utils
import com.badlogic.gdx.utils.Array as GdxArray

class MenuScreen : Screen, GenericListSelector.Listener<PlayerType> {


    val stage = Stage(
        FitViewport(
            Gdx.graphics.width + 0f, Gdx.graphics.height + 0f,
            OrthographicCamera()
        )
    )

    private val table = Table()
    private val playButton = VisTextButton("Play")
    private val selectPlayerButton = VisTextButton("Select Player")
    private val playerSelectedLabel = VisLabel()
    private val powerUpsButton = VisTextButton("PowerUps")
    private val exitButton = VisTextButton("Exit")

    private val playerSelectionWindow = GenericListSelector<PlayerType>("Select Player", true)
    private var playerSelected = PlayerType.GREG

    init {
        table.setFillParent(true)
        table.defaults().center().pad(10f)

        playButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                EventManager.announceNot2Enemies(VEvent.GAME_START, "")
            }
        })

        selectPlayerButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                showPlayerSelectionWindow()
            }
        })

        powerUpsButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Vamps.changeScreen(Vamps.ScreenType.POWER_UP_MENU)
            }
        })

        exitButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Gdx.app.exit()
            }
        })

        playerSelectionWindow.listener = this

        playerSelectionWindow.toBack()
        playerSelectionWindow.isVisible = false

        table.add(playButton)
        table.row()
        table.add(playerSelectedLabel)
        table.add(selectPlayerButton)
        table.row()
        table.add(powerUpsButton)
        table.row()
        table.add(exitButton)

        stage.addActor(playerSelectionWindow)
        stage.addActor(table)

        updatePlayerLabel()
    }

    private fun updatePlayerLabel() {
        playerSelectedLabel.setText("Player: $playerSelected")
    }

    private fun showPlayerSelectionWindow() {
        val options = GdxArray<GenericListSelector.Option<PlayerType>>()
        for(type in PlayerType.values()) {
            if(type != PlayerType.NONE)
                options.add(GenericListSelector.Option(PlayerFactory.getDescriptionForType(type), type))
        }

        playerSelectionWindow.setPosition(Gdx.graphics.width / 2 - playerSelectionWindow.width,
            Gdx.graphics.height / 2 + playerSelectionWindow.height / 2)
        playerSelectionWindow.setOptions(options)

        playerSelectionWindow.toFront()
        playerSelectionWindow.isVisible = true
    }

    override fun show() {
        Vamps.multiplexer().addProcessor(stage)
    }

    override fun render(delta: Float) {
        if(playerSelectionWindow.isVisible) playerSelectionWindow.update()

        ScreenUtils.clear(0f, 0f, 0f, 1f)
        stage.act(Gdx.graphics.deltaTime)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }

    override fun pause() {}

    override fun resume() {}

    override fun hide() {
        Vamps.multiplexer().removeProcessor(stage)
    }

    override fun dispose() {
        stage.dispose()
    }

    override fun onOptionSelected(option: PlayerType) {
        playerSelected = option
        updatePlayerLabel()
        EventManager.announceNot2Enemies(VEvent.PLAYER_TYPE_SELECTED, Utils.json.toJson(option))
        playerSelectionWindow.isVisible = false
    }
}