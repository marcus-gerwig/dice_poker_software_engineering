package de.htwg.se.dicepoker.controller

import de.htwg.se.dicepoker.model.{ PokerTable, Player, Bid }
import de.htwg.se.dicepoker.util.Observable
import de.htwg.se.dicepoker.model.Round
import de.htwg.se.dicepoker.util.DiceWereRollen
import de.htwg.se.dicepoker.util.PlayerHasWon
import de.htwg.se.dicepoker.util.PlayerWithHighestBidLied
import de.htwg.se.dicepoker.util.PlayerWithHighestBidNotLied

class DPController(var table: PokerTable) extends Observable {

  def startGame(players: Vector[Player]): Unit = {
    table = new PokerTable(players)
  }

  def rolling: Unit = {
    table = table.rollTheDice
    notifyObservers(DiceWereRollen)
  }

  def newPlayer(name: String): Player = new Player(name)
  def newRound(highestBid: Bid): Round = new Round(highestBid)
  def raiseHighestBid(bid: Bid, round: Round): Round = if (bid.bidResult.isHigherThan(round.highestBid.bidResult)) round.setHighestBid(bid); else null;
  def getHighestBid(round: Round) = round.highestBid
  def getHighestBidResult(round: Round) = getHighestBid(round).bidResult
  def getHighestBidPlayer(round: Round) = getHighestBid(round).bidPlayer

  def whichPlayerStarts(loserLastRound: Player): Player = if(loserLastRound==null) table.players(scala.util.Random.nextInt(table.players.length)) else loserLastRound

  def whichPlayerFollows(startingPlayer: Player): Player = {
    var playerFollows = new Player(null)
    for (p <- table.players) {
      if (!startingPlayer.equals(p)) playerFollows = p
    }
    playerFollows
  }

  def solveRound(round: Round): Player = {
    val highestBidPlayer = round.highestBid.bidPlayer
    val opponent = whichPlayerFollows(highestBidPlayer)
    val winner = round.theRoundWins(opponent)
    decrementLoserDiceCount(winner)
    winner
  }
  def playerLied:Unit = notifyObservers(PlayerWithHighestBidLied)
  def playerDidNotLie = notifyObservers(PlayerWithHighestBidNotLied)
  def decrementLoserDiceCount(winner: Player) = table = table.updateTable(table.players.filterNot { p => p.equals(winner) }.map { p => p.hasLostRound } :+ winner)

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
    notifyObservers(PlayerHasWon)
    winner
  }

  def bidIsValid(input: String, player: Player): Boolean = new Bid().inputIsValidBid(input, player)
  def newBid(input: String, player: Player): Bid = new Bid(null, player).convertStringToBid(input)
  def playerResult(player:Player) = player.diceCup.getMaxResult()
}