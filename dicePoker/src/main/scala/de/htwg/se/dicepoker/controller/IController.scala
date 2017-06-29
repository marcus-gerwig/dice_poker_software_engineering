package de.htwg.se.dicepoker.controller

import de.htwg.se.dicepoker.model.ITable
import de.htwg.se.dicepoker.model.tableComponent._
import de.htwg.se.dicepoker.util.Observable

/**
  * Created by Marcus on 29.06.2017.
  */
trait IController extends Observable {
  def createPlayers: Unit

  def menuNavigation: Unit

  def stopGameWanted: Unit

  def setPlayerName(index: Int, name: String)

  def printTable: Unit

  def beginRound: Unit

  def playerName(player: Player):String

  def newRound: Unit

  def getHighestBidPlayer: Player

  def getHighestBidResult: Result

  def whichPlayerFollows(startingPlayer: Player): Option[Player]

  def getLastLoser: Option[Player]

  def setLastLoser(ll: Option[Player]):Unit

  def playerResult(player: Player): Result

  def getPlayerStarted: Option[Player]

  def declareFirstBid(firstBid: String): Unit

  def startGame:Unit

  def restartGame:Unit

  def playerMistrusts

  def playerRaisesBid(playerRaises: Player)

  def printPlayer(player: Player)

  def declareHigherBid(higherBid: String, playerRaises: Player): Unit

  def getTable: ITable

}
