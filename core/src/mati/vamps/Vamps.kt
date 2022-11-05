package mati.vamps

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.ObjectMap
import com.kotcrab.vis.ui.VisUI
import mati.vamps.events.EventManager
import mati.vamps.events.VEvent
import mati.vamps.power_ups.GoldManager
import mati.vamps.screens.DeadScreen
import mati.vamps.utils.Utils.ATLAS_PATH
import mati.vamps.screens.GameScreen
import mati.vamps.screens.MenuScreen

object Vamps : Game(), EventManager.VEventListener {

    private lateinit var batch: SpriteBatch
    private lateinit var sr: ShapeRenderer
    private lateinit var multiplexer: InputMultiplexer
    private lateinit var assets: AssetManager

    fun batch() : SpriteBatch { return batch }
    fun sr() : ShapeRenderer { return sr }
    fun multiplexer() : InputMultiplexer { return multiplexer }
    fun assets() : AssetManager { return assets }
    fun atlas() : TextureAtlas { return assets.get(ATLAS_PATH, TextureAtlas::class.java) }

    fun goldMgr() : GoldManager { return goldManager }

    private lateinit var goldManager: GoldManager

    private lateinit var mainMenuScreen: MenuScreen
    private lateinit var gameScreen: GameScreen
    private lateinit var deadScreen: DeadScreen

    enum class ScreenType {
        MAIN_MENU_SCEEN, GAME_SCREEN, DEAD_SCREEN
    }

    override fun create() {
        batch = SpriteBatch()
        sr = ShapeRenderer()
        multiplexer = InputMultiplexer()
        Gdx.input.inputProcessor = multiplexer

        assets = AssetManager()
        assets.load(ATLAS_PATH, TextureAtlas::class.java)
        VisUI.load()

        assets.finishLoading()

        goldManager = GoldManager()
        mainMenuScreen = MenuScreen()
        gameScreen = GameScreen()
        deadScreen = DeadScreen()

        EventManager.subscribe(this)
        ProfileManager.load<ObjectMap<String, Object>>()

        setScreen(mainMenuScreen)
    }

    fun changeScreen(screenType: ScreenType) { setScreen(getScreenByType(screenType)) }

    private fun getScreenByType(screenType: ScreenType): Screen {
        return when (screenType) {
            ScreenType.MAIN_MENU_SCEEN -> mainMenuScreen
            ScreenType.GAME_SCREEN -> gameScreen
            ScreenType.DEAD_SCREEN -> deadScreen
        }
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
        assets.dispose()
    }

    override fun onVEvent(event: VEvent, params: String) {
        when(event) {
            VEvent.GAME_START -> {
                setScreen(getScreenByType(ScreenType.GAME_SCREEN))
            }
        }
    }
}