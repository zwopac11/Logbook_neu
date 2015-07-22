/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paul
 */
public class DB_ConnectionPool implements DB_Config {

    private LinkedList<Connection> connections = new LinkedList<>();
    private static final int MAX_CONN = 100;
    private static int num_conn = 0;

    private static DB_ConnectionPool theInstance = null;

    public static DB_ConnectionPool getInstance() throws ClassNotFoundException {
        if (theInstance == null) {
            theInstance = new DB_ConnectionPool();
        }
        return theInstance;
    }

    private DB_ConnectionPool() throws ClassNotFoundException {
        Class.forName(DB_DRIVER);
    }

    public synchronized Connection getConnection() throws Exception {
        if (connections.isEmpty()) {
            if (num_conn == MAX_CONN) {
                throw new Exception("Maximum number of connections reached");
            }
             //Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/logbooksql1?user=root&password=");
             Connection conn = DriverManager.getConnection( DB_URL+DB_NAME+"?user="+DB_USER+"&password="+DB_PASSWD+"");
//            Connection conn = DriverManager.getConnection("jdbc:mysql://main.sunlime.at/logbooksql1?" +
//                                   "user=logbooksql1&password=M2YWRkMzg1");//DB_URL + DB_NAME, DB_USER, DB_PASSWD);
            num_conn++;
            return conn;

        } else {
            return connections.poll();
        }
    }

    public synchronized void relaseConnection(Connection conn) {
        connections.offer(conn);
    }

}
