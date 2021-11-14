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
    static NetworkTableEntry bootEntry = meta.getEntry("boot");
    static double bootTime = 0;
    static NetworkTableEntry returnedEntry = meta.getEntry("return");
    static NetworkTableEntry ping = meta.getEntry("ping");

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

        while (!rioHandshake()) {
            try {
                System.out.println("Unable to complete handshake!");
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (!handshakeRecieved()) {
            try {
                System.out.println("Handshake Completed but not verified!");
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
            ping.setString("pong");

            if (bootEntry.getDouble(0) != bootTime--){
                System.out.println("Lost synchronization with rio!");
                while (!rioHandshake()) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                while (!handshakeRecieved()) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

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
                if (!(e.value > 1 || e.value < -1)) {
                updateTableValueDouble(entry, e.value);
                } else {
                    updateTableValueDouble(entry, 0);
                    System.out.println("Output Value Out of Bounds!");
                }
            }

            // Send Right Stick Button States to NetworkTables
            for (NormalizedJoystickEvent e : rightButtonStates) {
                String entry = new StringBuilder("1B").append(e.number).toString();
                updateTableValueBoolean(entry, (e.value == 3.051850947599719E-5 ? true : false));
            }

            // Send Right Stick Axis States to NetworkTables
            for (NormalizedJoystickEvent e : rightAxisStates) {
                String entry = new StringBuilder("1A").append(e.number).toString();
                if (!(e.value > 1 || e.value < -1)) {
                updateTableValueDouble(entry, e.value);
                } else {
                    updateTableValueDouble(entry, 0);
                    System.out.println("Output Value Out of Bounds!");
                }
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

    public static Boolean tableConnect() {
        return inst.isConnected();
    }

    public static Boolean rioHandshake(){
        bootEntry.setDouble(0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (bootEntry.getDouble(0) != 0){
            double state = bootEntry.getDouble(1);
            state = state++;
            bootEntry.setDouble(state);
            bootTime = state;
            System.out.println(bootTime);
            return true;
        } else {
            return false;
        }
    }

    public static boolean handshakeRecieved() {
        if (bootTime-- == bootEntry.getDouble(0)) {
            return true;
        } else {
            return false;
        }
    }
}
