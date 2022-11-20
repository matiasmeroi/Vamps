package mati.vamps.ui

interface UIInputHandler {
    fun poll()
    fun up() : Boolean
    fun down() : Boolean
    fun left() : Boolean
    fun right() : Boolean
    fun enter() : Boolean
    fun escape() : Boolean
}