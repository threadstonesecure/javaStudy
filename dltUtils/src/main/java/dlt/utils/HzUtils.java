package dlt.utils;


import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 汉字专拼音和五笔工具类
 * Created by denglt on 2017/1/13.
 */
public class HzUtils {

    public enum CaseType {
        LOWERCASE, UPPERCASE
    }

    /**
     * 获取汉字字符串拼音首字母，无法转化的返回原字符，空格直接忽略
     *
     * @param str
     * @param caseType
     * @return
     */
    public static String getPinyinCap(String str, CaseType caseType) {
        String separator = "#";
        StringBuilder sb = new StringBuilder();
        StringBuilder result = new StringBuilder();

        for (int i = 0, len = str.length(); i < len; i++) {
            char c = str.charAt(i);
            if (!ChineseHelper.isChinese(c) && c != '〇') {
                if (c != ' ')
                    result.append(c);
            } else {
                int j = i + 1;
                sb.append(c);
                while (j < len && (ChineseHelper.isChinese(str.charAt(j)) || str.charAt(j) == '〇')) {
                    sb.append(str.charAt(j));
                    j++;
                }
                try {
                    String hanziPinyin = PinyinHelper.convertToPinyinString(sb.toString(), separator, PinyinFormat.WITHOUT_TONE);
                    String[] pinyinArray = hanziPinyin.split(separator);
                    for (String string : pinyinArray) {
                        result.append(string.charAt(0));
                        i++;
                    }
                    i--;
                    sb.setLength(0);
                } catch (PinyinException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return toCaseTypeStr(result, caseType);
    }

    /**
     * 获取汉字字符串拼音
     * @param str
     * @param separator
     * @param caseType
     * @return
     */
    public static String getPinyin(String str, String separator, CaseType caseType) {
        try {
            String hanziPinyin = PinyinHelper.convertToPinyinString(str, separator, PinyinFormat.WITHOUT_TONE);
            return toCaseTypeStr(hanziPinyin, caseType);
        } catch (PinyinException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取汉字字符串五笔首字母
     *
     * @param str
     * @param caseType
     * @return
     */
    public static String getWbCap(String str, CaseType caseType) {
        if (WB_TABLE == null) {
            initWbTable();
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0, len = str.length(); i < len; i++) {
            char c = str.charAt(i);
            String wb = getWb(c);
            if (wb != null)
                result.append(wb.charAt(0));
        }

        return toCaseTypeStr(result, caseType);
    }

    /**
     * 获取汉字字符串五笔
     *
     * @param str
     * @param separator
     * @param caseType
     * @return
     */
    public static String getWb(String str, String separator, CaseType caseType) {
        if (WB_TABLE == null) {
            initWbTable();
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0, len = str.length(); i < len; i++) {
            char c = str.charAt(i);
            String wb = getWb(c);
            if (wb != null)
                result.append(wb + (i == len - 1 ? "" : separator));
        }

        return toCaseTypeStr(result, caseType);
    }


    private static String getWb(char c) {
        if (!ChineseHelper.isChinese(c) && c != '〇') {
            if (c != ' ')
                return "" + c;
        } else {
            return WB_TABLE.get("" + c);
        }
        return null;
    }

    private synchronized static void initWbTable() {
        if (WB_TABLE != null)
            return;
        InputStream is = HzUtils.class.getResourceAsStream("wb.dict");
        Reader reader;
        try {
            reader = new InputStreamReader(is, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        WB_TABLE = new ConcurrentHashMap<>();
        BufferedReader br = new BufferedReader(reader);
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().split("=");
                WB_TABLE.put(tokens[1].trim(), tokens[0].trim());
            }
            br.close();
        } catch (IOException e) {
            WB_TABLE = null;
            throw new RuntimeException(e);
        }

    }

    private static String toCaseTypeStr(StringBuilder sb, CaseType caseType) {
        return toCaseTypeStr(sb.toString(), caseType);
    }

    private static String toCaseTypeStr(String str, CaseType caseType) {
        if (caseType == CaseType.UPPERCASE)
            return str.toUpperCase();

        if (caseType == CaseType.LOWERCASE)
            return str.toLowerCase();

        return str.toString();
    }

    private static Map<String, String> WB_TABLE;

    public static void main(String[] args) throws Exception {

        String str = "&Ab邓隆通!！率市长 揠苗助长";
        String pinyin = PinyinHelper.convertToPinyinString(str, ",", PinyinFormat.WITHOUT_TONE);
        System.out.println(pinyin);
        pinyin = getPinyin(str,",", CaseType.UPPERCASE);
        System.out.println(pinyin);
        pinyin = PinyinHelper.getShortPinyin(str);
        System.out.println(pinyin);
        pinyin = getPinyinCap(str, HzUtils.CaseType.UPPERCASE);
        System.out.println(pinyin);

        String wb = getWbCap("梅毒 NOS", null);
        System.out.println(wb);
        wb = getWb("梅毒 NOS", ",", null);
        System.out.println(wb);
    }
}
