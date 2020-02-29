/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import static serverapp.ServerApp.gpList;

/**
 *
 * @author Hassan Khamis
 */
public class OnlinePlayers {

    int playerID;
    Socket playerSocket;
    int port;
    String status;
    DataInputStream dis;
    DataOutputStream dos;

    OnlinePlayers(Socket virtualSocket, int port) {
        playerSocket = virtualSocket;
        this.port = port;
        playerID = 0;
        status = "available";
        try {

            dis = new DataInputStream(playerSocket.getInputStream());
            dos = new DataOutputStream(playerSocket.getOutputStream());

        } catch (IOException ex) {
            Logger.getLogger(OnlinePlayers.class.getName()).log(Level.SEVERE, null, ex);
        }
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!playerSocket.isClosed()) {
//                    if (playerID == 0) {
//                        try {
//
//                            //SharedData.client = new Socket("127.0.0.1",5000);
//                            //port = SharedData.client.getLocalPort();
//                            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javaproject", "root", "root");
//                            PreparedStatement stmt = con.prepareStatement("select ID from player where portID = ?");
//                            stmt.setInt(1, port);
//                            ResultSet rs;
//                            rs = stmt.executeQuery();
//                            rs.next();
//                            playerID = rs.getInt("ID");
//                            con.close();
//                            System.out.println("player ID = done");
//                        } catch (SQLException ex) {
//                            System.out.println("NotFound Yet in DB I Will Try Again");
//                        }
//                    } else {
                    try {
                        if (status.equals("available")) {
                            boolean isFound = false;
                            String msg = dis.readUTF();
                            String[] strArr = msg.split("-");
                            if (strArr[1].equals("ID")) {
                                playerID = Integer.parseInt(strArr[0]);
                                Database db = new Database();
                                Connection con = db.openConnection();
                                db.registerPlayerOnline(con, playerID, 1);
                                db.closeConnection(con);
                                continue;
                            }
                            if (playerID > 0) {
                                switch (strArr[1]) {
                                    case "random":
                                        for (int i = 0; i < ServerApp.gpList.size(); i++) {
                                            if (ServerApp.gpList.get(i).desiredPayer == 0 && strArr[0].equals(ServerApp.gpList.get(i).gameOption)) {
                                                ServerApp.gpList.get(i).vrtualSocket2 = playerSocket;
                                                System.out.println("second");
                                                System.out.println("will add room");
                                                ServerApp.gpList.get(i).desiredPayer = -1;
                                                DataOutputStream dos2 = new DataOutputStream(gpList.get(i).vrtualSocket2.getOutputStream());
//                                                dos2.writeUTF("PlayRandom-" + playerID);
                                                isFound = true;
                                                if (strArr[0].equals("tic")) {
                                                    dos2.writeUTF("PlayRandom-" + playerID);
                                                    new ServerGameHandler(gpList.get(i).vrtualSocket1, gpList.get(i).vrtualSocket2);
                                                } else if (strArr[0].equals("connect")) {
                                                    new ServerGameHandlerConnectFour(gpList.get(i).vrtualSocket1, gpList.get(i).vrtualSocket2);
                                                } else if (ServerApp.gpList.get(i).gameOption.equals("GuesTheWord")) {
                                                    new ServerGameHandlerGuesTheWord(gpList.get(i).vrtualSocket1, gpList.get(i).vrtualSocket2);
                                                }
                                                break;
                                            }
                                        }
                                        if (!isFound) {
                                            GamePlayers gp = new GamePlayers(playerSocket, 0, strArr[0]);
                                            ServerApp.gpList.add(gp);
                                            if (gp.gameOption.equals("tic")) {
                                                dos.writeUTF("PlayRandom-" + playerID);
                                            }
                                            System.out.println("First");

                                        }
                                        status = "pending";
                                        break;
                                    case "RequestAnswer":
                                        switch (strArr[0]) {
                                            case "OK":

//                                                    gp.vrtualSocket2 = s;
//                                                    System.out.println("will add room");
//                                                    gp.desiredPayer = -1;
//                                                    if (strArr[0].equals("tic")) {
////                                                                new ServerGameHandler(gpList.get(i).vrtualSocket1, gpList.get(i).vrtualSocket2);
//                                                        new ServerGameHandler(gp.vrtualSocket1, gp.vrtualSocket2);
//                                                    } else if (strArr[0].equals("connect")) {
////                                                                new ServerGameHandlerConnectFour(gpList.get(i).vrtualSocket1, gpList.get(i).vrtualSocket2);
//                                                        new ServerGameHandlerConnectFour(gp.vrtualSocket1, gp.vrtualSocket2);
//                                                    }
                                                for (int i = 0; i < ServerApp.gpList.size(); i++) {
                                                    if (ServerApp.gpList.get(i).desiredPayer == playerID) {
                                                        ServerApp.gpList.get(i).vrtualSocket2 = playerSocket;
                                                        System.out.println("second");
                                                        System.out.println("will add room");
                                                        ServerApp.gpList.get(i).desiredPayer = -1;
//                                                        isFound = true;
//                                                        status = "pending";
                                                        if (ServerApp.gpList.get(i).gameOption.equals("tic")) {
                                                            new ServerGameHandler(gpList.get(i).vrtualSocket1, gpList.get(i).vrtualSocket2);
                                                        } else if (ServerApp.gpList.get(i).gameOption.equals("connect")) {
                                                            new ServerGameHandlerConnectFour(gpList.get(i).vrtualSocket1, gpList.get(i).vrtualSocket2);
                                                        } else if (ServerApp.gpList.get(i).gameOption.equals("GuesTheWord")) {
                                                            new ServerGameHandlerConnectFour(gpList.get(i).vrtualSocket1, gpList.get(i).vrtualSocket2);
                                                        }
                                                        break;
                                                    }
                                                }
                                                break;
                                            case "NO":
                                                Database db = new Database();
                                                Connection con = db.openConnection();
                                                db.registerPlayerRequested(con, playerID, 0);
                                                db.closeConnection(con);
                                                dos.writeUTF("requestRefused");
                                                break;
                                        }
                                        status = "pending";
                                        break;
                                    default:
                                        GamePlayers gp = new GamePlayers(playerSocket, Integer.parseInt(strArr[1]), strArr[0]);
                                        ServerApp.gpList.add(gp);
                                        Socket s;
                                        try {
                                            int player2id = Integer.parseInt(strArr[1]);
//                                            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javaproject", "root", "root");
                                            Database db = new Database();
                                            Connection con = db.openConnection();
                                            playerID = db.getPlayerIDUsingPort(con, port);
//                                            PreparedStatement stmt = con.prepareStatement("select ID from player where portID = ?");
//                                            stmt.setInt(1, port);
//                                            ResultSet rs;
//                                            rs = stmt.executeQuery();
//                                            rs.next();
//                                            playerID = rs.getInt("ID");
//                                            con.close();
                                            db.registerPlayerRequested(con, playerID, 1);
                                            db.closeConnection(con);
                                            s = ServerApp.opList.stream().filter(f -> f.playerID == player2id).map(m -> m.playerSocket).findFirst().get();
                                            DataOutputStream dos2 = new DataOutputStream(s.getOutputStream());
//                                            DataInputStream dis2 = new DataInputStream(s.getInputStream());
//                                            ObjectOutputStream socketObj =  new ObjectOutputStream(playerSocket.getOutputStream());
                                            dos2.writeUTF("Playwith-" + playerID);
                                            dos.writeUTF("Playwith-" + playerID);

//                                            socketObj.writeObject(playerSocket);
                                            //String friendMsg = dis2.readUTF();
                                        } catch (Exception e) {
                                            System.out.println("player closed from one minute ago");
                                            dos.writeUTF("player closed from one minute ago");
                                        }
                                        status = "pending";
                                        break;
                                }
                            }
//                                for (int i = 0; i < ServerApp.opList.size(); i++) {
//                                    if (Integer.parseInt(msg) == ServerApp.opList.get(i).playerID) {
//                                        DataOutputStream dos2 = new DataOutputStream(ServerApp.opList.get(i).playerSocket.getOutputStream());
//                                        DataInputStream dis2 = new DataInputStream(ServerApp.opList.get(i).playerSocket.getInputStream());
//                                        dos2.writeUTF("Playwith-" + playerID);
//                                        String friendMsg = dis2.readUTF();
//                                        switch (friendMsg) {
//                                            case "OK":
//
//                                                ServerApp.gpList.get(i).vrtualSocket2 = playerSocket;
//                                                System.out.println("will add room");
//                                                ServerApp.gpList.get(i).desiredPayer = -1;
//                                                new ServerGameHandler(gpList.get(i).vrtualSocket1, gpList.get(i).vrtualSocket2);
//                                                break;
//                                            case "NO":
//                                                dos.writeUTF("requestRefused");
//                                                break;
//                                        }
//                                        break;
//                                    }
//                                    try {
//                                        TimeUnit.SECONDS.sleep(1);
//                                    } catch (InterruptedException ex) {
//                                        Logger.getLogger(OnlinePlayers.class.getName()).log(Level.SEVERE, null, ex);
//                                    }
//                                }
//                                break;
                        }
                    } catch (IOException ex) {
                        try {
                            Database db = new Database();
                            Connection con = db.openConnection();
                            db.registerPlayerOnline(con, playerID, 0);
                            db.closeConnection(con);
                            playerSocket.close();

                            OnlinePlayers opObj = ServerApp.opList.stream().filter(f -> f.playerID == playerID).findFirst().get();
                            ServerApp.opList.remove(opObj);
                            System.out.println("player " + playerID + " Closed");
//                                updateCloseInDB();
                        } catch (IOException ex1) {
                            Logger.getLogger(OnlinePlayers.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    }

//                    }
                }
            }
        });
        th.start();
        /*Thread hearFromPlayer = new Thread(() -> {
            try {
                while (status.equals("available")) {
                    dis.readUTF();

                }
            } catch (IOException ex) {
                Logger.getLogger(OnlinePlayers.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        hearFromPlayer.start();*/
    }

}
