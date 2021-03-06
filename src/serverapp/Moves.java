/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapp;

import java.sql.Time;
import java.time.LocalTime;

/**
 *
 * @author hassan
 */
class Moves
{
    private int playerID;
    private LocalTime currentTime;
    private long delayTimeSec;
    private String moveType;
    private String blockNo;
    public Moves(int id,LocalTime current,String type,String number)
    {
        playerID = id;
        currentTime = current;
        //delayTimeSec = delay;
        moveType = type;
        blockNo = number;
    }
    public void setDelay(long delay)
    {
        delayTimeSec = delay;
    }
    
    public int getPalyerID()
    {
        return playerID; 
    }
    public long getdelayTimeSec()
    {
        return delayTimeSec; 
    }
    public LocalTime getCurrentTime()
    {
        return currentTime;
    }
    public String getMoveType()
    {
        return moveType;
    }
    public String getBlockNumber()
    {
        return blockNo;
    }
}