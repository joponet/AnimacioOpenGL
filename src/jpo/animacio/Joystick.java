package jpo.animacio;

import java.io.IOException;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDManager;

public class Joystick {
	
	// posicions analogic per Rainbow
	public static final int DIRECTION_Z = 1;
	public static final int DIRECTION_W = 2;
	public static final int DIRECTION_X = 3;
	public static final int DIRECTION_Y = 4;
	// tecles direcció
	public static final int UP_POS = 5;
	public static final int UP_VAL = 0x00;
	public static final int UP_RIGHT_POS = 5;
	public static final int UP_RIGHT_VAL = 0x01;
	public static final int RIGHT_POS = 5;
	public static final int RIGHT_VAL = 0x02;
	public static final int DOWN_RIGHT_POS = 5;
	public static final int DOWN_RIGHT_VAL = 0x03;
	public static final int DOWN_POS = 5;
	public static final int DOWN_VAL = 0x04;
	public static final int DOWN_LEFT_POS = 5;
	public static final int DOWN_LEFT_VAL = 0x05;
	public static final int LEFT_POS = 5;
	public static final int LEFT_VAL = 0x06;
	public static final int UP_LEFT_POS = 5;
	public static final int UP_LEFT_VAL = 0x07;
	// tecles 1234
	public static final int B1_POS = 5;
	public static final int B1_VAL = 0x10;
	public static final int B2_POS = 5;
	public static final int B2_VAL = 0x20;
	public static final int B3_POS = 5;
	public static final int B3_VAL = 0x40;
	public static final int B4_POS = 5;
	public static final int B4_VAL = 0x80;
	// butons opt
	public static final int L2_POS = 6;
	public static final int L2_VAL = 0x01;
	public static final int R2_POS = 6;
	public static final int R2_VAL = 0x02;
	public static final int L1_POS = 6;
	public static final int L1_VAL = 0x04;
	public static final int R1_POS = 6;
	public static final int R1_VAL = 0x08;
	public static final int SELECT_POS = 6;
	public static final int SELECT_VAL = 0x10;
	public static final int START_POS = 6;
	public static final int START_VAL = 0x20;
	public static final int BX_POS = 6;
	public static final int BX_VAL = 0x40;
	public static final int BZ_POS = 6;
	public static final int BZ_VAL = 0x80;
	
	private HIDManager manager;
	private HIDDeviceInfo deviceInfo[] = null;
	private HIDDevice device = null;
	
	private float posX=0;
	private float posY=0;
	private byte buf[] = new byte[8];


	public Joystick() {
		ClassPathLibraryLoader.loadNativeHIDLibrary();
		try {
			manager = HIDManager.getInstance();
			deviceInfo = manager.listDevices();
			for (int i=0 ; i<deviceInfo.length ; i++) {
				if (deviceInfo[i].getUsage() == 4) device = manager.openByPath(deviceInfo[i].getPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void display() {
		try {
			// llista de dispositius
			for (int i=0 ; i<deviceInfo.length ; i++) {
				System.out.println(deviceInfo[i].getProduct_string() + " -> " + deviceInfo[i].getPath());
			}
			if (device == null) return;
			// estat dispositiu
			byte buf[] = new byte[8];
			int l = device.read(buf);
			System.out.print(l+":");
			for (int i=0 ; i<buf.length ; i++) {
				System.out.printf("%1$9.8h",buf[i]);
			}
			System.out.println();
			System.out.println(device.getManufacturerString() + "/" + device.getProductString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public float getPosX() {
		posX -= getInc(DIRECTION_X);
		return posX;
	}
	
	public float getPosY() {
		posY += getInc(DIRECTION_Y);
		return posY;
	}
	
	public int getXdir() {
		int dir = 0;
		float fdir = getInc(DIRECTION_X);
		if ( fdir > 0 ) dir = -1;
		else if ( fdir < 0) dir = 1;
		else dir = 0;
		return dir;
	}
	
	public float getIncX() {
		if (device  == null) return 0;
		this.tick();
		return getInc(DIRECTION_X)/1024;
	}
	
	public float getIncY() {
		if (device  == null) return 0;
		this.tick();
		return getInc(DIRECTION_Y)/1024;
	}

	public float getIncZ() {
		if (device  == null) return 0;
		this.tick();
		return getInc(DIRECTION_Z)/128;
	}

	public float getIncW() {
		if (device  == null) return 0;
		this.tick();
		return getInc(DIRECTION_W)/128;
	}

	public boolean getButton(int pos, int value) {
		boolean pressed = false;
		if ((buf[pos] & value) > 0) pressed = true;
		return pressed;
	}
	
	private float getInc(int pos) {
		float inc = 0f;
		inc = (0x80 - (buf[pos] & 0x000000ff));
		return inc;
	}
	
	void tick() {
		if (device  == null) return;
		try {
			device.read(buf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
