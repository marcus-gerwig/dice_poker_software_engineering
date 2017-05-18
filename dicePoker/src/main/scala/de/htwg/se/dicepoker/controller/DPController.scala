package de.htwg.se.dicepoker.controller

import de.htwg.se.dicepoker.model.{ PokerTable, Player, Bid }
import de.htwg.se.dicepoker.util.Observable
import de.htwg.se.dicepoker.model.Round
import de.htwg.se.dicepoker.util.DiceWereRollen
import de.htwg.se.dicepoker.util.PlayerHasWon

class DPController(var table: PokerTable) extends Observable {

  def startGame(players: Vector[Player]): Unit = {
    table = new PokerTable(players)
  }

  def rolling: Unit = {
    table = table.rollTheDice
    notifyObservers(PlayerHasWon)
  }

  def newPlayer(name: String): Player = new Player(name)
  def newRound(highestBid: Bid): Round = new Round(highestBid)
  def raiseHighestBid(bid: Bid, round: Round): Round = round.setHighestBid(bid)
  def getHighestBid(round: Round) = round.highestBid
  def whichPlayerStarts: Player = table.players(scala.util.Random.nextInt(table.players.length))
  def whichPlayerFollows(startingPlayer: Player): Player = {
    var playerFollows = new Player(null)
    for (p <- table.players) {
      if (!startingPlayer.equals(p)) playerFollows = p
    }
    playerFollows
  }

  def solveRound(round:Round): Player = {
    val highestBidPlayer = round.highestBid.bidPlayer
    val opponent = whichPlayerFollows(highestBidPlayer)
    round.theRoundWins(opponent)
  }

  def gameIsOver: Boolean = {
    for (p <- table.players) {
      if (p.hasLostGame) return true
    }
    return false
  }

  def whoWonTheGame: Player = {
    var winner = new Player()
    for (p <- table.players) {
      if (!p.hasLostGame) winner = p
    }
    winner
  }

  def bidIsValid(input: String): Boolean = new Bid().inputIsValidBid(input)
  def newBid(input: String, player: Player): Bid = new Bid(null, player).convertStringToBid(input)

}