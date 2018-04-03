package com.ociweb.GPS;

import com.ociweb.gl.api.GreenTokenizer;

public class GPSRMCData {
    public String GPSDataCategory;
    public int hour;
    public int minute;
    public int second;
    public int millisecond;
    public String DataValidity;
    public double latitude;
    public String NorS;
    public double longitude;
    public String EorW;
    public Double Speed;
    public Double Course;
    public int date;
    public int month;
    public int year;

    GPSRMCData (GreenTokenizer rmcext){
        this.GPSDataCategory = rmcext.extractedString(0);
        int UTCTime1 = (int) rmcext.extractedDecimalMantissa(1);
        int UTCTime2 = (int)rmcext.extractedDecimalMantissa(2);
        int UTCTime3 = (int)rmcext.extractedDecimalExponent(2);
        this.hour = (int) (UTCTime1/10000);
        this.minute = (int) ((UTCTime1%10000)/100);
        this.second = (int) (UTCTime1%100);
        this.millisecond = (int) (UTCTime2*(Math.pow(10,UTCTime3)));
        this.DataValidity = rmcext.extractedString(3);
        long latitude1 = rmcext.extractedDecimalMantissa(4);
        long latitude2 = rmcext.extractedDecimalMantissa(5);
        long latitude3 = rmcext.extractedDecimalExponent(5);
        int lat_degrees = (int)(latitude1/100);
        float lat_minutes = (float)((latitude1%100)+(latitude2*(Math.pow(10,latitude3)))/60);
        this.latitude = lat_degrees + lat_minutes;
        this.NorS = rmcext.extractedString(6);
        long longitude1 = rmcext.extractedDecimalMantissa(7);
        long longitude2 = rmcext.extractedDecimalMantissa(8);
        long longitude3 = rmcext.extractedDecimalExponent(8);
        int log_degrees = (int)(longitude1/100);
        double log_minutes = ((longitude1%100)+(longitude2*(Math.pow(10,longitude3)))/60);
        this.longitude = log_degrees + log_minutes;
        this.EorW = rmcext.extractedString(9);
        int speed1 = (int) rmcext.extractedDecimalMantissa(10);
        int speed2 = (int) rmcext.extractedDecimalMantissa(11);
        int speed3 = (int) rmcext.extractedDecimalExponent(11);
        this.Speed = speed1 + (speed2 * (Math.pow(10, speed3)));
        int course1 = (int) rmcext.extractedDecimalMantissa(12);
        int course2 = (int) rmcext.extractedDecimalMantissa(13);
        int course3 = (int) rmcext.extractedDecimalExponent(13);
        this.Course = course1 + (course2 * (Math.pow(10, course3)));
        int date_int = (int) rmcext.extractedLong(14);
        this.date = (int) date_int/10000;
        this.month = ((int) date_int/100)%100;
        this.year = (int) date_int%100;
    }
}
