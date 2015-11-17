/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author paulz
 */
@Entity
public class Track {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private LocalDateTime trackID;
    private int serialNumber;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "track")
    private Set<Point> points = new LinkedHashSet<>();

//    public Track(LocalDateTime trackID) {
//        this.trackID = trackID;
//    }


    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDateTime getTrackID() {
        return trackID;
    }

    
    
    
    
    public void addPoint(Point point)
    {
        point.setTrack(this);
        //point.setTrackID(this);
        points.add(point);
    } 

    @Override
    public String toString() {
        return id + ";" +trackID + ";" + serialNumber + ";" + points;
    }
    
    
    
}
