package com.svalero.aliensonearth.manager;

import java.sql.*;

public class DatabaseManager {
    private Connection conn;

    public void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:game_data.db");
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            System.out.println("SQLite connected.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        String sql1 = "CREATE TABLE IF NOT EXISTS player_progress (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL, " +
            "higher_level_played INTEGER NOT NULL, " +
            "player_level INTEGER NOT NULL, " +
            "global_score INTEGER NOT NULL" +
            ")";

        String sql2 = "CREATE TABLE IF NOT EXISTS game_progress (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "player_id INTEGER NOT NULL, " +
            "game_level INTEGER NOT NULL, " +
            "score INTEGER NOT NULL, " +
            "creation_timestamp TEXT NOT NULL, " +
            "FOREIGN KEY (player_id) REFERENCES player_progress(id) ON DELETE CASCADE" +
            ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql1);
            stmt.execute(sql2);
            System.out.println("Tables created/verified.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveGameProgress(int playerId, int gameLevel, int score) {
        String sql = "INSERT INTO game_progress(player_id, game_level, score, creation_timestamp) " +
            "VALUES(?, ?, ?, datetime('now'))";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, playerId);
            pstmt.setInt(2, gameLevel);
            pstmt.setInt(3, score);
            pstmt.executeUpdate();
            System.out.println("Progress saved for player " + playerId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerProgress(String name, int higherLevelPlayed, int playerLevel, int globalScore, int playerId) {
        if(playerId == -1){
            String sql = "INSERT INTO player_progress(name, higher_level_played, player_level, global_score) VALUES(?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setInt(2, higherLevelPlayed);
                pstmt.setInt(3, playerLevel);
                pstmt.setInt(4, globalScore);
                pstmt.executeUpdate();
                System.out.println("Progress saved.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            String sql = "UPDATE player_progress SET higher_level_played = ?, player_level = ?, global_score = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, higherLevelPlayed);
                pstmt.setInt(2, playerLevel);
                pstmt.setInt(3, globalScore);
                pstmt.setInt(4, playerId);
                pstmt.executeUpdate();
                System.out.println("Progress saved.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public int getPlayerIdByName(String name) {
        String sql = "SELECT id FROM player_progress WHERE name = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getHigherLevelPlayed(int playerId) {
        String sql = "SELECT higher_level_played FROM player_progress WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, playerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("higher_level_played");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getPlayerLevel(int playerId) {
        String sql = "SELECT player_level FROM player_progress WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, playerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("player_level");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void showGameProgress() {
        String sql = "SELECT * FROM game_progress";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Game Level: " + rs.getInt("game_level") +
                    ", Score: " + rs.getInt("score") +
                    ", Made at: " + rs.getString("creation_timestamp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showPlayerProgress() {
        String sql = "SELECT * FROM player_progress";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Name: " + rs.getString("name") +
                    ", Higher Level Played: " + rs.getInt("higher_level_played") +
                    ", Player Level: " + rs.getInt("player_level") +
                    ", Score: " + rs.getInt("global_score"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
