//INCLUDES
//ADXL345
#include <SparkFun_ADXL345.h>
//Ethernet
#include <SPI.h>
#include <Ethernet.h>

//USERCONFIG
//Ethernet
IPAddress server(192,168,1,20);    //Ip address for server
int port = 12345;                     //Port for server
IPAddress ip(192, 168, 1, 101);       //This device
//Sensitivity
int hitValue = 200;
boolean finished = false;


//CONFIG
//LEDS
int red = 5;
int yellow = 4;
int green = 3;
//Ethernet
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED }; //Mac address
EthernetClient client;
//ADXL I2C
ADXL345 adxl = ADXL345();
//Boolean for hit detect and enable
boolean hit_detected = false;
//start time for fastpunch game mode
unsigned long startTime = 0;
//amount of hits in fastpunch
int hitCount = 0;
int hit_detected_counter = 0;
//countdown for fastpunch
int countdown;

//Counter for the cycles
int counter = 0;
//String for storage of all values
String storage = "";
//Calibration variables
int calibrationCounter = 0;
int calibrationXStorage[6] = {0, 0, 0, 0, 0, 0};
int calibrationYStorage[6] = {0, 0, 0, 0, 0, 0};
int calibrationZStorage[6] = {0, 0, 0, 0, 0, 0};
int calibrationXOffset = 0;
int calibrationYOffset = 0;
int calibrationZOffset = 0;

//Method for connecting to server
void connect() {
  setLed(2);
  Serial.println("connecting...");
  int status = 100;
  if (status = client.connect(server, port)) {
    Serial.println("connected");
  } else {
    Serial.println("connection failed");
    setLed(3);
    delay(2000);
    connect();
  }
  Serial.println("Status: ");
  Serial.println(status);
}
//set leds to a preset
void setLed(int led) {
  switch (led) {
    case 1:
      //Serial.println("LED Green");
      digitalWrite(green, HIGH);
      digitalWrite(yellow, LOW);
      digitalWrite(red, LOW);
      break;
    case 2:
      //Serial.println("LED Yellow");
      digitalWrite(green, LOW);
      digitalWrite(yellow, HIGH);
      digitalWrite(red, LOW);
      break;
    case 3:
      //Serial.println("LED Red");
      digitalWrite(green, LOW);
      digitalWrite(yellow, LOW);
      digitalWrite(red, HIGH);
      break;
    case 4:
      //Serial.println("LED All");
      digitalWrite(green, HIGH);
      digitalWrite(yellow, HIGH);
      digitalWrite(red, HIGH);
      break;
    case 5:
      //Serial.println("LED NONE");
      digitalWrite(green, LOW);
      digitalWrite(yellow, LOW);
      digitalWrite(red, LOW);
      break;
  }
}
//flash all LEDs
void highScoreBlink() {
  for (int i = 0; i < 10; i++) {
    digitalWrite(green, HIGH);
    digitalWrite(yellow, HIGH);
    digitalWrite(red, HIGH);
    delay(250);
    digitalWrite(green, LOW);
    digitalWrite(yellow, LOW);
    digitalWrite(red, LOW);
    delay(250);
  }
}

void setup() {
  //Serial begin
  Serial.begin(9600);
  Serial.println();
  //LED setup
  pinMode(green, OUTPUT);
  pinMode(yellow, OUTPUT);
  pinMode(red, OUTPUT);
  setLed(4);
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


  int next_state = 0;
  int state = 0;
  int x;
  int y;
  int z;  
//main programloop
void loop(){
  Serial.println("Entered loop");
  calibrate();
  while(client.connected()){
    adxl.readAccel(&x,&y,&z);
    if(client.available() && !finished){
      byte recived = client.read();
      Serial.println("Client sent message");
      switch(recived){  
        
        //1 enable 2 disable 3 new hs 4 Fast 5 Hard
        
        //hardpunch
        case 5:
          Serial.println("Entering Hardpunch");
          next_state = 1;
          break;  
        //fastpunch
        case 4:
          Serial.println("Entering Fastpunch");
          next_state = 2;
          break;  
        //calibrate
        case 2: case 1:
          next_state = 0;
          break;
        //highscore
        case 3:
          highScoreBlink();
          next_state = 0;
          setLed(2);
          break;
      }
    }
    switch(state){
      case 1:
      
        doHardPunch();
        if(finished){
          next_state = 0;
          finished = false;
        }
        break;
      case 2:
        
        doFastPunch();
        if(finished){
          next_state = 0;
          finished = false;
        }
        break;
      case 0:
        calibrate();
        break;
    }
    state = next_state;
  }
  connect();
}

//calubrate adxl values to dX, dY, dZ
void calibrate(){
    
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
}
//Hardpunch
void doHardPunch(){
  calibrate();
  //Green led
  setLed(1);
  
  hitValue = 200;
  if (x > hitValue || x < -hitValue || y > hitValue || y < -hitValue || z > hitValue || z < -hitValue) {//If dX, dY, dZ have pased our threshold
    //Yellow led
    setLed(2);
    hit_detected = true;
    Serial.println("h:");
    Serial.println(x);
    Serial.println(y);
    Serial.println(z);
  }
  if (hit_detected) {
    //Store
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
      hitCount = 0;
      startTime = 0;
      //Write to buffer
      byte ret[storage.length()];
      storage.getBytes(ret, storage.length());
      client.write(ret, 900);
      //flush buffer
      client.flush();
      Serial.println("Data sent");
      //client.write(buff, 2000);
      Serial.println(storage);
      Serial.println(sizeof(ret));
      setLed(2);
      finished = true;
      storage = "";
      next_state = 0;
    }
  }
}

int hitRegX, hitRegY, hitRegZ;
//Fastpunch
void doFastPunch(){
  adxl.readAccel(&x,&y,&z);
  hitValue = 200;
  if(startTime == 0){//Did we just start?
    startTime = millis(); //Reset starttime
  }
  unsigned long currentTime = millis();

  
  if(currentTime - startTime <= 16000){//Have 16000ms passed?
    if(currentTime - startTime <= 3000){//Have 3000ms passed
      //Yellow led
      setLed(2);
    }else {
      //Green led
      setLed(1);
      if ((x > hitValue || x < -hitValue || y > hitValue || y < -hitValue || z > hitValue || z < -hitValue) && !hit_detected ){//If dX, dY, dZ have pased our threshold
        hit_detected = true;
        hitRegX = x;
        hitRegY = y;
        hitRegZ = z;
        
      }
      if(hit_detected){
        if(hit_detected_counter < 20){
          hit_detected_counter++;
        }else{
        if(x * hitRegX < 0 || y * hitRegY < 0 || z * hitRegZ < 0){ //Have dX, dY, dZ changed direction
          hit_detected_counter = 0;
          hit_detected = false;
          currentTime = currentTime - 100;
          hitCount++;
          Serial.println(hitCount);
          }
        }
      }
    }
  }else{
    storage = "";
    Serial.println(sizeof(hitCount));
    client.print(hitCount);
    Serial.println(hitCount);
    //Send hitcount
    client.flush();
    finished = true;
    hitCount = 0;
    
    startTime = 0;
    next_state = 0;
    setLed(2);
  }
}
