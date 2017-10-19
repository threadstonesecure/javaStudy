package com.example;

import java.sql.SQLException;

/**
 * Created by denglt on 2016/3/25.
 */
public class TestEventProtocol {

    public static void main(String[] args) {
        EventProtocol.Event.Builder builder =  EventProtocol.Event.newBuilder();

        EventProtocol.Event event = builder.build();

        try{
            runSql();
        }catch (SQLException ex){
            ex.printStackTrace();
        }

    }

    public  static void runSql() throws SQLException {
         throw  new SQLException();
    }
}