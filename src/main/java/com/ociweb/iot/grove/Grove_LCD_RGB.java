package com.ociweb.iot.grove;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ociweb.iot.hardware.IODevice;
import com.ociweb.iot.maker.CommandChannel;
import com.ociweb.pronghorn.iot.i2c.I2CStage;
import com.ociweb.pronghorn.iot.schema.GroveResponseSchema;
import com.ociweb.pronghorn.iot.schema.I2CCommandSchema;
import com.ociweb.pronghorn.pipe.DataOutputBlobWriter;
import com.ociweb.pronghorn.pipe.Pipe;
import com.ociweb.pronghorn.pipe.PipeWriter;
import com.ociweb.pronghorn.pipe.RawDataSchema;

/**
 * TODO: This class probably needs to be renamed and moved; it's now both a simple API and collection of constants.
 *
 * @author Nathan Tippy
 * @author Brandon Sanders [brandon@alicorn.io]
 */
public class Grove_LCD_RGB implements IODevice{ //TODO: convert this into an IODevice

    // Device I2C Adress (note this only uses the lower 7 bits)
    public static int LCD_ADDRESS  =   (0x7c>>1); //  11 1110  0x3E
    public static final int RGB_ADDRESS  =   (0xc4>>1); // 110 0010  0x62


    // color define 
    public static final int WHITE       =    0;
    public static final int RED         =    1;
    public static final int GREEN       =    2;
    public static final int BLUE        =    3;

    public static final int REG_RED     =    0x04;        // pwm2
    public static final int REG_GREEN   =    0x03;        // pwm1
    public static final int REG_BLUE    =    0x02;        // pwm0

    public static final int REG_MODE1    =   0x00;
    public static final int REG_MODE2    =   0x01;
    public static final int REG_OUTPUT   =   0x08;

    // commands
    public static final int LCD_CLEARDISPLAY   =0x01;
    public static final int LCD_RETURNHOME     =0x02;
    public static final int LCD_ENTRYMODESET   =0x04;
    public static final int LCD_DISPLAYCONTROL =0x08;
    public static final int LCD_CURSORSHIFT    =0x10;
    public static final int LCD_FUNCTIONSET    =0x20;
    public static final int LCD_TWO_LINES      =0x28;
    public static final int LCD_SETCGRAMADDR   =0x40;
    public static final int LCD_SETDDRAMADDR   =0x80;

    // flags for display entry mode
    public static final int LCD_ENTRYRIGHT          =0x00;
    public static final int LCD_ENTRYLEFT           =0x02;
    public static final int LCD_ENTRYSHIFTINCREMENT =0x01;
    public static final int LCD_ENTRYSHIFTDECREMENT =0x00;

    // flags for display on/off control
    public static final int LCD_DISPLAYON  =0x04;
    public static final int LCD_DISPLAYOFF =0x00;
    public static final int LCD_CURSORON   =0x02;
    public static final int LCD_CURSOROFF  =0x00;
    public static final int LCD_BLINKON    =0x01;
    public static final int LCD_BLINKOFF   =0x00;

    // flags for display/cursor shift
    public static final int LCD_DISPLAYMOVE =0x08;
    public static final int LCD_CURSORMOVE  =0x00;
    public static final int LCD_MOVERIGHT   =0x04;
    public static final int LCD_MOVELEFT    =0x00;

    // flags for function set
    public static final int LCD_8BITMODE =0x10;
    public static final int LCD_4BITMODE =0x00;
    public static final int LCD_2LINE =0x08;
    public static final int LCD_1LINE =0x00;
    public static final int LCD_5x10DOTS =0x04;
    public static final int LCD_5x8DOTS =0x00;


    
    /**
     * <pre>
     * Creates a complete byte array that will set the text and color of a Grove RGB
     * LCD display when passed to a {@link com.ociweb.pronghorn.stage.test.ByteArrayProducerStage}
     * which is using chunk sizes of 3 and is being piped to a {@link I2CStage}.
     *
     * <b>Note: Internally, this method makes calls to {@link #commandForText(String)}
     * and {@link #commandForColor(int, int, int)} and then combines the results into
     * a single array. This results in some leftover arrays that could create garbage.</b>
     *
     * TODO: This function is currently causing the last letter of the text to be dropped
     *       when displayed on the Grove RGB LCD; there's currently a work-around that
     *       simply appens a space to the incoming text variable, but it's hackish
     *       and should be looked into more...
     *
     * @param text String to display on the Grove RGB LCD.
     * @param r 0-255 value for the Red color.
     * @param g 0-255 value for the Green color.
     * @param b 0-255 value for the Blue color.
     *
     * @return Formatted byte array which can be passed directly to a
     *         {@link com.ociweb.pronghorn.stage.test.ByteArrayProducerStage}.
     * </pre>
     */
    public static boolean commandForTextAndColor(CommandChannel target, String text, int r, int g, int b) {
        
        if (!target.i2cIsReady()) {
            return false;
        }
        
        showRGBColor(target, r, g, b);
        showTwoLineText(target, text);
        while (!target.i2cFlushBatch()) {
            //WARNING: this is now a blocking call, should NEVER happen because we checked up front.
        }
        return true;
    }
    public static boolean commandForColor(CommandChannel target, int r, int g, int b) {
        
        if (!target.i2cIsReady()) {
            return false;
        }
        
        showRGBColor(target, r, g, b);
        while (!target.i2cFlushBatch()) {
            //WARNING: this is now a blocking call, should NEVER happen because we checked up front.
        }
        return true;
    }
    public static boolean commandForText(CommandChannel target, String text) {
        
        if (!target.i2cIsReady()) {
            return false;
        }
        showTwoLineText(target, text);
        while (!target.i2cFlushBatch()) {
            //WARNING: this is now a blocking call, should NEVER happen because we checked up front.
        }
        return true;
    }

