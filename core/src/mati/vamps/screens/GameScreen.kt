package mati.vamps.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import mati.vamps.Entity
import mati.vamps.Utils
import mati.vamps.Vamps
import mati.vamps.events.EventManager
import mati.vamps.events.VEvent
import mati.vamps.map.Map
import mati.vamps.players.Player
import mati.vamps.players.PlayerFactory
import mati.vamps.players.PlayerType

class GameScreen : Screen, EventManager.VEventListener {

    val mainStage = Stage(
        FitViewport(Gdx.graphics.width + 0f, Gdx.graphics.height + 0f,
            OrthographicCamera()
        )

    )

    private val sr = ShapeRenderer()
    private val playerFactory = PlayerFactory()
    private val player: Player
    private val map = Map(mainStage)
    init {
        EventManager.subscribe(this)

        playerFactory.load()

        player = playerFactory.create(PlayerType.GREG)
        mainStage.addActor(player)
    }

    override fun show() {

    }

    private fun update() {
        player.handleInput()

        val mx = Gdx.input.getX()
        val my = Gdx.input.getY()
        val scr = Vector2(mx+0f, my+0f)
        val stg = mainStage.screenToStageCoordinates(scr)


        val mr = Rectangle(stg.x, stg.y, 1f, 1f)
        if(player.getRect().overlaps(mr)) {
            println("OK")
        }



        map.update()

    }

    private fun debugDraw() {
        Vamps.sr().projectionMatrix = mainStage.camera.combined

        Vamps.sr().begin(ShapeRenderer.ShapeType.Line)

        for(act in mainStage.actors) {
            if(act is Entity) {
                act.drawColRect()
            }
        }

        Vamps.sr().end()
    }

    override fun render(delta: Float) {
        update()

        ScreenUtils.clear(0f, 0f, 0f, 1f)

        map.draw()

        mainStage.camera.position.set(player.getX(), player.y, 0f);
        mainStage.camera.update();
        mainStage.batch.setProjectionMatrix(mainStage.getCamera().combined);

        mainStage.draw()

        debugDraw()

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
    }

    override fun resize(width: Int, height: Int) {
        mainStage.viewport.update(width, height)
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        mainStage.dispose()
    }

    override fun onVEvent(event: VEvent, params: String) {
        when(event) {
//            VEvent.PLAYER_MOVED_BY -> {
//                val p = params.split(EventManager.PARAM_SEP)
//                val dx = Utils.json.fromJson(Float::class.java, p[0])
//                val dy = Utils.json.fromJson(Float::class.java, p[1])
//                mainStage.camera.translate(dx, dy, 0f)
//            }
        }
    }
}