package mati.vamps.utils
import com.badlogic.gdx.utils.Array as GdxArray
class ProbabilityBag<T> {

    val array = GdxArray<T>()

    fun add(elem: T, num: Int) {
        for(i in 0 until num)
            array.add(elem)
    }

    fun get() : T {
        return array[Utils.r.nextInt(array.size)]
    }

}