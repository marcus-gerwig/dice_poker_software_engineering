package de.htwg.se.dicepoker.model.tableComponent

import org.scalatest.WordSpec
import org.scalatest.Matchers
import de.htwg.se.dicepoker.model.playerComponent.Player

class PlayerSpec extends WordSpec with Matchers {
  "A Player" can {

    val aDiceCup = DiceCup(List(1, 2, 3, 4, 5))
    var aPlayer = new Player("Testplayer", 5, aDiceCup)

    "lose Round" in {

      aPlayer.hasLostRound should be(aPlayer.copy(aPlayer.name, aPlayer.diceCount - 1, aPlayer.diceCup));
    }
    "lose the Game" in {

      aPlayer.hasLostGame should be(false)

      aPlayer = aPlayer.copy(aPlayer.name, 0, new DiceCup(Nil))
      aPlayer.hasLostGame should be (true)
    }

  }

}