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
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Laura
 */
public class gps_gui extends JFrame 
{

    private GPSdEndpoint gpsEndPoint;
    private BL bl;

    public gps_gui() throws HeadlessException 
    {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(750, 525);
        bl = new BL();
        startGpsdClient();
        //initComponents();
        
    }
    
    public void initComponents() 
    {
        Container cont = this.getContentPane();

        cont.setLayout(new GridLayout(2, 1));

        //set layouts
        pnTop.setLayout(new GridLayout(3, 2));
        pnBottom.setLayout(new GridLayout(4, 2));
        //set borders
        pnTop.setBorder(new TitledBorder("establish connection"));
        pnBottom.setBorder(new TitledBorder("GPS data"));

        //add objects
        pnTop.add(lbServerAddress);
        pnTop.add(tfServerAddress);
        pnTop.add(lbServerPort);
        pnTop.add(tfServerPort);
        pnTop.add(btConnect);
        pnTop.add(btDisconnect);

        pnBottom.add(lbLongitude);
        pnBottom.add(tfLongitude);
        pnBottom.add(lbLatitude);
        pnBottom.add(tfLatitude);
        pnBottom.add(lbAltitude);
        pnBottom.add(tfAltitude);
        pnBottom.add(lbSpeed);
        pnBottom.add(tfSpeed);

        //events
        btConnect.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                startGpsdClient();
            }
        });

        btDisconnect.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopGpsdClient();
            }
        });

        //add panels
        cont.add(pnTop);
        cont.add(pnBottom);
    }

    
    public void startGpsdClient()
    {
        try {
                    gpsEndPoint = new GPSdEndpoint(tfServerAddress.getText(), Integer.valueOf(tfServerPort.getText()),
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
                        tfLatitude.setText(String.valueOf(tpv.getLatitude()));
                        tfLongitude.setText(String.valueOf(tpv.getLongitude()));
                        tfAltitude.setText(String.valueOf(tpv.getAltitude()));
                        tfSpeed.setText(String.valueOf(tpv.getSpeed()));
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
    
    private void stopGpsdClient() 
    {
//        if (gpsEndPoint != null) 
//        {
//            gpsEndPoint.stop();
//            gpsEndPoint = null;
//        }
    }
    
    private void showError(String error) 
    {
//        JOptionPane.showMessageDialog(frame,
//                error,
//                "Error:",
//                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) 
    {
//        JPoint p1 = new JPoint("trackid", 33333, 1234, "uuidddd3", 123, 1.0, 2.0, 3.0, 4.0);
//        JPoint p2 = new JPoint("trackid", 33333, 1234, "uuidddd4", 123, 1.0, 2.0, 3.0, 4.0);
//        JContainer container = new JContainer(1234, "sowas von geheim22");
//        container.getPoints().add(p1);
//        container.getPoints().add(p2);
//        
//        String json = new Gson().toJson(container);
//        
//        try {
//            URL url = new URL("http://localhost/gps.php");//logbook.main.sunlime.at
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            
//            
//            //add reuqest header
//            con.setRequestMethod("POST");
//            con.setRequestProperty("User-Agent", "Raspi-0815");
//            con.setRequestProperty("Accept-Language", "en-US");
//
//            
//            //TODO
//            //Limit of uploded points
//            
//            // Send post request
//            con.setDoOutput(true);
//            OutputStream output_stream = con.getOutputStream();
//            byte[] json_bytes = json.getBytes("UTF-8");
//            output_stream.write(json_bytes);
//            output_stream.flush();
//            output_stream.close();
//
//            int responseCode = con.getResponseCode();
//            System.out.println("\nSending 'POST' request to URL : " + url);
//            System.out.println("Response Code : " + responseCode);
//
//            BufferedReader in = new BufferedReader(
//            new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//            while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//            }
//            in.close();
//            
//            System.out.println("Response:\n" + response.toString());
//
//            
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(gps_gui.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(gps_gui.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        System.out.println(json);
         new gps_gui();//.setVisible(true);
    }

    private JPanel pnTop = new JPanel();
    private JPanel pnBottom = new JPanel();

    private JLabel lbServerAddress = new JLabel("gpsd-Server IP: ");
    private JLabel lbServerPort = new JLabel("gpsd-ServerPort: ");

    private JTextField tfServerAddress = new JTextField("127.0.0.1");//@Home 192.168.178.44 //handy 192.168.43.69 //127.0.0.1
    private JTextField tfServerPort = new JTextField("2947");

    private JButton btConnect = new JButton("Connect");
    private JButton btDisconnect = new JButton("Disconnect");

    private JLabel lbLatitude = new JLabel("Latitude: ");
    private JLabel lbLongitude = new JLabel("Longitude: ");
    private JLabel lbAltitude = new JLabel("Altitude(m): ");
    private JLabel lbSpeed = new JLabel("Speed(m/s): ");

    private JTextField tfLatitude = new JTextField();
    private JTextField tfLongitude = new JTextField();
    private JTextField tfAltitude = new JTextField();
    private JTextField tfSpeed = new JTextField();
}
