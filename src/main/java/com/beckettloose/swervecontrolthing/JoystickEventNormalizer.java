package com.beckettloose.swervecontrolthing;

public class JoystickEventNormalizer {
    private RawJoystickEvent in;
    private NormalizedJoystickEvent out = new NormalizedJoystickEvent();

    JoystickEventNormalizer(RawJoystickEvent event) {
        this.in = event;
    }

    public NormalizedJoystickEvent get() {
        out.setType(in.getType());
        out.setNumber(in.getNumber());
        out.setValue(((in.getValue()+32768)/(32768+32768))*(1+1)-1.0);
        return out;
    }
}
