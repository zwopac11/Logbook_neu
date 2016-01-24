/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author paulz
 */
@Entity
@Table(name="point")
@IdClass(PointId.class)
public class Point {

    /**
     * coordinateY = longitude = längengrade
     * coordinateX = latidtude = breitengrade
     */
    
    @Id
    private String id;
//    @Id 
//    @Column(columnDefinition = "TIMESTAMP(3)")
    private LocalDateTime timestamp;
    @Id
    @ManyToOne
    private Track track;
    private Double coordinateY, coordinateX, acceleration, distance;

    
    public Point() {
    }
    
    public Point(LocalDateTime timestamp, Double latitude, Double longitude, Double distance, Double acceleration, Track track) {
        this.id = UUID.randomUUID().toString();
        this.coordinateY = longitude;
        this.coordinateX = latitude;
        this.acceleration = acceleration;
        this.timestamp = timestamp;
        this.distance = distance;
        this.addTrack(track);
    }

    public Point(String id,LocalDateTime timestamp, Double latitude, Double longitude, Double distance, Double acceleration, Track track) {
        this.id = id;
        this.coordinateY = longitude;
        this.coordinateX = latitude;
        this.acceleration = acceleration;
        this.timestamp = timestamp;
        this.distance = distance;
        this.addTrack(track);
    }
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(Double coordinateY) {
        this.coordinateY = coordinateY;
    }

    public Double getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(Double coordinateX) {
        this.coordinateX = coordinateX;
    }

    public Double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Double acceleration) {
        this.acceleration = acceleration;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return track;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.timestamp, other.timestamp)) {
            return false;
        }
        if (!Objects.equals(this.track, other.track)) {
            return false;
        }
        if (!Objects.equals(this.coordinateY, other.coordinateY)) {
            return false;
        }
        if (!Objects.equals(this.coordinateX, other.coordinateX)) {
            return false;
        }
        if (!Objects.equals(this.acceleration, other.acceleration)) {
            return false;
        }
        if (!Objects.equals(this.distance, other.distance)) {
            return false;
        }
        return true;
    }

    
    
     public void addTrack(Track track)
    {
        track.getPoints().add(this);
        this.setTrack(track);
    } 
    
    @Override
    public String toString() {
        return track.toString()+ ";"+id+ ";" + timestamp.toString()  + ";" + coordinateY + ";" + coordinateX  + ";" + distance + ";" + acceleration; //+ ";" + track
    }
    
}
