package net.seannos.valve.access.lamp;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;

import org.apache.catalina.valves.ValveBase;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.util.LifecycleSupport;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import javax.servlet.ServletException;

import java.lang.reflect.Field;
import java.io.IOException;

public class AccessLampValve
    extends ValveBase
    implements Lifecycle {

    protected boolean started = false;
    protected LifecycleSupport lifecycle = new LifecycleSupport(this);

	GpioController gpio;
	GpioPinDigitalOutput pin;

	private String pinNumber;


	public void start() throws LifecycleException {

        if (started)
            throw new LifecycleException("accessLampValve.alreadyStarted");

		gpio = GpioFactory.getInstance();
		pin = gpio.provisionDigitalOutputPin(getPin(pinNumber), "MyLED",
				PinState.LOW);

        lifecycle.fireLifecycleEvent(START_EVENT, null);
        started = true;
	}

	public void stop() throws LifecycleException {

        if (!started)
            throw new LifecycleException("accessLampValve.notStarted");

		gpio.shutdown();

        lifecycle.fireLifecycleEvent(STOP_EVENT, null);
        started = false;
	}

	public void invoke(Request request, Response response) throws IOException,
			ServletException {
        if (started) {                
		    pin.high();
            getNext().invoke(request, response);
		    pin.low();
        } else {
            getNext().invoke(request, response);
        }
	}

    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycle.removeLifecycleListener(listener);
    }

    public LifecycleListener[] findLifecycleListeners() {
        return lifecycle.findLifecycleListeners();
    }

    public void addLifecycleListener(LifecycleListener listener) {
        lifecycle.addLifecycleListener(listener);
    }

	private static Pin getPin(String pinNumber) {
		String raspiPinClass = RaspiPin.class.getName();
		if (pinNumber == null || pinNumber.startsWith(raspiPinClass) == false) {
			throw new RuntimeException();
		}
		String gpioFieldName = pinNumber.replace(raspiPinClass + ".", "");
		try {
			Field gpioField = RaspiPin.class.getField(gpioFieldName);
			Pin gpioPin = (Pin) gpioField.get(RaspiPin.class);
			return gpioPin;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

    // getter and setter
	public void setPinNumber(String no) {
		this.pinNumber = no;
	}

	public String getPinNumber() {
		return pinNumber;
	}
}
