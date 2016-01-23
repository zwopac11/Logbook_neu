package gps;


import beans.BL;
import beans.DataManager;
import beans.Point;
import beans.Track;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import gps.pgsd4java.api.ObjectListener;
import gps.pgsd4java.backend.GPSdEndpoint;
import gps.pgsd4java.backend.ResultParser;
import gps.pgsd4java.types.TPVObject;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import database.DB_Access;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.net.ftp.FTPClient;

public class GpsdTestApp {

    private GPSdEndpoint gpsEndPoint;

    private JFrame frame;
    private JTextField txtServerAdress;
    private JTextField txtServerPort;

    private JButton btnConnect;
    private JButton btnDisconnect;

    private JTextField txtLatitude;
    private JTextField txtLongitude;
    private JTextField txtAltitude;
    private JTextField txtSpeed;
    private BL bl;

    public GpsdTestApp() {
        bl = new BL();
    }


    
    

    public void begin() {
        startGpsdClient();

    }

    private void buildComponents() {
        txtServerAdress = new JTextField();
        txtServerAdress.setText("192.168.178.44");//192.168.1.107 //127.0.0.1//192.168.43.69
        txtServerPort = new JTextField();
        txtServerPort.setText("2947");
        txtLatitude = new JTextField();
        txtLongitude = new JTextField();
        txtAltitude = new JTextField();
        txtSpeed = new JTextField();

        btnConnect = new JButton("Connect");
        btnDisconnect = new JButton("Disconnect");
    }

    private void createUI() {
        buildComponents();
        initEventHandling();

        frame = new JFrame();

        //formatter:off
        FormLayout formLayout = new FormLayout("f:p, $lcgap, 60dlu, $lcgap, 60dlu",
                "9*(20dlu, p)");

        PanelBuilder builder = new PanelBuilder(formLayout);

        builder.addLabel("gpsd-Server IP:", CC.xy(1, 1));
        builder.add(txtServerAdress, CC.xy(3, 1));

        builder.addLabel("gpsd-Server Port:", CC.xy(1, 3));
        builder.add(txtServerPort, CC.xy(3, 3));

        builder.add(btnConnect, CC.xy(3, 5));
        builder.add(btnDisconnect, CC.xy(5, 5));

        builder.addSeparator("GPS-Daten:", CC.xyw(1, 7, 5));

        builder.addLabel("Longitude:", CC.xy(1, 9));
        builder.add(txtLongitude, CC.xy(3, 9));
        builder.addLabel("Latitude:", CC.xy(1, 11));
        builder.add(txtLatitude, CC.xy(3, 11));
        builder.addLabel("Altitude (m):", CC.xy(1, 13));
        builder.add(txtAltitude, CC.xy(3, 13));
        builder.addLabel("Speed (m/s):", CC.xy(1, 15));
        builder.add(txtSpeed, CC.xy(3, 15));

        //formatter:on		
        builder.background(Color.white);
        builder.border(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        frame.add(builder.getPanel());
//		frame.setSize(400, 400);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void initEventHandling() {
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGpsdClient();

                System.out.println("afadfasdfasLongitude: " + CC.xy(3, 9) + "Latitude: " + CC.xy(3, 9));
            }
        });
        btnDisconnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopGpsdClient();
            }
        });
    }

    private void startGpsdClient() {
        try {
            gpsEndPoint = new GPSdEndpoint(txtServerAdress.getText(), Integer.valueOf(txtServerPort.getText()),
                    new ResultParser());

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
                txtLatitude.setText(String.valueOf(tpv.getLatitude()));
                txtLongitude.setText(String.valueOf(tpv.getLongitude()));
                txtAltitude.setText(String.valueOf(tpv.getAltitude()));
                txtSpeed.setText(String.valueOf(tpv.getSpeed()));
                
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
        if (gpsEndPoint != null) {
            gpsEndPoint.stop();
            gpsEndPoint = null;
        }
    }

    private void showError(String error) {
        JOptionPane.showMessageDialog(frame,
                error,
                "Error:",
                JOptionPane.ERROR_MESSAGE);
    }

    
    
//    public static void main(String[] args) {
//        Runnable r = new Runnable() {
//            public void run() {
//
//                new GpsdTestApp().createUI();
//            }
//        };
//        EventQueue.invokeLater(r);
//    }
}
