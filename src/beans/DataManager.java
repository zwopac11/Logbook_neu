/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import json.JContainer;
import json.JPoint;

/**
 *
 * @author Claudio, paulz
 */
public class DataManager {

    private BlockingQueue<JPoint> points_offline = new LinkedBlockingQueue<>();
    private String filepath = System.getProperty("user.dir") + File.separator + "save.csv";
    private String url_string = "http://192.168.178.43/gps.php";//logbook.main.sunlime.at

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
                
                JPoint point = new JPoint(str[0], Long.parseLong(str[1]), Integer.parseInt(str[2]), str[3].replace("[", ""),Long.parseLong(str[4]), Double.parseDouble(str[5]), Double.parseDouble(str[6]), 
                        Double.parseDouble(str[7]), Double.parseDouble(str[8].replace("]", "")));
                points_offline.add(point);
            }
            br.close();

        }
    }

    public BlockingQueue<JPoint> getPoints_offline() {
        return points_offline;
    }

    
    
    public void writeFile(BlockingQueue<JPoint> points) throws ParseException, IOException {
        File file = new File(filepath);
        
            try {
               FileOutputStream fos = new FileOutputStream(file);
                fos.write("".getBytes());
                fos.close();
                
                for (JPoint point : points) {
                
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

    
        public BlockingQueue<JPoint> uploadContainer(JContainer container) throws Exception{

            BlockingQueue<JPoint> removePoints = new LinkedBlockingQueue<>();
        
        //TODO
        //Limit of uploded points
            
        for (JPoint point : container.getPoints()) {
 
                removePoints.add(point);
        }
        String json = new Gson().toJson(container);
        URL url = new URL(url_string);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            
            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Raspi-0815");
            con.setRequestProperty("Accept-Language", "en-US");

            
            // Send post request
            con.setDoOutput(true);
            OutputStream output_stream = con.getOutputStream();
            byte[] json_bytes = json.getBytes("UTF-8");
            output_stream.write(json_bytes);
            output_stream.flush();
            output_stream.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();
            
            System.out.println("Response:\n" + response.toString());
        
        return removePoints;
    }
}
