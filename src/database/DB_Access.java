/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import beans.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paul
 */
public class DB_Access extends Thread implements DB_Config{

    private DB_ConnectionPool connPool;
    //private LinkedList<Film> filme = new LinkedList<>();
    private static DB_Access theInstance = null;
    private double speed;
    private boolean updating = false;
    private String dateiname;
    private LinkedList<Point> track = new LinkedList<>();

    private DB_Access() throws ClassNotFoundException {
        connPool = DB_ConnectionPool.getInstance();
    }

    public static DB_Access getInstance() throws ClassNotFoundException {
        if (theInstance == null) {
            theInstance = new DB_Access();
        }
        return theInstance;
    }

    public void addPoint(Point pt) throws Exception {
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        
            String sqlString = "INSERT INTO POINT "
                + "VALUES (" + pt.getTimestamp()+","+pt.getCoordinateY()+","+ pt.getCoordinateX()+","+ pt.getAcceleration()+");";
        //String sqlString = "SELECT * FROM books;";
        ResultSet rs = stat.executeQuery(sqlString);
        
        

        System.out.println(rs);

        connPool.relaseConnection(conn);
    }

    // update the points
    public void upload_to_Database() {
        updating = true;
        try {
            readFile();
        } catch (IOException ex) {
            Logger.getLogger(DB_Access.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(DB_Access.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Thread upThread = new Thread("upThread");
        updating = false;
    }

    public void readFile() throws FileNotFoundException, IOException, ParseException {

        File file = new File(dateiname);//+ File.separator + "src" + File.separator + "data" +

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String zeile = "";
        Date converter= new Date(Calendar.getInstance().getTimeInMillis());
        String[] str = null;
        while ((zeile = br.readLine()) != null) {
            
            str = zeile.split(";");
//            long itemLong = (long) (Double.parseDouble(str[0]));
//            converter = new Date(itemLong);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.ENGLISH);
            converter = format.parse(str[0]);

//            converter = new Date(str[0]);
            
//             Termin termin = new Termin(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]), str[3], str[4], str[5]);
//             termine.add(termin);
        }
        br.close();
//        track.add(new Point(converter, Double.parseDouble(str[1]), Double.parseDouble(str[2]), Double.parseDouble(str[4]), Double.parseDouble(str[3])));
            Thread t = new Thread()
            {
                public void run()
                {
                    
                    //einlesen -
                    //liste erstellen -
                    //md5 generieren -
                    //einlogg algorithmus ??
                    //im intervall checken ob inet verbindung da ist
                    //liste von hinten beginnen zu senden -
                    //gesendete daten entfernen
                    for(int i=track.size()-1; i>0;i--)
                    {
                        try {
                            addPoint(track.get(i));
                        } catch (Exception ex) {
                            System.out.println(ex.toString());
                        }
                    }
                    
                }
            };
            t.start();
    }
    
    public  String md5(String inputString) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(inputString.getBytes());

        byte[] digest = md.digest();

        return convertByteToHex(digest);
    }
    
    private static String convertByteToHex(byte[] byteData) {

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < byteData.length; i++) {
        sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    }

    return sb.toString();
}

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean getUpdating() {
        return updating;
    }

    public void setDateiname(String dateiname) {
        this.dateiname = dateiname;
    }

}
