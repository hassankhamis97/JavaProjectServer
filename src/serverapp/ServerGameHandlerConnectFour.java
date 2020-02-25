/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author hassan
 */
public class ServerGameHandlerConnectFour {

    DataInputStream dis1;
    DataInputStream dis2;
    DataOutputStream dos1;
    DataOutputStream dos2;
    Connection con;
    ResultSet rs;
    PreparedStatement ps;
    boolean isPlayer1Wait = false;
    List<Moves> mvlst = new ArrayList<Moves>();
    Moves mv;
    Thread th1;
    Thread th2;
    //boolean gameEnd =false;
    PlayerStatus player1Ready = PlayerStatus.Ready;
    PlayerStatus player2Ready = PlayerStatus.Ready;
    boolean isFinish;

    public ServerGameHandlerConnectFour(Socket virtualSocketPlayer1, Socket virtualSocketPlayer2) {
        try {
            //        try {
            dis1 = new DataInputStream(virtualSocketPlayer1.getInputStream());
            dis2 = new DataInputStream(virtualSocketPlayer2.getInputStream());
            dos1 = new DataOutputStream(virtualSocketPlayer1.getOutputStream());
            dos2 = new DataOutputStream(virtualSocketPlayer2.getOutputStream());
            dos1.writeUTF("startGame-connect");
            dos2.writeUTF("startGame-connect");
            isFinish = false;
//            mv = new Moves("0", LocalTime.now(), "-", "0");
//            mvlst.add(mv);
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerGameHandlerConnectFour.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {

                con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javaproject", "root", "root");
            } catch (SQLException ex) {
                Logger.getLogger(ServerGameHandlerConnectFour.class.getName()).log(Level.SEVERE, null, ex);
            }
            Thread th1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!isFinish) {

                        String msgP1 = "closed";
                        try {
                            msgP1 = dis1.readUTF();

                        } catch (IOException ex) {
                            try {
                                dos2.writeUTF("11-closed");
                                isFinish = true;
                                virtualSocketPlayer1.close();
                            } catch (IOException ex1) {
                                Logger.getLogger(ServerGameHandlerConnectFour.class.getName()).log(Level.SEVERE, null, ex1);
                            }

                        }
                        if (msgP1.equals("closed")) {
                            try {
                                dos2.writeUTF("11-closed");
                                isFinish = true;
                                virtualSocketPlayer1.close();
                            } catch (IOException ex) {
                                Logger.getLogger(ServerGameHandlerConnectFour.class.getName()).log(Level.SEVERE, null, ex);

                            }
                        } else {
                            String[] msgList = {};
                            msgList = msgP1.split("-");

                            if (msgList[0].equals("msg")) {
                                try {
                                    dos1.writeUTF("msg-own-" + msgList[1]);
                                    dos2.writeUTF("msg-notOwn-" + msgList[1]);
                                } catch (IOException ex) {
                                    Logger.getLogger(ServerGameHandlerConnectFour.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                if (player1Ready == PlayerStatus.Ready && player2Ready == PlayerStatus.Ready) {

                                    /*if (!msgList[0].equals("ready")) {
                                        mv = new Moves(msgList[1], LocalTime.now(), "X", msgList[0]);
                                        mvlst.add(mv);
                                        calculateDelayTime();

                                    }*/
                                    try {
                                        System.out.println("false-" + msgList[1]);
                                        
                                        dos2.writeUTF("true-" + msgList[1]);
                                        dos1.writeUTF("true-" + msgList[1]);
                                        
                                        dos1.writeUTF("0-wait");
                                        dos2.writeUTF("0-start");
//                                        if (mvlst.size() >= 5) {
//                                            checkWinner(1);
//                                        }
                                    } catch (IOException ex) {
                                        Logger.getLogger(ServerGameHandlerConnectFour.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {

                                    if (player1Ready == PlayerStatus.NotDecided) {
                                        if (msgP1.equals("ready")) {
                                            player1Ready = PlayerStatus.Ready;
                                        } else if (msgP1.equals("refused")) {
                                            player1Ready = PlayerStatus.Refused;
                                            //endRoom(1);
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            });
            Thread th2 = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (!isFinish) {

                        String msgP2 = "closed";
                        try {
                            msgP2 = dis2.readUTF();
                        } catch (IOException ex) {
                            try {
                                isFinish = true;
                                dos1.writeUTF("11-closed");
                                virtualSocketPlayer2.close();
                            } catch (IOException ex1) {
                                Logger.getLogger(ServerGameHandlerConnectFour.class.getName()).log(Level.SEVERE, null, ex1);
                            }
                        }
                        if (msgP2.equals("closed")) {
                            try {
                                dos1.writeUTF("11-closed");
                                isFinish = true;
                                virtualSocketPlayer2.close();
                            } catch (IOException ex) {
                                Logger.getLogger(ServerGameHandlerConnectFour.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            String[] msgList = {};
                            msgList = msgP2.split("-");

                            if (msgList[0].equals("msg")) {
                                try {
                                    dos1.writeUTF("msg-notOwn-" + msgList[1]);
                                    dos2.writeUTF("msg-own-" + msgList[1]);
                                } catch (IOException ex) {
                                    Logger.getLogger(ServerGameHandlerConnectFour.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                if (player1Ready == PlayerStatus.Ready && player2Ready == PlayerStatus.Ready)//isPlayer1Wait
                                {

                                    //msgList = msgP2.split("-");

                                   /* if (!msgList[0].equals("ready")) {
                                        mv = new Moves(msgList[1], LocalTime.now(), "O", msgList[0]);
                                        mvlst.add(mv);
                                        calculateDelayTime();
                                    }*/
                                    try {
                                        //System.out.println("thread2-fun");
                                        dos2.writeUTF("false-" + msgList[1]);
                                        dos1.writeUTF("false-" + msgList[1]);
                                        dos2.writeUTF("0-wait");
                                        dos1.writeUTF("0-start");
//                                        if (mvlst.size() >= 5) {
//                                            checkWinner(2);
//                                        }
                                    } catch (IOException ex) {
                                        Logger.getLogger(ServerGameHandlerConnectFour.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    isPlayer1Wait = false;
                                } else {

                                    if (player2Ready == PlayerStatus.NotDecided) {
                                        if (msgP2.equals("ready")) {
                                            player2Ready = PlayerStatus.Ready;
                                        } else if (msgP2.equals("refused")) {
                                            player2Ready = PlayerStatus.Refused;
                                            //endRoom(2);
                                        }
                                    }
                                }
                            }

                        }

                    }
                }
            });
            th1.start();
            th2.start();
            dos1.writeUTF("0-start");
            dos2.writeUTF("0-wait");
        } catch (IOException ex) {
            Logger.getLogger(ServerGameHandlerConnectFour.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

//    public void calculateDelayTime() {
//        LocalTime t2 = mvlst.get(mvlst.size() - 1).getCurrentTime();
//        LocalTime t1 = mvlst.get(mvlst.size() - 2).getCurrentTime();
//        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
//        try {
//
//            Date d1 = f.parse(t1.toString());
//            Date d2 = f.parse(t2.toString());
//            long dif = d2.getTime() - d1.getTime();
//            mvlst.get(mvlst.size() - 1).setDelay(dif);
//            //System.out.println(dif);
//        } catch (ParseException ex) {
//            Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }
//
//    private void checkWinner(int playerNo) {
//        String filterType = playerNo == 1 ? "X" : "O";
//
//        String result = mvlst.stream().filter(s -> s.getMoveType().equals(filterType)).map(f -> f.getBlockNumber()).collect(Collectors.toList()).toString();
//        if (result.contains("1") && result.contains("2") && result.contains("3")
//                || result.contains("1") && result.contains("4") && result.contains("7")
//                || result.contains("1") && result.contains("5") && result.contains("9")
//                || result.contains("2") && result.contains("5") && result.contains("8")
//                || result.contains("3") && result.contains("6") && result.contains("9")
//                || result.contains("3") && result.contains("6") && result.contains("9")
//                || result.contains("3") && result.contains("5") && result.contains("7")
//                || result.contains("4") && result.contains("5") && result.contains("6")
//                || result.contains("7") && result.contains("8") && result.contains("9")) {
//            try {
//                if (playerNo == 1) {
//                    dos1.writeUTF("10-winner");
//                    dos2.writeUTF("10-loser");
//                    System.out.println("Player 1 win");
//                } else {
//                    dos2.writeUTF("10-winner");
//                    dos1.writeUTF("10-loser");
//                    System.out.println("Player 2 win");
//                }
//                player1Ready = PlayerStatus.NotDecided;
//                player2Ready = PlayerStatus.NotDecided;
//            } catch (IOException ex) {
//                Logger.getLogger(ServerGameHandlerConnectFour.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//            if (mvlst.size() == 10) {
//                try {
//                    dos1.writeUTF("10-draw");
//                    dos2.writeUTF("10-draw");
//                    System.out.println("draw");
//                    player1Ready = PlayerStatus.NotDecided;
//                    player2Ready = PlayerStatus.NotDecided;
//                } catch (IOException ex) {
//                    Logger.getLogger(ServerGameHandlerConnectFour.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//
//    }
//
//    private void endRoom(int playerNo) {
//
//        
//    }
    /*@Override
        public void run() {
                while(true)
                {
                        try {   
                                
                                String msgP1 = dis1.readLine();
                                System.out.println(msgP1);
                                String msgP2 = dis2.readLine();
                                System.out.println(msgP2);

                        } catch (IOException ex) {
                                Logger.getLogger(ServerGameHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                }
        }*/

}
