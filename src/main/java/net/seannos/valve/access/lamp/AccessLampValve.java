package net.seannos.valve.access.lamp;

import org.apache.catalina.valves.AccessLogValve;

public class AccessLampValve extends AccessLogValve{
  private boolean onPi;
  private String pinNumber;

  public void setOnPi(boolean onpi){
		  this.onPi = onpi;
  }

  public boolean getOnPi(){
		  return onPi;
  }

  public void setPinNumber(String no){
		  this.pinNumber = no;
  }

  public String getPinNumber(){
		  return pinNumber;
  }
}
