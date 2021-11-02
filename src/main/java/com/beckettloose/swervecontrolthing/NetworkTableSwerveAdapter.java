package com.beckettloose.swervecontrolthing;

/**
 * Hello world!
 *
 */
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import com.beckettloose.swervecontrolthing.InputHandler;

public class NetworkTableSwerveAdapter 
{
    static NetworkTableInstance inst = NetworkTableInstance.getDefault();
    static NetworkTable table = inst.getTable("JoystickControls");
    NetworkTableEntry lxEntry = table.getEntry("0x");
    NetworkTableEntry lyEntry = table.getEntry("0y");
    NetworkTableEntry rxEntry = table.getEntry("0rx");
    NetworkTableEntry ryEntry = table.getEntry("0ry");
    public static void main( String[] args )
    {
        System.out.println( "Swerve Joystick Controller Starting" );
        new NetworkTableSwerveAdapter().run();
    }

    public void run() {
        
        inst.startClient("10.0.1.68");
        //inst.startClientTeam(3707);
        InputHandler.main();
    }

    public static void updateTableValueDouble(String entry, double value) {
        table.getEntry(entry).forceSetDouble(value);
    }
}
