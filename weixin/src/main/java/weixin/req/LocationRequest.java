package weixin.req;

/**
 * Created by denglt on 2015/11/17.
 */
public class LocationRequest extends BaseRequest {

    //	地理位置维度
    private double location_x;
    //  地理位置经度
    private double location_y;
    //	地图缩放大小
    private double scale;
    //地理位置信息
    private String label;

    public double getLocation_x() {
        return location_x;
    }

    public void setLocation_x(double location_x) {
        this.location_x = location_x;
    }

    public double getLocation_y() {
        return location_y;
    }

    public void setLocation_y(double location_y) {
        this.location_y = location_y;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
