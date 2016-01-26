/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;



/**
 *
 * @author paulz
 */
public class JPoint {
    
    private String trackId;
    private long trackTimestamp;
    private int serialNumber;
    private String pointId;
    private long timestamp;//LocalDateTime
    private Double coordinateY, coordinateX, distance ,acceleration;

    public JPoint() {
    }

    
    
    public JPoint(String trackId, long trackTimestamp, int serialNumber, String pointId, long timestamp, Double coordinateY, Double coordinateX, Double distance, Double acceleration) {
        this.trackId = trackId;
        this.trackTimestamp = trackTimestamp;
        this.serialNumber = serialNumber;
        this.pointId = pointId;
        this.timestamp = timestamp;
        this.coordinateY = coordinateY;
        this.coordinateX = coordinateX;
        this.distance = distance;
        this.acceleration = acceleration;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public long getTrackTimestamp() {
        return trackTimestamp;
    }

    public void setTrackTimestamp(long trackTimestamp) {
        this.trackTimestamp = trackTimestamp;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Double acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public String toString() {
        return trackId + ";" + trackTimestamp + ";" + serialNumber + ";" + pointId + ";" + timestamp + ";" + coordinateY + ";" + coordinateX + ";" + distance + ";" + acceleration;
    }
    
    
    

}
