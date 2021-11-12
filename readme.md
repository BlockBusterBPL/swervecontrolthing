# swervecontrolthing

Connect Joysticks to your FRC Robot via NetworkTables.

## Instructions

 1. Clone the repository
 2. Set your robot's team number in NetworkTableSwerveAdapter.java
 3. Configure Joystick Device Instances
 4. run `mvn package` to build the Jar file
 5. run `mvn exec:exec` to start the program

## Usage

This program creates a Network Table called `JoystickControls`.
Values will only be updated when the controller you are using sends an event.
The format for how each entry is named is `(ControllerID)(EventType)(EventID)`.
For instance, the Nth axis on the first controller would be `0AN`.
Likewise, the 4th button on the third controller would be `2B4`.
