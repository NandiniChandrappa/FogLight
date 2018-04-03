package com.ociweb.GPS;

import com.ociweb.gl.api.GreenTokenizer;
//import com.ociweb.iot.maker.FogRuntime;

import java.util.ArrayList;

public class GPSGSAData {
    public String GPSDataCategory;
    public String Mode;
    public int ViewMode;
    public static class ID{public int SatelliteIDs;}
    public double PDOP;
    public double HDOP;
    public double VDOP;


    public ID[] SatelliteID;

    GPSGSAData(GreenTokenizer gsaext) {
        this.GPSDataCategory = gsaext.extractedString(0);
        this.Mode = gsaext.extractedString(1);
        this.ViewMode = (int)gsaext.extractedLong(2);
        int i;
        int count=12;
        SatelliteID = new ID[count];
        for (i=0;i<count;i++){
            SatelliteID[i] = new ID();
            this.SatelliteID[i].SatelliteIDs = (int) gsaext.extractedLong(3 + (i+1));
        }
        int PDOP1 = (int) gsaext.extractedDecimalMantissa(15);
        int PDOP2 = (int) gsaext.extractedDecimalMantissa(16);
        int PDOP3 = (int) gsaext.extractedDecimalExponent(16);
        this.PDOP = PDOP1 + (PDOP2 * (Math.pow(10, PDOP3)));
        int HDOP1 = (int) gsaext.extractedDecimalMantissa(17);
        int HDOP2 = (int) gsaext.extractedDecimalMantissa(18);
        int HDOP3 = (int) gsaext.extractedDecimalExponent(18);
        this.HDOP = HDOP1 + (HDOP2 * (Math.pow(10, HDOP3)));
        int VDOP1 = (int) gsaext.extractedDecimalMantissa(19);
        int VDOP2 = (int) gsaext.extractedDecimalMantissa(20);
        int VDOP3 = (int) gsaext.extractedDecimalExponent(20);
        this.VDOP = VDOP1 + (VDOP2 * (Math.pow(10, VDOP3)));
    }
}
