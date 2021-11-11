package com.beckettloose.swervecontrolthing;

/**
 * Hello world!
 *
 */
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import com.beckettloose.swervecontrolthing.RawInputHandler;

public class NetworkTableSwerveAdapter {
    static NetworkTableInstance inst = NetworkTableInstance.getDefault();
    static NetworkTable table = inst.getTable("JoystickControls");
    static RawInputHandler leftStick = new RawInputHandler("/dev/input/js0", 0);
    static NormalizedJoystickEvent[] leftButtonStates;
    static NormalizedJoystickEvent[] leftAxisStates;
    static RawInputHandler rightStick = new RawInputHandler("/dev/input/js1", 1);
    static NormalizedJoystickEvent[] rightButtonStates;
    static NormalizedJoystickEvent[] rightAxisStates;

    public static void main( String[] args )
    {
        System.out.println( "Swerve Joystick Controller Starting" );
        inst.startClient("10.0.1.68");
        //inst.startClientTeam(3707);
        InputHandler.main();
        leftStick.run();
        rightStick.run();
        while (true) {
            leftButtonStates = leftStick.getButtonStates();
            leftAxisStates = leftStick.getAxisStates();
            rightButtonStates = rightStick.getButtonStates();
            rightAxisStates = rightStick.getAxisStates();
            for (NormalizedJoystickEvent e : leftButtonStates) {
                String entry = new StringBuilder(0).append(e.getNumber()).toString();
                updateTableValueBoolean(entry, (e.getValue() == 1.0f ? true : false));
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
