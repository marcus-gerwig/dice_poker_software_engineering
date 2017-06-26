package de.htwg.se.dicepoker.aview

import scala.swing.event._
import de.htwg.se.dicepoker.controller.DPController
import javax.swing.text.html.FrameSetView
import java.io.InputStream

import scala.swing.Alignment
import scala.swing.Frame
import scala.swing.GridPanel
import scala.swing.Label
import scala.swing.TextField
import scala.swing._
import javax.swing.ImageIcon
import javax.swing.border.EmptyBorder
import javax.imageio.ImageIO

import Swing._
import java.awt.Color
import java.net.URL

import de.htwg.se.dicepoker.model.{Bid, Player, PokerTable, Round}

import scala.language.postfixOps
import de.htwg.se.dicepoker.util.Observer

import scala.compat.Platform.EOL
import de.htwg.se.dicepoker.util.Event
import de.htwg.se.dicepoker.util._

//noinspection ScalaStyle
class Gui(controller: DPController) extends Frame with Observer {

  title = "Dice Poker"
  minimumSize = new Dimension(300, 100)
  controller.add(this)
  var roundNr = 1

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


  def newIField = new TextField {
    text = ""
    columns = 3
    horizontalAlignment = Alignment.Right
  }

  val bidInput = newIField

  def newTField(initialText: String) = new TextField {
    text = initialText
    columns = 5
    horizontalAlignment = Alignment.Right
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
        val labelPlayer1 = new Label("Hello Player 1 ! Please enter your name: ")
        val labelPlayer2 = new Label("Hello Player 2 ! Please enter your name: ")
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
      case LetShowBegin => println("Spielstart")
      case ExplainCommands => controller.newRound
      case DiceWereRollen =>
        controller.getLastLoser match {
          case None => {
            title = "Dice were roled ..."
            val player1 = controller.table.players(0)
            val player2 = controller.table.players(1)
            contents = new BoxPanel(Orientation.Vertical) {
              border = Swing.EmptyBorder(10, 10, 10, 10)
              contents += new BoxPanel(Orientation.Horizontal) {
                //for(i <- 0 until playerStarted.diceCount) yield contents += picSelection(i, playerStarted)
                /*contents += picSelection(0, playerStarted)
              contents += picSelection(1, playerStarted)
              contents += picSelection(1, playerStarted)*/
                contents += new Label(controller.playerName(player1) + ": ")
                for (index <- 0 until player1.diceCount) {
                  contents += new Label("" + player1.diceCup.dieCombi(index))
                  contents += Swing.HStrut(5)
                }

              }
              contents += Swing.VStrut(10)
              contents += new BoxPanel(Orientation.Horizontal) {
                contents += new Label(controller.playerName(player2) + ": ")
                for (index <- 0 until player2.diceCount) {
                  contents += new Label("" + player2.diceCup.dieCombi(index))
                  contents += Swing.HStrut(5)
                }
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
            val player1 = controller.table.players(0)
            val player2 = controller.table.players(1)
            contents = new BoxPanel(Orientation.Vertical) {
              border = Swing.EmptyBorder(10, 10, 10, 10)
              contents += new BoxPanel(Orientation.Horizontal) {
                background = Color.ORANGE
                //for(i <- 0 until playerStarted.diceCount) yield contents += picSelection(i, playerStarted)
                /*contents += picSelection(0, playerStarted)
              contents += picSelection(1, playerStarted)
              contents += picSelection(1, playerStarted)*/
                contents += new Label{
                  text = controller.playerName(player1) + ": "
                }
                for (index <- 0 until player1.diceCount) {
                  contents += new Label("" + player1.diceCup.dieCombi(index))
                  contents += Swing.HStrut(5)
                }

              }
              contents += Swing.VStrut(10)
              contents += new BoxPanel(Orientation.Horizontal) {
                contents += new Label(controller.playerName(player2) + ": ")
                for (index <- 0 until player2.diceCount) {
                  contents += new Label("" + player2.diceCup.dieCombi(index))
                  contents += Swing.HStrut(5)
                }
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

      case NewRound => title = "Round Nr."+roundNr
      case DeclareFirstBid =>
        controller.getPlayerStarted match {
          case None =>
          case Some(playerStarted) => {
            val inputField = newIField
            contents = new BoxPanel(Orientation.Vertical) {
              border = Swing.EmptyBorder(10, 10, 10, 10)

              contents += new BoxPanel(Orientation.Horizontal) {
                contents += new Label(controller.playerName(playerStarted) + ": ")
                contents += Swing.HStrut(5)
                for (index <- 0 until playerStarted.diceCount) {
                  contents += new Label("" + playerStarted.diceCup.dieCombi(index))
                  contents += Swing.HStrut(5)
                }
              }
              contents += Swing.VStrut(10)
              contents += new BoxPanel(Orientation.Horizontal) {
                contents += new Label(controller.playerName(playerStarted) + ", please declare the first bid (e.g. 3,2 means your bid is a double of 3):")
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
            //new FlowPanel(new Label(playerStarted.name + ": "), picSelection(0, playerStarted), picSelection(1, playerStarted), picSelection(2, playerStarted), new Button(Action("Continue") {}))
          }
        }
      case LineSeparator =>
      case Input =>
      case AskIfMistrusts => {
        val player = controller.whichPlayerFollows(controller.getHighestBidPlayer).get
        val playerWithHighestBid = controller.getHighestBidPlayer
        contents = new BoxPanel(Orientation.Vertical) {
          border = Swing.EmptyBorder(10, 10, 10, 10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += new Label("Highest bid at the moment from " + controller.playerName(playerWithHighestBid) + " = " + controller.getHighestBidResult)
            contents += Swing.HStrut(5)
            /*for (index <- 0 until playerStarted.diceCount) {
              contents += new Label("" + playerStarted.diceCup.dieCombi(index))
              contents += Swing.HStrut(5)
            }*/
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += new Label("Now it's your turn " + controller.playerName(player))
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
      case PrintPlayer =>
      case RequestHigherBid => {
        val player: Player = RequestHigherBid.attachment.asInstanceOf[Player]
        val playerWithHighestBid = controller.getHighestBidPlayer
        val inputField = newIField
        contents = new BoxPanel(Orientation.Vertical) {
          border = Swing.EmptyBorder(10, 10, 10, 10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += new Label("Highest bid at the moment from " + controller.playerName(playerWithHighestBid) + " = " + controller.getHighestBidResult)
            contents += Swing.HStrut(5)
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += new Label(controller.playerName(player) + ": ")
            contents += Swing.HStrut(5)
            for (index <- 0 until player.diceCount) {
              contents += new Label("" + player.diceCup.dieCombi(index))
              contents += Swing.HStrut(5)
            }
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += new Label(controller.playerName(player) + ", please declare your bid :")
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
      case PlayerHasWonRound =>
      case PlayerWithHighestBidLied => {
        roundNr +=1
        val winner = controller.whichPlayerFollows(controller.getLastLoser.get)
        val winnerName = controller.playerName(winner.get)
        val loser = controller.getLastLoser.get
        val loserName = controller.playerName(loser)
        contents = new BoxPanel(Orientation.Vertical) {
          border = Swing.EmptyBorder(10, 10, 10, 10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += new Label(loserName + " lied. His actual result was " + controller.playerResult(loser) + ".")
            contents += Swing.HStrut(5)
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += new Label(winnerName + " has won this round!")
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
        roundNr +=1
        val winner = controller.whichPlayerFollows(controller.getLastLoser.get)
        val winnerName = controller.playerName(winner.get)
        contents = new BoxPanel(Orientation.Vertical) {
          border = Swing.EmptyBorder(10, 10, 10, 10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += new Label(winnerName + " did not lie. His actual result was " + controller.playerResult(winner.get) + ".")
            contents += Swing.HStrut(5)
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += new Label(winnerName + " has won this round!")
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
            contents += new Label("...and the winner is " + winner + "!  " + "Congratulations, " + winner + "!")
            contents += Swing.HStrut(5)
          }
          contents += Swing.VStrut(10)
          contents += new BoxPanel(Orientation.Horizontal) {
            contents += new Button(Action("New Game") {
              controller.lastLoser = None
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
      case GameWasCancelled =>

    }
  }

  //  def nextScreen(methodName: String) =
  //    playerNameInput
  //    System.exit(0)
  //

  //  def player1NameInput = contents = new FlowPanel(new Label(" Please enter Player 1's name:  "), namePlayer1, new Button(Action("Continue") { setNameP1(namePlayer1.text) })) {
  //    border = Swing.EmptyBorder(15, 10, 10, 10)
  //  }
  //
  //  def player2NameInput = contents = new FlowPanel(new Label(" Please enter Player 2's name:  "), namePlayer2, new Button(Action("Continue") { setNameP2(namePlayer2.text) })) {
  //    border = Swing.EmptyBorder(15, 10, 10, 10)
  //  }
  //
  //  def setNameP1(namePlayer1: String) = {
  //    players = players :+ controller.newPlayer(namePlayer1)
  //
  //    player2NameInput
  //  }
  //
  //  def setNameP2(namePlayer2: String) = {
  //    players = players :+ controller.newPlayer(namePlayer2)
  //
  //    controller.createPlayers(players)
  //
  //    controller.newRoundGUI
  //
  //    round
  //  }

  //  val pic1 = new Label {
  //    icon = new ImageIcon("C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl1.png")
  //  }
  //  val pic2 = new Label {
  //    icon = new ImageIcon("C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl2.png")
  //  }
  //  val pic3 = new Label {
  //    icon = new ImageIcon("C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl3.png")
  //  }
  //  val pic4 = new Label {
  //    icon = new ImageIcon("C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl4.png")
  //  }
  //  val pic5 = new Label {
  //    icon = new ImageIcon("C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl5.png")
  //  }
  //  val pic6 = new Label {
  //    icon = new ImageIcon("C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl6.png")
  //  }

  //  def round = contents = new FlowPanel(new Label(controller.playerName(controller.getPlayerStarted) + " your throw: " + controller.getPlayerStarted.diceCup.toString()), picSelectionStart(0), picSelectionStart(1), picSelectionStart(2), new Button(Action("Continue") { declareBid }))
  //
  //  def picSelectionStart(i: Int): Label = {
  //
  //    var picPath: java.net.URL = null
  //
  //    val picList = List[Label]()
  //
  //    val d = controller.getPlayerStarted.diceCup.dieCombi(i)
  //
  //      d match {
  //        case 1 => picPath=getClass.getResource("/Zahl1.png")
  //        case 2 => picPath=getClass.getResource("/Zahl2.png")
  //        case 3 => picPath=getClass.getResource("/Zahl3.png")
  //        case 4 => picPath=getClass.getResource("/Zahl4.png")
  //        case 5 => picPath=getClass.getResource("/Zahl5.png")
  //        case 6 => picPath=getClass.getResource("/Zahl6.png")
  //        case _ => picPath = getClass.getResource("/Empty.png")
  //      }
  //
  //
  //    val pic = new Label { icon = new ImageIcon(picPath) }
  //    pic
  //  }
  //
  //  def picSelectionFollows: Label = {
  //
  //    var picPath = ""
  //
  //    for (d <- controller.getPlayerFollowed.diceCup.dieCombi) {
  //
  //      d match {
  //        case 1 => picPath = "C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl1.png"
  //        case 2 => picPath = "C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl2.png"
  //        case 3 => picPath = "C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl3.png"
  //        case 4 => picPath = "C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl4.png"
  //        case 5 => picPath = "C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl5.png"
  //        case 6 => picPath = "C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl6.png"
  //        case _ =>
  //      }
  //    }
  //    val pic = new Label { icon = new ImageIcon(picPath) }
  //    pic
  //  }
  //
  //  def newIField = new TextField {
  //    text = "Bid"
  //    columns = 3
  //    horizontalAlignment = Alignment.Right
  //  }
  //
  //  val bidInput = newIField
  //
  //  def declareBid = contents = new FlowPanel(new Label(controller.playerName(controller.getPlayerStarted) + " declare your bid (e.g. 3,2 /means your bid is a double of 3): "), bidInput, new Button(Action("Continue") { askIfMistrusts }))
  //
  //  def askIfMistrusts = contents = new FlowPanel(new Label("Highest bid at the moment = " + controller.getHighestBidResult + "Do you mistrust " + controller.playerName(controller.getHighestBidPlayer) + " or do you want to set a higher bid?"), new Button(Action("Set higher Bid") { controller.setUserInteraction("b") }), new Button(Action("Mistrust") { controller.setUserInteraction("m") }))

  def picSelection(i: Int, p: Player): Label = {

    var picPath: java.net.URL = null

    val picList = List[Label]()

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

}