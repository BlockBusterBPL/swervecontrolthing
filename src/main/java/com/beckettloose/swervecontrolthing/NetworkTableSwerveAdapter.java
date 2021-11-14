package com.beckettloose.swervecontrolthing;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * A system to convert linux joystick commands to FRC NetworkTables Data
 * @author Beckett Loose
 */
public class NetworkTableSwerveAdapter {
    // Get instance of NetworkTables
    static NetworkTableInstance inst = NetworkTableInstance.getDefault();
    static NetworkTable table = inst.getTable("JoystickControls");

    // Meta Table
    static NetworkTable meta = inst.getTable("JoystickMeta");
    static NetworkTableEntry bootEntry = inst.getEntry("boot");

    // Set up Input Handler and value storage for Left Joystick
    static RawInputHandler leftStick = new RawInputHandler("/dev/input/js0", 0);
    static NormalizedJoystickEvent[] leftButtonStates = new NormalizedJoystickEvent[0xF];
    static NormalizedJoystickEvent[] leftAxisStates = new NormalizedJoystickEvent[0xF];

    // Set up Input Handler and value storage for Right Joystick
    static RawInputHandler rightStick = new RawInputHandler("/dev/input/js1", 1);
    static NormalizedJoystickEvent[] rightButtonStates = new NormalizedJoystickEvent[0xF];
    static NormalizedJoystickEvent[] rightAxisStates = new NormalizedJoystickEvent[0xF];

    // Create threads for each handler
    static Thread leftStickThread = new Thread(leftStick);
    static Thread rightStickThread = new Thread(rightStick);

    /**
     * The main function for the program
     * @param args
     */
    public static void main( String[] args )
    {
        System.out.println( "Swerve Joystick Controller Starting" );
        inst.startClient("10.0.1.68"); // Start NetworkTables Client for testing purpouses
        //inst.startClientTeam(3707); // Start NetworkTables Client for roboRIO
        //InputHandler.main();

        // Init arrays for value storage
        leftButtonStates = RawInputHandler.getEmptyArray(0x1);
        leftAxisStates = RawInputHandler.getEmptyArray(0x2);
        rightButtonStates = RawInputHandler.getEmptyArray(0x1);
        rightAxisStates = RawInputHandler.getEmptyArray(0x2);

        // Start Input handling threads in background. They return values asynchronously from the main function
        leftStickThread.start();
        rightStickThread.start();

        // Main loop
        try {
        while (true) {
            long startTime = System.currentTimeMillis();

            // Get button states from each Handler
            leftButtonStates = leftStick.getButtonStates();
            leftAxisStates = leftStick.getAxisStates();
            rightButtonStates = rightStick.getButtonStates();
            rightAxisStates = rightStick.getAxisStates();

            // Send Left Stick Button States to NetworkTables
            for (NormalizedJoystickEvent e : leftButtonStates) {
                String entry = new StringBuilder("0B").append(e.number).toString();
                updateTableValueBoolean(entry, (e.value == 3.051850947599719E-5 ? true : false));
            }

            // Send Left Stick Axis States to NetworkTables
            for (NormalizedJoystickEvent e : leftAxisStates) {
                String entry = new StringBuilder("0A").append(e.number).toString();
                updateTableValueDouble(entry, e.value);
            }

            // Send Right Stick Button States to NetworkTables
            for (NormalizedJoystickEvent e : rightButtonStates) {
                String entry = new StringBuilder("1B").append(e.number).toString();
                updateTableValueBoolean(entry, (e.value == 3.051850947599719E-5 ? true : false));
            }

            // Send Right Stick Axis States to NetworkTables
            for (NormalizedJoystickEvent e : rightAxisStates) {
                String entry = new StringBuilder("1A").append(e.number).toString();
                updateTableValueDouble(entry, e.value);
            }

            long endTime = System.currentTimeMillis();

            System.out.println(new StringBuilder("Main loop length: ").append(endTime - startTime).append("ms"));
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateTableValueDouble(String entry, double value) {
        table.getEntry(entry).forceSetDouble(value);
    }

    public static void updateTableValueBoolean(String entry, Boolean value) {
        table.getEntry(entry).forceSetBoolean(value);
    }

    public Boolean tableConnect() {
        return inst.isConnected();
    }

    public void boot() {
        bootEntry.setDouble(0);
    }

    public Boolean rioHandshake(){
        Boolean handshakeComplete = false;
        bootEntry.setDouble(0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (bootEntry.getDouble(0) != 0){
            double state = bootEntry.getDouble(1);
            state = state++;
            bootEntry.setDouble(state);
        } else {
            return false;
        }
        return handshakeComplete;
    }

    public boolean handshakeRecived() {
        Boolean returned = false;
        return returned;
    }
}
