package com.ociweb.GPS;

import com.ociweb.gl.api.GreenTokenMap;
import com.ociweb.gl.api.GreenTokenizer;
/*import com.ociweb.gl.api.Writable;
import com.ociweb.iot.maker.FogCommandChannel;
import com.ociweb.iot.maker.FogRuntime;
import com.ociweb.iot.maker.SerialListener;*/
import com.ociweb.iot.transducer.SerialListenerTransducer;
import com.ociweb.pronghorn.pipe.ChannelReader;

import java.util.ArrayList;
import java.util.List;

public class GPSTransducer {
    private byte[] myBuffer = new byte[1024];
    private static GreenTokenizer ggaext;
    private static GreenTokenizer gsaext;
    private static GreenTokenizer gsvext;
    private static GreenTokenizer rmcext;
    public GPSTransducerListener listener;


    public static String asciiBytesToString(byte[] bytes, int count) {
        if ((bytes == null) || (bytes.length == 0)) {
            return "";
        }
        char[] result = new char[bytes.length];

        for (int i = 0; i < Math.min(count, bytes.length); i++) {
            result[i] = (char) bytes[i];
        }
        return new String(result);
    }

    public int message(ChannelReader reader) {
        int bytesRead = reader.read(myBuffer);
        final String str = asciiBytesToString(myBuffer, bytesRead);
        int idx = str.indexOf("$");
        if (idx != -1) {
            int idxend = str.indexOf("\r\n", idx);
            if (idxend != -1) {
                String finalstr = str.substring(idx, idxend);
                onWindowedMessage(finalstr);
                return idxend;
            } else {
                return idx;
            }
        }
        return 0;
    }

    private void onWindowedMessage(String message) {
        String key = message.substring(0, 6);

        if (key.equals("$GPGGA")) {
            ggaext = new GreenTokenMap()
                    .add(1, "$%b,%i%.,%i%.,%b,%i%.,%b,%o,%o,%i%.,%i%.,%b,%i%.,%b,%o,*%i")
                    .newTokenizer();
            long t = ggaext.tokenize(message);
            if (t == 1) {
                System.out.println("Name: " + key + " -- " + message);
                GPSGGAData object = new GPSGGAData(ggaext);
                if (listener != null) {
                    listener.onGGA(object);
                }
            } else {
                System.out.println("GPGGA Our pattern string is wrong");
            }
        } else if (key.equals("$GPGSA")) {
            gsaext = new GreenTokenMap()
                    .add(1, "$%b,%b,%i,%o,%o,%o,%o,%o,%o,%o,%o,%o,%o,%o,%o,%i%.,%i%.,%i%.*%b")
                    .newTokenizer();
            long t = gsaext.tokenize(message);
            if (t == 1) {
                System.out.println("Name: " + key + " -- " + message);
                GPSGSAData object = new GPSGSAData(gsaext);
                if (listener != null) {
                    listener.onGSA(object);
                }
            } else {
                System.out.println("GPGSA Our pattern string is wrong");
            }
        } else if (key.equals("$GPGSV")) {
            gsvext = new GreenTokenMap()
                    .add(1, "$%b,%i,%i,%i,%o,%o,%o,%o*%b")
                    .add(2, "$%b,%i,%i,%i,%o,%o,%o,%o,%o,%o,%o,%o*%b")
                    .add(3, "$%b,%i,%i,%i,%o,%o,%o,%o,%o,%o,%o,%o,%o,%o,%o,%o*%b")
                    .add(4, "$%b,%i,%i,%i,%o,%o,%o,%o,%o,%o,%o,%o,%o,%o,%o,%o,%o,%o,%o,%o*%b")
                    .newTokenizer();
            long t = gsvext.tokenize(message);
            if (t <= 4) {
                System.out.println("Name: " + key + " -- " + message);
                GPSGSVData object = new GPSGSVData(gsvext);
                if (listener != null) {
                    listener.onGSV(object);
                }
            } else {
                System.out.println("$GPGSV Our pattern string is wrong");
            }
        } else if (key.equals("$GPRMC")) {
            rmcext = new GreenTokenMap()
                    .add(1, "$%b,%i%.,%b,%i%.,%b,%i%.,%b,%i%.,%i%.,%i,%o,%o,%b*%b")
                    .newTokenizer();
            long t = rmcext.tokenize(message);
            if (t == 1) {
                System.out.println("Name: " + key + " -- " + message);
                GPSRMCData object = new GPSRMCData(rmcext);
                if (listener != null) {
                    listener.onRMC(object);
                }
            } else {
                System.out.println("$GPRMC Our pattern string is wrong");
            }
        }
    }
    }


