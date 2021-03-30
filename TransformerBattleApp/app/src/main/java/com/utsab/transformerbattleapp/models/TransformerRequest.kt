package com.utsab.transformerbattleapp.models

data class TransformerRequest(
    var id: String,
    var name: String? = "",
    var courage: Int? = 0,
    var endurance: Int? = 0,
    var firepower: Int? = 0,
    var intelligence: Int? = 0,
    var rank: Int? = 0,
    var skill: Int? = 0,
    var speed: Int? = 0,
    var strength: Int? = 0,
    var team: String? = ""
)