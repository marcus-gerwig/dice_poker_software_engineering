package de.htwg.se.dicepoker.controller

import de.htwg.se.dicepoker.model.{ PokerTable, Player, Bid, Round }
import de.htwg.se.dicepoker.util.Observable
import de.htwg.se.dicepoker.util.DiceWereRollen
import de.htwg.se.dicepoker.util.PlayerHasWonRound
import de.htwg.se.dicepoker.util.PlayerWithHighestBidLied
import de.htwg.se.dicepoker.util.PlayerWithHighestBidNotLied
import de.htwg.se.dicepoker.util.NewRound
import de.htwg.se.dicepoker.util.DeclareFirstBid
import de.htwg.se.dicepoker.util.Input
import de.htwg.se.dicepoker.util.AskIfMistrusts
import de.htwg.se.dicepoker.util.PrintPlayer
import de.htwg.se.dicepoker.util.RequestHigherBid
import de.htwg.se.dicepoker.util.ExplainCommands
import de.htwg.se.dicepoker.util.AppConst
import de.htwg.se.dicepoker.util.EnterPlayerName
import de.htwg.se.dicepoker.util.LetShowBegin
import de.htwg.se.dicepoker.util.GameWasCancelled
import de.htwg.se.dicepoker.util.GameIsOver
import de.htwg.se.dicepoker.util.WelcomeMsg

class DPController(var table: PokerTable) extends Observable {

  var lastLoser: Player = null
  var playerStarted: Player = null
  var playerFollowed: Player = null
  var lastUserInteraction: String = null
  var currentRound: Round = null

  def createPlayers: Unit = {
    notifyObservers(WelcomeMsg)
    table = new PokerTable(initPlayer)

  }

  def initPlayer = {
    var players: Vector[Player] = Vector.empty
    var index = 0
    for (i <- 1 to AppConst.number_of_player) {
      index = i
      EnterPlayerName.set(index)
      notifyObservers(EnterPlayerName)
      players = players :+ newPlayer(lastUserInteraction)
    }
    notifyObservers(LetShowBegin)
    players
  }

  def menuNavigation = {
    notifyObservers(ExplainCommands)
    notifyObservers(Input)
    lastUserInteraction match {
      case "q" => {
        setUserInteraction("Q")
      }
      case "s" => {
        while (!gameIsOver) newRound
        val winner = whoWonTheGame
        GameIsOver.set(winner)
        notifyObservers(GameIsOver)
        notifyObservers(GameWasCancelled)
      }
      case "r" =>
    }
  }

  def rolling: Unit = {
    table = table.rollTheDice
    notifyObservers(DiceWereRollen)
  }

  def newRound: Unit = {
    rolling
    playerStarted = whichPlayerStarts
    playerFollowed = whichPlayerFollows(playerStarted)

    notifyObservers(NewRound)
    do {
      notifyObservers(DeclareFirstBid)
      notifyObservers(Input)
    } while (!inputIsValid(lastUserInteraction, playerStarted))
    val bid = newBid(lastUserInteraction, playerStarted)
    currentRound = newRound(bid)
    continue
  }

  def continue: Unit = {
    notifyObservers(AskIfMistrusts)
    lastUserInteraction match {
      case "b" => {
        currentRound = raiseBid(playerFollowed)
        continue
      }
      case "m" => {
        val roundWinner: Player = solveRound
        lastLoser = if (playerStarted.eq(roundWinner)) playerFollowed else playerStarted
        if (lastLoser.equals(currentRound.highestBid.bidPlayer)) playerLied
        else playerDidNotLie
        PlayerHasWonRound.set(roundWinner)
        notifyObservers(PlayerHasWonRound)
      }
    }
  }

  def raiseBid(playerRaises: Player): Round = {
    println("->->->->->->->->->->->->")
    var bid: Bid = null
    var newRound: Round = new Round()
    do {
      PrintPlayer.set(playerRaises)
      notifyObservers(PrintPlayer)
      RequestHigherBid.set(playerRaises)
      notifyObservers(RequestHigherBid)
      notifyObservers(Input)
      if (inputIsValid(lastUserInteraction, playerRaises) && newBidIsHigher(lastUserInteraction)) {
        bid = newBid(lastUserInteraction, playerRaises)
        newRound = raiseHighestBid(bid)
      }
    } while (getHighestBid(newRound) == null)
    newRound
  }

  def newPlayer(name: String): Player = new Player(name)
  def newRound(highestBid: Bid): Round = new Round(highestBid)
  //obs
  def raiseHighestBid(bid: Bid, round: Round): Round = if (bid.bidResult.isHigherThan(round.highestBid.bidResult)) round.setHighestBid(bid); else null;
  def raiseHighestBid(bid: Bid): Round = if (bid.bidResult.isHigherThan(currentRound.highestBid.bidResult)) currentRound.setHighestBid(bid); else null;
  //obs
  def getHighestBid(round: Round) = round.highestBid
  //obs
  def getHighestBidResult(round: Round) = getHighestBid(round).bidResult
  def getHighestBidResult = currentRound.highestBid.bidResult
  //obs
  def getHighestBidPlayer(round: Round) = getHighestBid(round).bidPlayer
  def getHighestBidPlayer: Player = getHighestBid(currentRound).bidPlayer

  def whichPlayerStarts(loserLastRound: Player): Player = if (loserLastRound == null) table.players(scala.util.Random.nextInt(table.players.length)) else loserByName(loserLastRound)
  def whichPlayerStarts: Player = if (lastLoser == null) table.players(scala.util.Random.nextInt(table.players.length)) else loserByName(lastLoser)
  def loserByName(loserLastRound: Player): Player = table.getPlayerByName(loserLastRound.name)
  def whichPlayerFollows(startingPlayer: Player): Player = {
    var playerFollows = new Player(null)
    for (p <- table.players) {
      if (!startingPlayer.equals(p)) playerFollows = p
    }
    playerFollows
  }

  def solveRound: Player = {
    val highestBidPlayer = currentRound.highestBid.bidPlayer
    val opponent = whichPlayerFollows(highestBidPlayer)
    val winner = currentRound.theRoundWins(opponent)
    decrementLoserDiceCount(winner)
    winner
  }
  def playerLied: Unit = notifyObservers(PlayerWithHighestBidLied)
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
    winner
  }

  def inputIsValid(input: String, player: Player): Boolean = new Bid().inputIsValidBid(input, player)
  //obs
  def newBidIsHigher(input: String, round: Round): Boolean = if (newBid(input, null).bidResult.isHigherThan(round.highestBid.bidResult)) true else false
  def newBidIsHigher(input: String): Boolean = if (newBid(input, null).bidResult.isHigherThan(currentRound.highestBid.bidResult)) true else false
  def newBid(input: String, player: Player): Bid = new Bid(null, player).convertStringToBid(input)
  def playerResult(player: Player) = player.diceCup.getMaxResult()
  def getPlayerStarted: Player = playerStarted
  def getPlayerFollowed: Player = playerFollowed
  def playerName(player: Player) = player.name
  def getCurrentRound: Round = currentRound

  def setUserInteraction(input: String): Unit = {
    lastUserInteraction = input
    stopGameWanted
  }
  def printTable = table.toString()
  def printPlayer(player: Player) = player.toString()
  def getLastLoser = lastLoser

  def stopGameWanted: Unit = if (lastUserInteraction == "Q") { notifyObservers(GameWasCancelled); System.exit(0) }
}