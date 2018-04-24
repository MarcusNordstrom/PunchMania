//INCLUDES
//ADXL345
#include <SparkFun_ADXL345.h>
//Ethernet
#include <SPI.h>
#include <Ethernet.h>

//USERCONFIG
//Ethernet
IPAddress server(192, 168, 1, 20);    //Ip address for server
int port = 12345;                     //Port for server
IPAddress ip(192, 168, 1, 101);       //This device
//Sensitivity
int hitValue = 150;

//CONFIG
//Ethernet
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED }; //Mac address
EthernetClient client;
//ADXL I2C
ADXL345 adxl = ADXL345();
boolean hit_detected = false;
//Counter for the cycles
int counter = 0;
//String for storage of all values
String storage = "";
//Calibration variables
int calibrationCounter = 0;
int calibrationXStorage[6] = {0, 0, 0, 0, 0, 0};
int calibrationYStorage[6] = {0, 0, 0, 0, 0, 0};
int calibrationZStorage[6] = {0, 0, 0, 0, 0, 0};


//Method for connecting to server
void connect() {
  Serial.println("connecting...");
  int status = 100;
  if (status = client.connect(server, port)) {
    Serial.println("connected");
  } else {
    Serial.println("connection failed");
  }
  Serial.println("Status: ");
  Serial.println(status);
}

void setup() {
  //Serial begin
  Serial.begin(9600);
  Serial.println();
  //ADXL startup
  adxl.powerOn(); //Power on accelerometer
  adxl.setRangeSetting(16); //Sets sensitivty to 16G
  //Ethernet startup
  /*if (Ethernet.begin(mac) == 0) {
    Serial.println("Failed to configure Ethernet using DHCP");
    Ethernet.begin(mac, ip);
    }*/
  Ethernet.begin(mac, ip);  //Config with specific settings, comment line and uncomment chunk above for DHCP
  delay(1000);
  Serial.println(Ethernet.localIP());
  connect();
}
int calibrationXOffset = 0;
int calibrationYOffset = 0;
int calibrationZOffset = 0;
void loop() {
  // axis variables
  int x, y, z;
  adxl.readAccel(&x, &y, &z); //Reads the accelerometer values and stores them in the axis variables
  //if any axis exceeds the hitvalue (both positive and negative) set boolean to true
  calibrationCounter++;
  switch (calibrationCounter) {
    case 50:
      calibrationXStorage[0] = x;
      calibrationYStorage[0] = y;
      calibrationZStorage[0] = z;
      break;
    case 100:
      calibrationXStorage[1] = x;
      calibrationYStorage[1] = y;
      calibrationZStorage[1] = z;
      break;
    case 150:
      calibrationXStorage[2] = x;
      calibrationYStorage[2] = y;
      calibrationZStorage[2] = z;
      break;
    case 200:
      calibrationXStorage[3] = x;
      calibrationYStorage[3] = y;
      calibrationZStorage[3] = z;
      break;
    case 250:
      calibrationXStorage[4] = x;
      calibrationYStorage[4] = y;
      calibrationZStorage[4] = z;
      break;
    case 300:
      calibrationXStorage[5] = x;
      calibrationYStorage[5] = y;
      calibrationZStorage[5] = z;
      calibrationCounter = 0;
      break;
  }
  for (int i = 0; i < 6; i++) {
    calibrationXOffset += calibrationXStorage[i];
    calibrationYOffset += calibrationYStorage[i];
    calibrationZOffset += calibrationZStorage[i];
  }
  calibrationXOffset = calibrationXOffset / 6;
  calibrationYOffset = calibrationYOffset / 6;
  calibrationZOffset = calibrationZOffset / 6;
  x -= calibrationXOffset;
  y -= calibrationYOffset;
  z -= calibrationZOffset;
  /*Serial.print(x);        //DEBUG CODE
    Serial.print(", ");
    Serial.print(y);
    Serial.print(", ");
    Serial.println(z);*/

  if (x > hitValue || x < -hitValue || y > hitValue || y < -hitValue || z > hitValue || z < -hitValue) {
    hit_detected = true;
    Serial.println("h:");
    Serial.println(x);
    Serial.println(y);
    Serial.println(z);
  }
  //if an hit has been detected save values for 1000 cycles then set the boolean and counter to false resp. 0
  if (hit_detected) {
    if (counter < 1000) {
      counter++;
      storage += String(x);
      storage += ",";
      storage += String(y);
      storage += ",";
      storage += String(z);
      storage += ";";
    } else {
      counter = 0;
      hit_detected = false;
      if (client.connected()) {
        Serial.println(client.print(storage));
        client.flush();
        //client.write(buff, 2000);
        Serial.println(storage);
        storage = "";
      } else {
        connect();
        Serial.println(client.print(storage));
        client.flush();
        //client.write(buff, 2000);
        Serial.println(storage);
        storage = "";
      }

    }
  }       //EXPERIMENTAL UNTESTED UNIT FOR DUPLEX COMUNICATION
  if (client.available()) {
    char c = client.read();
    Serial.print("RECIVED: ");
    Serial.println(c);
  }
}

