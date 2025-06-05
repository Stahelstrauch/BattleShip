package models;

import java.awt.*;
import java.util.ArrayList;

public class Model {
    private int boardSize = 10; // Vaikimisi laua suurus
    private ArrayList<GridData> gridData; // Loome listi
    private Game game; //Laevade info mängulaual

    public Model() {
        gridData = new ArrayList<>(); // See hakkab hoidma ridade, veergude, x,y kordinaatide jne infot
    }

    /**
     * Tagastab hiire koordinaatide põhjal massiivi indeksi ehk id
     * @param mouseX hiire X koordinaat
     * @param mouseY hiire Y koordinaat
     * @return tagastab lahtri ID
     */
    public int checkGridIndex(int mouseX, int mouseY) { //Kontrollib hiire kordinaate
        int result = -1; //Viga
        int index = 0; //Alguspunkt on 0
        for(GridData gd : gridData) {
            if(mouseX > gd.getX() && mouseX <=(gd.getX() + gd.getWidth()) && mouseY > gd.getY() && mouseY <= (gd.getY() + gd.getHeight())) {
                result = index;
            }
            index++; // Index kasvab
        }
        return result; //Lahtri ID
    }

    /**
     * Tagastab mängulaua reanumbri saadud id põhjal (checkGridIndex)
     * @param id mängulaualaua id
     * @return tagastab rea numbri
     */
    public int getRowById(int id) {
        if(id != -1) { // Kui ei ole -1
            return gridData.get(id).getRow();
        }
        return -1; //Viga
    }

    /**
     * Tagastab mängulaua veerunumbri saadud id põhjal
     * @param id mängulaua id
     * @return tagastab veeru numbri
     */
    public int getColById(int id) {
        if(id != -1) { // Kui ei ole -1
            return gridData.get(id).getCol();
        }
        return -1; //Viga
    }


    public void setupNewGame() {
        game = new Game(boardSize);
    }

    public void drawUserBoard(Graphics g) { //  Kaasa antakse graafiline joonistuslaud - paintComponentis on see g
        ArrayList<GridData> gdList = getGridData(); // See loodi mängulaua joonistamisel
        int [][] matrix = game.getBoardMatrix(); // Siin on laevade info, ja vesi jne

        for(GridData gd : gdList) {
            int row = gd.getRow(); //Rida
            int col = gd.getCol(); //veerg
            int cellValue = matrix[row][col]; // Väärtus: 0, 1-5, 7, 8

            // Määrame värvi ja suuruse sõltuvalt lahtri suuruses (cellValue)
            Color color = null; // Algselt värvi pole
            int padding = 0; // Objekt on aga väärtus pole

            switch(cellValue) { // Väärtus saab olla 0, 1-5, 7, 8
                case 0: // Vesi
                    color = new Color(0, 190, 255);
                    break;
                case 7: // Pihtas
                    color = Color.GREEN;
                    break;
                case 8: //Mööda
                    color = Color.RED;
                    padding = 3;
                    break;
                default:
                    if(cellValue >= 1 && cellValue <= 5) { // Laevad 1-5
                        //Kommenteeri välja kui ei soovi mängulaual laevu näha
                        color = new Color(246, 246, 5, 237);

                    }

            }
            // Kui värv on määratud, joonista ruut
            if(color != null) {
                g.setColor(color); // Määra värv
                g.fillRect(
                        gd.getX() + padding,
                        gd.getY() + padding,
                        gd.getWidth() - 2 * padding,
                        gd.getHeight() -2 * padding
                );

            }
        }
    }


    //GETTERS

    public int getBoardSize() {
        return boardSize;
    }

    public ArrayList<GridData> getGridData() {
        return gridData;
    }

    public Game getGame() {
        return game;
    }

    //SETTERS

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void setGridData(ArrayList<GridData> gridData) {
        this.gridData = gridData;
    }
}
