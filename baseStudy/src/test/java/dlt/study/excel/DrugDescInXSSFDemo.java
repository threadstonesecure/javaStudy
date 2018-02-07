package dlt.study.excel;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Map;

public class DrugDescInXSSFDemo {
    public static void main(String[] args) throws Exception {
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream("/Users/denglt/mywork/脚本/药品说明书-dlt.xlsx"));
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow header = sheet.getRow(0);
        Map<Integer, String> fieldNames = Maps.newHashMap();
        header.forEach(cell -> {
            if (!StringUtils.isEmpty(cell.getStringCellValue()))
                fieldNames.put(cell.getColumnIndex(), cell.getStringCellValue());
        });
        // System.out.println(fieldNames);
        //{0=药品通用名, 1=商品名, 2=主要成分, 3=规格, 4=功能主治（适应症）, 5=用法用量, 6=不良反应, 7=禁忌, 8=注意事项, 9=儿童用药, 10=老年用药, 11=孕妇及哺乳期妇女用药, 12=药物相互作用, 13=药物过量, 14=药理毒理, 15=药代动力学, 16=贮藏, 17=生产企业, 18=批准文号}

        Table<Integer, Integer, String> table = HashBasedTable.create();
        sheet.forEach(row -> {
            if (row.getRowNum() != 0) {
                row.forEach(cell -> table.put(row.getRowNum(), cell.getColumnIndex(), cell.getStringCellValue()));
            }
        });


        String url = "jdbc:mysql://127.0.0.1:3306/med2?zeroDateTimeBehavior=convertToNull";
        String name = "com.mysql.jdbc.Driver";
        String user = "root";
        String password = "denglt";
        Class.forName(name);//指定连接类型
        Connection conn = DriverManager.getConnection(url, user, password);//获取连接
        conn.setAutoCommit(false);
        String sql = "insert into olt_drug_direction(ODD_ID,DRUG_CHEM_NAME, DRUG_TRADE_NAME,DRUG_SPEC,DRUG_MANUFATURER,UPDATE_TIME,CREATE_TIME) " +
                "values (?,?,?,?,?,now(),now()) ";
        PreparedStatement ps = conn.prepareStatement(sql);//准备执行语句
        String sql2 = "insert into olt_drug_direction_item(ODD_ID,item_label,item_value,item_order) " +
                "values (?,?,?,?) ";
        PreparedStatement ps2 = conn.prepareStatement(sql2);
        table.rowKeySet().forEach(rowNum -> {
            try {
                // 主表
                ps.setInt(1, rowNum);
                ps.setString(2, table.get(rowNum, 0));
                ps.setString(3, table.get(rowNum, 1));
                ps.setString(4, table.get(rowNum, 3));
                ps.setString(5, table.get(rowNum, 17));
                ps.executeUpdate();
                // 明细
                fieldNames.forEach((colIndex, fieldName) -> {
                    try {
                        ps2.setInt(1, rowNum);
                        ps2.setString(2, fieldName);
                        ps2.setString(3, table.get(rowNum, colIndex));
                        ps2.setInt(4, colIndex + 1);
                        ps2.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println("错误数据：" + rowNum);
                        e.printStackTrace();
                    }
                });

            } catch (SQLException e) {
                System.out.println("错误数据：" + rowNum);
                e.printStackTrace();
            }

        });
        conn.commit();
    }
}
