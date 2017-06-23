package de.htwg.se.dicepoker.controller

import de.htwg.se.dicepoker.model.{ Bid, Player, PokerTable, Round }
import de.htwg.se.dicepoker.util._

//noinspection ScalaStyle
class DPController(var table: PokerTable) extends Observable {

  var lastLoser: Option[Player] = None
  var playerStarted: Option[Player] = None
  var playerFollowed: Option[Player] = None
  var currentRound: Option[Round] = None

  def createPlayers: Unit = {
    notifyObservers(WelcomeMsg)
    initPlayer
  }

  def initPlayer: Unit = {
    table = new PokerTable(Vector.empty)
    for (index <- 1 to AppConst.number_of_player) {
      EnterPlayerName.set(index)
      notifyObservers(EnterPlayerName)
    }
    notifyObservers(LetShowBegin)
  }

  def menuNavigation = {
    notifyObservers(ExplainCommands)
    notifyObservers(Input)
  }

  def startGame = {
    while (!gameIsOver) newRound
    val winner = whoWonTheGame
    GameIsOver.set(winner)
    notifyObservers(GameIsOver)
    notifyObservers(GameWasCancelled)
  }

  def rolling: Unit = {
    table = table.rollTheDice
    notifyObservers(DiceWereRollen)
  }

  def newRound: Unit = {
    rolling
    playerStarted = whichPlayerStarts
    playerFollowed = whichPlayerFollows(playerStarted.get)

    notifyObservers(NewRound)
    notifyObservers(DeclareFirstBid)
  }

  def continue: Unit = {
    notifyObservers(AskIfMistrusts)
  }

  def playerRaisesBid(playerRaises: Player) = {
    PrintPlayer.set(playerRaises)
    notifyObservers(PrintPlayer)
    if (higherBidIsNotPossible(playerRaises)) playerMistrusts
    else {
      RequestHigherBid.set(playerRaises)
      notifyObservers(RequestHigherBid)
      continue
    }
  }

  def playerMistrusts = {
    val roundWinner: Player = solveRound
    lastLoser = if (playerStarted.get.equals(roundWinner)) Some(playerFollowed.get) else Some(playerStarted.get)
    if (lastLoser.get.equals(currentRound.get.highestBid.bidPlayer)) notifyObservers(PlayerWithHighestBidLied)
    else notifyObservers(PlayerWithHighestBidNotLied)
    PlayerHasWonRound.set(roundWinner)
    notifyObservers(PlayerHasWonRound)
  }

  def newPlayer(name: String): Player = new Player(name)

  def newRound(highestBid: Bid): Option[Round] = Some(new Round(highestBid))

  def raiseHighestBid(bid: Bid) = currentRound = Some(currentRound.get.setHighestBid(bid))

  def getHighestBidResult = currentRound.get.highestBid.bidResult

  def getHighestBidPlayer: Player = currentRound.get.highestBid.bidPlayer

  def whichPlayerStarts: Option[Player] = if (lastLoser == None) Some(table.players(scala.util.Random.nextInt(table.players.length))) else Some(table.players.filter { p => p.equals(lastLoser.get) }.head)

  def whichPlayerFollows(startingPlayer: Player): Option[Player] = Some(table.players.filter { p => !p.equals(startingPlayer) }.head)

  def solveRound: Player = {
    val highestBidPlayer = currentRound.get.highestBid.bidPlayer
    val opponent = whichPlayerFollows(highestBidPlayer)
    val winner = currentRound.get.theRoundWins(opponent.get)
    decrementLoserDiceCount(winner)
    winner
  }

  def gameIsOver: Boolean = table.players.exists { p => p.hasLostGame }

  def whoWonTheGame: Player = table.players.filterNot { p => p.hasLostGame }.head

  def decrementLoserDiceCount(winner: Player) = table = table.updateTable(table.players.filterNot { p => p.equals(winner) }.map { p => p.hasLostRound } :+ winner)

  def inputIsValid(input: String, player: Player): Boolean = new Bid().inputIsValidBid(input, player)

  def newBidIsHigher(input: String): Boolean = if (newBid(input, null).bidResult.isHigherThan(currentRound.get.highestBid.bidResult)) true else false

  def newBid(input: String, player: Player): Bid = new Bid(null, player).convertStringToBid(input)

  def printTable = table.toString()

  def printPlayer(player: Player) = player.toString()

  def getLastLoser = lastLoser

  def playerName(player: Player) = player.name

  def playerResult(player: Player) = player.diceCup.getMaxResult()

  def getPlayerStarted: Option[Player] = playerStarted

  def stopGameWanted: Unit = {
    notifyObservers(GameWasCancelled);
    System.exit(0)
  }

  def setPlayerName(index: Int, name: String) = {
    var currPlayer: Vector[Player] = table.players
    val newPlayer: Player = new Player(name)
    currPlayer = currPlayer :+ newPlayer
    table = table.updateTable(currPlayer)
  }

  def declareFirstBid(firstBid: String): Unit = {
    if (inputIsValid(firstBid, playerStarted.get)) {
      val bid = newBid(firstBid, playerStarted.get)
      currentRound = newRound(bid)
      continue
    } else notifyObservers(DeclareFirstBid)

  }

  def declareHigherBid(higherBid: String, playerRaises: Player): Unit = {
    if (inputIsValid(higherBid, playerRaises) && newBidIsHigher(higherBid)) {
      var bid = newBid(higherBid, playerRaises)
      raiseHighestBid(bid)
    } else notifyObservers(RequestHigherBid)
  }

  def higherBidIsNotPossible(playerRaises: Player): Boolean = {
    val highestBid = currentRound.get.highestBid
    if (highestBid.bidResult.dieValue == 6 && highestBid.bidResult.frequency >= playerRaises.diceCount) true
    else false
  }

}

