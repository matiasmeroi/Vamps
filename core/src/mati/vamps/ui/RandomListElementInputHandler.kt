package mati.vamps.ui

import mati.vamps.utils.Utils

class RandomListElementInputHandler: UIInputHandler {

    companion object {
        const val SELECTED_TIME = 60
    }

    private enum class State {
        SCROLLING, SELECTED, ENTER, IDLE
    }

    private var state: State = State.SCROLLING
    private var SCROLL_TIME = Utils.r.nextInt(50) + 100
    private var timer = 0

    override fun poll() {
        when(state) {
            State.SCROLLING -> {
                timer++
                if(timer == SCROLL_TIME) {
                    timer = 0
                    state = State.SELECTED
                }
            }
            State.SELECTED -> {
                timer++
                if(timer == SELECTED_TIME) {
                    state = State.ENTER
                }
            }
            State.ENTER -> {
                state = State.IDLE
            }
            State.IDLE -> {}
        }
    }

    override fun up(): Boolean {
        return false
    }

    override fun down(): Boolean {
        return timer % 5 == 0 && state == State.SCROLLING
    }

    override fun left(): Boolean {
        return false
    }

    override fun right(): Boolean {
        return false
    }

    override fun enter(): Boolean {
        return state == State.ENTER
    }

    override fun escape(): Boolean {
        return false
    }
}