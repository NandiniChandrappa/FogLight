package com.ociweb.GPS;

import com.ociweb.gl.api.GreenTokenizer;

public class GPSGSVData {
    public String GPSDataCategory;
    public int messages;
    public int message_no;
    public int satellites;

    public static class SatDetailList {
        public int message_ID;
        public int elevation;
        public int azimuth;
        public int SNR;
    }

    public SatDetailList[] SatDetail;

    GPSGSVData(GreenTokenizer gsvext) {
        this.GPSDataCategory = gsvext.extractedString(0);
        int x = this.messages = (int) gsvext.extractedLong(1);
        int y = this.message_no = (int) gsvext.extractedLong(2);
        int z = this.satellites = (int) gsvext.extractedLong(3);

        int j;
        if ((z-(4*y)>=0)) {
            int count = 4;
            SatDetail = new SatDetailList[count];
            for (j = 1; j <= 4; j++) {
                int r=j+(4*(y-1));
                SatDetail[j-1] = new SatDetailList();
                this.SatDetail[j-1].message_ID = (int) gsvext.extractedLong(4 + (4 * (j - 1)));
                this.SatDetail[j-1].elevation = (int) gsvext.extractedLong(5 + (4 * (j - 1)));
                this.SatDetail[j-1].azimuth = (int) gsvext.extractedLong(6 + (4 * (j - 1)));
                this.SatDetail[j-1].SNR = (int) gsvext.extractedLong(7 + (4 * (j - 1)));
            }
        } else {
            int count = (z % 4);
            SatDetail = new SatDetailList[count];
            for (j = 1; j<= count; j++) {
                int r=j+(4*(y-1));
                SatDetail[j-1] = new SatDetailList();
                this.SatDetail[j-1].message_ID = (int) gsvext.extractedLong(4 + (4 * (j - 1)));
                this.SatDetail[j-1].elevation = (int) gsvext.extractedLong(5 + (4 * (j - 1)));
                this.SatDetail[j-1].azimuth = (int) gsvext.extractedLong(6 + (4 * (j - 1)));
                this.SatDetail[j-1].SNR = (int) gsvext.extractedLong(7 + (4 * (j - 1)));
            }
        }
    }
}








