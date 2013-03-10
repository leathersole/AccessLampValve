package net.seannos.valve.access.lamp;

import org.apache.catalina.valves.AccessLogValve;

public class AccessLampValve extends AccessLogValve{
  private boolean onPi;

  public void setOnPi(boolean onpi){
		  this.onPi = onpi;
  }

  public boolean getOnPi(){
		  return onPi;
  }
}
