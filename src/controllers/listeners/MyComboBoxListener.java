package controllers.listeners;

import models.Model;
import views.View;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class MyComboBoxListener implements ItemListener {
    private Model model;
    private View view;


    public MyComboBoxListener(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        //System.out.println(e.getItem()); //Test
        if(e.getStateChange() == ItemEvent.SELECTED) {
            // TEST System.out.println(e.getItem());
            String number = e.getItem().toString(); // teeb stringiks väärtuse
            int size = Integer.parseInt(number); //Tee eelnev string täisarvuks
            view.getLblGameBoard().setText(String.valueOf(size + " x " + size));
            model.setBoardSize(size); //Määrab mängulaua suuruse
            model.setGame(null); // Paneb vana mängu nulli
            model.setGridData(new ArrayList<>()); // Teeb uue gridata ( mänguinfo) listi

            view.pack(); // Et suurus muutuks
            //view.repaint(); // Joonista uuesti
            view.getGameBoard().repaint(); // teeb tühjaks
        }
    }
}
