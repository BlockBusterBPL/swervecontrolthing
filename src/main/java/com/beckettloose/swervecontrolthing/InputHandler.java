package com.beckettloose.swervecontrolthing;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class InputHandler {
	public InputHandler() {
		while (true) {
			/* Get the available controllers */
			Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
			if (controllers.length == 0) {
				System.out.println("Found no controllers.");
				try {
					Thread.sleep(5000); // Keep checking for controllers every 5 seconds
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			for (int i = 0; i < controllers.length; i++) {
				/* Remember to poll each one */
				controllers[i].poll();

				/* Get the controllers event queue */
				EventQueue queue = controllers[i].getEventQueue();

				/* Create an event object for the underlying plugin to populate */
				Event event = new Event();

				/* For each object in the queue */
				while (queue.getNextEvent(event)) {
					Component comp = event.getComponent();
					if (/*comp.getIdentifier().toString().matches("x|y|rx|ry")*/true) {
						StringBuffer buffer = new StringBuffer().append(controllers[i].getPortNumber());
						buffer.append(comp.getIdentifier()).append(" set ");
						float value = event.getValue();

						StringBuffer id = new StringBuffer().append(controllers[i].getPortNumber())
								.append(comp.getIdentifier());

						/*
						 * Check the type of the component and display an appropriate value
						 */
						if (comp.isAnalog()) {
							NetworkTableSwerveAdapter.updateTableValueDouble(id.toString(), value);
							buffer.append(value);
						} else {
							if (value == 1.0f) {
								NetworkTableSwerveAdapter.updateTableValueBoolean(id.toString(), true);
								buffer.append("On");
							} else {
								NetworkTableSwerveAdapter.updateTableValueBoolean(id.toString(), false);
								buffer.append("Off");
							}
						}
						System.out.println(buffer.toString());
					}
				}
			}

			/*
			 * Sleep for 20 milliseconds, in here only so the example doesn't thrash the
			 * system.
			 */
		}
	}

	public static void main() {
		new InputHandler();
	}
}
