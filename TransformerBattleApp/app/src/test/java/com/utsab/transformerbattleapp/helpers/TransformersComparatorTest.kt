package com.utsab.transformerbattleapp.helpers

import com.utsab.transformerbattleapp.models.Transformer
import org.junit.Assert.*
import org.junit.Test

class TransformersComparatorTest{

    var autobots = arrayListOf<Transformer>(
        Transformer(
            "12","Autobot 1",
            5,5,5,5,5,5,5,5,"A", ""
        ),
        Transformer(
            "122","Autobot 2",
            5,5,5,5,5,5,5,5,"A", ""
        )
    )

    var decepticon = arrayListOf<Transformer>(
        Transformer(
            "12","Decepticon 1",
            5,5,5,5,5,5,5,5,"D", ""
        ),
        Transformer(
            "122","Autobot 2",
            5,5,5,5,5,5,5,5,"D", ""
        )
    )

    @Test
    fun `Optimus prime or predaking wins automatically`(){
        autobots[0].name = "Optimus Prime"
        val result = TransformersComparator.decideWinner(autobots, decepticon)
        assertEquals(Constants.TEAM_AUTOBOT, result[0])
    }

    @Test
    fun `Optimus prime and predaking fight`(){
        autobots[0].name = "Optimus Prime"
        decepticon[0].name = "Predaking"
        val result = TransformersComparator.decideWinner(autobots, decepticon)
        assertEquals(0, result.size)
    }

    @Test
    fun `Higher Courage and strength Autobot`(){
        autobots[1].apply {
            courage = 9
            strength = 8
        }
        decepticon[1].apply {
            courage = 4
            strength = 4
        }
        val result = TransformersComparator.decideWinner(autobots, decepticon)
        assertEquals(Constants.TEAM_AUTOBOT, result[1])
    }

    @Test
    fun `4 Higher Courage and 3 strength Decepticon`(){
        decepticon[1].apply {
            courage = 9
            strength = 8
        }
        autobots[1].apply {
            courage = 4
            strength = 4
        }
        val result = TransformersComparator.decideWinner(autobots, decepticon)
        assertEquals(Constants.TEAM_DECEPTICON, result[1])
    }

    @Test
    fun `3 or more skill higher wins`(){
        decepticon[1].apply {
            skill = 8
        }
        autobots[1].apply {
            skill = 4
        }
        val result = TransformersComparator.decideWinner(autobots, decepticon)
        assertEquals(Constants.TEAM_DECEPTICON, result[1])
    }

    @Test
    fun `overall Rating higher wins`(){
        autobots[1].apply {
            strength = 1
            intelligence = 1
            speed = 1
            endurance = 1
            firepower = 1
        }
        decepticon[1].apply {
            strength = 2
            intelligence = 2
            speed = 2
            endurance = 2
            firepower = 2
        }
        val result = TransformersComparator.decideWinner(autobots, decepticon)
        assertEquals(Constants.TEAM_DECEPTICON, result[1])
    }

    @Test
    fun `overall Rating draw`(){
        decepticon[1].apply {
            strength = 2
            intelligence = 2
            speed = 1
            endurance = 1
            firepower = 1
        }
        autobots[1].apply {
            strength = 1
            intelligence = 1
            speed = 1
            endurance = 2
            firepower = 2
        }
        val result = TransformersComparator.decideWinner(autobots, decepticon)
        assertEquals(Constants.DRAW, result[1])
    }
}