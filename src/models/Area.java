package models;

public class Area {
    int startRow; // Ala ülemise rea index
    int endRow; // Ala alumise rea indeks
    int startCol; // Ala vasakpoolse veeru indeks
    int endCol; // Ala parempoolse veeru indeks

    public Area(int startRow, int endRow, int startCol, int endCol) {
        this.startRow = startRow;
        this.endRow = endRow;
        this.startCol = startCol;
        this.endCol = endCol;
    }

}
