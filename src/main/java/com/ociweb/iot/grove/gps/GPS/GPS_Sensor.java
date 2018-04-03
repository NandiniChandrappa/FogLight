package com.ociweb.GPS;


import com.ociweb.iot.maker.*;
import com.ociweb.pronghorn.pipe.ChannelReader;

public class GPS_Sensor implements FogApp {
    @Override
    public void declareConnections(Hardware hardware) {
        hardware.useSerial(Baud.B_____9600); //optional device can be set as the second argument

    }

    @Override
    public void declareBehavior(FogRuntime runtime) {
        System.out.println("i am here");
        runtime.addSerialListener(new GPSDataUtilization());
    }
}