package net.seannos.valve.access.lamp;

import java.io.IOException;
import java.lang.reflect.Field;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.valves.AccessLogValve;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class AccessLampValve extends AccessLogValve {

	private String pinNumber;
	@Override
	public void start() throws LifecycleException {
		gpio = GpioFactory.getInstance();
		pin = gpio.provisionDigitalOutputPin(getPin(pinNumber), "MyLED",
				PinState.LOW);
		super.start();
	}

	GpioController gpio;
	GpioPinDigitalOutput pin;

    protected LifecycleSupport lifecycle = new LifecycleSupport(this);
    
	public AccessLampValve() {
	}

	public void setPinNumber(String no) {
		this.pinNumber = no;
	}

	public String getPinNumber() {
		return pinNumber;
	}

	@Override
	public void stop() throws LifecycleException {
		gpio.shutdown();
		super.stop();
	}

	@Override
	public void invoke(Request arg0, Response arg1) throws IOException,
			ServletException {
		pin.high();
		super.invoke(arg0, arg1);
		pin.low();
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
}
