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
import de.htwg.se.dicepoker.util.LineSeparator
import scala.swing.Publisher

class DPController(var table: PokerTable) extends Observable {

  var lastLoser: Option[Player] = None
  var playerStarted: Option[Player] = None
  var playerFollowed: Player = null
  var lastUserInteraction: String = ""
  var currentRound: Round = null

  def createPlayers: Unit = {
//    publish(new WelcomeMsg)
    notifyObservers(WelcomeMsg)
    table = new PokerTable(initPlayer)
  }
  
  

  def initPlayer = {
    var players: Vector[Player] = Vector.empty
 
    for (index <- 1 to AppConst.number_of_player) {

      EnterPlayerName.set(index)
//      publish(EnterPlayerName)
      
      
      
      notifyObservers(EnterPlayerName)
      while(lastUserInteraction == ""){
      
      }
      
      players = players :+ newPlayer(lastUserInteraction)
    }
//    publish(LetShowBegin)
    notifyObservers(LetShowBegin)
    players
  }

  def menuNavigation = {
//    publish(ExplainCommands)
    notifyObservers(ExplainCommands)
//    publish(Input)
    notifyObservers(Input)
    lastUserInteraction match {
      case "q" => {
        setUserInteraction("Q")
      }
      case "s" => {
        while (!gameIsOver) newRound
        val winner = whoWonTheGame
        GameIsOver.set(winner)
//        publish(GameIsOver)
        notifyObservers(GameIsOver)
//        publish(GameWasCancelled)
        notifyObservers(GameWasCancelled)
      }
      case "r" =>
    }
  }

  def rolling: Unit = {
    table = table.rollTheDice
//    publish(DiceWereRollen)
    notifyObservers(DiceWereRollen)
  }
  

  def newRound: Unit = {
    rolling
    playerStarted = whichPlayerStarts
    playerFollowed = whichPlayerFollows(playerStarted.get)

//    publish(NewRound)
    notifyObservers(NewRound)
    do {
//      publish(DeclareFirstBid)
      notifyObservers(DeclareFirstBid)
//      publish(Input)
      notifyObservers(Input)
    } while (!inputIsValid(lastUserInteraction, playerStarted.get))
    val bid = newBid(lastUserInteraction, playerStarted.get)
    currentRound = newRound(bid)
    continue
  }

  def continue: Unit = {
//    publish(AskIfMistrusts)
    notifyObservers(AskIfMistrusts)
    lastUserInteraction match {
      case "b" => {
        currentRound = raiseBid(playerFollowed)
        continue
      }
      case "m" => {
        val roundWinner: Player = solveRound
        lastLoser = if (playerStarted.eq(roundWinner)) Some(playerFollowed) else Some(playerStarted.get)
        if (lastLoser.get.equals(currentRound.highestBid.bidPlayer)) playerLied
        else playerDidNotLie
        PlayerHasWonRound.set(roundWinner)
//        publish(PlayerHasWonRound)
        notifyObservers(PlayerHasWonRound)
      }
    }
  }

  def raiseBid(playerRaises: Player): Round = {
//    publish(LineSeparator)
    notifyObservers(LineSeparator)
    var bid: Bid = null
    var newRound: Round = new Round()
    var inputCorrect = false
    do {
      PrintPlayer.set(playerRaises)
//      publish(PrintPlayer)
      notifyObservers(PrintPlayer)
      RequestHigherBid.set(playerRaises)
//      publish(RequestHigherBid)
      notifyObservers(RequestHigherBid)
//      publish(Input)
      notifyObservers(Input)
      if (inputIsValid(lastUserInteraction, playerRaises) && newBidIsHigher(lastUserInteraction)) {
        inputCorrect = true
        bid = newBid(lastUserInteraction, playerRaises)
        newRound = raiseHighestBid(bid)
      }
    } while (inputCorrect == false)
    newRound
  }

  def newPlayer(name: String): Player = new Player(name)
  def newRound(highestBid: Bid): Round = new Round(highestBid)
  def raiseHighestBid(bid: Bid): Round = currentRound.setHighestBid(bid)

  def getHighestBidResult = currentRound.highestBid.bidResult
  def getHighestBidPlayer: Player = currentRound.highestBid.bidPlayer

  def whichPlayerStarts: Option[Player] = if (lastLoser == None) Some(table.players(scala.util.Random.nextInt(table.players.length))) else Some(table.players.filter { p => p.equals(lastLoser.get) }.head)
  def whichPlayerFollows(startingPlayer: Player): Player = table.players.filter { p => !p.equals(startingPlayer) }.head

  def solveRound: Player = {
    val highestBidPlayer = currentRound.highestBid.bidPlayer
    val opponent = whichPlayerFollows(highestBidPlayer)
    val winner = currentRound.theRoundWins(opponent)
    decrementLoserDiceCount(winner)
    winner
  }
  def gameIsOver: Boolean = table.players.exists { p => p.hasLostGame }
  def whoWonTheGame: Player = table.players.filterNot { p => p.hasLostGame }.head
  def playerLied: Unit = 
//    publish(PlayerWithHighestBidLied)
    notifyObservers(PlayerWithHighestBidLied)
  def playerDidNotLie = 
//    publish(PlayerWithHighestBidNotLied)
    notifyObservers(PlayerWithHighestBidNotLied)
  def decrementLoserDiceCount(winner: Player) = table = table.updateTable(table.players.filterNot { p => p.equals(winner) }.map { p => p.hasLostRound } :+ winner)

  def inputIsValid(input: String, player: Player): Boolean = new Bid().inputIsValidBid(input, player)
  def newBidIsHigher(input: String): Boolean = if (newBid(input, null).bidResult.isHigherThan(currentRound.highestBid.bidResult)) true else false
  def newBid(input: String, player: Player): Bid = new Bid(null, player).convertStringToBid(input)

  def setUserInteraction(input: String): Unit = {
    lastUserInteraction = input
    stopGameWanted
  }

  def printTable = table.toString()
  def printPlayer(player: Player) = player.toString()
  def getLastLoser = lastLoser
  def playerName(player: Player) = player.name
  def playerResult(player: Player) = player.diceCup.getMaxResult()
  def getPlayerStarted: Option[Player] = playerStarted
  def stopGameWanted: Unit = if (lastUserInteraction == "Q") {
//    publish (GameWasCancelled)
    notifyObservers(GameWasCancelled); 
    System.exit(0) }
  
  
  //methods added from Andreas Graule
  
   
   
  def newRoundGUI: Unit = {
    rolling
    playerStarted = whichPlayerStarts
    playerFollowed = whichPlayerFollows(playerStarted.get)
  }
  
  def getPlayerFollowed: Player = playerFollowed
}

