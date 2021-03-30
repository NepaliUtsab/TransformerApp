package com.utsab.transformerbattleapp.helpers

import androidx.databinding.InverseMethod


@InverseMethod("positionToStat")
fun statToPosition(statAttr: Int?): Int? {
    return statAttr?.minus(1)
}

fun positionToStat(position: Int): Int {
    return position.plus(1)
}