package com.huebeiro.insuranceco.connection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.huebeiro.insuranceco.AddCauseActivity;
import com.huebeiro.insuranceco.model.Attachment;
import com.huebeiro.insuranceco.model.Car;
import com.huebeiro.insuranceco.model.Cause;
import com.huebeiro.insuranceco.model.Event;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Adilson on 19/04/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    static final int VERSION = 1;

    static final String DATABASE = "InsuranceCo.db";

    static final String[] SQL_SCRIPT = new String[] {
            "CREATE TABLE RelatedCause ( " +
            "relID INTEGER PRIMARY KEY, " +
            "relDescription TEXT " +
            "); ",
            "INSERT INTO RelatedCause(relID,relDescription) " +
            "VALUES " +
            "(1,'Direction System'), " +
            "(2,'Doors'), " +
            "(3,'Engine'), " +
            "(4,'Gear Box'), " +
            "(5,'Radiator'), " +
            "(6,'Suspension System'), " +
            "(8,'Tires'), " +
            "(9,'Wheels'); ",
            "CREATE TABLE Car ( " +
            "carID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "carLicencePlate TEXT, " +
            "carFipeID INTEGER, " +
            "carMarca TEXT, " +
            "carModelo TEXT, " +
            "carAno INTEGER " +
            "); ",
            "CREATE TABLE Event ( " +
            "eveID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "carID INTEGER, " +
            "eveDate DATETIME, " +
            "FOREIGN KEY(carID) REFERENCES Car(carID) " +
            "); ",
            "CREATE TABLE EventAttach ( " +
            "evaID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "eveID INTEGER, " +
            "evaFile BLOB, " +
            "evaDescription TEXT, " +
            "FOREIGN KEY(eveID) REFERENCES Event(eveID) " +
            "); ",
            "CREATE TABLE EventRelCause ( " +
            "ercID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "eveID INTEGER, " +
            "relID INTEGER, " +
            "FOREIGN KEY(eveID) REFERENCES Event(eveID), " +
            "FOREIGN KEY(relID) REFERENCES RelatedCause(relID) " +
            ");"
    };


    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(String script : SQL_SCRIPT)
            db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            db.execSQL("DROP TABLE " + RelatedCause.TABLE_NAME);
            db.execSQL("DROP TABLE " + Car.TABLE_NAME);
            db.execSQL("DROP TABLE " + Event.TABLE_NAME);
            db.execSQL("DROP TABLE " + EventAttach.TABLE_NAME);
            db.execSQL("DROP TABLE " + EventRelCause.TABLE_NAME);
            onCreate(db);
        }
    }

    public static com.huebeiro.insuranceco.model.Car[] getAllCars(SQLiteDatabase database){
        ArrayList<com.huebeiro.insuranceco.model.Car> aCars = new ArrayList<>();
        Cursor cCars = database.query(DatabaseHelper.Car.TABLE_NAME,
                new String[]{
                        DatabaseHelper.Car.carID,
                        DatabaseHelper.Car.carAno,
                        DatabaseHelper.Car.carMarca,
                        DatabaseHelper.Car.carModelo,
                        DatabaseHelper.Car.carFipeID,
                        DatabaseHelper.Car.carLicencePlate
                }, null ,null, null, null, DatabaseHelper.Car.carLicencePlate);


        while(cCars.moveToNext()){
            com.huebeiro.insuranceco.model.Car car = new com.huebeiro.insuranceco.model.Car();
            car.setBrand(cCars.getString(cCars.getColumnIndex(DatabaseHelper.Car.carMarca)));
            car.setYear(cCars.getInt(cCars.getColumnIndex(DatabaseHelper.Car.carAno)));
            car.setModel(cCars.getString(cCars.getColumnIndex(DatabaseHelper.Car.carModelo)));
            car.setPlate(cCars.getString(cCars.getColumnIndex(DatabaseHelper.Car.carLicencePlate)));
            car.setId(cCars.getInt(cCars.getColumnIndex(DatabaseHelper.Car.carID)));
            car.setFipeId(cCars.getInt(cCars.getColumnIndex(DatabaseHelper.Car.carFipeID)));
            car.setTotalEvents(getTotalEvents(database,car));
            aCars.add(car);
        }

        com.huebeiro.insuranceco.model.Car[] cars = new com.huebeiro.insuranceco.model.Car[aCars.size()];
        cars = aCars.toArray(cars);

        cCars.close();

        return cars;
    }

    public static int getTotalEvents(SQLiteDatabase database, com.huebeiro.insuranceco.model.Car car){
        int total = 0;
        Cursor cursor = database.rawQuery(
                "Select count(*) total from " + Event.TABLE_NAME + " where " + Event.carID + " = " + car.getId()
                , null);
        if(cursor.moveToNext()){
            total = cursor.getInt(0);
        }
        return total;
    }

    public static int getTotalEvents(SQLiteDatabase database, com.huebeiro.insuranceco.model.Car car, String date){
        int total = 0;
        Cursor cursor = database.rawQuery(
                "Select count(*) total from " + Event.TABLE_NAME +
                        " where " + Event.carID + " = " + car.getId() +
                        " and " + Event.eveDate + " = '" + date + "'"
                , null);
        if(cursor.moveToNext()){
            total = cursor.getInt(0);
        }
        return total;
    }

    public static com.huebeiro.insuranceco.model.Event[] getEvents(SQLiteDatabase database, com.huebeiro.insuranceco.model.Car car, String date){
        int total = 0;
        ArrayList<com.huebeiro.insuranceco.model.Event> aEvents = new ArrayList<>();
        Cursor cursor = database.rawQuery(
                "Select * from " + Event.TABLE_NAME +
                        " where " + Event.carID + " = " + car.getId() +
                        " and " + Event.eveDate + " = '" + date + "'"
                , null);
        while(cursor.moveToNext()){
            com.huebeiro.insuranceco.model.Event event = new com.huebeiro.insuranceco.model.Event();
            event.setId(cursor.getInt(cursor.getColumnIndex(Event.eveID)));
            event.setCarId(cursor.getInt(cursor.getColumnIndex(Event.carID)));
            event.setDate(date);
            event.setTotalAttachments(getTotalAttachments(database, event));
            event.setTotalCauses(getTotalRelatedCauses(database, event));
            aEvents.add(event);
        }
        com.huebeiro.insuranceco.model.Event[] events = new com.huebeiro.insuranceco.model.Event[aEvents.size()];
        events = aEvents.toArray(events);
        return events;
    }

    public static int getTotalAttachments(SQLiteDatabase database, com.huebeiro.insuranceco.model.Event event){
        int total = 0;
        Cursor cursor = database.rawQuery(
                "Select count(*) total from " + EventAttach.TABLE_NAME +
                        " where " + EventAttach.eveID + " = " + event.getId()
                , null);
        if(cursor.moveToNext()){
            total = cursor.getInt(0);
        }
        return total;
    }

    public static int getTotalRelatedCauses(SQLiteDatabase database, com.huebeiro.insuranceco.model.Event event){
        int total = 0;
        Cursor cursor = database.rawQuery(
                "Select count(*) total from " + EventRelCause.TABLE_NAME +
                        " where " + EventRelCause.eveID + " = " + event.getId()
                , null);
        if(cursor.moveToNext()){
            total = cursor.getInt(0);
        }
        return total;
    }

    public static Cause[] getCauses(SQLiteDatabase database, com.huebeiro.insuranceco.model.Event event) {
        ArrayList<Cause> aCauses = new ArrayList<>();
        Cursor cursor = database.rawQuery(
                "Select * from " + EventRelCause.TABLE_NAME +
                        " where " + EventRelCause.eveID + " = " + event.getId()
                , null);
        while(cursor.moveToNext()){
            Cause cause = new Cause();
            cause.setId(cursor.getInt(cursor.getColumnIndex(EventRelCause.relID)));
            cause.setDescription(AddCauseActivity.causes[cause.getId()]);
            aCauses.add(cause);
        }
        Cause[] causes = new Cause[aCauses.size()];
        causes = aCauses.toArray(causes);
        return causes;
    }

    public static Attachment[] getAttachments(SQLiteDatabase database, com.huebeiro.insuranceco.model.Event event) {
        ArrayList<Attachment> aAttach = new ArrayList<>();
        Cursor cursor = database.rawQuery(
                "Select * from " + EventAttach.TABLE_NAME +
                        " where " + EventAttach.eveID + " = " + event.getId()
                , null);
        while(cursor.moveToNext()){
            Attachment attach = new Attachment();
            attach.setId(cursor.getInt(cursor.getColumnIndex(EventAttach.evaID)));
            attach.setDescription(cursor.getString(cursor.getColumnIndex(EventAttach.evaDescription)));
            attach.setFile(cursor.getBlob(cursor.getColumnIndex(EventAttach.evaFile)));
            aAttach.add(attach);
        }
        Attachment[] attachs = new Attachment[aAttach.size()];
        attachs = aAttach.toArray(attachs);
        return attachs;
    }

    /*Database Reference Classes*/

    public class RelatedCause{
        public static final String TABLE_NAME = "RelatedCause";
        public static final String relID = "relID";
        public static final String relDescription = "relDescription";
    }

    public class Car{
        public static final String TABLE_NAME = "Car";
        public static final String carID = "carID";
        public static final String carLicencePlate = "carLicencePlate";
        public static final String carFipeID = "carFipeID";
        public static final String carMarca = "carMarca";
        public static final String carModelo = "carModelo";
        public static final String carAno = "carAno";
    }

    public class Event{
        public static final String TABLE_NAME = "Event";
        public static final String eveID = "eveID";
        public static final String carID = "carID";
        public static final String eveDate = "eveDate";
    }

    public class EventAttach{
        public static final String TABLE_NAME = "EventAttach";
        public static final String evaID = "evaID";
        public static final String eveID = "eveID";
        public static final String evaFile = "evaFile";
        public static final String evaDescription = "evaDescription";
    }

    public class EventRelCause {
        public static final String TABLE_NAME = "EventRelCause";
        public static final String ercID = "ercID";
        public static final String eveID = "eveID";
        public static final String relID = "relID";
    }
}

