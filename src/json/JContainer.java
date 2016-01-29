/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author paulz
 */
public class JContainer {
    
    private int serialNumber;
    private  String secret;
    private BlockingQueue<JPoint> points = new LinkedBlockingQueue<>();

    public JContainer(int serialNumber, String secret) {
        this.serialNumber = serialNumber;
        this.secret = secret;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public BlockingQueue<JPoint> getPoints() {
        return points;
    }

    public void setPoints(BlockingQueue<JPoint> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return serialNumber +";"+secret + ";" + points;
    }
    
    
    
}
