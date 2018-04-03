package com.ociweb.GPS;


import com.ociweb.iot.maker.SerialListener;
import com.ociweb.iot.transducer.SerialListenerTransducer;
import com.ociweb.pronghorn.pipe.ChannelReader;

public class GPSDataUtilization implements SerialListener, GPSTransducerListener{
    private final GPSTransducer t;

    public GPSDataUtilization() {
        this.t = new GPSTransducer();
        System.out.println("next");
        t.listener = this;
    }

       public int message(ChannelReader reader) {
        return t.message(reader);
    }
    public void onGGA(GPSGGAData data) {
        System.out.println("first here");
        System.out.println("GPS data Category: " + data.GPSDataCategory);
        System.out.println("UTCTime <hh:mm:ss:ms>: " + data.hour +":"+ data.minute +":" + data.second +":"+data.millisecond);
        System.out.println("latitude: " + data.latitude +"degree" +data.NorS);
        System.out.println("longitude:" + data.longitude +data.EorW);
        System.out.println("Position: " + data.Position);
        System.out.println("Satellites:" + data.Satellites);
        System.out.println("HDOP:" + data.HDOP);
        System.out.println("Altitude:" + data.Altitude+ data.AltitudeUnit);
        System.out.println("GeoidSeparation:" + data.GeoidSeparation+ data.GeoidSeparationUnit);
    }


    public void onGSA(GPSGSAData data) {
        System.out.println("second here");
        System.out.println("GPS data Category: " + data.GPSDataCategory);
        System.out.println("Mode: " + data.Mode);
        System.out.println("View Mode (2D/3D): " + data.ViewMode);
        for (GPSGSAData.ID SatID : data.SatelliteID) {
            System.out.println("Satellite IDs" + "--->" + SatID.SatelliteIDs);
        }
        System.out.println("PDOP: " + data.PDOP);
        System.out.println("HDOP: " + data.HDOP);
        System.out.println("VDOP: " + data.VDOP);
    }

    public void onGSV (GPSGSVData data){
        System.out.println("third here");
        System.out.println("GPS data Category: " + data.GPSDataCategory);
        System.out.println("Number of Msg Strings: " + data.messages);
        System.out.println("Message string number: " + data.message_no);
        System.out.println("Number of satellites details: " + data.satellites);
        for (GPSGSVData.SatDetailList SatDetailElement : data.SatDetail) {
            System.out.println("Message ID --> " + SatDetailElement.message_ID);
            System.out.println("Elevation in degrees of --> " + SatDetailElement.elevation);
            System.out.println("azimuth in degrees of --> " + SatDetailElement.azimuth);
            System.out.println("SNR in dBHz of -->" + SatDetailElement.SNR);}
    }

    public void onRMC (GPSRMCData data){
        System.out.println("fourth here");
        System.out.println("GPS data Category: " + data.GPSDataCategory);
        System.out.println("UTCTime <hh:mm:ss:ms>: " + data.hour + ":" + data.minute + ":" + data.second + ":" + data.millisecond);
        System.out.println("Data Validity (A -> Yes; V -> No): " + data.DataValidity);
        System.out.println("latitude: " + data.latitude + "degree" + data.NorS);
        System.out.println("longitude:" + data.longitude + data.EorW);
        System.out.println("Speed of GPS over ground: " + data.Speed);
        System.out.println("Course of GPS over the ground: " + data.Course);
        System.out.println("Date [dd/mm/yy]: " + data.date + "/" + data.month + "/" + data.year);
    }
}
