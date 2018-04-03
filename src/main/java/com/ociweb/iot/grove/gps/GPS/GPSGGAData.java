package com.ociweb.GPS;

import com.ociweb.gl.api.GreenTokenizer;

public class GPSGGAData {
    public String GPSDataCategory;
    public int hour;
    public int minute;
    public int second;
    public int millisecond;
    public double latitude;
    public String NorS;
    public double longitude;
    public String EorW;
    public int Position;
    public int Satellites;
    public double HDOP;
    public Double Altitude;
    public String AltitudeUnit;
    public Double GeoidSeparation;
    public String GeoidSeparationUnit;

    GPSGGAData(GreenTokenizer ggaext){

        this.GPSDataCategory = ggaext.extractedString(0);
        int UTCTime1 = (int) ggaext.extractedDecimalMantissa(1);
        int UTCTime2 = (int)ggaext.extractedDecimalMantissa(2);
        int UTCTime3 = (int)ggaext.extractedDecimalExponent(2);
        this.hour = (int) (UTCTime1/10000);
        this.minute = (int) ((UTCTime1%10000)/100);
        this.second = (int) (UTCTime1%100);
        this.millisecond = (int) (UTCTime2*(Math.pow(10,UTCTime3)));
        long latitude1 = ggaext.extractedDecimalMantissa(3); // 3840
        long latitude2 = ggaext.extractedDecimalMantissa(4); // 4808
        long latitude3 = ggaext.extractedDecimalExponent(4); // -4
        int lat_degrees = (int)(latitude1/100);
        float lat_minutes = (float)((latitude1%100)+(latitude2*(Math.pow(10,latitude3)))/60);
        this.latitude = lat_degrees + lat_minutes;
        this.NorS = ggaext.extractedString(5);
        long longitude1 = ggaext.extractedDecimalMantissa(6);
        long longitude2 = ggaext.extractedDecimalMantissa(7);
        long longitude3 = ggaext.extractedDecimalExponent(7);
        int log_degrees = (int)(longitude1/100);
        double log_minutes = ((longitude1%100)+(longitude2*(Math.pow(10,longitude3)))/60);
        this.longitude = log_degrees + log_minutes;
        this.EorW = ggaext.extractedString(8);
        this.Position = (int)ggaext.extractedDecimalMantissa(9);
        this.Satellites = (int)ggaext.extractedDecimalMantissa(10);
        int HDOP1 =  (int)ggaext.extractedDecimalMantissa(11);
        int HDOP2 =  (int)ggaext.extractedDecimalMantissa(12);
        int HDOP3 =  (int)ggaext.extractedDecimalExponent(12);
        this.HDOP = HDOP1 + (HDOP2*(Math.pow(10,HDOP3)));
        int Altitude1 = (int) ggaext.extractedDecimalMantissa(13);
        int Altitude2 = (int) ggaext.extractedDecimalMantissa(14);
        int Altitude3 = (int) ggaext.extractedDecimalExponent(14);
        this.Altitude = Altitude1 + (Altitude2*(Math.pow(10,Altitude3)));
        this.AltitudeUnit = ggaext.extractedString(15);
        int GeoidSeparation1 =  (int)ggaext.extractedDecimalMantissa(16);
        int GeoidSeparation2 =  (int)ggaext.extractedDecimalMantissa(17);
        int GeoidSeparation3 =  (int)ggaext.extractedDecimalExponent(17);
        int x = ((0-(GeoidSeparation1>>>63))|1);
        this.GeoidSeparation = GeoidSeparation1+(x*(GeoidSeparation2*Math.pow(10,GeoidSeparation3)));
        this.GeoidSeparationUnit = ggaext.extractedString(18);
    }
}