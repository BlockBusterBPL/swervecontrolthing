# swervecontrolthing

Connect a joystick to your FRC Robot via NetworkTables.

## Instructions

 1. Clone the repository
 2. Set your robot's team number in NetworkTableSwerveAdapter.java
 3. run `mvn package` to build the Jar file
 4. run `mvn exec:exec` to start the program

## Usage

This program creates a Network Table called `JoystickControls`.
Values will only be updated when the controller you are using sends an event.
The format for how each entry is named is `(ControllerID)(EventID)`.
For instance, the X axis on the first controller would be `0x`.
Likewise, the Z Rotation axis on the second controller would be `1rz`.
