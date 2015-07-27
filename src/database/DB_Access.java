/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

/**
 *
 * @author Paul
 */
public class DB_Access {

    private DB_ConnectionPool connPool;
    //private LinkedList<Film> filme = new LinkedList<>();
    private static DB_Access theInstance = null;
    private double speed;
    private boolean updateing;
    private String dateiname;

    private DB_Access() throws ClassNotFoundException {
        connPool = DB_ConnectionPool.getInstance();
    }

    public static DB_Access getInstance() throws ClassNotFoundException {
        if (theInstance == null) {
            theInstance = new DB_Access();
        }
        return theInstance;
    }

    public void addPoint() throws Exception {
        Connection conn = connPool.getConnection();
        Statement stat = conn.createStatement();
        String sqlString = "SELECT * "
                + "FROM address;";
        //String sqlString = "SELECT * FROM books;";
        ResultSet rs = stat.executeQuery(sqlString);

        System.out.println(rs);

       
        connPool.relaseConnection(conn);
    }

    public void upload_to_Database() {
        updateing = true;
        
    }

    public void readFile() throws FileNotFoundException, IOException {

        File file = new File(dateiname);//+ File.separator + "src" + File.separator + "data" +

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String zeile = "";

        while ((zeile = br.readLine()) != null) {
            String[] str = zeile.split(";");
//             Termin termin = new Termin(Integer.parseInt(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]), str[3], str[4], str[5]);
//             termine.add(termin);
        }
        br.close();
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean getUpdateing() {
        return updateing;
    }

    public void setDateiname(String dateiname) {
        this.dateiname = dateiname;
    }
 
    
}
