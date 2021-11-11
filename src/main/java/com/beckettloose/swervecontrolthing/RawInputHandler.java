package com.beckettloose.swervecontrolthing;

import edu.wpi.first.networktables.*;
import java.io.*;
import com.beckettloose.swervecontrolthing.RawJoystickEvent;

public class RawInputHandler implements Runnable {

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("JoystickControls");

    String path;
    int stickID;
    DataInputStream in;

    NormalizedJoystickEvent[] buttonStates;
    NormalizedJoystickEvent[] axisStates;

    RawInputHandler(String path, int stickID) {
        this.path = path;
        this.stickID = stickID;
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(path));
            this.in = in;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                NormalizedJoystickEvent event = new JoystickEventNormalizer(poll()).get();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public RawJoystickEvent poll() throws IOException {
        in.readInt();
        int value = in.readShort();
        int type = in.readUnsignedByte();
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
