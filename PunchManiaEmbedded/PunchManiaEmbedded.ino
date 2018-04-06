//ADXL345
#include <SparkFun_ADXL345.h> 
//Ethernet
#include <Dhcp.h>
#include <Dns.h>
#include <Ethernet.h>
#include <EthernetClient.h>
#include <EthernetServer.h>
#include <EthernetUdp.h>


//ADXL I2C
ADXL345 adxl = ADXL345();
//Over or under this value and log 1000 cycles
int hitValue = 250;
//Over or under hitValue on any axis
boolean hit_detected = false;
//Counter for the cycles
int counter = 0;
void setup() {
  //DEBUG - REMOVE LATER
  Serial.begin(9600);
  Serial.println();

  //ADXL config
  adxl.powerOn(); //Power on accelerometer
  adxl.setRangeSetting(16); //Sets sensitivty to 16G
  
}

void loop() {
  int x,y,z;   // axis variables
  adxl.readAccel(&x, &y, &z); //Reads the accelerometer values and stores them in the axis variables
  //if any axis exceeds the hitvalue (both positive and negative) set boolean to true
  if(x > hitValue || x < -hitValue || y > hitValue || y < -hitValue || z > hitValue || z < -Value) {
    hit_detected = true;
  }
  //if an hit has been detected save values for 1000 cycles then set the boolean and counter to false resp. 0
  if(hit_detected) {
    if(counter < 1000) {
      counter++;
      Serial.print(x);
      Serial.print(", ");
      Serial.print(y);
      Serial.print(", ");
      Serial.println(z);
    } else {
      counter = 0;
      hit_detected = false;
    }
  }

}
