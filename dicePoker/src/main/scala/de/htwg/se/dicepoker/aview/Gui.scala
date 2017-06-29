package de.htwg.se.dicepoker.aview


import scala.swing.Alignment
import scala.swing.Frame
import scala.swing.Label
import scala.swing.TextField
import scala.swing._
import javax.swing.ImageIcon

import Swing._
import java.awt.Color
import java.net.URL

import de.htwg.se.dicepoker.controller.IController
import de.htwg.se.dicepoker.controller.controllerComponent.DPController
import de.htwg.se.dicepoker.model.tableComponent._

import scala.language.postfixOps
import de.htwg.se.dicepoker.util.Observer
import de.htwg.se.dicepoker.util.Event
import de.htwg.se.dicepoker.util._

//noinspection ScalaStyle
class Gui(controller: IController) extends Frame with Observer {

  title = "Dice Poker"
  minimumSize = new Dimension(300, 100)
  controller.add(this)
  var roundNr = 1

  def setFont = new Font("Bodoni MT", 0, 20)


  menuBar = new MenuBar {
    contents += new Menu("File") {
      contents += new MenuItem(Action("Quit") {
        System.exit(0)
      })
      centerOnScreen
      visible = true
    }
  }

  val startButton = new Button(Action("Start 2 Players Game") {
    update(WelcomeMsg)
    update(EnterPlayerName)
  })
  val quitButton = new Button(Action("Quit") {
    System.exit(0)
  })
  contents = new FlowPanel() {
    contents += startButton
    contents += quitButton
  }

  def newLabel(newText: String): Label = new Label {
    text = newText
    font = setFont
  }

  def newTField(initialText: String) = new TextField {
    text = initialText
    columns = 5
    horizontalAlignment = Alignment.Right
    font = setFont
  }

  def restrictHeight(s: Component) {
    s.maximumSize = new Dimension(Short.MaxValue, s.preferredSize.height)
  }

  centerOnScreen
  visible = true

  override def update(e: Event): Unit = {
    e match {

      case WelcomeMsg =>
        println("GUI is starting")

      case EnterPlayerName => {
        val labelPlayer1 = newLabel("Hello Player 1 ! Please enter your name: ")
        val labelPlayer2 = newLabel("Hello Player 2 ! Please enter your name: ")
        val namePlayer1 = newTField("Mac")
        val namePlayer2 = newTField("Andi")
        val commitButton = new Button(Action("Continue") {
          controller.setPlayerName(0, namePlayer1.text)
          controller.setPlayerName(1, namePlayer2.text)
          controller.menuNavigation
        })
        restrictHeight(namePlayer1)

        contents = new BoxPanel(Orientation.Vertical) {
          border = Swing.EmptyBorder(10, 10, 10, 10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += labelPlayer1
            contents += Swing.HStrut(5)
            contents += namePlayer1
          }
          contents += Swing.VStrut(5)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += labelPlayer2
            contents += Swing.HStrut(5)
            contents += namePlayer2
          }
          contents += Swing.VStrut(5)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += commitButton
          }
          for (e <- contents)
            e.xLayoutAlignment = 0.0
        }
      }
      case ExplainCommands => controller.newRound
      case DiceWereRollen =>
        controller.getLastLoser match {
          case None => {
            title = "Dice were roled ..."
            val player1 = controller.getTable.players(0)
            val player2 = controller.getTable.players(1)
            contents = new BoxPanel(Orientation.Vertical) {
              border = Swing.EmptyBorder(10, 10, 10, 10)
              contents += new BoxPanel(Orientation.Horizontal) {

                contents += newLabel(controller.playerName(player1) + ": ")
                for (i <- 0 until player1.diceCount) yield contents += picSelection(i, player1)
              }
              contents += Swing.VStrut(10)
              contents += new BoxPanel(Orientation.Horizontal) {
                contents += newLabel(controller.playerName(player2) + ": ")
                for (i <- 0 until player2.diceCount) yield contents += picSelection(i, player2)
              }
              contents += Swing.VStrut(10)
              contents += new BoxPanel(Orientation.Horizontal) {
                contents += new Button(Action("Start Round") {
                  controller.beginRound
                })
              }
            }
          }
          case Some(lastLoser) => {
            title = "Dice were roled ..."
            val player1 = controller.getTable.players(0)
            val player2 = controller.getTable.players(1)
            contents = new BoxPanel(Orientation.Vertical) {
              border = Swing.EmptyBorder(10, 10, 10, 10)
              contents += new BoxPanel(Orientation.Horizontal) {
                background = Color.ORANGE
                contents += newLabel(controller.playerName(player1) + ": ")
                for (i <- 0 until player1.diceCount) yield contents += picSelection(i, player1)

              }
              contents += Swing.VStrut(10)
              contents += new BoxPanel(Orientation.Horizontal) {
                contents += newLabel(controller.playerName(player2) + ": ")
                for (i <- 0 until player2.diceCount) yield contents += picSelection(i, player2)
              }
              contents += Swing.VStrut(10)
              contents += new BoxPanel(Orientation.Horizontal) {
                contents += new Button(Action("Start Round") {
                  controller.beginRound
                })
              }
            }
          }
        }

