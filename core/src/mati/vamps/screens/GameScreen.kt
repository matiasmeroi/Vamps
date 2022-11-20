package mati.vamps.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Buttons
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import mati.vamps.*
import mati.vamps.enemies.EnemyFactory
import mati.vamps.enemies.EnemyType
import mati.vamps.enemies.spawner.Spawner
import mati.vamps.enemies.spawner.SpawnerData
import mati.vamps.events.EventManager
import mati.vamps.events.EventManager.PARAM_SEP
import mati.vamps.events.VEvent
import mati.vamps.items.ItemFactory
import mati.vamps.map.Map
import mati.vamps.particles.BloodParticle
import mati.vamps.particles.DmgParticle
import mati.vamps.players.Player
import mati.vamps.players.PlayerFactory
import mati.vamps.players.PlayerType
import mati.vamps.WeaponUpgradeInfo
import mati.vamps.items.ItemEffect
import mati.vamps.ui.GameTimer
import mati.vamps.ui.GenericListSelector
import mati.vamps.ui.StatsUI
import mati.vamps.ui.UIWindowsManager
import mati.vamps.utils.KillCounter
import mati.vamps.utils.Utils
import mati.vamps.weapons.Holster
import mati.vamps.weapons.projectiles.ProjectileFactory

class GameScreen : Screen, EventManager.VEventListener, GameTimer.Listener,
    GenericListSelector.Listener<Upgrade> {

    val mainStage = Stage(
        FitViewport(Gdx.graphics.width + 0f, Gdx.graphics.height + 0f,
            OrthographicCamera()
        )
    )

    val uiStage = Stage(
        FitViewport(Gdx.graphics.width + 0f, Gdx.graphics.height + 0f,
            OrthographicCamera()
        )
    )


    private val gameTimer = GameTimer()

    private val playerFactory = PlayerFactory()
    private val enemyFactory = EnemyFactory()
    private val itemFactory = ItemFactory(mainStage)
    private val projectileFactory = ProjectileFactory()

    private val collisionManager = CollisionManager()
    private var xpHandler = XpHandler(uiStage)
    private var killCounter = KillCounter()
    private val uiWindowsManager = UIWindowsManager(uiStage)

    private var playerType: PlayerType  = PlayerType.GREG
    private lateinit var player: Player
    private var holster = Holster(projectileFactory)

    private var map = Map(mainStage)

    private var spawner = Spawner(gameTimer, enemyFactory, mainStage)

    private val statsUI = StatsUI()

    private var initialized = false

    init {
        EventManager.subscribe(this)

        playerFactory.load()
        enemyFactory.load()
        itemFactory.load()
        projectileFactory.load()
        SpawnerData.load()

        holster = Holster(projectileFactory)

        map = Map(mainStage)

        spawner = Spawner(gameTimer, enemyFactory, mainStage)

        initialized = false
    }

    fun reset() {
        mainStage.clear()
        uiStage.clear()

        enemyFactory.clear()

        killCounter = KillCounter()
        xpHandler = XpHandler(uiStage)
        holster = Holster(projectileFactory)

        gameTimer.reset()
        spawner.reset()

        player.reset()
    }

    private fun initialize() {
        gameTimer.addListener(this)

        player = playerFactory.create(mainStage, playerType)
        Vamps.puMgr().applyBought()
        holster.add(player.initialWeapon())

        projectileFactory.initialize(mainStage, player, enemyFactory.getOnScreenList())

        uiWindowsManager.initialize()
        uiWindowsManager.upgradeSelectionUI.listener = this
        uiWindowsManager.presentUI.listener = this

        statsUI.initialize(killCounter, holster)

        mainStage.addActor(player)

        uiStage.addActor(xpHandler)
        uiStage.addActor(gameTimer)
        uiStage.addActor(statsUI)

        initialized = true
    }

    override fun show() {
        Vamps.multiplexer().addProcessor(uiStage)
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

        if(Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
            val e = enemyFactory.create(EnemyType.BAT_MEDIUM)
            e.setPosition(stg.x, stg.y)
            mainStage.addActor(e)
        }

        if(Gdx.input.isButtonPressed(Buttons.RIGHT)) {
            val e = enemyFactory.create(EnemyType.BAT_MEDIUM)
            e.setPosition(stg.x, stg.y)
            mainStage.addActor(e)
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            EventManager.announceNot2Enemies(VEvent.ITEM_EFFECT_ACTIVATED, Utils.json.toJson(ItemEffect.GAIN_XP_100))
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            EventManager.announceNot2Enemies(VEvent.ITEM_EFFECT_ACTIVATED, Utils.json.toJson(ItemEffect.COIN_10))
        }

        holster.update(player)

        collisionManager.run(player, enemyFactory.getList(), itemFactory.getList(), holster)
        xpHandler.update()

        spawner.update(player.getPosition())

        mainStage.act(Gdx.graphics.deltaTime)
        map.update()
        uiStage.act(Gdx.graphics.deltaTime)

    }

    private fun debugDraw() {
        if(!Utils.DEBUG) return

        Gdx.graphics.setTitle("Vamps - ${Gdx.graphics.framesPerSecond}")

        Vamps.sr().projectionMatrix = mainStage.camera.combined

        Vamps.sr().begin(ShapeRenderer.ShapeType.Line)

        for(act in mainStage.actors) {
            if(act is Entity) {
                act.drawColRect()
            }
        }

        player.drawPickUpRect()

        Vamps.sr().end()
    }

    override fun render(delta: Float) {
        if(!initialized) return

        uiWindowsManager.update()

        if(!uiWindowsManager.isWindowOpen())
            update()

        ScreenUtils.clear(0f, 0f, 0f, 1f)

        map.draw()

        mainStage.camera.position.set(player.getX(), player.y, 0f);
        mainStage.camera.update();
        mainStage.batch.setProjectionMatrix(mainStage.getCamera().combined);

        mainStage.draw()
        mainStage.batch.begin()
        holster.draw(mainStage.batch)
        mainStage.batch.end()
        uiStage.draw()

        debugDraw()

        if(player.isDead()) {
            EventManager.announceNot2Enemies(VEvent.GAME_END, "")
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit()
        }
    }

    override fun resize(width: Int, height: Int) {
        mainStage.viewport.update(width, height)
        uiStage.viewport.update(width, height)
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {
        Vamps.multiplexer().removeProcessor(uiStage)
    }

    override fun dispose() {
        mainStage.dispose()
        uiStage.dispose()
    }

    override fun onVEvent(event: VEvent, params: String) {
        val j = Utils.json
        when(event) {
            VEvent.GAME_START -> {
                initialize()
            }
            VEvent.PLAYER_TYPE_SELECTED -> {
                val t = Utils.json.fromJson(PlayerType::class.java, params)
                playerType = t
            }
            VEvent.PLAYER_ENEMY_COLLISION -> {
                val p  = BloodParticle()
                p.setPosition(player.x, player.y)
                mainStage.addActor(p)
            }
            VEvent.ENEMY_HIT -> {
                val p = params.split(PARAM_SEP)
                val x = j.fromJson(Int::class.java, p[0])
                val y = j.fromJson(Int::class.java, p[1])
                val dmg = j.fromJson(Int::class.java, p[2])
                val par = DmgParticle(dmg)
                par.setPosition(x + 0f, y + 15f)
                mainStage.addActor(par)
            }
            VEvent.NEXT_LEVEL -> {
                uiWindowsManager.showUpgradeWindow(xpHandler.getCurrentLevel(), holster)
            }
            VEvent.GAME_END -> {
                EventManager.announceNot2Enemies(VEvent.ROUND_TIME, "${gameTimer.getMinutes()}" + PARAM_SEP + "${gameTimer.getSeconds()}")
                EventManager.announceNot2Enemies(VEvent.ROUND_KILLS, "${killCounter.getCount()}")
            }
            VEvent.ITEM_EFFECT_ACTIVATED -> {
                val effect = j.fromJson(ItemEffect::class.java, params)
                if(effect == ItemEffect.OPEN_PRESENT) uiWindowsManager.showPresentWindow(holster)
            }

        }
    }


    override fun onMinutesChange(minutes: Int) {

    }

    override fun onSecondChange(seconds: Int) {

    }

    override fun onOptionSelected(option: Upgrade) {
        if(option is WeaponUpgradeInfo)
            holster.applyWeaponUpgrade(option)
        else if(option is PlayerUpgradeInfo)
            player.applyUpgrade(option)
    }
}