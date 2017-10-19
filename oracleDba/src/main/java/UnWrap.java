/**
 * Created by denglt on 2017/7/5.
 */
import java.io.*;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.zip.Deflater;
import java.util.zip.InflaterInputStream;
import oracle.jdbc.driver.OracleDriver;
import sun.misc.BASE64Decoder;
/**
 * 作者：邓隆通
 * 功能：解密Oracle 10g加密的过程＼函数＼包．
 */
public class UnWrap {
    public String ip;
    public String port = "1521";
    public String sid;
    public String username;
    public String password;
    public boolean isDefaultConnect = false;
    private Connection connection = null;

    private static HashMap<String, String> translateMap;

    static {
        translateMap = new HashMap<String, String>();
        translateMap.put("30", "78");
        translateMap.put("83", "DA");
        translateMap.put("99", "0B");
        translateMap.put("B8", "70");
        translateMap.put("F5", "74");
        translateMap.put("33", "F6");
        translateMap.put("9F", "76");
        translateMap.put("BF", "77");
        translateMap.put("5C", "55");
        translateMap.put("5A", "48");
        translateMap.put("21", "34");
        translateMap.put("70", "60");
        translateMap.put("A6", "00");
        translateMap.put("F3", "11");
        translateMap.put("02", "85");
        translateMap.put("E1", "02");
        translateMap.put("41", "9E");
        translateMap.put("91", "64");
        translateMap.put("07", "87");
        translateMap.put("4F", "9F");
        translateMap.put("E0", "62");
        translateMap.put("F4", "89");
        translateMap.put("0E", "A0");
        translateMap.put("C9", "66");
        translateMap.put("6D", "8B");
        translateMap.put("6B", "A1");
        translateMap.put("38", "61");
        translateMap.put("F0", "8D");
        translateMap.put("51", "A2");
        translateMap.put("01", "65");
        translateMap.put("D0", "8F");
        translateMap.put("8C", "A3");
        translateMap.put("0B", "63");
        translateMap.put("F7", "91");
        translateMap.put("1A", "A4");
        translateMap.put("17", "67");
        translateMap.put("B7", "93");
        translateMap.put("63", "A5");
        translateMap.put("A0", "B4");
        translateMap.put("8D", "95");
        translateMap.put("E8", "A6");
        translateMap.put("9C", "97");
        translateMap.put("B0", "A7");
        translateMap.put("8E", "14");
        translateMap.put("40", "86");
        translateMap.put("72", "CF");
        translateMap.put("E3", "88");
        translateMap.put("50", "D0");
        translateMap.put("18", "8A");
        translateMap.put("B3", "D1");
        translateMap.put("5F", "8C");
        translateMap.put("25", "D2");
        translateMap.put("4A", "8E");
        translateMap.put("EB", "D3");
        translateMap.put("C8", "90");
        translateMap.put("77", "D4");
        translateMap.put("F1", "92");
        translateMap.put("3D", "D5");
        translateMap.put("89", "94");
        translateMap.put("3C", "D6");
        translateMap.put("A7", "96");
        translateMap.put("B1", "D7");
        translateMap.put("75", "98");
        translateMap.put("B4", "D8");
        translateMap.put("65", "32");
        translateMap.put("C2", "B2");
        translateMap.put("58", "99");
        translateMap.put("13", "9B");
        translateMap.put("31", "D9");
        translateMap.put("DF", "36");
        translateMap.put("EC", "9A");
        translateMap.put("61", "B6");
        translateMap.put("6E", "9C");
        translateMap.put("7F", "31");
        translateMap.put("90", "9D");
        translateMap.put("2D", "B1");
        translateMap.put("05", "DB");
        translateMap.put("2A", "35");
        translateMap.put("0D", "B5");
        translateMap.put("68", "DC");
        translateMap.put("82", "33");
        translateMap.put("03", "B3");
        translateMap.put("55", "DD");
        translateMap.put("78", "37");
        translateMap.put("C4", "B7");
        translateMap.put("4D", "A8");
        translateMap.put("E5", "AA");
        translateMap.put("19", "DE");
        translateMap.put("7C", "30");
        translateMap.put("26", "A9");
        translateMap.put("4B", "B0");
        translateMap.put("0A", "AB");
        translateMap.put("3B", "AD");
        translateMap.put("9A", "DF");
        translateMap.put("D8", "AC");
        translateMap.put("E2", "AE");
        translateMap.put("8F", "E0");
        translateMap.put("64", "AF");
        translateMap.put("EE", "E1");
        translateMap.put("53", "B8");
        translateMap.put("06", "E2");
        translateMap.put("BE", "B9");
        translateMap.put("45", "BA");
        translateMap.put("34", "BC");
        translateMap.put("86", "E3");
        translateMap.put("98", "BB");
        translateMap.put("ED", "BD");
        translateMap.put("BC", "BE");
        translateMap.put("DD", "C0");
        translateMap.put("5D", "E4");
        translateMap.put("9D", "BF");
        translateMap.put("AF", "C1");
        translateMap.put("15", "C2");
        translateMap.put("DB", "C4");
        translateMap.put("43", "E5");
        translateMap.put("6A", "C3");
        translateMap.put("95", "C5");
        translateMap.put("D4", "C6");
        translateMap.put("85", "C8");
        translateMap.put("8B", "E6");
        translateMap.put("C6", "C7");
        translateMap.put("FA", "C9");
        translateMap.put("97", "CA");
        translateMap.put("47", "CC");
        translateMap.put("C1", "E7");
        translateMap.put("D9", "CB");
        translateMap.put("D7", "CD");
        translateMap.put("FF", "CE");
        translateMap.put("7D", "E8");
        translateMap.put("29", "E9");
        translateMap.put("FC", "EA");
        translateMap.put("1E", "EB");
        translateMap.put("8A", "EC");
        translateMap.put("CD", "ED");
        translateMap.put("B9", "EE");
        translateMap.put("16", "28");
        translateMap.put("BB", "EF");
        translateMap.put("A5", "F0");
        translateMap.put("08", "F1");
        translateMap.put("9B", "F2");
        translateMap.put("D2", "F4");
        translateMap.put("4E", "F3");
        translateMap.put("81", "F5");
        translateMap.put("CE", "F8");
        translateMap.put("FE", "F7");
        translateMap.put("3A", "F9");
        translateMap.put("92", "FA");
        translateMap.put("E4", "FC");
        translateMap.put("F8", "FB");
        translateMap.put("73", "FD");
        translateMap.put("F9", "FE");
        translateMap.put("94", "15");
        translateMap.put("B5", "FF");
        translateMap.put("FB", "01");
        translateMap.put("36", "04");
        translateMap.put("1D", "03");
        translateMap.put("44", "05");
        translateMap.put("39", "06");
        translateMap.put("E7", "08");
        translateMap.put("5B", "07");
        translateMap.put("BD", "09");
        translateMap.put("9E", "0A");
        translateMap.put("74", "0C");
        translateMap.put("B2", "0D");
        translateMap.put("CB", "0E");
        translateMap.put("2F", "10");
        translateMap.put("28", "0F");
        translateMap.put("D3", "12");
        translateMap.put("32", "75");
        translateMap.put("C0", "72");
        translateMap.put("AB", "1A");
        translateMap.put("CA", "20");
        translateMap.put("BA", "5B");
        translateMap.put("66", "22");
        translateMap.put("71", "5C");
        translateMap.put("14", "24");
        translateMap.put("4C", "5D");
        translateMap.put("1B", "26");
        translateMap.put("87", "5E");
        translateMap.put("0F", "5F");
        translateMap.put("C5", "2A");
        translateMap.put("56", "2C");
        translateMap.put("D1", "2E");
        translateMap.put("A4", "1D");
        translateMap.put("1C", "1E");
        translateMap.put("EF", "23");
        translateMap.put("6C", "25");
        translateMap.put("49", "27");
        translateMap.put("3E", "29");
        translateMap.put("D5", "2B");
        translateMap.put("62", "2D");
        translateMap.put("96", "2F");
        translateMap.put("57", "38");
        translateMap.put("AD", "39");
        translateMap.put("DA", "3B");
        translateMap.put("7B", "3A");
        translateMap.put("7A", "3C");
        translateMap.put("22", "3E");
        translateMap.put("11", "68");
        translateMap.put("B6", "13");
        translateMap.put("6F", "16");
        translateMap.put("04", "18");
        translateMap.put("00", "3D");
        translateMap.put("24", "3F");
        translateMap.put("35", "41");
        translateMap.put("DE", "69");
        translateMap.put("1F", "17");
        translateMap.put("88", "19");
        translateMap.put("76", "1C");
        translateMap.put("67", "40");
        translateMap.put("E6", "42");
        translateMap.put("A2", "44");
        translateMap.put("27", "6A");
        translateMap.put("FD", "1B");
        translateMap.put("84", "43");
        translateMap.put("E9", "45");
        translateMap.put("80", "47");
        translateMap.put("F6", "6B");
        translateMap.put("2C", "1F");
        translateMap.put("A8", "21");
        translateMap.put("60", "46");
        translateMap.put("F2", "4A");
        translateMap.put("7E", "6C");
        translateMap.put("A1", "49");
        translateMap.put("0C", "4B");
        translateMap.put("2E", "4D");
        translateMap.put("79", "6D");
        translateMap.put("59", "4C");
        translateMap.put("DC", "4E");
        translateMap.put("69", "50");
        translateMap.put("48", "6E");
        translateMap.put("AE", "4F");
        translateMap.put("CC", "51");
        translateMap.put("5E", "53");
        translateMap.put("20", "6F");
        translateMap.put("09", "52");
        translateMap.put("C3", "54");
        translateMap.put("2B", "56");
        translateMap.put("EA", "57");
        translateMap.put("93", "59");
        translateMap.put("52", "71");
        translateMap.put("54", "58");
        translateMap.put("A3", "5A");
        translateMap.put("C7", "73");
        translateMap.put("42", "79");
        translateMap.put("23", "7A");
        translateMap.put("12", "7B");
        translateMap.put("CF", "7C");
        translateMap.put("10", "7D");
        translateMap.put("AA", "7F");
        translateMap.put("3F", "7E");
        translateMap.put("A9", "80");
        translateMap.put("AC", "82");
        translateMap.put("37", "81");
        translateMap.put("D6", "83");
        translateMap.put("46", "84");

    }

