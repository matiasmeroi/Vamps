package mati.vamps.weapons

enum class LevelUpEffect(val description: String = "Undef") {
    INCREASE_STRENGTH_10("Increase strength by 10%"),
    INCREASE_STRENGTH_20("Increase strength by 20%"),
    INCREASE_STRENGTH_30("Increase strength by 30%"),

    ADD_PROJECTILE("Plus one projectile"),

    INCREASE_AREA_10("Increase area by 10%"),
    INCREASE_AREA_20("Increase area by 20%"),
    INCREASE_AREA_40("Increase area by 40%"),

    REDUCE_COOLDOWN_8("Reduce cool-down by 8%")
}