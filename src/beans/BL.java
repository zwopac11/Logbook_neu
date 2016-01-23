/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import gps.GpsdTestApp;
import gps.pgsd4java.types.TPVObject;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import org.apache.commons.net.ftp.FTPClient;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;
import com.mysql.jdbc.log.StandardLogger;

/**
 *
 * @author paulz
 */
public class BL {


    private boolean updateing;
    private static final Logger LOGGER = Logger.getLogger(BL.class.getName());
    private double latOld = -1, latNew = -1, lonOld = -1, lonNew = -1, d = 0;
    private BlockingQueue<Point> points = new LinkedBlockingQueue<>();
    private BlockingQueue<Track> tracks = new LinkedBlockingQueue<>();
    private EntityManagerFactory emf;
    private EntityManager em;
    private DataManager data_manager;
    private Track track;
    private saveThread saveThread;

    
    private boolean reconnecting = false;
    private boolean reconnectWithoutEMF=false;
    private boolean loadDataOffline=true;
    
    private StandardLogger logger_mysql = new StandardLogger("test");
    
    public BL() {
        try {
            emf = Persistence.createEntityManagerFactory("GPSPU");
            em = emf.createEntityManager();
        } catch (PersistenceException ex) {
            System.out.println("PersistenceEx +-+-+-+-+-+-+-+-+-+");
            LOGGER.log(Level.WARNING,"Fehler ",ex);
            System.out.println(ex.toString());
            reconnecting = true;
        }

        data_manager = new DataManager();
        
        
//        try {
//            data_manager.readOfflineData();
//            BlockingQueue<Point> points_offline = data_manager.getPoints_offline();
//            BlockingQueue<Track> tracks_offline = data_manager.getTracks_offline();
//            for (Track track_offline : tracks_offline) {
//                tracks.put(track_offline);
//            }
//            for (Point point_offline : points_offline) {
//                points.put(point_offline);
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(BL.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ParseException ex) {
//            Logger.getLogger(BL.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(BL.class.getName()).log(Level.SEVERE, null, ex);
//        }
        track = new Track(LocalDateTime.now(), 1234);
        tracks.add(track);
        
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
        Point point = new Point(LocalDateTime.now(), tpv.getLatitude(), tpv.getLongitude(), d, tpv.getSpeed(), track);

        

        try {
            points.put(point);
        } catch (InterruptedException ex) {
            Logger.getLogger(BL.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            try {
                System.out.println("points size:"+points.size());
                System.out.println("traks size:"+tracks.size());
                for (Track track1 : tracks) {
                    System.out.println("track: "+track1.getId());
                }
                data_manager.writeFile(points);
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
    public void upload(String filename) throws IOException {
        FTPClient client = new FTPClient();
        FileInputStream fis = null;

        client.connect("ftp.sunlime.at", 990);
        client.login("admin", "secret");

        fis = new FileInputStream(filename);
        client.storeFile(filename, fis);
        client.logout();
        fis.close();
    }

    class saveThread extends Thread {

        @Override
        public void run() {
            System.out.println("-------------------------Thread gestartet-----------------------------------");
            synchronized (this) {
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
                BlockingQueue<Point> points_offline = data_manager.getPoints_offline();
                BlockingQueue<Track> tracks_offline = data_manager.getTracks_offline();
                for (Track track_offline : tracks_offline) {
                    if(!tracks.contains(track_offline))
                    {
                        tracks.put(track_offline);
                    }
                    
                }
                for (Point point_offline : points_offline) {
                    points.put(point_offline);
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
    
    public void processData() {
        if(loadDataOffline)
        {
          loadOfflineDataInList();
          
        }
        if (reconnecting) {
            do {
                try {
                    emf = Persistence.createEntityManagerFactory("GPSPU");
                    em = emf.createEntityManager();
                    reconnecting = false;
                } catch (PersistenceException ex) {
                    
                    System.out.println("PersistenceEx +-+-+-+-+-+-+-+-+-+");
                    System.out.println(ex.toString());
                    reconnecting = true;
                }

            } while (reconnecting);
            System.out.println("EMF started****************************");
        }
        if (reconnectWithoutEMF) {
            do {
                try {
                    System.out.println("reconnect");
                    em = emf.createEntityManager();
                    reconnectWithoutEMF = false;
                } catch (PersistenceException ex) {
                    
                    System.out.println("PersistenceEx +-+-+-+-+-+-+-+-+-+");
                    System.out.println(ex.toString());
                    LOGGER.log(Level.WARNING, "Reconect Exc", ex);
                    reconnectWithoutEMF = true;
                }

            } while (reconnectWithoutEMF);
            System.out.println("EMF started****************************");
        }
        try {
            BlockingQueue<Track> removeTracks = new LinkedBlockingQueue<>();
            removeTracks = data_manager.uploadTracks(em, tracks);
            for (Track removeTrack : removeTracks) {
                tracks.remove(removeTrack);
            }
            BlockingQueue<Point> removePoints = new LinkedBlockingQueue<>();
            removePoints = data_manager.uploadPoints(em, points);
            for (Point removePoint : removePoints) {
                points.remove(removePoint);
            }
        } catch (Exception ex) {
            reconnectWithoutEMF = true;
            
            System.out.println("Starting to reconnect..........");
            System.out.println(ex.toString());
            LOGGER.log(Level.WARNING, "Exc Exc", ex);
        }

    }

}


