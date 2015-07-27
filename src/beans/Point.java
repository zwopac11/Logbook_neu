/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.Date;

/**
 *
 * @author Claudio
 */
public class Point {

    Double longitude, latitude, acceleration, distance;
    Date timestamp;

    public Point(Date timestamp, Double latitude, Double longitude, Double distance, Double acceleration) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.acceleration = acceleration;
        this.timestamp = timestamp;
        this.distance = distance;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Double acceleration) {
        this.acceleration = acceleration;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