    /**
     * 中文支持有问题
     * @param src
     * @return
     * @throws IOException
     * @throws SQLException
     */
    private String Inflate_old(byte[] src) throws IOException, SQLException {
        ByteArrayInputStream bis = new ByteArrayInputStream(src);
        InflaterInputStream iis = new InflaterInputStream(bis);
        StringBuffer sb = new StringBuffer();
        for (int c = iis.read(); c != -1; c = iis.read()) {
            sb.append((char) c);
        }
        String source = sb.toString();
        return source;
    }

    private static int copy(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }

    /**
     * 支持中文
     * @param src
     * @return
     * @throws IOException
     * @throws SQLException
     */
    private String Inflate(byte[] src) throws IOException, SQLException {
        ByteArrayInputStream bis = new ByteArrayInputStream(src);
        InflaterInputStream iis = new InflaterInputStream(bis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copy(iis,baos);
        return new String(baos.toByteArray());
    }

    private String InflateEx(String str) throws IOException, SQLException {
        byte[] src = hex2Byte(str);
        return Inflate(src);

    }

    private byte[] Deflate(String src, int quality) throws IOException {
        byte[] tmp = new byte[src.length() + 100];
        Deflater defl = new Deflater(quality);
        defl.setInput(src.getBytes("UTF-8"));
        defl.finish();
        int cnt = defl.deflate(tmp);
        byte[] res = new byte[cnt];
        for (int i = 0; i < cnt; i++)
            res = tmp;
        return res;

    }

    private byte[] hex2Byte(String str) { //16进制2byte
        int iLength = str.length() % 2;
        if (iLength != 0) {
            throw new RuntimeException("数据错误!");
        }
        iLength = str.length();
        byte[] result = new byte[iLength / 2];
        for (int i = 0; i < iLength / 2; i++) {
            String temp = str.substring(i * 2, i * 2 + 2);
            result[i] = (byte) Integer.parseInt(temp, 16);
        }
        return result;
    }

    private String byte2Hex(byte[] b) { //一个字节的数，

        // 转成16进制字符串

        StringBuffer hs = new StringBuffer();
        String tmp = null;
        for (int n = 0; n < b.length; n++) {
            //整数转成十六进制表示
            tmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                hs.append("0" + tmp);
            } else {
                hs.append(tmp);
            }
        }
        return hs.toString().toUpperCase(); //转成大写

    }