      case NewRound => title = "Round Nr." + roundNr
      case DeclareFirstBid =>
        controller.getPlayerStarted match {
          case None =>
          case Some(playerStarted) => {
            val inputField = newTField("")
            contents = new BoxPanel(Orientation.Vertical) {
              border = Swing.EmptyBorder(10, 10, 10, 10)

              contents += new BoxPanel(Orientation.Horizontal) {
                contents += newLabel(controller.playerName(playerStarted) + ": ")
                contents += Swing.HStrut(5)
                for (i <- 0 until playerStarted.diceCount) yield contents += picSelection(i, playerStarted)
              }
              contents += Swing.VStrut(10)
              contents += new BoxPanel(Orientation.Horizontal) {
                contents += newLabel(controller.playerName(playerStarted) + ", please declare the first bid (e.g. 3,2 means your bid is a double of 3):")
                contents += Swing.HStrut(5)
                contents += inputField
              }
              contents += Swing.VStrut(10)
              contents += new BoxPanel(Orientation.Horizontal) {
                contents += Swing.HGlue
                contents += Swing.HGlue
                contents += new Button(Action("Declare Bid") {
                  controller.declareFirstBid(inputField.text)
                })
              }
              for (e <- contents)
                e.xLayoutAlignment = 0.0
            }
          }
        }
      case AskIfMistrusts => {
        val player = controller.whichPlayerFollows(controller.getHighestBidPlayer).get
        val playerWithHighestBid = controller.getHighestBidPlayer
        contents = new BoxPanel(Orientation.Vertical) {
          border = Swing.EmptyBorder(10, 10, 10, 10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += newLabel("Highest bid at the moment from " + controller.playerName(playerWithHighestBid) + " = ")
            contents += Swing.HStrut(5)
            for (i <- 0 until controller.getHighestBidResult.frequency) yield contents += picSelection(i, controller.getHighestBidResult)

          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += newLabel("Now it's your turn " + controller.playerName(player))
            contents += Swing.HStrut(5)
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += Swing.HStrut(10)
            contents += new Button(Action("Mistrust") {
              controller.playerMistrusts
            })
            contents += Swing.HStrut(10)
            contents += new Button(Action("Raise Bid") {
              controller.playerRaisesBid(player)
            })
          }
          for (e <- contents)
            e.xLayoutAlignment = 0.0
        }
      }
      case RequestHigherBid => {
        val player: Player = RequestHigherBid.attachment.asInstanceOf[Player]
        val playerWithHighestBid = controller.getHighestBidPlayer
        val inputField = newTField("")
        contents = new BoxPanel(Orientation.Vertical) {
          border = Swing.EmptyBorder(10, 10, 10, 10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += newLabel("Highest bid at the moment from " + controller.playerName(playerWithHighestBid) + " = " + controller.getHighestBidResult)
            contents += Swing.HStrut(5)
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += newLabel(controller.playerName(player) + ": ")
            contents += Swing.HStrut(5)
            for (i <- 0 until player.diceCount) yield contents += picSelection(i, player)
            /*for (index <- 0 until player.diceCount) {
              contents += newLabel("" + player.diceCup.dieCombi(index))
              contents += Swing.HStrut(5)
            }*/
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += newLabel(controller.playerName(player) + ", please declare your bid :")
            contents += Swing.HStrut(5)
            contents += inputField
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += Swing.HGlue
            contents += Swing.HGlue
            contents += new Button(Action("Declare Bid") {
              controller.declareHigherBid(inputField.text, player)
            })
          }
          for (e <- contents)
            e.xLayoutAlignment = 0.0
        }
      }
      case PlayerWithHighestBidLied => {
        roundNr += 1
        val winner = controller.whichPlayerFollows(controller.getLastLoser.get)
        val winnerName = controller.playerName(winner.get)
        val loser = controller.getLastLoser.get
        val loserName = controller.playerName(loser)
        contents = new BoxPanel(Orientation.Vertical) {
          border = Swing.EmptyBorder(10, 10, 10, 10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += newLabel(loserName + " lied. His actual result was " + controller.playerResult(loser) + ".")
            contents += Swing.HStrut(5)
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += newLabel(winnerName + " has won this round!")
            contents += Swing.HStrut(5)
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += new Button(Action("Continue") {
              controller.newRound
            })
          }
        }
      }
      case PlayerWithHighestBidNotLied => {
        roundNr += 1
        val winner = controller.whichPlayerFollows(controller.getLastLoser.get)
        val winnerName = controller.playerName(winner.get)
        contents = new BoxPanel(Orientation.Vertical) {
          border = Swing.EmptyBorder(10, 10, 10, 10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += newLabel(winnerName + " did not lie. His actual result was " + controller.playerResult(winner.get) + ".")
            contents += Swing.HStrut(5)
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += newLabel(winnerName + " has won this round!")
            contents += Swing.HStrut(5)
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += new Button(Action("Continue") {
              controller.newRound
            })
          }
        }
      }
      case GameIsOver => {
        title = "Game Over"
        val winner = controller.playerName(GameIsOver.attachment.asInstanceOf[Player])
        contents = new BoxPanel(Orientation.Vertical) {
          border = Swing.EmptyBorder(10, 10, 10, 10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += newLabel("...and the winner is " + winner + "!  " + "Congratulations, " + winner + "!")
            contents += Swing.HStrut(5)
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += new Button(Action("New Game") {
              controller.setLastLoser(None)
              roundNr = 1
              update(EnterPlayerName)
            })
            contents += Swing.HStrut(5)
            contents += Swing.HGlue
            contents += new Button(Action("Restart") {
              roundNr = 1
              controller.restartGame
              controller.menuNavigation
            })
            contents += Swing.HStrut(5)
            contents += Swing.HGlue
            contents += new Button(Action("Quit") {
              System.exit(0)
            })
          }
        }
      }
      case Input =>
      case LineSeparator =>
      case PrintPlayer =>
      case PlayerHasWonRound =>
      case GameWasCancelled =>
      case LetShowBegin =>
    }
  }


  def picSelection(i: Int, p: Player): Label = {

    var picPath: java.net.URL = null

    val d = p.diceCup.dieCombi(i)

    d match {
      case 1 => picPath = getClass.getResource("/Zahl1.png")
      case 2 => picPath = getClass.getResource("/Zahl2.png")
      case 3 => picPath = getClass.getResource("/Zahl3.png")
      case 4 => picPath = getClass.getResource("/Zahl4.png")
      case 5 => picPath = getClass.getResource("/Zahl5.png")
      case 6 => picPath = getClass.getResource("/Zahl6.png")
      case _ => picPath = getClass.getResource("/Empty.png")
    }

    val pic = new Label {
      icon = new ImageIcon(picPath)
    }
    pic
  }

  def picSelection(i: Int, p: Result): Label = {

    var picPath: java.net.URL = null

    val d = p.dieValue

    d match {
      case 1 => picPath = getClass.getResource("/Zahl1.png")
      case 2 => picPath = getClass.getResource("/Zahl2.png")
      case 3 => picPath = getClass.getResource("/Zahl3.png")
      case 4 => picPath = getClass.getResource("/Zahl4.png")
      case 5 => picPath = getClass.getResource("/Zahl5.png")
      case 6 => picPath = getClass.getResource("/Zahl6.png")
      case _ => picPath = getClass.getResource("/Empty.png")
    }

    val pic = new Label {
      icon = new ImageIcon(picPath)
    }
    pic
  }

}