    private static void showRGBColor(CommandChannel target, int r, int g, int b) {
        writeSingleByteToRegister(target, ((Grove_LCD_RGB.RGB_ADDRESS)), 0, 0);
        writeSingleByteToRegister(target, ((Grove_LCD_RGB.RGB_ADDRESS)), 1, 0);
        writeSingleByteToRegister(target, ((Grove_LCD_RGB.RGB_ADDRESS)), 0x08, 0xaa);
        writeSingleByteToRegister(target, ((Grove_LCD_RGB.RGB_ADDRESS)), 4, r);
        writeSingleByteToRegister(target, ((Grove_LCD_RGB.RGB_ADDRESS)), 3, g);
        writeSingleByteToRegister(target, ((Grove_LCD_RGB.RGB_ADDRESS)), 2, b);
    }

    private static void showTwoLineText(CommandChannel target, String text) {
        //clear display
        writeSingleByteToRegister(target, ((Grove_LCD_RGB.LCD_ADDRESS)), LCD_SETDDRAMADDR, LCD_CLEARDISPLAY);
        //display on - no cursor
        writeSingleByteToRegister(target, ((Grove_LCD_RGB.LCD_ADDRESS)), LCD_SETDDRAMADDR, ((byte) LCD_DISPLAYCONTROL | (byte) LCD_ENTRYMODESET));
        //two lines
        writeSingleByteToRegister(target, ((Grove_LCD_RGB.LCD_ADDRESS)), LCD_SETDDRAMADDR, LCD_TWO_LINES);
                  
        //Parse text.
        int count = 0;
        int row = 0;
        for(int i = 0; i < text.length(); i++ ) {
            char c = text.charAt(i);
            if (c == '\n' || count == 16) {
                count = 0;
                row += 1;
                if (row == 2) break;
        
                //Write a thing. TODO: What's the thing?
                writeSingleByteToRegister(target, ((Grove_LCD_RGB.LCD_ADDRESS)), LCD_SETDDRAMADDR, 0xc0);

                if (c == '\n') continue;
            }
        
            count += 1;
        
            //Write chars.
            writeSingleByteToRegister(target, ((Grove_LCD_RGB.LCD_ADDRESS)), LCD_SETCGRAMADDR, c);

        }
    }
    
    private static void writeSingleByteToRegister(CommandChannel target, int address, int register, int value) {
        try {
            DataOutputBlobWriter<RawDataSchema> i2cPayloadWriter;
            do {
                i2cPayloadWriter = target.i2cCommandOpen(address);
            } while (null==i2cPayloadWriter); //WARNING: this is now a blocking call, NOTE be sure pipe is long enough for the known messages to ensure this never happens  TODO: check this figure.
            i2cPayloadWriter.writeByte(register);
            i2cPayloadWriter.writeByte(value);
            target.i2cCommandClose();
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }
	@Override
	public void writeBit(Pipe<GroveResponseSchema> responsePipe, int connector, long time, int bitValue) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void writeInt(Pipe<GroveResponseSchema> responsePipe, int connector, long time, int intValue, int average) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void writeRotation(Pipe<GroveResponseSchema> responsePipe, int connector, long time, int value, int delta,
			int speed) {
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean isInput() {
		return false;
	}
	@Override
	public boolean isOutput() {
		return true;
	}
	@Override
	public boolean isPWM() {
		return false;
	}
	@Override
	public int pwmRange() {
		throw new UnsupportedOperationException();
	}
//	@Override
//	public boolean isI2C() {
//		return true;
//	}
	@Override
	public byte[] getReadMessage() {
		throw new UnsupportedOperationException();
	}
	@Override
	public boolean isGrove() {
		return false;
	}
    
}
