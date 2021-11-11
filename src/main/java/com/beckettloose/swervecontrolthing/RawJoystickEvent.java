package com.beckettloose.swervecontrolthing;


/**
 * Represents a raw joystick event as it is recieved from the device data stream
 */
public class RawJoystickEvent {
    int value;
    int type;
    int number;

    RawJoystickEvent() {
        
    }

    /**
     * Creates an instance of a raw joystick event
     * @param value the 16 bit signed integer representing the position of the axis or state of the button
     * @param type an 8 bit integer representing the type of event (0x1 for button, 0x2 for axis)
     * @param number an 8 bit integer representing the ID of the axis or button of the event
     */
    RawJoystickEvent(int value, int type, int number) {
        this.value = value;
        this.type = type;
        this.number = number;
    }

    /**
     * 
     * @return the value of the event
     */
    public int getValue() {
        return value;
    }

    /**
     * 
     * @return the type of the event
     */
    public int getType() {
        return type;
    }

    /**
     * 
     * @return the axis/button number of the event
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets the value of the event
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Sets the type of the event
     * @param type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Sets the axis/button number for the event
     * @param number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * 
     * @return Whether or not the current event is an Axis
     */
    public Boolean isAxis() {
        return (this.type == 0x2 ? true : false);
    }
}
