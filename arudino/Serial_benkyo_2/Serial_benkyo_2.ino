#include <Servo.h>
int data;
int x,y,z;
int xk[200],yk[200],zk[200];
int xa,ya,za;
int i;
float byo;
int flag;

Servo servo;

void setup() {
  servo.attach(10);
  Serial.begin(115200);
  for(i=0;i<50;i++){
    xk[i] = 0;
    yk[i] = 0;
    zk[i] = 0;
  }
  byo = 5;
  data = '6';
  flag = 0;
}

void loop() {
  if(Serial.available() > 0){
    data = Serial.read();
    if(data == '6'){
      flag = 0;
    }
    if(data == '5'){
      flag = 1;
    }
    if(data == '4'){
      byo = 200;
    }
    if(data == '3'){
      byo = 150;
    }
    if(data == '2'){
      byo = 100;
    }
    if(data == '1'){
      byo = 50;
    }
    
  }
  
  if(flag == 1){
    zk[0] = analogRead(1);
    xk[0] = analogRead(2);
    yk[0] = analogRead(3);
    delay(100);
    
    for(i=byo-1;i>0;i--){
      xk[i] = xk[i-1];
      yk[i] = yk[i-1];
      zk[i] = zk[i-1];
    }
  
    xa = 0;
    ya = 0;
    za = 0;
    for(i=0;i<byo;i++){
      xa += xk[i];
      ya += yk[i];
      za += zk[i];
    }
    xa /= byo;
    ya /= byo;
    za /= byo;
    
    for(i=0;i<byo;i++){
      if(xa - xk[i] < 3 && xk[i] - xa < 3){
        x++;
      }else{
        x= 0;
      }
      if(ya - yk[i] < 3 && yk[i] - ya < 3){
        y++;
      }else{
        y= 0;
      }
      if(za - zk[i] < 3 && zk[i] - za < 3){
        z++;
      }else{
        z= 0;
      }
    }
    if(x>byo || y>byo || z>byo){
      servo.write(125);
      delay(300);
      servo.write(80);
    }else{
      servo.write(80);
    }
    Serial.print('1');
  }else if(flag == 0){
    Serial.print('0');
  }
  
}
