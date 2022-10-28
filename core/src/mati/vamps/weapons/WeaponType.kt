package mati.vamps.weapons

enum class WeaponType(val desciption: String = "") {
    NONE,
    KNIVES(desciption = "Throw knives at enemies"),
    GARLIC(desciption = "Hurts nearby enemies"),
    WHIP(desciption = "Hits several enemies at a time"),
    HOLY_WATER(desciption = "Creates areas of damage")
}