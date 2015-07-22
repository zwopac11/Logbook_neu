package gps;

/*
 * Java gpsd TestApp
 * 
 * Copyright (C) 2014 Marcus Hottenrott - www.it-adviser.net
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;
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
        
        private DB_Access dba;
        private boolean updateing;
        
        private double latOld=-1,latNew=-1,lonOld=-1,lonNew=-1,d=0;;

	public static void main(String[] args) {
		Runnable r = new Runnable() {
			public void run() {
				new GpsdTestApp().createUI();
			}
		};
		EventQueue.invokeLater(r);
	}

	
	private void buildComponents(){
		txtServerAdress = new JTextField();
		txtServerAdress.setText("192.168.1.107");
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
            
                try {
                                dba = DB_Access.getInstance();
                                
                               // dba.getFilm();
                            } catch (ClassNotFoundException ex) {
                                System.out.println(ex.toString());
                            } catch (Exception ex) {
                                System.out.println(ex.toString());
                            }
            
		buildComponents();
		initEventHandling();
		
		frame = new JFrame();
		frame.setTitle("Java gpsd Testclient - www.it-adviser.net");

		//formatter:off
		FormLayout formLayout = new FormLayout("f:p, $lcgap, 60dlu, $lcgap, 60dlu",
				"9*(20dlu, p)");

		PanelBuilder builder = new PanelBuilder(formLayout);
		
		builder.addLabel("gpsd-Server IP:",  CC.xy(1, 1));
		builder.add(txtServerAdress, 		 CC.xy(3, 1));

		builder.addLabel("gpsd-Server Port:", CC.xy(1, 3));
		builder.add(txtServerPort,		     CC.xy(3, 3));

		builder.add(btnConnect,		         CC.xy(3, 5));
		builder.add(btnDisconnect,		     CC.xy(5, 5));

		builder.addSeparator("GPS-Daten:",   CC.xyw(1, 7, 5));
		
		builder.addLabel("Longitude:",       CC.xy(1, 9));
		builder.add(txtLongitude, 		     CC.xy(3, 9));
		builder.addLabel("Latitude:",        CC.xy(1, 11));
		builder.add(txtLatitude, 		     CC.xy(3, 11));
		builder.addLabel("Altitude (m):",    CC.xy(1, 13));
		builder.add(txtAltitude, 		     CC.xy(3, 13));
		builder.addLabel("Speed (m/s):",     CC.xy(1, 15));
		builder.add(txtSpeed, 		         CC.xy(3, 15));
                
		
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
                                
                                System.out.println("afadfasdfasLongitude: "+CC.xy(3, 9)+"Latitude: "+CC.xy(3, 9));
			}
		});
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopGpsdClient();				
			}
		});
	}


	private void startGpsdClient(){
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
                                
                                if(latOld==-1&&lonOld==-1)
                                {
                                  latNew=tpv.getLatitude();
                                  lonNew=tpv.getLongitude();
                                }
                                latOld=latNew;
                                lonOld=lonNew;
                                latNew=tpv.getLatitude();
                                lonNew=tpv.getLongitude();
                                
                                System.out.println(tpv.getTimestamp()+"afadfasdfasLongitude: "+tpv.getLatitude()+"Latitude: "+tpv.getLongitude());
                                

                               
                                
                                
////                            //upload auf Fileservver    
////                            try {
////                                String filename = "daten.txt";
////                                upload(filename);
////                            } catch (IOException ex) {
////                                Logger.getLogger(GpsdTestApp.class.getName()).log(Level.SEVERE, null, ex);
////                            }
                                
////                                //interpolation
//////?                                double R = 6371000; // metres
//////                                double ?1 = Math.toRadians(latOld);
//////                                double ?2 = Math.toRadians(latNew);
//////                                double ?? = (latNew-latOld)* Math.PI / 180;
//////                                double ?? = (lonNew-lonOld)* Math.PI / 180;
//////
//////                                double a = Math.sin(??/2) * Math.sin(??/2) +
//////                                        Math.cos(?1) * Math.cos(?2) *
//////                                        Math.sin(??/2) * Math.sin(??/2);
//////                                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//////
//////?                                double d = R * c;
////                                
                                double R = 6371000; // metres
                                
                                double dLat = Math.toRadians(latNew-latOld);
                                double dLon = Math.toRadians(lonNew-lonOld); 
                                double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                                    Math.cos(Math.toRadians(latOld)) * Math.cos(Math.toRadians(latNew)) * 
                                    Math.sin(dLon/2) * Math.sin(dLon/2); 
                                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
                                d = d + (R * c);
                                System.out.println(d);
                                
                               
                                
                            try {
                                writeFile(tpv.getTimestamp(),tpv.getLatitude(),tpv.getLongitude(),d);
                            } catch (ParseException ex) {
                                Logger.getLogger(GpsdTestApp.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(GpsdTestApp.class.getName()).log(Level.SEVERE, null, ex);
                            }

                                dba.setSpeed(tpv.getSpeed());
                                
                                if(tpv.getSpeed()>=100&&updateing==false)
                                {
                                    updateing=true;
                                    updateing = dba.upload_to_Database();
                                }
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
	

        
        /**
         * Upload FTP Server
         * @param filename
         * @throws IOException 
         */
        public void upload (String filename) throws IOException
        {
            FTPClient client = new FTPClient();
            FileInputStream fis = null;

            client.connect("ftp.sunlime.at",990);
            client.login("admin", "secret");
            

            fis = new FileInputStream(filename);
            client.storeFile(filename, fis);
            client.logout();
            fis.close();
        }
        
       public void writeFile(double timestamp,double latitude, double longitude,double drivenKM) throws ParseException, IOException {
        File file = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "data" + File.separator + "save.csv");
        FileOutputStream fos = null;
        FileWriter fw = new FileWriter(file, true);
        fw.write(timestamp+";"+latitude+";"+longitude+";"+drivenKM);
        fw.close();
    }
        
	private void stopGpsdClient() {
		if (gpsEndPoint != null) {
			gpsEndPoint.stop();
			gpsEndPoint = null;
		}
	}
	
	private void showError(String error){
        JOptionPane.showMessageDialog(frame,
                error,
                "Error:",					      
                JOptionPane.ERROR_MESSAGE);
	}
}
