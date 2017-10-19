package dlt.study.guava.graph;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class City {

    public static final Map<String, City> FIXED_CITY;

    public static final City BEIJIN = new City("beijin", "北京");
    public static final City SHANGHAI = new City("shanghai", "上海");
    public static final City GUANGZHOU = new City("guangzhou", "广州");
    public static final City SHENZHEN = new City("shenzhen", "深圳");

    static {
        FIXED_CITY = ImmutableMap.of(BEIJIN.getCode(), BEIJIN,
                SHANGHAI.code, SHANGHAI,
                GUANGZHOU.getCode(), GUANGZHOU,
                SHENZHEN.getCode(),SHENZHEN);
    }

    private String code;
    private String name;

    public City(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        return code != null ? code.equals(city.code) : city.code == null;
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "City{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
