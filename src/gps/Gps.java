/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gps;

import beans.BL;
import gps.pgsd4java.api.ObjectListener;
import gps.pgsd4java.backend.GPSdEndpoint;
import gps.pgsd4java.backend.ResultParser;
import gps.pgsd4java.types.TPVObject;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Laura
 */
public class Gps {

    private GPSdEndpoint gpsEndPoint;
    private BL bl;
    private String serverAddress = "10.0.0.2";
    private int serverPort = 2947;

    public Gps() throws HeadlessException {
        
        bl = new BL();
        startGpsdClient();
    }

//    public void initComponents() 
//    {
//        Container cont = this.getContentPane();
//
//        cont.setLayout(new GridLayout(2, 1));
//
//        //set layouts
//        pnTop.setLayout(new GridLayout(3, 2));
//        pnBottom.setLayout(new GridLayout(4, 2));
//        //set borders
//        pnTop.setBorder(new TitledBorder("establish connection"));
//        pnBottom.setBorder(new TitledBorder("GPS data"));
//
//        //add objects
//        pnTop.add(lbServerAddress);
//        pnTop.add(tfServerAddress);
//        pnTop.add(lbServerPort);
//        pnTop.add(tfServerPort);
//        pnTop.add(btConnect);
//        pnTop.add(btDisconnect);
//
//        pnBottom.add(lbLongitude);
//        pnBottom.add(tfLongitude);
//        pnBottom.add(lbLatitude);
//        pnBottom.add(tfLatitude);
//        pnBottom.add(lbAltitude);
//        pnBottom.add(tfAltitude);
//        pnBottom.add(lbSpeed);
//        pnBottom.add(tfSpeed);
//
//        //events
//        btConnect.addActionListener(new ActionListener() 
//        {
//            @Override
//            public void actionPerformed(ActionEvent e) 
//            {
//                startGpsdClient();
////                System.out.println("afadfasdfasLongitude: " + CC.xy(3, 9) + "Latitude: " + CC.xy(3, 9));
//            }
//        });
//
//        btDisconnect.addActionListener(new ActionListener() 
//        {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                stopGpsdClient();
//            }
//        });
//
//        //add panels
//        cont.add(pnTop);
//        cont.add(pnBottom);
//    }
    
    public void startGpsdClient() {
        try {
            gpsEndPoint = new GPSdEndpoint(serverAddress, serverPort,new ResultParser());

        } catch (UnknownHostException e) {
            showError(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            showError(e.getMessage());
            e.printStackTrace();
        }
        gpsEndPoint.addListener(new ObjectListener() {
            @Override
            public void handleTPV(TPVObject tpv) {
//                txtLatitude.setText(String.valueOf(tpv.getLatitude()));
//                txtLongitude.setText(String.valueOf(tpv.getLongitude()));
//                txtAltitude.setText(String.valueOf(tpv.getAltitude()));
//                txtSpeed.setText(String.valueOf(tpv.getSpeed()));
                /**
                 * Aufruf von BL
                 */
                bl.test(tpv);
            }
        });
        gpsEndPoint.start();
        // Start JSON output
        try {
            gpsEndPoint.voidCommand("?WATCH={\"enable\":true,\"json\":true};\"");
        } catch (IOException e) {
            showError(e.getMessage());
            e.printStackTrace();
        }
    }

    private void stopGpsdClient() {
        if (gpsEndPoint != null) 
        {
            gpsEndPoint.stop();
            gpsEndPoint = null;
        }
    }

    private void showError(String error) {
//        JOptionPane.showMessageDialog(frame,
//                error,
//                "Error:",
//                JOptionPane.ERROR_MESSAGE);
    }

//    private JPanel pnTop = new JPanel();
//    private JPanel pnBottom = new JPanel();
//
//    private JLabel lbServerAddress = new JLabel("gpsd-Server IP: ");
//    private JLabel lbServerPort = new JLabel("gpsd-ServerPort: ");
//
//    private JTextField tfServerAddress = new JTextField("10.0.0.2");
//    private JTextField tfServerPort = new JTextField("2947");
//
//    private JButton btConnect = new JButton("Connect");
//    private JButton btDisconnect = new JButton("Disconnect");
//
//    private JLabel lbLatitude = new JLabel("Latitude: ");
//    private JLabel lbLongitude = new JLabel("Longitude: ");
//    private JLabel lbAltitude = new JLabel("Altitude(m): ");
//    private JLabel lbSpeed = new JLabel("Speed(m/s): ");
//
//    private JTextField tfLatitude = new JTextField();
//    private JTextField tfLongitude = new JTextField();
//    private JTextField tfAltitude = new JTextField();
//    private JTextField tfSpeed = new JTextField();
}
