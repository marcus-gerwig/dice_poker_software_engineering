package de.htwg.se.dicepoker.aview

import scala.swing._
import scala.swing.event._
import de.htwg.se.dicepoker.controller.DPController
import javax.swing.text.html.FrameSetView
import scala.swing.Alignment
import scala.swing.Frame
import scala.swing.GridPanel
import scala.swing.Label
import scala.swing.TextField
import scala.swing._
import javax.swing.ImageIcon
import de.htwg.se.dicepoker.model.{ PokerTable, Player, Bid, Round }
import scala.language.postfixOps

class Gui(controller: DPController) extends Frame {

  title = "Dice Poker"

  var players: Vector[Player] = Vector.empty

  controller.createPlayers

  menuBar = new MenuBar {
    contents += new Menu("File") {

      //         contents += new MenuItem(Action("New") { controller.initPlayer })
      contents += new MenuItem(Action("Quit") { System.exit(0) })
    }
  }

  contents = new BoxPanel(Orientation.Horizontal) {
    contents += new Button(Action("Start 2 Player Game") { player1NameInput })
    contents += new Button(Action("Quit") { System.exit(0) })
    centerOnScreen
  }

  //  def nextScreen(methodName: String) =
  //    playerNameInput
  //    System.exit(0)
  //    

  def newTField = new TextField {
    text = "Name"
    columns = 5
    horizontalAlignment = Alignment.Right
  }

  val namePlayer1 = newTField
  val namePlayer2 = newTField

  def player1NameInput = contents = new FlowPanel(new Label(" Please enter Player 1's name:  "), namePlayer1, new Button(Action("Continue") { setNameP1(namePlayer1.text) })) {
    border = Swing.EmptyBorder(15, 10, 10, 10)
  }

  def player2NameInput = contents = new FlowPanel(new Label(" Please enter Player 2's name:  "), namePlayer2, new Button(Action("Continue") { setNameP2(namePlayer2.text) })) {
    border = Swing.EmptyBorder(15, 10, 10, 10)
  }

  def setNameP1(namePlayer1: String) = {
    players = players :+ controller.newPlayer(namePlayer1)

    player2NameInput
  }

  def setNameP2(namePlayer2: String) = {
    players = players :+ controller.newPlayer(namePlayer2)

    controller.createPlayers(players)

    controller.newRoundGUI

    round
  }

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

  def round = contents = new FlowPanel(new Label(controller.playerName(controller.getPlayerStarted) + " your throw: " + controller.getPlayerStarted.diceCup.toString()), picSelectionStart(0), picSelectionStart(1), picSelectionStart(2), new Button(Action("Continue") { declareBid }))

  def picSelectionStart(i: Int): Label = {

    var picPath: java.net.URL = null

    val picList = List[Label]()

    val d = controller.getPlayerStarted.diceCup.dieCombi(i)

      d match {
        case 1 => picPath=getClass.getResource("/Zahl1.png")
        case 2 => picPath=getClass.getResource("/Zahl2.png")
        case 3 => picPath=getClass.getResource("/Zahl3.png")
        case 4 => picPath=getClass.getResource("/Zahl4.png")
        case 5 => picPath=getClass.getResource("/Zahl5.png")
        case 6 => picPath=getClass.getResource("/Zahl6.png")
        case _ => picPath = getClass.getResource("/Empty.png")
      }
   
    
    val pic = new Label { icon = new ImageIcon(picPath) }
    pic
  }

  def picSelectionFollows: Label = {

    var picPath = ""

    for (d <- controller.getPlayerFollowed.diceCup.dieCombi) {

      d match {
        case 1 => picPath = "C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl1.png"
        case 2 => picPath = "C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl2.png"
        case 3 => picPath = "C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl3.png"
        case 4 => picPath = "C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl4.png"
        case 5 => picPath = "C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl5.png"
        case 6 => picPath = "C:\\Users\\andre\\Desktop\\Würfelwerte\\Zahl6.png"
        case _ =>
      }
    }
    val pic = new Label { icon = new ImageIcon(picPath) }
    pic
  }

  def newIField = new TextField {
    text = "Bid"
    columns = 3
    horizontalAlignment = Alignment.Right
  }

  val bidInput = newIField

  def declareBid = contents = new FlowPanel(new Label(controller.playerName(controller.getPlayerStarted) + " declare your bid (e.g. 3,2 /means your bid is a double of 3): "), bidInput, new Button(Action("Continue") { askIfMistrusts }))

  def askIfMistrusts = contents = new FlowPanel(new Label("Highest bid at the moment = " + controller.getHighestBidResult + "Do you mistrust " + controller.playerName(controller.getHighestBidPlayer) + " or do you want to set a higher bid?"), new Button(Action("Set higher Bid") { controller.setUserInteraction("b") }), new Button(Action("Mistrust") { controller.setUserInteraction("m") }))
  
//  def roundPlayerFollows = contents = new FlowPanel(new Label(controller.playerName(controller.getPlayerFollowed) + " your throw: " + controller.getPlayerFollowed.diceCup.toString()), picSelectionFollows, new Button(Action("Continue") { print("Hallo") }))

  //    lazy val topNavigation = new FlowPanel(new Button("Start 2 Player Game") { controller.initPlayer }, new Button(" Quit Game") { System.exit(0) }) {
  //    border = Swing.EmptyBorder(15, 10, 10, 10)
  //  }

  //  size = new Dimension(600, 600)
  //      contents = topNavigation
  centerOnScreen
  visible = true

  //      menuBar = new MenuBar {
  //      contents += new Menu("File") {
  //        mnemonic = Key.F
  //        contents += new MenuItem(Action("New") { controller.initPlayer })
  //        contents += new MenuItem(Action("Quit") { System.exit(0) })
  //      }
  //    }

  //  def newField = new TextField {
  //    text = "0"
  //    columns = 5
  //    horizontalAlignment = Alignment.Right
  //  }
  //
  //  val namePlayer1 = newField
  //  val namePlayer2 = newField
  //
  //  lazy val topNavigation = new FlowPanel(new Button("Start 2 Player Game") { openPlayerNameInput }, new Button(" Quit Game")) {
  //    border = Swing.EmptyBorder(15, 10, 10, 10)
  //  }
  //
  //  def openPlayerNameInput {
  //
  //    val frame = new MainFrame {
  //      lazy val nameInput = new FlowPanel(new Label(" Please enter Player 1's name:  "), namePlayer1, new Label(" Please enter Player 2's name:  "), namePlayer2) {
  //        border = Swing.EmptyBorder(15, 10, 10, 10)
  //      }
  //      title = "Dice Poker"
  //      size = new Dimension(600, 600)
  //      contents = nameInput
  //      centerOnScreen
  //      visible = true
  //    }
  //  }

  //  lazy val nameInput = new FlowPanel(new Label(" Please enter Player 1's name:  "), namePlayer1, new Label(" Please enter Player 2's name:  "), namePlayer2) {
  //    border = Swing.EmptyBorder(15, 10, 10, 10)

  //  contents = topNavigation
  //  contents = nameInput
  //  visible = true

  //  menuBar = new MenuBar {
  //    contents += new Menu("Game") {
  //      mnemonic = Key.F
  //      contents += new MenuItem(Action("Start 2 Player Game") { controller.initPlayer })
  //      contents += new MenuItem(Action("Quit Game") { System.exit(0) })
  //    }
  //  }

  //  maximize()

}