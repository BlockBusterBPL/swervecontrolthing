package com.beckettloose.swervecontrolthing;

import org.hid4java.*;

public class HIDInputHandler {
    HIDInputHandler() {
        HidServicesSpecification hsSpec = new HidServicesSpecification();
        HidServices serv = HidManager.getHidServices(hsSpec);
        
    }
}
