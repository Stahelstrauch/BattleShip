package views.panels;

import models.GridData;
import models.Model;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameBoard extends JPanel {
    private Model model; // Anname mudeli kaasa
    private int startX = 5; // Vasak ülemine x-koordinaat
    private int startY = 5; // Vasak ülemine y-koordinaat
    private int width = 30; // ruudu laius
    private int height = 30; // ruudu kõrgus
    private String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"}; // Ülakomasid ei või kasutada. See on mängulaua tähestik

    public GameBoard(Model model) { // Konstruktor
        this.model = model;
        setBackground(new Color(0, 190, 255));
    }

    @Override
    public Dimension getPreferredSize() { // Akna tegemine, et oleks 10x10 board või 15x15 board
        int w = (width * model.getBoardSize()) + width + (2 * startX);
        int h = (height * model.getBoardSize()) + height + (2 * startY);
        return new Dimension(w, h);
    }

    @Override
    protected void paintComponent(Graphics g) { //Meetod joonte joonistamiseks (ruudustik)
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
        // Kirjastiil ja suurus mängulaual
        g.setFont(new Font("Verdana", Font.BOLD, 14));
        // Tähestik koos ruutudega
        drawColumnAlphabet(g);

        // Vasakule serva numbrid ruutudega
        drawRowColumn(g);

        //Hakkame joonistama laevu
        //TODO pole kõige parem lahendus
        if(model.getGame() != null) { //Selleks, et mängu lõppedes tulemus ei kaoks tuli && !model.getGame().isGameOver() osa ära võtta
            model.drawUserBoard(g);
        }

        // Täidab ülejäänud ruudustiku
        drawGameGrid(g);

    }

    private void drawGameGrid(Graphics g) {
        ArrayList<GridData> matrix = new ArrayList<>(); // Tühja maatriksi tegemine
        g.setColor(Color.BLACK);

        int x = startX + width;
        int y = startY + height;
        int i = 1;

        for(int r = 0; r < model.getBoardSize(); r++) { // rida vasakult paremale (row)
            for(int c = 0; c < model.getBoardSize(); c++) { // veerg ehk column
                g.drawRect(x, y, width, height); // Joonistab ühe ruudu
                matrix.add(new GridData(r, c, x, y, width, height));
                x += width;

            }
            // Järgmise rea seaded
            y = (startY + height) + (height * i);
            i++;
            x = startX + width;
        }
        model.setGridData(matrix);

    }

    private void drawRowColumn(Graphics g) {
        int i = 1; // Esimene number mängulaual
        g.setColor(Color.RED);

        for(int y = startY + height; y< (height * model.getBoardSize()) + height; y += height) {
            g.drawRect(startX, y, width, height); // Joonistab ruudustiku ülevalt alla
            if (i < 10) {
                g.drawString(String.valueOf(i), startX + (width / 2) - 5, y + 2 * (startY + startY));

            }else {
                g.drawString(String.valueOf(i), startX + (width / 2)  -10, y + 2 * (startY + startY));
            }
            i++;
        }

    }

    private void drawColumnAlphabet(Graphics g) { // Tähestiku lisamise meetod
        int i = 0;// Tähestiku massiivi esimese tähe index A=0, B=1 jne
        g.setColor(Color.WHITE); // Tähestiku joonistamise värv

        for (int x = startX; x<= (width * model.getBoardSize()) + width; x += width){
            g.drawRect(x, startY, width, height); // Joonista ruut 30x30
            if (x > startX) {
                g.drawString(alphabet[i], x + (width / 2) - 5, 2 * (startY + startY) + 5);
                i++; // Suurenda tähe indeksit ühe võrra
            }


        }
    }


}
