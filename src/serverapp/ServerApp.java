/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hassan
 */
public class ServerApp {

    /**
     * @param args the command line arguments
     */
    ServerSocket server;
    //DataInputStream dis;
    //DataOutputStream dos;
    //PrintStream ps;
    Socket virtualSocket;
    static List<GamePlayers> gpList = new ArrayList<GamePlayers>();
    GamePlayers gp;
    static List<OnlinePlayers> opList = new ArrayList<OnlinePlayers>();
//boolean begin = true;

    public ServerApp() {
//        gp = new GamePlayers();
        serverEstablishConnection();
    }

    public static void main(String[] args) {
        new ServerApp();

    }

    private void serverEstablishConnection() {
        try {
            // TODO code application logic here
                           
                server = new ServerSocket(5000);
                
            
        } catch (IOException ex) {
            Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {

            try {
                virtualSocket = server.accept();
            } catch (IOException ex) {
                Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
            }
//            Thread mainThread;
//            mainThread = new Thread(() -> {
//                try {
//                    while (true) {                        
//                        
//                        
//                    }
//                } catch (IOException ex) {
//                    Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            });
//            if(begin)
//            {
//                mainThread.start();
//                
//            }
            //virtualSocket = server.accept();
            //mainThread.start();
            /*try {
                    dis = new DataInputStream(virtualSocket.getInputStream());
                    ps = new PrintStream(virtualSocket.getOutputStream());
                    } catch (IOException ex) {
                    Logger.getLogger(ServerApp.class.getName()).log(Level.SEVERE, null, ex);
                    }*/
            OnlinePlayers onlineP = new OnlinePlayers(virtualSocket, virtualSocket.getPort());
            opList.add(onlineP);
            //                               int port1 = virtualSocket.getLocalPort();
//                                int port2 = virtualSocket.getPort();

//new Thread();
//            if (gp.vrtualSocket1 == null) {
//
//                gp.vrtualSocket1 = virtualSocket;
//            } else if (gp.vrtualSocket2 == null) {
//                gp.vrtualSocket2 = virtualSocket;
//                gpList.add(gp);
//                /*Thread th = new Thread(new Runnable() {
//    @Override
//    public void run() {*/
//                System.out.println("will add room");
//                ServerGameHandler s = new ServerGameHandler(gpList.get(gpList.size() - 1).vrtualSocket1, gpList.get(gpList.size() - 1).vrtualSocket2);
//                //}
//                //});
//                //th.start();
////                gp = new GamePlayers();
//            }
        }

    }
}
//
//class GamePlayers {
//
//    /*SocketChannel socketChanel1 = null;
//        SocketChannel socketChanel2= null;*/
//    Socket vrtualSocket1 = null;
//    Socket vrtualSocket2 = null;
//    int desiredPayer;
//}
