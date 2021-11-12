package com.beckettloose.swervecontrolthing;

import edu.wpi.first.networktables.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Represents an instance of a raw input handler thread
 */
public class RawInputHandler implements Runnable {

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("JoystickControls");

    String path;
    int stickID;
    DataInputStream in;

    NormalizedJoystickEvent[] buttonStates = new NormalizedJoystickEvent[0xF];
    NormalizedJoystickEvent[] axisStates = new NormalizedJoystickEvent[0xF];

    /**
     * Create an instance of the raw input handler
     * @param path the file path to the joystick's device file (usually in /dev/input/)
     * @param stickID the ID of the event handler. This should usually be the same ID as the device file (e.g. /dev/input/js0 would be 0)
     */
    RawInputHandler(String path, int stickID) {
        this.path = path;
        this.stickID = stickID;
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
            this.in = in;
            System.out.println("Initialized RawInputHandler for joystick " + stickID);
            this.buttonStates = getEmptyArray(0x1);
            this.axisStates = getEmptyArray(0x2);
        } catch (FileNotFoundException e) {
            System.out.println("Device File Not Found!");
            e.printStackTrace();
        }
    }

    /**
     * The main method for the Raw Input Handler thread
     */
    public void run() {
        buttonStates = getEmptyArray(0x1);
        axisStates = getEmptyArray(0x2);
        while (!Thread.interrupted()) {
            try {
                NormalizedJoystickEvent event = new JoystickEventNormalizer(getNextEvent()).getNormalizedEvent();
                Boolean isEventAxis = event.isAxis();
                int eventId = event.getNumber();
                if (isEventAxis) {
                    axisStates[eventId] = event;
                } else {
                    buttonStates[eventId] = event;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Reads the next event from the instance's Input Buffer
     * @return the Raw Joystick Event recieved
     * @throws IOException if the method fails to read from the Input Buffer
     */
    public RawJoystickEvent getNextEvent() throws IOException {
        in.readInt();
        short value = swapEndian(in.readNBytes(2));
        int type = (in.readUnsignedByte() << 0xF ) >> 0xF ;
        int number = in.readUnsignedByte();
        return new RawJoystickEvent(value, type, number);
    }

    private short swapEndian(byte[] b) {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(b[0]);
        bb.put(b[1]);
        return bb.getShort(0);
    }

    public NormalizedJoystickEvent[] getButtonStates() {
        return this.buttonStates;
    }

    public NormalizedJoystickEvent[] getAxisStates() {
        return this.axisStates;
    }

    public static NormalizedJoystickEvent[] getEmptyArray(int type) {
        NormalizedJoystickEvent[] out = new NormalizedJoystickEvent[0xF];
        for (int i = 0; i < 0xF; i++) {
            out[i] = new NormalizedJoystickEvent(0, type, i);
        }
        return out;
    }
}
