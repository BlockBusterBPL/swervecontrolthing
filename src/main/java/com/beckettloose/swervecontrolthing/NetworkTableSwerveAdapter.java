package com.beckettloose.swervecontrolthing;

/**
 * Hello world!
 *
 */
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NetworkTableSwerveAdapter {
    static NetworkTableInstance inst = NetworkTableInstance.getDefault();
    static NetworkTable table = inst.getTable("JoystickControls");

    static RawInputHandler leftStick = new RawInputHandler("/dev/input/js0", 0);
    static NormalizedJoystickEvent[] leftButtonStates = new NormalizedJoystickEvent[0xFF];
    static NormalizedJoystickEvent[] leftAxisStates = new NormalizedJoystickEvent[0xFF];

    static RawInputHandler rightStick = new RawInputHandler("/dev/input/js1", 1);
    static NormalizedJoystickEvent[] rightButtonStates = new NormalizedJoystickEvent[0xFF];
    static NormalizedJoystickEvent[] rightAxisStates = new NormalizedJoystickEvent[0xFF];

    static Thread leftStickThread = new Thread(leftStick);
    static Thread rightStickThread = new Thread(rightStick);

    public static void main( String[] args )
    {
        System.out.println( "Swerve Joystick Controller Starting" );
        inst.startClient("10.0.1.67");
        //inst.startClientTeam(3707);
        //InputHandler.main();
        leftStickThread.start();
        rightStickThread.start();
        while (true) {
            leftButtonStates = leftStick.getButtonStates();
            leftAxisStates = leftStick.getAxisStates();
            rightButtonStates = rightStick.getButtonStates();
            rightAxisStates = rightStick.getAxisStates();
            for (NormalizedJoystickEvent e : leftButtonStates) {
                String entry = new StringBuilder("0B").append(e.getNumber()).toString();
                updateTableValueBoolean(entry, (e.getValue() == 1.0f ? true : false));
            }
            for (NormalizedJoystickEvent e : leftAxisStates) {
                String entry = new StringBuilder("0A").append(e.getNumber()).toString();
                updateTableValueDouble(entry, e.getValue());
            }
            for (NormalizedJoystickEvent e : rightButtonStates) {
                String entry = new StringBuilder("1B").append(e.getNumber()).toString();
                updateTableValueBoolean(entry, (e.getValue() == 1.0f ? true : false));
            }
            for (NormalizedJoystickEvent e : rightAxisStates) {
                String entry = new StringBuilder("1A").append(e.getNumber()).toString();
                updateTableValueDouble(entry, e.getValue());
            }
        }
    }

    public static void updateTableValueDouble(String entry, double value) {
        table.getEntry(entry).forceSetDouble(value);
    }

    public static void updateTableValueBoolean(String entry, Boolean value) {
        table.getEntry(entry).forceSetBoolean(value);
    }
}
