package models;

import java.util.Random;
import java.util.stream.IntStream;

public class Game {
    private int boardSize; // Mängulaua suurus, vaikimisi 10x10
    private int[][] boardMatrix; //Mängulaeval asuvad laevad
    private Random random = new Random(); // Juhuslikkuse jaoks
    //private int[] ships = {4, 3, 3, 2, 2, 2, 1}; // Laeva pikkus (US)
    //private int [] ships = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1, 1}; //Õpilased
    private int[] ships = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1}; //EE variant
    private int shipsCounter = 0; // Laevu kokku
    private int clickCounter = 0; //Mitu korda klikiti mäüngus

    public Game(int boardSize) {
        this.boardSize = boardSize;
        this.boardMatrix = new int[boardSize][boardSize]; //Uuemängulaua loomine
    }

    /**
     * Näita konsoolis mängulaua sisu
     */
    public void showGameBoard() {
        System.out.println(); //Tühi rida
        for (int row = 0; row < boardMatrix.length; row++) {
            for (int col = 0; col < boardMatrix[row].length; col++) {
                System.out.print(boardMatrix[row][col] + " ");
            }
            System.out.println(); //Tühi rida, et hakata järgmist rida joonistama
        }
    }

    /**
     * Meetod millega paneme laevad lauale
     */
    public void setupGameBoard() {
        boardMatrix = new int[boardSize][boardSize]; // Uus laua suurus - algseis
        int shipsTotal = ships.length; //Kui palju on laevu kokku
        int shipsPlaced = 0; //Kui palju on laevu paigutatud
        // TODO laevade järjkorra segamine

        while (shipsPlaced < shipsTotal) {
            int length = ships[shipsPlaced]; // Millist laeva paigutada (laeva pikkus)
            boolean placed = false; //laeva pole paigutatud

            // Valime juhusliku alguspunkti
            int startRow = random.nextInt(boardSize); //Juhuslik rida
            int startCol = random.nextInt(boardSize); // juhuslik veerg

            // Käime kogu laua läbi alates sellest punktist
            outerLoop:
            // Lihtsalt silt (label) ehk nimi for-loobile
            for (int rOffset = 0; rOffset < boardSize; rOffset++) { //rida
                int r = (startRow + rOffset) % boardSize;
                for (int cOffset = 0; cOffset < boardSize; cOffset++) { //veerg
                    int c = (startCol + cOffset) % boardSize;

                    boolean vertical = random.nextBoolean(); //Määrame juhusliku suuna true = vertical, false= horizontal
                    if (tryPlaceShip(r, c, length, vertical || tryPlaceShip(r, c, length, !vertical))) {
                        placed = true; //Laev paigutatud
                        break outerLoop; //Katkesta mõlemad for-loop kordused
                    }

                }


            }

            if (placed) {
                shipsPlaced++; //Järgmine laev
            } else {
                // Kui ei leitud sobivat kohta, katkestame ja alustame uuesti
                setupGameBoard(); // Iseenda välja kutsumine!
                return;
            }

        }
        // Eemaldame ajutised kaitsetsoonid (9-d), jättes alles ainult laevad (1-4) ja tühjad veekohad (0)
        //replaceNineToZero();
    }

    private void replaceNineToZero() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (boardMatrix[row][col] == 9) {
                    boardMatrix[row][col] = 0;
                }
            }

        }
    }

    private boolean tryPlaceShip(int row, int col, int length, boolean vertical) {
        // Kontrolli kas laev üldse mahub mängulauale
        if (vertical && row + length > boardSize) return false;
        if (!vertical && col + length > boardSize) return false;

        //Kontrolli kas sihtpiirkond on vaba (sh kaitsetsoon)
        if (!canPlaceShip(row, col, length, vertical)) return false;

        // Kirjutame laevamängu lauale: paigutame igasse lahtrisse laeva pikkuse
        for (int i = 0; i < length; i++) {
            int r = vertical ? row + i : row; //Kasutame rida, või mitte, olenevalt suunast
            int c = vertical ? col : col + i; //Kasutame veergu, või mitte, olenevalt suunast
            boardMatrix[r][c] = length; // Määrame laeva lahtrisse selle pikkuse

        }
        //Määrame ümber laeva kaitsetsooni (vältimaks kontakset paigutust)
        makeSurrounding(row, col, length, vertical);
        return true;


    }

    private void makeSurrounding(int row, int col, int length, boolean vertical) {
        Area area = getShipsSurroundingArea(row, col, length, vertical);
        // Käime ala igas lahtris ja kui seal on vesi (0), siis märgime selle kaitseks(9)
        for (int r = area.startRow; r <= area.endRow; r++) {
            for (int c = area.startCol; c <= area.endCol; c++) {
                if (boardMatrix[r][c] == 0) { //Kas on vesi
                    boardMatrix[r][c] = 9; // Pane kaitse
                }
            }

        }
    }

    private boolean canPlaceShip(int row, int col, int length, boolean vertical) {
        Area area = getShipsSurroundingArea(row, col, length, vertical); // Saame laeva ümbritseva ala
        // Kontrollime igat lahtit alal - kui kuskil pole tühjust (0), katkestame
        for (int r = area.startRow; r <= area.endRow; r++) {
            for (int c = area.startCol; c <= area.endCol; c++) {
                if (boardMatrix[r][c] > 0 && boardMatrix[r][c] <= 4) return false; // Midagi on ees, ei sobi

            }
        }
        return true; // Kõik kohad olid vabad
    }

    private Area getShipsSurroundingArea(int row, int col, int length, boolean vertical) {
        // Arvutame ümbritseva ala piirid, hoides neid mängulaua piires
        int startRow = Math.max(0, row - 1);
        int endRow = Math.min(boardSize - 1, vertical ? row + length : row + 1);
        int startCol = Math.max(0, col - 1);
        int endCol = Math.min(boardSize - 1, vertical ? col + 1 : col + length);
        return new Area(startRow, endRow, startCol, endCol);
    }
    //GETTERS
    // Eemaldame ajutised kaitsetsoonid (9-d), jättes alles ainult laevad (1-4) ja tühjad veekohad (0)

    public int[][] getBoardMatrix() {
        return boardMatrix;
    }

    public int getShipsCounter() {
        return shipsCounter;
    }

    public int getClickCounter() {
        return clickCounter;
    }

    /**
     * {4, 3, 3....} laevade summa näide on 10
     * @return laeva pikkuste summa
     */
    public int getShipsParts() {
        return IntStream.of(ships).sum();
    }
}
