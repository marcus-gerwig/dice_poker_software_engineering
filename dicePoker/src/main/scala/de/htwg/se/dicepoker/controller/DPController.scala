package de.htwg.se.dicepoker.controller

import de.htwg.se.dicepoker.model.{ Bid, Player, PokerTable, Round }
import de.htwg.se.dicepoker.util._

//noinspection ScalaStyle
class DPController(var table: PokerTable) extends Observable {

  var lastLoser: Option[Player] = None
  var playerStarted: Option[Player] = None
  var playerFollowed: Player = null
  var lastUserInteraction: String = ""
  var currentRound: Round = null

  def setUserInteraction(text: String) = lastUserInteraction = text

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
    /*
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
*/
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
    /*
    do {
      notifyObservers(DeclareFirstBid)
      notifyObservers(Input)
    } while (!inputIsValid(lastUserInteraction, playerStarted.get))
    val bid = newBid(lastUserInteraction, playerStarted.get)
    currentRound = newRound(bid)
    continue
*/
  }

  def continue: Unit = {
    notifyObservers(AskIfMistrusts)
    /*
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
        notifyObservers(PlayerHasWonRound)
      }
    }
*/
  }

  def playerRaisesBid(playerRaises: Player) = {
    //currentRound = raiseBid(playerFollowed)
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
    lastLoser = if (playerStarted.get.equals(roundWinner)) Some(playerFollowed) else Some(playerStarted.get)
    if (lastLoser.get.equals(currentRound.highestBid.bidPlayer)) notifyObservers(PlayerWithHighestBidLied)
    else notifyObservers(PlayerWithHighestBidNotLied)
    PlayerHasWonRound.set(roundWinner)
    notifyObservers(PlayerHasWonRound)
  }

  /*
  def raiseBid(playerRaises: Player): Round = {
    notifyObservers(LineSeparator)
    var bid: Bid = null
    var newRound: Round = new Round()
    var inputCorrect = false
    do {
      PrintPlayer.set(playerRaises)
      notifyObservers(PrintPlayer)
      RequestHigherBid.set(playerRaises)
      notifyObservers(RequestHigherBid)

      notifyObservers(Input)
      if (inputIsValid(lastUserInteraction, playerRaises) && newBidIsHigher(lastUserInteraction)) {
        inputCorrect = true
        bid = newBid(lastUserInteraction, playerRaises)
        newRound = raiseHighestBid(bid)
      }
    } while (inputCorrect == false)
    newRound
  }
*/

  def newPlayer(name: String): Player = new Player(name)

  def newRound(highestBid: Bid): Round = new Round(highestBid)

  def raiseHighestBid(bid: Bid) = currentRound = currentRound.setHighestBid(bid)

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

  def decrementLoserDiceCount(winner: Player) = table = table.updateTable(table.players.filterNot { p => p.equals(winner) }.map { p => p.hasLostRound } :+ winner)

  def inputIsValid(input: String, player: Player): Boolean = new Bid().inputIsValidBid(input, player)

  def newBidIsHigher(input: String): Boolean = if (newBid(input, null).bidResult.isHigherThan(currentRound.highestBid.bidResult)) true else false

  def newBid(input: String, player: Player): Bid = new Bid(null, player).convertStringToBid(input)

  /*
  def setUserInteraction(input: String): Unit = {
    lastUserInteraction = input
    stopGameWanted
  }
*/

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
    val highestBid = currentRound.highestBid
    if (highestBid.bidResult.dieValue == 6 && highestBid.bidResult.frequency >= playerRaises.diceCount) true
    else false
  }

}

