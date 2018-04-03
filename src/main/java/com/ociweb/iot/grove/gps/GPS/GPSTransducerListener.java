package com.ociweb.GPS;

public interface GPSTransducerListener {
    void onGGA(GPSGGAData data);
    void onGSA(GPSGSAData data);
    void onGSV(GPSGSVData data);
    void onRMC(GPSRMCData data);
}

