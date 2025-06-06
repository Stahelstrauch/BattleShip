package views;

import controllers.Controller;
import models.Model;
import views.panels.GameBoard;
import views.panels.InfoBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

public class View extends JFrame {
    private Model model;
    private GameBoard gameBoard; // Mängulaud
    private InfoBoard infoBoard; // Infotahvel

    public View(Model model) {
        super("Laevade pommitamine"); // Pealkiri, supper laseb kogu frame sisu kasutada
        this.model = model;

        gameBoard = new GameBoard(model); // Loome mängulaua
        infoBoard = new InfoBoard(); // Loome infotahvli

        JPanel container = new JPanel(new BorderLayout()); // Loome uue paneeli
        container.add(gameBoard, BorderLayout.CENTER); // Paneme paneeli peale mängulaua ujuvale osale
        container.add(infoBoard, BorderLayout.EAST); // Paneme paneeli peale infotahvli idasse

        add(container); // Paneme konteineri peale

        //TEST Frame ja Panel Layout Managerid
//        System.out.println("JFrame:        " + this.getLayout());
//        System.out.println("container:     " + container.getLayout());
//        System.out.println("GameBoard:     " + gameBoard.getLayout());
//        System.out.println("InfoBoard:     " + infoBoard.getLayout());
//        System.out.println("pnlComponents: " + infoBoard.getPnlComponent().getLayout());
    }

    public JLabel getLblMouseXY() {
        return infoBoard.getLblMouseXY();
    }

    public JLabel getLblID() {
        return infoBoard.getLblID();
    }

    public JLabel getLblRowCol() {
        return infoBoard.getLblRowCol();
    }

    public JLabel getLblTime() {
        return infoBoard.getLblTime();
    }

    public JLabel getLblShip() {
        return infoBoard.getLblShip();
    }

    public JLabel getLblGameBoard() {
        return infoBoard.getLblGameBoard();
    }

    public JComboBox<String> getCmbSize() {
        return infoBoard.getCmbSize();
    }

    public JButton getBtnNewGame() {
        return infoBoard.getBtnNewGame();
    }

    public JButton getBtnScoreBoard() {
        return infoBoard.getBtnScoreBoard();
    }

    public JRadioButton getRdoFile() {
        return infoBoard.getRdoFile();
    }

    public JRadioButton getRdoDb() {
        return infoBoard.getRdoDb();
    }

    public JCheckBox getChcWhere() {
        return infoBoard.getChcWhere();
    }

    public void registerGameBoardMouse(Controller controller) {
        gameBoard.addMouseListener(controller); // Hiire kuulamine. Võtame mängulaualt selle hiire liikumise
        gameBoard.addMouseMotionListener(controller);
    }
    public void registerComboBox(ItemListener itemListener) { //Registreerib endale selle valiku, mis on tehtud comboboxis
        infoBoard.getCmbSize().addItemListener(itemListener); //Hiire liikumine
    }

    public void registerNewGameButton(ActionListener actionListener) {
        infoBoard.getBtnNewGame().addActionListener(actionListener); // Nupu klikimise funktsionaalsus
    }

    public void registerScoreBoardButton(ActionListener actionListener) {
        infoBoard.getBtnScoreBoard().addActionListener(actionListener);
    }

    public GameBoard getGameBoard() { // Getter vajalik, et saaks ligipääsu GameBoardile
        return gameBoard;
    }
}
