package mati.vamps

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.kotcrab.vis.ui.VisUI
import mati.vamps.Utils.ATLAS_PATH
import mati.vamps.screens.GameScreen
import mati.vamps.screens.MenuScreen

object Vamps : Game() {

    private lateinit var batch: SpriteBatch
    private lateinit var sr: ShapeRenderer
    private lateinit var multiplexer: InputMultiplexer
    private lateinit var assets: AssetManager

    fun batch() : SpriteBatch { return batch }
    fun sr() : ShapeRenderer { return sr }
    fun multiplexer() : InputMultiplexer { return multiplexer }
    fun assets() : AssetManager { return assets }
    fun atlas() : TextureAtlas { return assets.get(ATLAS_PATH, TextureAtlas::class.java) }

    private lateinit var mainMenuScreen: MenuScreen

    private lateinit var gameScreen: GameScreen

    enum class ScreenType {
        MAIN_MENU_SCEEN, GAME_SCREEN
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

        mainMenuScreen = MenuScreen()
        gameScreen = GameScreen()

        setScreen(gameScreen)
    }

    fun getScreenByType(screenType: ScreenType?): Screen {
        return when (screenType) {
            ScreenType.MAIN_MENU_SCEEN -> mainMenuScreen
            ScreenType.GAME_SCREEN -> gameScreen
            else -> gameScreen
        }
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
        assets.dispose()
    }
}