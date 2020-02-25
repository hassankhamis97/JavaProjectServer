/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;

import java.net.Socket;

/**
 *
 * @author Hassan Khamis
 */
public class GamePlayers {
    Socket vrtualSocket1;
    Socket vrtualSocket2;
    int desiredPayer;
    String gameOption;

    GamePlayers(Socket playerSocket, int dplayer,String option) {
        vrtualSocket1 = playerSocket;
        desiredPayer = dplayer;
       gameOption = option;
    }
}
