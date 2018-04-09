//INCLUDES
//ADXL345
#include <SparkFun_ADXL345.h> 
//Ethernet
#include <SPI.h>
#include <Ethernet.h>

//CONFIG
//Ethernet
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xEF }; //Mac address
IPAddress server(74,125,232,128); //Ip address for server CHANGE LATER
IPAddress ip(192, 168, 0, 101); //This device
EthernetClient client;
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

  //ADXL startup
  adxl.powerOn(); //Power on accelerometer
  adxl.setRangeSetting(16); //Sets sensitivty to 16G
  //Ethernet startup
  if (Ethernet.begin(mac) == 0) {
    Ethernet.begin(mac, ip);
  }
  delay(1000);
  if (client.connect(server, 80)) {
    Serial.println("connected");
  } else {
    Serial.println("connection failed");
  }
}
String storage = "";
void loop() {
  int x,y,z;   // axis variables
  adxl.readAccel(&x, &y, &z); //Reads the accelerometer values and stores them in the axis variables
  //if any axis exceeds the hitvalue (both positive and negative) set boolean to true
  if(x > hitValue || x < -hitValue || y > hitValue || y < -hitValue || z > hitValue || z < -hitValue) {
    hit_detected = true;
    Serial.print("H");
  }
  //if an hit has been detected save values for 1000 cycles then set the boolean and counter to false resp. 0
  if(hit_detected) {
    if(counter < 1000) {
      counter++;
      /*Serial.print(x);
      Serial.print(", ");
      Serial.print(y);
      Serial.print(", ");
      Serial.println(z);*/
      storage += String(x);
      storage += ",";
      storage += String(y);
      storage += ",";
      storage += String(z);
      storage += ";";
    } else {
      counter = 0;
      hit_detected = false;
      client.print(storage);
      //Serial.println(storage);
      storage = "";
    }
  }

}
