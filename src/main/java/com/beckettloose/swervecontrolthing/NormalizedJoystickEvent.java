package com.beckettloose.swervecontrolthing;

/**
 * Represents a Normalized Joystick Event that is ready to be sent to networktables
 */
public class NormalizedJoystickEvent {
    double value;
    int type;
    int number;

    /**
     * The default data for the event
     */
    NormalizedJoystickEvent() {
        this.value = 0;
        this.number = 0;
        this.type = 1;
    }

    /**
     * Creates a new Normalized Joystick Event
     * @param value a double that represents the position of the axis/button from -1 to 1
     * @param type an integer representing if this event is an axis (2) or a button (1)
     */
    NormalizedJoystickEvent(double value, int type, int number) {
        this.value = value;
        this.type = type;
        this.number = number;
    }

    /**
     * 
     * @return the value of the event
     */
    public double getValue() {
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
     * @return the aixis/button number for the event
     */
    public int getNumber() {
        return number;
    }

    /**
     * Sets the value of the event
     * @param value
     */
    public void setValue(double value) {
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
     * Set the axis/button number of the event
     * @param number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * 
     * @return whether or not the event is an axis
     */
    public Boolean isAxis() {
        return (this.type == 2 ? true : false);
    }
}
