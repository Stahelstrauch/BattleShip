package models;

import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Model {
    private int boardSize = 10; // Vaikimisi laua suurus
    private ArrayList<GridData> gridData; // Loome listi
    private Game game; //Laevade info mängulaual
    //Edetabeli failiga seotud muutujad
    private String scoreFile = "scores.txt"; //Edetabeli fail
    private String[] columnNames = new String []{"Nimi", "Aeg", "Klikke", "Laua suurus", "Mängu aeg"}; // lahtrite pealkirjad
    //Edetabeli andmebaasiga seotud muutujad
    private String scoreDatabase = "scores.db"; // Andmebaas
    private String scoreTable = "scores"; // tabel andmebaasis


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

    /**
     * Edetabeli faili olemasoli ja siu kontroll
     * @return  true kui on korras, false kui pole
     */
    public boolean checkFileExistsAndContent() {
        File file = new File(scoreFile); // Tee scores.txt file objektiks
        if(!file.exists()) {// Kui faili pole, siis
            return false; // ....tagastab false
        }

        try (BufferedReader br = new BufferedReader(new FileReader(scoreFile))) {
            String line =br.readLine(); // Loeme esimese rea failist
            if(line == null) {
                return false; // Ridu pole üldse
            }
            String[] columns = line.split(";"); // Tükeldame semikoolonist
            return columns.length == columnNames.length; // Lihtsustatud if lause

        } catch (IOException e) {
            // throw new RuntimeException(e); // Muidu lõpetab programm igasuguse töö
            return false;
        }
    }

    /**
     * Edetabeli faili sisu loetakse massiivi ja tagastatakse
     * @return ScoreData list(edetabeli info)
     */
    public ArrayList<ScoreData> readFromFile() {
        ArrayList<ScoreData> scoreData = new ArrayList<>();
        File file = new File(scoreFile);
        if(file.exists()) { // Kui fail on olemas
            try (BufferedReader br = new BufferedReader(new FileReader(scoreFile))){
                int lineNumber = 0; // Rea number
                for(String line; (line = br.readLine()) != null;) {
                    if(lineNumber > 0) { // Alustame teisest reast, esimene on pealkiri
                        String[] columns = line.split(";"); // Tükeldame semikoolonist
                        if(Integer.parseInt(columns[3]) == boardSize) { // Teeb kolmanda veeru ehk mängulaua suuruse Stringist täisarvuks ja võrdleb mängulaua suurusega, selleks et näitaks edetabelis ainult sama suure lauaga mänginuid
                            String name = columns[0];
                            int gameTime = Integer.parseInt(columns[1]);
                            int clicks = Integer.parseInt(columns[2]);
                            int board = Integer.parseInt(columns[3]);
                            LocalDateTime played = LocalDateTime.parse(columns[4], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // Muudab selle DatetIme objektiks
                            scoreData.add(new ScoreData(name, gameTime, clicks, board, played)); // Lisab info scoredatasse
                        }

                    }
                    lineNumber++; //Reanumber peab kasvama
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return scoreData; // Tagasta sisu
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

    public String getScoreFile() {
        return scoreFile;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public String getScoreDatabase() {
        return scoreDatabase;
    }

    public String getScoreTable() {
        return scoreTable;
    }

    //SETTERS

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void setGridData(ArrayList<GridData> gridData) {
        this.gridData = gridData;
    }


}
