package dlt.study.guava.graph;

public class Distance {
    private int distance;

    public static Distance of(int i){
        return new Distance(i);
    }
    private Distance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "distance=" + distance +
                '}';
    }
}
