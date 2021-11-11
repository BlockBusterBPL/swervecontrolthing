package com.beckettloose.swervecontrolthing;

public class RawJoystickEvent {
    int value;
    int type;
    int number;

    RawJoystickEvent() {
        
    }

    RawJoystickEvent(int value, int type, int number) {
        this.value = value;
        this.type = type;
        this.number = number;
    }

    public int getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
