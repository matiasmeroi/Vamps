package mati.vamps.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.kotcrab.vis.ui.widget.VisWindow
import com.badlogic.gdx.utils.ObjectMap
import com.kotcrab.vis.ui.widget.VisList
import com.badlogic.gdx.utils.Array as GdxArray

class GenericListSelector<T>(t: String = "", val closable: Boolean = false) : VisWindow(t), UIWindowsManager.UIWindow {

    interface Listener<T> {
        fun onOptionSelected(option: T)
    }

    private val text2options = ObjectMap<String, T>()

    data class Option<T>(val text: String, val value: T)

    lateinit var listener: Listener<T>
    private val list = VisList<String>()

    init {
        isMovable = false
        isModal = true
        isVisible = false

        this.add(list)
    }

    override fun update() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) list.selectedIndex = (list.selectedIndex+1) % list.items.size
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            list.selectedIndex =
                if(list.selectedIndex > 0 )
                    (list.selectedIndex - 1) % list.items.size
                else list.items.size - 1

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            this.isVisible = false
            if(text2options.size > 0) listener.onOptionSelected(text2options.get(list.selected))
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && closable) {
            isVisible = false
        }
    }

    fun setOptions(opts: GdxArray<Option<T>>) {
        list.clear()
        text2options.clear()

        val items = GdxArray<String>()
        for(o in opts) {
            items.add(o.text)
            text2options.put(o.text, o.value)
        }
        list.setItems(items)

        list.pack()
        this.pack()
    }
}