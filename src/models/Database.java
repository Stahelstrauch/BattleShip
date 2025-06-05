package models;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Database implements AutoCloseable {
    private Connection connection;
    private String dbFile;
    private String dbUrl;
    private String tableName;

    public Database(Model model) throws SQLException {
        this.dbFile = model.getScoreDatabase();
        this.dbUrl = "jdbc:sqlite:" + dbFile; // Andmebaasiga ühendamiseks
        this.tableName = model.getScoreTable();

        //Alustame kohe ühendusega
        connect();
        ensureTableExists(); // Veendu kas andmebaasis on tabel olemas ja vajadusel tee see

    }

    private void ensureTableExists() {
        //Tabeli loomise sql lause
        String createTableSql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "name text NOT NULL," +
                "time integer NOT NULL," +
                "clicks INTEGER," +
                "board_size INTEGER," +
                "game_time TEXT);";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSql); // Loob tabeli kui vaja
            System.out.println("Tabel kontrollitud/loodud: " + tableName);
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            System.err.println("Viga tabeli loomisel: " + e.getMessage());
            //System.out.println(createTableSql); //test, et näha sql lauset
        }
    }


    private void connect() throws SQLException {
        connection = DriverManager.getConnection(dbUrl);
        System.out.println("Ühendus loodud: " + dbUrl); //Test, et ühendus andmebaasiga on loodud
    }


    @Override
    public void close() throws Exception {
        if(connection != null) {
            try {
                connection.close(); // sulge ühendus
                System.out.println("Ühendus suletud"); // Test
            } catch (SQLException e) {
                System.err.println("Viga ühenduse sulgemisel: " + e.getMessage());
            }
        }
    }

    /**
     * Mängija andmete lisamine andmebaasi tabelisse
     * @param name nimi
     * @param time mänguaeg sekundites
     * @param clicks klikkimiste arv
     * @param boardSize mängulaua suurus
     * @param played mängu kuupäeva ja kellaaeg
     */
    public void insert(String name, int time, int clicks, int boardSize, String played) {
        String sql = "INSERT INTO " + tableName + " (name, time, clicks, board_size, game_time) VALUES (?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name); // Küsimärgile nr 1 vastab name
            stmt.setInt(2, time);
            stmt.setInt(3, clicks);
            stmt.setInt(4, boardSize);
            stmt.setString(5, played);
            stmt.executeUpdate(); // Käivitab päriselt lisamise (insert into....)
        } catch (SQLException e) {
            //throw new RuntimeException(e);
            System.err.println("Viga andmete lisamisel: " + e.getMessage());
        }
    }

    public ArrayList<ScoreData> select(int boardSize) {
        ArrayList<ScoreData> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE board_size = ? ORDER BY time,clicks, game_time;"; // ? tahab väärtust saada ja see on see laua suurus

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, boardSize);
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) { // Loe järgmine kirje
                    String name = rs.getString("name");
                    int time = rs.getInt("time");
                    int clicks = rs.getInt("clicks");
                    int size = rs.getInt("board_size");
                    String gameTime = rs.getString("game_time");
                    //Teisendame gameTime => LocalDateTime
                    LocalDateTime played = LocalDateTime.parse(gameTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    ScoreData data = new ScoreData(name, time, clicks, size, played);
                    results.add(data);
            }

            }

        } catch (SQLException e) {
            //throw new RuntimeException(e);
            System.err.println("Viga SELECT päringus: " + e.getMessage());
        }
        return results;
    }

}
