/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author paulz
 */
public class TrackId implements Serializable{
    
    private String id;
    private int serialNumber;

    public TrackId() {
    }

    public TrackId(String id, int serialNumber) {
        this.id = id;
        this.serialNumber = serialNumber;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + this.serialNumber;
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
        final TrackId other = (TrackId) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (this.serialNumber != other.serialNumber) {
            return false;
        }
        return true;
    }
    
    
    
}
