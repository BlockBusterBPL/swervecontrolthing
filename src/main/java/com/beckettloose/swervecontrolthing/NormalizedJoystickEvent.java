package com.beckettloose.swervecontrolthing;

public class NormalizedJoystickEvent {
    double value;
    int type;
    int number;

    NormalizedJoystickEvent() {

    }

    NormalizedJoystickEvent(double value, int type, int number) {
        this.value = value;
        this.type = type;
        this.number = number;
    }

    public double getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
