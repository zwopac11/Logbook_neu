/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;

/**
 *
 * @author Claudio, paulz
 */
public class DataManager {

    private BlockingQueue<Point> points_offline = new LinkedBlockingQueue<>();
    private BlockingQueue<Track> tracks_offline = new LinkedBlockingQueue<>();
    private String filepath = System.getProperty("user.dir") + File.separator + "save.csv";

    public void readOfflineData() throws FileNotFoundException, IOException, ParseException {
        File file = new File(filepath);

        if (!file.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write("".getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String zeile = "";
            Date converter = new Date(Calendar.getInstance().getTimeInMillis());
            String[] str = null;

            while ((zeile = br.readLine()) != null) {
                str = zeile.split(";");      
                
                 
                Track tr = new Track(str[0], LocalDateTime.parse(str[1]), Integer.parseInt(str[2]));
                Point p = new Point(str[3].replace("[", ""),LocalDateTime.parse(str[4]), Double.parseDouble(str[5]), Double.parseDouble(str[6]), 
                        Double.parseDouble(str[7]), Double.parseDouble(str[8].replace("]", "")),tr);

                points_offline.add(p);
                tracks_offline.add(tr);
            }
            br.close();

        }
    }

    public BlockingQueue<Point> getPoints_offline() {
        return points_offline;
    }

    public BlockingQueue<Track> getTracks_offline() {
        return tracks_offline;
    }


    
    
    public void writeFile(BlockingQueue<Point> points) throws ParseException, IOException {
        File file = new File(filepath);
        
            try {
               FileOutputStream fos = new FileOutputStream(file);
                fos.write("".getBytes());
                fos.close();
                
                for (Point point : points) {
                
                        FileWriter fw = new FileWriter(file, true);
                        fw.write(point.toString() + System.getProperty("line.separator"));
                        fw.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
           
    }

    public BlockingQueue<Track> uploadTracks(EntityManager em, BlockingQueue<Track> tracks) throws Exception{
        BlockingQueue<Track> removeTracks = new LinkedBlockingQueue<>();
        em.getTransaction().begin();
        for (Track track : tracks) {

                em.merge(track);
                em.flush();

        }
        
        em.getTransaction().commit();
        return removeTracks;
    }

    public BlockingQueue<Point> uploadPoints(EntityManager em, BlockingQueue<Point> points) throws Exception{

        BlockingQueue<Point> removePoints = new LinkedBlockingQueue<>();
        em.getTransaction().begin();
        
        //TODO
        //Maximale 
        
        for (Point point : points) {
                em.merge(point); //zu testen
                em.flush();
                removePoints.add(point);
        }
        

        
        em.getTransaction().commit();
        return removePoints;
    }
}
