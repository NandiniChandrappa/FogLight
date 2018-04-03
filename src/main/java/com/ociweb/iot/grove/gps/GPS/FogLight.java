package com.ociweb.GPS;

import com.ociweb.iot.maker.FogRuntime;

public class FogLight {

	public static void main(String[] args) {
		FogRuntime.run(new GPS_Sensor(),args);
	}
	
}
