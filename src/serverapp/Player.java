/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hassan Khamis
 */
public class Player implements Runnable {

    List<Moves> mvlst = new ArrayList<Moves>();
    Moves mv;
    DataInputStream dis;
    DataOutputStream dos;
    DataOutputStream dos2;
    PlayerStatus isPlayerReady = PlayerStatus.Ready;
    Player p2;
    Socket player2Socket;

    Player(Socket virtualSocketPlayer1, String type) {
        try {
            dis = new DataInputStream(virtualSocketPlayer1.getInputStream());
            dos = new DataOutputStream(virtualSocketPlayer1.getOutputStream());

        } catch (IOException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startGame() {
        if(player2Socket != null)
        {
            try {
                dos2 = new DataOutputStream(player2Socket.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
             
            run();
        }
    }

    @Override
    public void run() {
        while (true) {
            String msgP1;
            if (isPlayerReady == PlayerStatus.Ready) {
                String[] msgList = {};
                try {
                    msgP1 = dis.readUTF();
                    msgList = msgP1.split("-");
                } catch (IOException ex) {
                    Logger.getLogger(ServerGameHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
               
//                try {
//                    dos2.writeUTF(msgList[0] + "-X");
//                    dos1.writeUTF(msgList[0] + "-X");
//                    dos1.writeUTF("0-wait");
//                    dos2.writeUTF("0-start");
//                    if (mvlst.size() >= 5) {
//                        checkWinner(1);
//                    }
//                    /*dos2.writeUTF("X");
//                                                dos1.writeUTF("X");*/
//                } catch (IOException ex) {
//                    Logger.getLogger(ServerGameHandler.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                /*ServerGameHandler.this.ps2.println(msgList[0] + "-X");
//                                        ServerGameHandler.this.ps1.println(msgList[0] + "-X");*/
//                //isPlayer1Wait = true;
//
//                //ServerGameHandler.this.ps1.println("wait");
//                //dos1.writeUTF("wait");
//            } else {
//
//                try {
//                    if (player1Ready == PlayerStatus.NotDecided) {
//                        msgP1 = dis1.readUTF();
//                        if (msgP1.equals("ready")) {
//                            player1Ready = PlayerStatus.Ready;
//                        } else if (msgP1.equals("refused")) {
//                            player1Ready = PlayerStatus.Refused;
//                            endRoom(1);
//                        }
//                    } else if (player2Ready == PlayerStatus.NotDecided) {
//                        dos1.writeUTF("wait other");
//                        /*msgP1 = dis2.readUTF();
//                                                                        if (msgP1.equals("ready")) {
//                                                                                player2Ready = PlayerStatus.Ready;
//                                                                        } else if (msgP1.equals("refused")) {
//                                                                                player2Ready = PlayerStatus.Refused;
//                                                                                endRoom(1);
//                                                                        }*/
//                        //break;
//                        //th2.start();
//                    }
//                } catch (IOException ex) {
//                    Logger.getLogger(ServerGameHandler.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        }
    }

}
