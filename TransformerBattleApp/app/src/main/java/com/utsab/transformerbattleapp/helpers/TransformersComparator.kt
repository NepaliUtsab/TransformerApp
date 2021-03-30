package com.utsab.transformerbattleapp.helpers

import com.utsab.transformerbattleapp.models.Transformer
import com.utsab.transformerbattleapp.models.commonModels.Result

/**
 * Helper class for calculating winner for the battle
 */
object TransformersComparator {

    /*
    * A. Winning cases
    * 1. If transformer is named Optimus Prime or Predaking wins match automatically
    * 2. If one transformer has 4 or more courage and 3 or more strength than transformer, first fighter wins
    * 3. If one transformer has 3 or more skill than opponent, first transformer wins
    * 4. Winner is the one with highest overall rating
    *
    * B. If draw occurs: both transformers are eliminated
    *
    * C. Special case: If transformers named Optimus Prime and Predaking face each other then all transformers are
    *       destroyed and result is draw
    *
    * */
    fun decideWinner(
        autobots: ArrayList<Transformer>,
        decepticons: ArrayList<Transformer>
    ): HashMap<String, ArrayList<Transformer>> {

        var finishBattles = 0
        var finalResult = hashMapOf<String, ArrayList<Transformer>>()
        finalResult[Constants.TEAM_AUTOBOT] = arrayListOf()
        finalResult[Constants.TEAM_DECEPTICON] = arrayListOf()
        finalResult[Constants.DRAW] = arrayListOf()

        val totalBattles = if (autobots.size > decepticons.size) decepticons.size - 1
        else autobots.size - 1

        for (pos in 0..totalBattles) {
            val autobot = autobots[pos]
            val decepticon = decepticons[pos]
            when {
//                    Special Case C
                (autobot.name.equals(Constants.OPTIMUS, ignoreCase = true)
                        && decepticon.name.equals(Constants.PREDAKING, ignoreCase = true)) -> {
                    return hashMapOf<String, ArrayList<Transformer>>()
                }
//                Case 1 Autobot
                (autobot.name.equals(Constants.OPTIMUS, ignoreCase = true)) -> {
                    finishBattles.plus(1)
                    finalResult[Constants.TEAM_AUTOBOT]!!.add(autobot)

//                    finalResult.add(Constants.TEAM_AUTOBOT)
                }
//                Case 1 Decepticon
                (decepticon.name.equals(Constants.PREDAKING, ignoreCase = true)) -> {
                    finishBattles.plus(1)
                    finalResult[Constants.TEAM_DECEPTICON]!!.add(decepticon)
//                    finalResult.add(Constants.TEAM_DECEPTICON)
                }
//                Case 2
                (checkCourage(autobot, decepticon) != Constants.DRAW) -> {
                    finishBattles.plus(1)
                    val result = checkCourage(autobot, decepticon)

                    val winner = when (result) {
                        Constants.TEAM_AUTOBOT -> autobot
                        Constants.TEAM_DECEPTICON -> decepticon
                        else -> Transformer()
                    }

                    finalResult[result]!!.add(winner)
//                    finalResult.add(checkCourage(autobot,decepticon))
                }
//                Case 3 autobot
                (autobot.skill - decepticon.skill > 3) -> {
                    finishBattles.plus(1)
                    finalResult[Constants.TEAM_AUTOBOT]!!.add(autobot)
//                    finalResult.add(Constants.TEAM_AUTOBOT)
                }
//                Case 3 decepticon
                (decepticon.skill - autobot.skill > 3) -> {
                    finishBattles.plus(1)
                    finalResult[Constants.TEAM_DECEPTICON]!!.add(decepticon)
                }
//                Case 4
                else -> {
                    finishBattles.plus(1)
                    var result = checkOverallRating(autobot, decepticon)
                    val winner = when (result) {
                        Constants.TEAM_AUTOBOT -> autobot
                        Constants.TEAM_DECEPTICON -> decepticon
                        else -> Transformer()
                    }
                    finalResult[result]!!.add(winner)
                }

            }
        }
        return finalResult
    }

    private fun checkOverallRating(autobot: Transformer, decepticon: Transformer): String {
        return when {
            autobot.getRating() > decepticon.getRating() -> Constants.TEAM_AUTOBOT
            autobot.getRating() < decepticon.getRating() -> Constants.TEAM_DECEPTICON
            else -> Constants.DRAW
        }
    }

    private fun checkCourage(autobot: Transformer, decepticon: Transformer): String {
        return if (autobot.courage > decepticon.courage) {
            if (autobot.courage - decepticon.courage > 4) {
                if (autobot.strength - decepticon.strength > 3) {
                    Constants.TEAM_AUTOBOT
                } else Constants.DRAW
            } else Constants.DRAW
        } else {
            if (decepticon.courage - autobot.courage > 4) {
                if (decepticon.strength - autobot.strength > 3) {
                    Constants.TEAM_DECEPTICON
                } else Constants.DRAW
            } else Constants.DRAW
        }
    }
}