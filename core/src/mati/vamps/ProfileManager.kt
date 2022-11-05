package mati.vamps

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Base64Coder
import com.badlogic.gdx.utils.ObjectMap
import mati.vamps.utils.Utils
import com.badlogic.gdx.utils.Array as GdxArray

object ProfileManager {

    val TAG = ProfileManager::class.java.getSimpleName()

    interface ProfileListener {
        fun onProfileEvent(e: Event)
    }

    enum class Event { SAVE, LOAD, CLEAR }

    const val FILENAME = "VAMPS.sav"

    private val listeners = GdxArray<ProfileListener>()

    var properties = ObjectMap<String, Object>()

    fun addListener(pl: ProfileListener) {
        listeners.add(pl)
    }

    fun setProperty(key: String, value: Object) {
        properties.put(key, value)
    }

    fun getProperty(key: String) : Object {
        return properties.get(key)
    }

    fun removeProperty(key: String) {
        properties.remove(key)
    }

    fun save() {
        Gdx.app.log(TAG, "Saving data")
        notifyListeners(Event.SAVE)
        writeProfileToStorage(Utils.json.toJson(properties))
    }

    private fun writeProfileToStorage(fileData: String?) {
        val fullFilename = FILENAME

        var file: FileHandle? = null
        if (Gdx.files.isLocalStorageAvailable) {
            file = Gdx.files.local(fullFilename)
            val encodedString = Base64Coder.encodeString(fileData)
            file.writeString(encodedString, false)
        }
    }

    inline fun <reified T: ObjectMap<String, Object>> load() {
        if(!Gdx.files.local(FILENAME).exists()) {
            Gdx.app.log(TAG, "No save file available")
            return
        }

        Gdx.app.log(TAG, "Loading data")

        val encodedFile: FileHandle = Gdx.files.local(FILENAME)
        val s = encodedFile.readString()

        val decodedFile = Base64Coder.decodeString(s)

        properties = Utils.json.fromJson(T::class.java, decodedFile)
        notifyListeners(Event.LOAD)
    }

    fun clear() {
        notifyListeners(Event.CLEAR)
    }

    fun notifyListeners(e: Event) {
        for(lis in listeners) lis.onProfileEvent(e)
    }

}