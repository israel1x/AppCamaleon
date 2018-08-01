package com.example.pasantias.appcamaleon.DataBase;

import android.arch.persistence.room.TypeConverter;

import java.sql.Date;

public class DataConverter {

    @TypeConverter
    public  Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }


    @TypeConverter
    public  Long toTimestamp(Date date) {
        return date == null ? null : date.getTime(); }
}
