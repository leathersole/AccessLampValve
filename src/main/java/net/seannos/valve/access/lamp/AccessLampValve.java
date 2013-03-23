package net.seannos.valve.access.lamp;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.AccessLogValve;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class AccessLampValve extends AccessLogValve {

	private boolean onPi;
	private String pinNumber;
	final GpioController gpio;
	final GpioPinDigitalOutput pin;

	public void setOnPi(boolean onpi) {
		this.onPi = onpi;
	}

	public boolean getOnPi() {
		return onPi;
	}

	public void setPinNumber(String no) {
		this.pinNumber = no;
	}

	public String getPinNumber() {
		return pinNumber;
	}

	@Override
	public void start() throws LifecycleException {
		gpio = GpioFactory.getInstance();
		pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, "MyLED",
				PinState.LOW);
		super.start();
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
}
