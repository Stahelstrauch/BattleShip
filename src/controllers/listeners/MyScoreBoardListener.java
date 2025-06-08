package controllers.listeners;

import models.Database;
import models.Model;
import models.ScoreData;
import views.View;
import views.dialogs.ScoreboardDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class MyScoreBoardListener implements ActionListener {
    private Model model;
    private View view;
    private JDialog dlgScoreBoard; // Edetabeli aken (JDialog)

    public MyScoreBoardListener(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void actionPerformed(ActionEvent e) {
        // System.out.println("Edetabel"); // test konsooli kuvamiseks
        ArrayList<ScoreData> result;
        if (view.getInfoBoard().getChcWhere().isSelected()) {
            if (view.getRdoFile().isSelected()) { // Raadionuppude valikust on valitud fail
                result = model.readFromFile(); // Loe faili sisu massiivi
                if (createTable(result)) {
                    setupDlgScoreBoard();
                } else {
                    JOptionPane.showMessageDialog(view, "Andmeid pole!");
                }
            } else { // Kui on valitud nupp andmebaas (see on default sest kui sa ei vali fail siis valid järelikult andmebaas)
                try (Database db = new Database(model)) {
                    result = db.select(model.getBoardSize());
                    if (!result.isEmpty() && createTableDb(result)) {
                        setupDlgScoreBoard();
                    } else {
                        JOptionPane.showMessageDialog(view, "Andmebaasi tabel on tühi!");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        } else {
            // System.out.println("Eraldi aknas ei ole checkitud"); test
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel.add(view.getInfoBoard());
            panel.add(view.getGameBoard());
            view.repaint();

            if (view.getRdoFile().isSelected()) {
                result = model.readFromFile();
                if (!result.isEmpty()) {
                    JTable table = createTableFromFileData(result);
                    createTableToMainView(table, "Edetabel failist");
                } else {
                    JOptionPane.showMessageDialog(view, "Andmeid pole failis!");
                }
            } else {
                try (Database db = new Database(model)) {
                    result = db.select(model.getBoardSize());
                    if (!result.isEmpty()) {
                        JTable table = createTableFromFileData(result); // Võid teha ka eraldi meetodi DB jaoks
                        createTableToMainView(table, "Edetabel andmebaasist");
                    } else {
                        JOptionPane.showMessageDialog(view, "Andmebaasi tabel on tühi!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(view, "Viga andmebaasiga: " + ex.getMessage());
                }
            }



        }
    }



    private boolean createTableDb(ArrayList<ScoreData> result) {
        if (!result.isEmpty()) {
            String[][] data = new String[result.size()][5]; // result.size on ridade arv, 5 on veergude arv
            for (int i = 0; i < result.size(); i++) { // Käime läbi for-loobiga iga rea
                data[i][0] = result.get(i).getName();
                data[i][1] = result.get(i).formatGameTime(result.get(i).getTime());
                data[i][2] = String.valueOf(result.get(i).getClicks());
                data[i][3] = String.valueOf(result.get(i).getBoard());
                data[i][4] = result.get(i).getPlayedTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

            }

            //Loome read-only TableModel (topelt klik lahtir võimatu)
            DefaultTableModel tableModel = new DefaultTableModel(data, model.getColumnNames()) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // EDi luba lahtri sisu muuta
                }
            };

            JTable table = new JTable(tableModel);

            //TODO Tabelil klikkimine

            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && !e.isConsumed()) {
                        e.consume();
                        int row = table.rowAtPoint(e.getPoint());
                        int col = table.columnAtPoint(e.getPoint());
                        // Näita kogu rida
                        StringBuilder rowData = new StringBuilder();
                        for (int i = 0; i < table.getColumnCount(); i++) {
                            rowData.append(table.getValueAt(row, i)).append(" | ");

                        }
                        JOptionPane.showMessageDialog(table, "Valitud rida\n" + rowData);

                        //ainult lahti sisu
                        Object cellObject = table.getValueAt(row, col);
                        JOptionPane.showMessageDialog(table, "Valitud lahter\n" + cellObject);
                    }
                }
            });

            //Teeme tabeli päise rasvaseks
            JTableHeader header = table.getTableHeader();
            Font headerFont = header.getFont().deriveFont(Font.BOLD);
            header.setFont(headerFont);

            //Määrame veergude laiuse
            int[] columnWidths = {100, 120, 80, 90, 150};
            for (int i = 0; i < columnWidths.length; i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]); // Määrame igale veerule tema laiuse

            }

            // Joondame alates teisest veerust paremale serva
            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
            for (int i = 1; i < model.getColumnNames().length; i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
            }

            dlgScoreBoard = new ScoreboardDialog(view);
            JScrollPane scrollPane = new JScrollPane(table);
            dlgScoreBoard.add(scrollPane);
            dlgScoreBoard.setTitle("Edetabel andmebaasist");
            return true;


        }
        return false;


    }

    private boolean createTable(ArrayList<ScoreData> result) {
        if (!result.isEmpty()) { // ei ole tühi
            Collections.sort(result); // sorteerib vastavalt compareTo(9 ScoreData
            // Loome kahemõõtmelise stringide massiivi
            String[][] data = new String[result.size()][5]; // result.size on ridade arv, 5 on veergude arv
            for (int i = 0; i < result.size(); i++) { // Käime läbi for-loobiga iga rea
                data[i][0] = result.get(i).getName();
                data[i][1] = result.get(i).formatGameTime(result.get(i).getTime());
                data[i][2] = String.valueOf(result.get(i).getClicks());
                data[i][3] = String.valueOf(result.get(i).getBoard());
                data[i][4] = result.get(i).getPlayedTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));

            }

            JTable table = new JTable(data, model.getColumnNames()); // Columnnames on massiiv

            //Määrame veergude laiuse
            int[] columnWidths = {100, 80, 60, 80, 160};
            for (int i = 0; i < columnWidths.length; i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]); // Määrame igale veerule tema laiuse
            }

            // Hakkame edetabeli akent looma, kerimisribaga
            dlgScoreBoard = new ScoreboardDialog(view); // meie loodud dialogi aken
            dlgScoreBoard.add(new JScrollPane(table)); // Paneme külge kerimisriba tabeli jaoks
            dlgScoreBoard.setTitle("Edetabel failist");
            return true;


        }
        return false;
    }

    private void setupDlgScoreBoard() {
        dlgScoreBoard.setModal(true); // teise akna peal, alumise akna peal klikida ei saa
        dlgScoreBoard.pack();
        dlgScoreBoard.setLocationRelativeTo(null); // Paigutame keset ekraani
        dlgScoreBoard.setVisible(true); // Teeme nähtavaks
    }

    private void createTableToMainView(JTable table, String title) {
        // Tabelipaneeli loomine
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(title));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Sulgemisnupp
        JButton closeButton = new JButton("Sulge");
        closeButton.addActionListener(e -> {
            //Vajutamisel taasta mängulaud
            Container contentPane = view.getContentPane();
            contentPane.removeAll();
            contentPane.add(view.getInfoBoard(), BorderLayout.NORTH);
            contentPane.add(view.getGameBoard(), BorderLayout.CENTER);
            contentPane.repaint();
        });

        // Nupupaneel alla paremasse nurka
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Lisa
        Container contentPane = view.getContentPane();
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(tablePanel, BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
    }


    private JTable createTableFromFileData(ArrayList<ScoreData> result) {
        if (result.isEmpty()) return null;

        Collections.sort(result);
        String[][] data = new String[result.size()][5];
        for (int i = 0; i < result.size(); i++) {
            data[i][0] = result.get(i).getName();
            data[i][1] = result.get(i).formatGameTime(result.get(i).getTime());
            data[i][2] = String.valueOf(result.get(i).getClicks());
            data[i][3] = String.valueOf(result.get(i).getBoard());
            data[i][4] = result.get(i).getPlayedTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
        }

        DefaultTableModel model = new DefaultTableModel(data, this.model.getColumnNames()) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));

        // Joondus ja veerulaius
        int[] columnWidths = {100, 80, 60, 80, 160};
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        for (int i = 1; i < this.model.getColumnNames().length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        return table;
    }


}
