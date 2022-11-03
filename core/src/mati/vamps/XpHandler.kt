package mati.vamps

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisProgressBar
import mati.vamps.events.EventManager
import mati.vamps.events.VEvent
import mati.vamps.items.ItemEffect
import mati.vamps.utils.Utils

class XpHandler : Table(), EventManager.VEventListener {

    private var level = 1
    private var xpToNext = 0
    private var currentXP = 0

    private val progressBar = VisProgressBar(0f, 1f, 0.001f, false)
    private val label = VisLabel("$level")

    init {
        progressBar.setSize(Gdx.graphics.width- 20f, 30f)
        progressBar.setPosition(10f, Gdx.graphics.height - progressBar.height)

        label.setPosition(Gdx.graphics.width - 40f, Gdx.graphics.height - progressBar.height + 3f)

        this.addActor(progressBar)
        this.addActor(label)

        calcXpToNext()
        EventManager.subscribe(this)
    }

    fun update() {
        if(currentXP >= xpToNext) {
            currentXP = 0
            level++
            EventManager.announceNot2Enemies(VEvent.NEXT_LEVEL, Utils.json.toJson(level))
            calcXpToNext()
            label.setText("$level")
        }
    }

    fun getCurrentLevel() : Int { return level }

    private fun calcXpToNext() {
        xpToNext = level * 50
    }

    private fun gainXp(v: Int) {
        currentXP += v
    }

    private fun onItemEffect(effect: ItemEffect) {
        when(effect) {
            ItemEffect.GAIN_XP_10 -> {
                gainXp(10)
            }
            ItemEffect.GAIN_XP_100 ->  {
                gainXp(100)
            }
        }
    }

    override fun onVEvent(event: VEvent, params: String) {
        when(event) {
            VEvent.ITEM_EFFECT_ACTIVATED -> {
                onItemEffect(Utils.json.fromJson(ItemEffect::class.java, params))
            }
        }
    }

    var i =0f

    override fun act(delta: Float) {
        super.act(delta)
        progressBar.setValue((currentXP / xpToNext.toFloat()))
    }

//    override fun draw(batch: Batch?, parentAlpha: Float) {
//        super.draw(batch, parentAlpha)
//        println("H")
//        batch!!.draw(Vamps.atlas().findRegion("players/health_bar_black"), 0f, Gdx.graphics.height - 30f,
//            Gdx.graphics.width + 0f, Gdx.graphics.height +0f)
//        batch!!.draw(Vamps.atlas().findRegion("players/health_bar_red"), 0f, Gdx.graphics.height - 30f,
//            Gdx.graphics.width * (currentXP / xpToNext) + 0f, Gdx.graphics.height + 0f)
//    }

}