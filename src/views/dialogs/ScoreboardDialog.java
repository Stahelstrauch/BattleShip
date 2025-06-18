package views.dialogs;

import views.View;

import javax.swing.*;
import java.awt.*;

public class ScoreboardDialog extends JDialog {
    public ScoreboardDialog(View view) {
        super(view, "Edetabel");
        setPreferredSize(new Dimension(500, 200)); // suurus

    }
}
