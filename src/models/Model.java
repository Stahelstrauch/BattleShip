package models;

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
