package weixin.req;

/**
 * Created by denglt on 2015/11/17.
 */
public class Event extends BaseRequest {

    private String event;//事件类型，subscribe(订阅)、unsubscribe(取消订阅)等等

    private String eventKey;

    private String ticket;

    //	地理位置纬度
    private double latitude;
    //	地理位置经度
    private double longitude;
    //	地理位置精度
    private double precision;


    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    @Override
    public String toString() {
        return "Event{" +
                "event='" + event + '\'' +
                ", eventKey='" + eventKey + '\'' +
                ", ticket='" + ticket + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", precision=" + precision +
                "} " + super.toString();
    }
}
