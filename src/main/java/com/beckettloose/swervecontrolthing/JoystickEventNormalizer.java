package com.beckettloose.swervecontrolthing;

public class JoystickEventNormalizer {
    private RawJoystickEvent in;
    private NormalizedJoystickEvent out = new NormalizedJoystickEvent();

    JoystickEventNormalizer(RawJoystickEvent event) {
        this.in = event;
    }

    public NormalizedJoystickEvent getNormalizedEvent() {
        out.setType(in.type);
        out.setNumber(in.number);
        out.setValue(in.value/32767);
        //out.setValue(in.value);
        return out;
    }
}
