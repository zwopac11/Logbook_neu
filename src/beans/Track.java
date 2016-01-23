/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 *
 * @author paulz
 */
@Entity
@IdClass(TrackId.class)
public class Track {
    
    @Id
    private String id;
    private LocalDateTime timestamp;
    @Id
    private int serialNumber;
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany( mappedBy = "track")
    private Set<Point> points = new LinkedHashSet<>();


    public Track() {
    }

    public Track(LocalDateTime timestamp, int serialNumber) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = timestamp;
        this.serialNumber = serialNumber;
    }

    public Track(String id, LocalDateTime timestamp, int serialNumber) {
        this.id = id;
        this.timestamp = timestamp;
        this.serialNumber = serialNumber;
    }
    

    public String getId() {
        return id;
    }
    
    

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDateTime getTrackID() {
        return timestamp;
    }

    public Set<Point> getPoints() {
        return points;
    }

    public void setPoints(Set<Point> points) {
        this.points = points;
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
        final Track other = (Track) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.timestamp, other.timestamp)) {
            return false;
        }
        if (this.serialNumber != other.serialNumber) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + ";" +timestamp + ";" + serialNumber;// + ";" + points
    }
    
    
    
}
