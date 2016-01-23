/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author paulz
 */
public class PointId implements Serializable{
    
    private String id;
    private LocalDateTime timestamp;
    private Track track;

    public PointId() {
    }

    public PointId(String id, Track track) {
        this.id = id;
        this.track = track;
    }

    public PointId(String id, LocalDateTime timestamp, Track track) {
        this.id = id;
        this.timestamp = timestamp;
        this.track = track;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.track);
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
        final PointId other = (PointId) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.track, other.track)) {
            return false;
        }
        return true;
    }

   
   
    
    
    
}
