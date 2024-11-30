package model;

public class Trip {
    private String departureTimeStr;
    private String arrivalTimeStr;

    private Long departureTime;
    private Long arrivalTime;

    public Trip(String departureTimeStr) {
        this.departureTimeStr = departureTimeStr;
    }

    public void setDepartureTimeStr(String departureTimeStr) {
        this.departureTimeStr = departureTimeStr;
    }

    public void setArrivalTimeStr(String arrivalTimeStr) {
        this.arrivalTimeStr = arrivalTimeStr;
    }

    public Long getDepartureTime() {
        if(null==departureTime){
            departureTime=Long.valueOf(departureTimeStr);
        }
        return departureTime;
    }

    public Long getArrivalTime() {
        if(null==arrivalTime){
            arrivalTime=Long.valueOf(arrivalTimeStr);
        }
        return arrivalTime;
    }
}