    private Connection getConnection() throws SQLException {

        if (connection != null) {
            return connection;
        }
        try {
            OracleDriver oracleDriver = new OracleDriver();
            connection = oracleDriver.defaultConnection();
            isDefaultConnect = true;
            return connection;
        } catch (SQLException e) {
            isDefaultConnect = false;
        }
        String url = "jdbc:oracle:thin:@" + ip + ":" + port + "/" + sid;
        // Create the properties object that holds all database details
        Properties props = new Properties();
        props.put("user", username);
        props.put("password", password);
        props.put("SetBigStringTryClob", "true");
        if (username.equals("sys") || username.equals("SYS")) {
            props.put("internal_logon", "sysdba");
        }

        // Load the Oracle JDBC driver class.
        DriverManager.registerDriver(new OracleDriver());
        // Get the database connection
        connection = DriverManager.getConnection(url, props);
        return connection;
    }

    public String unWrap(String owner, String name, String type) throws SQLException, IOException {
        String sql = "select line, text from dba_source  Where Owner = ? and Name = ? and type = ?";
        StringBuffer sb64 = new StringBuffer();
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, owner.toUpperCase());
        preparedStatement.setString(2, name.toUpperCase());
        preparedStatement.setString(3, type.toUpperCase());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String text = resultSet.getString(2);
            if (resultSet.getInt(1) == 1) {

                int iPos = 0;
                char cha = 10;
                for (int i = 1; i <= 20; i++) {
                    iPos = text.indexOf(cha, iPos + 1);
                }
                sb64.append(text.substring(iPos + 1));
            } else {
                sb64.append(text);
            }
        }
        resultSet.close();

        if (sb64.length() == 0) {
            throw new RuntimeException("没有找到对象!");
        }
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bs = decoder.decodeBuffer(sb64.toString());
        String src = byte2Hex(bs);
        src = src.substring(40);
        src = translateWrap(src);
        src = InflateEx(src);
        return src;
    }

    public String unWrap2DB(String owner, String name, String type) throws SQLException, IOException {
        String src = unWrap(owner, name, type);
        String sql = "delete dlt_unwrap_source where object_owner=? and object_name=? and object_type=?";
        Connection connection =getConnection();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, owner);
        ps.setString(2, name);
        ps.setString(3, type);
        ps.executeUpdate();
        ps.close();
        sql = "insert into dlt_unwrap_source (object_owner,object_name,object_type,src) values(?,?,?,?)";
        ps = connection.prepareStatement(sql);
        ps.setString(1, owner);
        ps.setString(2, name);
        ps.setString(3, type);
        ps.setString(4, src);
        ps.executeUpdate();
        if (isDefaultConnect == false) {
            connection.commit();
        }
        return src;
    }


    private String translateWrap(String src) {
        StringBuffer bs = new StringBuffer();
        int length = src.length();
        for (int i = 0; i < length / 2; i++) {
            String key = src.substring(i * 2, i * 2 + 2);
            String temp = translateMap.get(key);
            if (temp == null) {
                throw new RuntimeException("没有找到字符" + temp + "的对应关系!");
            }
            bs.append(temp);
        }
        return bs.toString();
    }

    public static void main(String[] args) throws IOException, SQLException {

        UnWrap unWrapper = new UnWrap();
        String owner, name, type;
        if (args.length < 8) {
            throw new RuntimeException("运行参数错误");
        } else {
            unWrapper.ip = args[0];//"localhost";
            unWrapper.port = args[1];//"1521";
            unWrapper.sid = args[2];//"orcl";;
            unWrapper.username = args[3];//"sys";
            unWrapper.password = args[4];// "sys";
            owner = args[5].toUpperCase();//object owner
            name = args[6].toUpperCase();//"DBMS_STREAMS_MT";
            type = args[7].toUpperCase();//"PACKAGE BODY";
        }
        String str = unWrapper.unWrap2DB(owner, name, type);
        System.out.println(str);
    }


}