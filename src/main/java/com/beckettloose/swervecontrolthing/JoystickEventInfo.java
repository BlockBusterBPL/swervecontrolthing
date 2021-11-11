package com.beckettloose.swervecontrolthing;

public class JoystickEventInfo {
    Boolean isNormalized = false;
    RawJoystickEvent re;
    NormalizedJoystickEvent ne;


    JoystickEventInfo(RawJoystickEvent re){
        this.isNormalized = false;
    }

    JoystickEventInfo(NormalizedJoystickEvent ne){
        this.isNormalized = true;
    }

    public Boolean isAxis() {
        if (this.isNormalized) {
            return (this.ne.type == 2 ? true : false);
        } else {
            return (this.re.type == 0x2 ? true : false);
        }
    }
}
