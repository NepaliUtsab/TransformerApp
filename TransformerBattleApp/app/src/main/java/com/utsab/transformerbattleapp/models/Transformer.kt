package com.utsab.transformerbattleapp.models

data class Transformer(
    var id: String = "",
    var name: String = "",
    var strength: Int = 1,
    var intelligence: Int = 1,
    var endurance: Int = 1,
    var courage: Int = 1,
    var rank: Int = 1,
    var speed: Int = 1,
    var firepower: Int = 1,
    var skill: Int = 1,
    var team: String = "",
    var team_icon: String = ""

){
    fun getRating(): Int{

        return (strength + intelligence + speed + endurance + firepower)
    }
}