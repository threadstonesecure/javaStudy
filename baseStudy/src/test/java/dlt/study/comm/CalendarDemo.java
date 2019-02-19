package dlt.study.comm;

import com.google.common.collect.Lists;
import dlt.study.jaxb.Data;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:
 * @Package: dlt.study.comm
 * @Author: denglt
 * @Date: 2019/2/15 1:10 PM
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class CalendarDemo {

    public static String formateDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    @Test
    public void add() {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.getClass());
        for (int i = 1; i <= 10; i++) {
            //calendar.add(Calendar.DAY_OF_MONTH, 90);
            calendar.add(Calendar.DATE, 90);
            System.out.println(formateDate(calendar.getTime()));
        }
    }


    @Test
    public void get() {
        Calendar calendar = Calendar.getInstance();
        System.out.println(formateDate(calendar.getTime()));
        System.out.println(calendar.get(Calendar.YEAR));
        System.out.println(calendar.get(Calendar.MONTH));
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
/*        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));*/
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        System.out.println(formateDate(calendar.getTime()));
    }

    @Test
    public void getWeekDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        System.out.println(calendar.getMinimalDaysInFirstWeek());

        System.out.println(formateDate(calendar.getTime()));
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        System.out.println(formateDate(calendar.getTime()));
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        System.out.println(formateDate(calendar.getTime()));
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));

        calendar.add(Calendar.DAY_OF_MONTH, 0);
        System.out.println(formateDate(calendar.getTime()));
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
    }

    @Test
    public void printCurrentWeek() {
        Calendar instance = Calendar.getInstance();
       // instance.add(Calendar.DATE, 1);
        List<Date> dates = getWeekDates(instance.getTime(),true);
        dates.forEach(d -> System.out.println(formateDate(d)));
    }

    /**
     * 获取指定时间周的一周日期
     * @Author: denglt
     * @param date
     * @param firstIsSunday
     * @return
     */
    public static List<Date> getWeekDates(Date date, boolean firstIsSunday) {
        System.out.println("date -> " + formateDate(date));
        List<Date> dates = Lists.newArrayList();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (!firstIsSunday) {
            currentDayOfWeek = currentDayOfWeek == 1 ? 7 :currentDayOfWeek--;
        }
        for (int i = 1; i <= 7; i++) {
            calendar.add(Calendar.DATE, i - currentDayOfWeek);
            dates.add(calendar.getTime());
            calendar.add(Calendar.DATE, -(i - currentDayOfWeek));
        }
        return dates;
    }

    /**
     * 获取当前时间周的一周日期
     * @Author: denglt
     * @param date
     * @param firstIsSunday
     * @return
     */
    public static List<Date> getCurrentWeekDates(boolean firstIsSunday) {
        return getWeekDates(new Date(),true);
    }
}
