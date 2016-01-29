/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import gps.pgsd4java.types.TPVObject;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.commons.net.ftp.FTPClient;
import java.util.UUID;
import json.JContainer;
import json.JPoint;

/**
 *
 * @author paulz
 */
public class BL {


    private boolean updateing;
    //private Logger
    private static final Logger LOGGER = Logger.getLogger(BL.class.getName());
    private double latOld = -1, latNew = -1, lonOld = -1, lonNew = -1, d = 0;
    private DataManager data_manager;
    private saveThread saveThread;

    
    private boolean reconnecting = false;
    private boolean reconnectWithoutEMF=false;
    private boolean loadDataOffline=true;
    
    private int serialNumber =1234;
    private String trackId;
    private long trackTimestamp;
    private JContainer container;
    
    public BL() {
        trackId = UUID.randomUUID().toString();
        trackTimestamp = System.currentTimeMillis();
        container = new JContainer(serialNumber, "sowas von geheim22");

        data_manager = new DataManager();
        
        
        saveThread = new saveThread();
        saveThread.start();
    }

    public void test(TPVObject tpv) {
        /**
         * Neue Idee alles zu speichern
         */
        /*Umwandlung von Double in LDT Problem das es zu ungenau ist
         Double test = (tpv.getTimestamp());
         long int_timestamp = test.longValue();
         LocalDateTime ldt = LocalDateTime.ofEpochSecond(int_timestamp, 0, ZoneOffset.UTC);
         */


        System.out.println(tpv.getTimestamp() + " Longitude: " + tpv.getLatitude() + " Latitude: " + tpv.getLongitude());
//TODO:
        /**
         * Algorithmus um die gefahrenen Km zu messen
         */
        //TODO
        //NAN
        //2. Thread löschen
        
        if(tpv.getSpeed()>2.77)
        {
            if (latOld == -1 && lonOld == -1) {
            latNew = tpv.getLatitude();
            lonNew = tpv.getLongitude();
        }
        latOld = latNew;
        lonOld = lonNew;
        latNew = tpv.getLatitude();
        lonNew = tpv.getLongitude();

        double R = 6371000; // metres

        double dLat = Math.toRadians(latNew - latOld);
        double dLon = Math.toRadians(lonNew - lonOld);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latOld)) * Math.cos(Math.toRadians(latNew))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            d = d + (R * c);
            System.out.println("Distance:"+d +"Speed:"+tpv.getSpeed());
        }

        JPoint jpoint = new JPoint(trackId, trackTimestamp, serialNumber, UUID.randomUUID().toString(), System.currentTimeMillis(), tpv.getLatitude(), tpv.getLongitude(), d, tpv.getSpeed());
        

        try {
            container.getPoints().put(jpoint);
        } catch (InterruptedException ex) {
            Logger.getLogger(BL.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            try {
                data_manager.writeFile(container.getPoints());
            } catch (ParseException ex) {
                Logger.getLogger(BL.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(BL.class.getName()).log(Level.SEVERE, null, ex);
            }

    }

    /**
     * Upload FTP Server
     *
     * @param filename
     * @throws IOException
     */
//    public void upload(String filename) throws IOException 
//    {
//        FTPClient client = new FTPClient();
//        FileInputStream fis = null;
//
//        client.connect("ftp.sunlime.at", 990);
//        client.login("admin", "secret");
//
//        fis = new FileInputStream(filename);
//        client.storeFile(filename, fis);
//        client.logout();
//        fis.close();
//    }

    class saveThread extends Thread 
    {

        @Override
        public void run() {
            System.out.println("-------------------------Thread gestartet-----------------------------------");
            synchronized (this) {
                if(loadDataOffline)
                {
                  loadOfflineDataInList();
                }
                while (!Thread.currentThread().isInterrupted()) {
                    processData();
                    try {
                        saveThread.wait(1000);

                    } catch (InterruptedException ex) {
                        System.out.println("Thread exception");
                        System.out.println(ex.toString());
                    }
                }
                /**
                 * schleife in hauptmethode process process wirft exceptions db
                 * offline fehler außer db conn in hauptschleife exc behandeln
                 *
                 */
                System.out.println("*************************************Thread stopped ***********************");
            }

        }
    }

    public void loadOfflineDataInList()
    {
        try {
                data_manager.readOfflineData();
                BlockingQueue<JPoint> points_offline = data_manager.getPoints_offline();
                
                for (JPoint point_offline : points_offline) {
                    container.getPoints().put(point_offline);
                }
              loadDataOffline=false;
            } catch (IOException ex) {
                Logger.getLogger(BL.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(BL.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(BL.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void processData() 
    {
        try {
           
            BlockingQueue<JPoint> removePoints = new LinkedBlockingQueue<>();
            removePoints = data_manager.uploadContainer(container);
            for (JPoint removePoint : removePoints) {
                container.getPoints().remove(removePoint);
            }
        } catch (Exception ex) {
            
            System.out.println("Starting to reconnect..........");
            System.out.println(ex.toString());
            LOGGER.log(Level.WARNING, "Exc Exc", ex);
        }

    }

}


