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
  controller.add(this)

  def processInputLine() = {
    controller.menuNavigation
  }

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
    text = "Bid"
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
          controller.setPlayerName(1, namePlayer1.text)
          controller.setPlayerName(2, namePlayer2.text)
          controller.menuNavigation
        })
        restrictHeight(namePlayer1)

        contents = new BoxPanel(Orientation.Vertical) {
          contents += new BoxPanel(Orientation.Horizontal) {
            border = Swing.EmptyBorder(10, 10, 10, 10)
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
      case ExplainCommands => controller.newRound/*controller.startGame*/
      case DiceWereRollen => {
        contents = new BoxPanel(Orientation.Vertical) {
          contents += new BoxPanel(Orientation.Horizontal) {

            border = Swing.EmptyBorder(10, 10, 10, 10)

            //for(i <- 0 until playerStarted.diceCount) yield contents += picSelection(i, playerStarted)
            /*contents += picSelection(0, playerStarted)
          contents += picSelection(1, playerStarted)
          contents += picSelection(1, playerStarted)
  */
/*            contents += new Label("Hallo, es beginnt eine neue Runde")
            contents += Swing.HStrut(5)
            contents += new BoxPanel(Orientation.Horizontal) {
              contents += new Button(Action("Continue") {})
            }*/

            contents += new Label("" + controller.table.players(1).diceCup.dieCombi(1))
            contents += new Label("" + controller.table.players(1).diceCup.dieCombi(2))
          }
        }
      }
      case PlayerHasWonRound => {
        val winner = controller.playerName(PlayerHasWonRound.attachment.asInstanceOf[Player])
        new FlowPanel(new Label(winner + " has won this round!"), new Button(Action("Continue") {}))
      }
      case PlayerWithHighestBidLied => new FlowPanel(new Label(controller.playerName(controller.getHighestBidPlayer) + " lied. His actual result was " + controller.playerResult(controller.getHighestBidPlayer) + "."), new Button(Action("Continue") {}))
      case PlayerWithHighestBidNotLied => {
        val winner = controller.whichPlayerFollows(controller.getLastLoser.get)
        new FlowPanel(new Label(controller.playerName(winner.get) + " did not lie. His actual result was " + controller.playerResult(winner.get) + "."), new Button(Action("Continue") {}))
      }
      case NewRound =>
        controller.getPlayerStarted match {
          case None =>
          case Some(playerStarted) => {
            contents = new BoxPanel(Orientation.Vertical) {
              contents += new BoxPanel(Orientation.Horizontal) {

                border = Swing.EmptyBorder(10, 10, 10, 10)
                contents += new Label(playerStarted.name + ": ")
                contents += Swing.HStrut(5)
                //for(i <- 0 until playerStarted.diceCount) yield contents += picSelection(i, playerStarted)
                /*contents += picSelection(0, playerStarted)
              contents += picSelection(1, playerStarted)
              contents += picSelection(1, playerStarted)
*/
                contents += new Label("" + playerStarted.diceCup.dieCombi(0))
                contents += new Label("" + playerStarted.diceCup.dieCombi(1))
                contents += new Label("" + playerStarted.diceCup.dieCombi(2))
              }
              //contents += Swing.VStrut(5)
              /* contents += new BoxPanel(Orientation.Horizontal) {
               contents += new Button(Action("Continue") {})
             }*/

              /*  for (e <- contents)
                e.xLayoutAlignment = 0.0
            }*/
              //new FlowPanel(new Label(playerStarted.name + ": "), picSelection(0, playerStarted), picSelection(1, playerStarted), picSelection(2, playerStarted), new Button(Action("Continue") {}))
            }

          }
        }
      /*contents = {
      new FlowPanel(new Label("New Round"), new Button(Action("Continue") {
      }))
    }*/
      case DeclareFirstBid => new FlowPanel(new Label(controller.playerName(controller.getPlayerStarted.get) + ", please declare the first bid (e.g. 3,2 /means your bid is a double of 3):"), bidInput, new Button(Action("Continue") {
        /*controller.setUserInteraction(bidInput.text)*/
      }))
      case LineSeparator =>
      case Input =>
      case AskIfMistrusts => {

        new FlowPanel(new Label("Highest bid at the moment = " + controller.getHighestBidResult), new Button(Action("Continue") {}))
        new FlowPanel(new Label("Now it's your turn " + controller.playerName(controller.whichPlayerFollows(controller.getHighestBidPlayer).get)), new Button(Action("Continue") {}))
        new FlowPanel(new Label("Do you mistrust " + controller.playerName(controller.getHighestBidPlayer) + " or do you want to set a higher bid?"), new Button(Action("Mistrust") {
          /*controller.setUserInteraction("m")*/
        }), new Button(Action("Set Higher Bid") {
          /*controller.setUserInteraction("b")*/
        }))

      }
      case PrintPlayer => {
        val player: Player = PrintPlayer.attachment.asInstanceOf[Player]
        new FlowPanel(new Label("Name: " + player.name + " Number_Of_Dice: " + player.diceCount + " DiceCup: " + player.diceCup.toString()), new Button(Action("Continue") {}))
      }
      case RequestHigherBid => {
        val player: Player = RequestHigherBid.attachment.asInstanceOf[Player]
        new FlowPanel(new Label(controller.playerName(player) + ", please declare a higher bid than " + controller.getHighestBidResult + " :"), bidInput, new Button(Action("Continue") {
          /*controller.setUserInteraction(bidInput.text)*/
        }))
      }
      case GameIsOver => {
        val winner = controller.playerName(GameIsOver.attachment.asInstanceOf[Player])
        new FlowPanel(new Label("...and the winner is " + winner + "!  " + "Congratulations, " + winner + "!"), new Button(Action("Restart") {
          controller.createPlayers
        }), new Button(Action("Quit") {
          System.exit(0)
        }))

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

    //val photo1 = ImageIO.read(new File("photo.jpg"))
    //val photi = ImageIO.read(inStream)
    val pic = new Label {
      icon = new ImageIcon(picPath)
    }
    pic
  }

}