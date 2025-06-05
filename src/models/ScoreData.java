package models;

import java.time.LocalDateTime;

public class ScoreData implements Comparable<ScoreData> {
    private String name; // Mängija nimi
    private int time; //Mängu aeg sekundites nt 68 (01:08)
    private int clicks; // Mitu korda on klikitud
    private int board; // Mängulaua suurus (10)
    private LocalDateTime playedTime; // Mängu aeg kuupäev, kellaaeg

    public ScoreData(String name, int time, int clicks, int board, LocalDateTime playedTime) { // Konstruktor
        this.name = name;
        this.time = time;
        this.clicks = clicks;
        this.board = board;
        this.playedTime = playedTime;
    }



    //GETTERS
    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public int getClicks() {
        return clicks;
    }

    public int getBoard() {
        return board;
    }

    public LocalDateTime getPlayedTime() {
        return playedTime;
    }

    @Override
    public String toString() {
        return "ScoreData{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", clicks=" + clicks +
                ", board=" + board +
                ", playedTime=" + playedTime +
                '}';
    }

    /**
     * Sorteerib time, clicks ja playedTime järgi kasvavalt
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(ScoreData o) {
        // Kasutamine mujal: Collections.sort(MASSIIVINIMI)
        int cmp = Integer.compare(this.time, o.time);
        if (cmp != 0) return cmp;

        cmp = Integer.compare(this.clicks, o.clicks);
        if (cmp != 0) return cmp;

        return this.playedTime.compareTo(o.playedTime);
    }

    /**
     * Vormindab etteantud aja sekundid kujule MM:SS
     * @param seconds sekundid kujul 98
     * @return vormindatud aeg 01:38
     */
    //Teeme sekundid inimlikuks
    public String formatGameTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }
}
