package com.beckettloose.swervecontrolthing;

/**
 * Hello world!
 *
 */
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import net.java.games.input.*;

public class NetworkTableSwerveAdapter 
{
    public static void main( String[] args )
    {
        System.out.println( "Swerve Joystick Controller Starting" );
        new NetworkTableSwerveAdapter().run();
    }

    public void run() {
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("JoystickControls");
        NetworkTableEntry lxEntry = table.getEntry("lx");
        NetworkTableEntry lyEntry = table.getEntry("ly");
        NetworkTableEntry rxEntry = table.getEntry("rx");
        NetworkTableEntry ryEntry = table.getEntry("ry");
        inst.startClientTeam(3707);
    }

    public void updateTableValueDouble(String entry, double value) {
        
    }
}
