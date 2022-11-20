package mati.vamps.items

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ObjectMap
import mati.vamps.utils.Utils
import mati.vamps.enemies.EnemyFactory
import mati.vamps.events.EventManager
import mati.vamps.events.EventManager.PARAM_SEP
import mati.vamps.events.VEvent
import mati.vamps.utils.ProbabilityBag
import com.badlogic.gdx.utils.Array as GdxArray

class ItemFactory(val stage: Stage) : EventManager.VEventListener {

    companion object {
        const val INFO_FILE = "data/items.json"
        const val CREATION_RATE = 0.5f
    }

    private val infoMap = ObjectMap<Item.Type, ItemInfo>()
    private val itemList = GdxArray<Item>()

    init{
        EventManager.subscribe(this)
    }

    fun load() {
        val fileString = Gdx.files.internal(INFO_FILE).readString()
        val infoArray = Utils.json.fromJson(Array<ItemInfo>::class.java, fileString)
        for(i in infoArray) {
            infoMap.put(i.type, i)
            Gdx.app.log(EnemyFactory.TAG, "Loaded: ${i.type}")
        }

        Gdx.app.log(EnemyFactory.TAG, "${infoArray.size} items loaded")
    }


    private fun getOnKillItem(): Item {
        val r = Utils.r.nextInt(10000)
        val item =
            if(r <= 10) {
                getSpecialItem()
            }
            else if(r <= 1000) {
                getCoin()
            }
            else {
                Item().apply { initialize(infoMap.get(Item.Type.DIAMOND_BLUE_10).copy()) }
            }
        return item
    }

    private fun getSpecialItem() : Item {
        val pb = ProbabilityBag<Item.Type>()
        pb.add(Item.Type.CHICKEN, 30)
        pb.add(Item.Type.UNHOLY_CROSS, 2)
        pb.add(Item.Type.PRESENT, 4)
        val item = Item()
        item.initialize(infoMap.get(pb.get()).copy())
        return item
    }

    private fun getCoin(): Item {
        val item = Item()
        if(Utils.r.nextInt(10) <= 2) item.initialize(infoMap.get(Item.Type.COIN_BAG).copy())
        else item.initialize(infoMap.get(Item.Type.COIN).copy())
        return item
    }

    private fun getFireItem(): Item {
        val pb = ProbabilityBag<Item.Type>()
        pb.add(Item.Type.COIN, 100)
        pb.add(Item.Type.CHICKEN, 60)
        pb.add(Item.Type.COIN_BAG, 10)
        pb.add(Item.Type.PRESENT, 1)
        return Item().apply { initialize(infoMap.get(pb.get()).copy()) }
    }

    override fun onVEvent(event: VEvent, params: String) {
        when(event) {
            VEvent.ENEMY_KILLED -> {
                if(Utils.r.nextFloat() <= CREATION_RATE) {
                    val p = params.split(PARAM_SEP)
                    val x = Utils.json.fromJson(Float::class.java, p[0])
                    val y = Utils.json.fromJson(Float::class.java, p[1])
                    val item = getOnKillItem()
                    item.setPosition(x, y)
                    stage.addActor(item)
                    itemList.add(item)
                }
            }
            VEvent.FIRE_KILLED -> {
                val p = params.split(PARAM_SEP)
                val x = Utils.json.fromJson(Float::class.java, p[0])
                val y = Utils.json.fromJson(Float::class.java, p[1])
                val item = getFireItem()
                item.setPosition(x, y)
                stage.addActor(item)
                itemList.add(item)
            }
        }
    }

    fun getList() : GdxArray<Item> {
        return itemList
    }

}