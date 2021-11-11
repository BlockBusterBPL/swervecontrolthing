package com.beckettloose.swervecontrolthing;

import edu.wpi.first.networktables.*;
import java.io.*;

public class RawInputHandler implements Runnable {

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("JoystickControls");

    String path;
    int stickID;
    DataInputStream in;

    NormalizedJoystickEvent[] buttonStates = new NormalizedJoystickEvent[0xFF];
    NormalizedJoystickEvent[] axisStates = new NormalizedJoystickEvent[0xFF];

    RawInputHandler(String path, int stickID) {
        this.path = path;
        this.stickID = stickID;
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
            this.in = in;
            System.out.println("Initialized RawInputHandler for joystick " + stickID);
        } catch (FileNotFoundException e) {
            System.out.println("Device File Not Found!");
            e.printStackTrace();
        }
    }

    public void run() {
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

    public RawJoystickEvent getNextEvent() throws IOException {
        in.readInt();
        int value = in.readShort();
        int type = (in.readUnsignedByte() << 1 ) >> 1 ;
        int number = in.readUnsignedByte();
        return new RawJoystickEvent(value, type, number);
    }

    public NormalizedJoystickEvent[] getButtonStates() {
        return this.buttonStates;
    }

    public NormalizedJoystickEvent[] getAxisStates() {
        return this.axisStates;
    }
}
