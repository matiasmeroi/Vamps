package mati.vamps.weapons

enum class WeaponType(val description: String = "") {
    NONE,
    KNIVES(description = "Throw knives at enemies"),
    GARLIC(description = "Hurts nearby enemies"),
    WHIP(description = "Hits several enemies at a time"),
    HOLY_WATER(description = "Creates areas of damage"),
    MAGIC_WAND(description = "Fires at the nearest enemy"),
    UNHOLY_BIBLE(description = "Orbits around the player")
}