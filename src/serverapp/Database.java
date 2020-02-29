/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hassan Khamis
 */
public class Database {

    public Connection openConnection() {
        Connection con;
        try {

            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javaproject", "root", "root");
        } catch (ClassNotFoundException ex) {
            con = null;
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            con = null;
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

    public void closeConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void registerPlayerOnline(Connection con, int playerID, int onlineStatus) {
        try {
            PreparedStatement stmt = con.prepareStatement("update player set IsOnline = ? where ID = ?");
            stmt.setInt(1, onlineStatus);
            stmt.setInt(2, playerID);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void registerPlayerRequested(Connection con, int playerID, int requestedStatus) {
        try {
            PreparedStatement stmt = con.prepareStatement("update player set IsRequest = ? where ID = ?");
            stmt.setInt(1, requestedStatus);
            stmt.setInt(2, playerID);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    public void saveGameMoves(Connection con,int player1ID,int timeToPlay, String moveType,int cellNo,int gameID)

    public void saveGameMoves(Connection con, ArrayList<Moves> movesList, long gameID) {
        PreparedStatement stmt = null;

        try {
            for (int i = 1; i < movesList.size(); i++) {
                stmt = con.prepareStatement("insert into gamemove set PlayerID = ? , TimeToPlay = ? , MoveType = ? , CellNo = ? , GameID = ?");
                stmt.setInt(1, movesList.get(i).getPalyerID());
                stmt.setInt(2, (int) movesList.get(i).getdelayTimeSec());
                stmt.setString(3, movesList.get(i).getMoveType());
                stmt.setString(4, movesList.get(i).getBlockNumber());
                stmt.setLong(5, gameID);
                stmt.execute();
            }

        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public long saveGame(Connection con, GameResult gameResult) {
        long gameID = 0;
        try {

            PreparedStatement stmt = con.prepareStatement("insert into game set Player1ID = ? , Player2ID = ? , WinnerID = ? , GameLevelID = ? , IsCompleted = ?, WhoSaved = ?");
            stmt.setInt(1, gameResult.player1ID);
            stmt.setInt(2, gameResult.player2ID);
            stmt.setInt(3, gameResult.WinnerID);
            stmt.setInt(4, gameResult.GameLevelID);
            stmt.setInt(5, gameResult.IsCompleted);
            stmt.setInt(6, gameResult.WhoSaved);

            stmt.execute();
            gameID = getGameID(con);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gameID;
    }

    private long getGameID(Connection con) {
        long gameID = 0;
        try {

            PreparedStatement stmt = con.prepareStatement("select max(ID) from game");
            ResultSet rs = stmt.executeQuery();
            if (rs.first()) {
                gameID = rs.getLong(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gameID;
    }

    int getPlayerIDUsingPort(Connection con,int port) {
        int playerID = 0;
        try {
            PreparedStatement stmt = con.prepareStatement("select ID from player where portID = ?");
            stmt.setInt(1, port);
            ResultSet rs;
            rs = stmt.executeQuery();
            rs.next();
            playerID = rs.getInt("ID");
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return playerID;
    }
}
