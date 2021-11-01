#include <Servo.h> 
#include <SoftwareSerial.h>

#define VIB_SIZE 8
#define SERVO_SIZE 2

#define MAX_MOTOR 40
#define MIN_MOTOR 25
#define DEFAULT_MOTOR 30

int vib_pins[] = { 2, 3, 4, 5, 6, 7, 8, 9 };
int servo_pins[] = { 10, 11 };
int bt_tx = 13;
int bt_rx = 12;
char now_direction;

Servo servos[SERVO_SIZE];
SoftwareSerial bt_serial(bt_tx, bt_rx);

void setup() {
  init_sensors();

  Serial.begin(9600);
  bt_serial.begin(9600);
  move_servo(0, 180);
  move_servo(1, DEFAULT_MOTOR);

  now_direction = 'a';
}

void loop() {
  Serial.println();
  long a = (get_vib(0) + get_vib(1));
  long b = (get_vib(2) + get_vib(3));
  long c = (get_vib(4) + get_vib(5));
  long d = (get_vib(6) + get_vib(7));

  long front = a + b;
  long back = c + d;
  
  Serial.println(a);
  Serial.println(b);
  Serial.println(c);
  Serial.println(d);

  if (front == 0 && back == 0) {
    move_servo(0, 180);
    move_servo(1, DEFAULT_MOTOR);
  }

  if (front == back) {
    move_servo(0, 180);
    move_servo(1, DEFAULT_MOTOR);
  }

  if (front > back) {
    move_servo(0, 180);
    move_servo(1, MAX_MOTOR);
  }

  if (front < back) {
    move_servo(0, 180);
    move_servo(1, MIN_MOTOR);
  }

//  if ( a+b+c+d == 0 ) {
//    move_servo(0, 180);
//    move_servo(1, DEFAULT_MOTOR);
//    now_direction = 'a';
//  } else if(now_direction == 'a') a_change_direction(a, b, c, d);
//    else if(now_direction == 'b') b_change_direction(a, b, c, d);
  delay(3000);
}

void init_sensors() {
  for (int i = 0; i < VIB_SIZE; i++) {
    pinMode(vib_pins[i], INPUT);
  }

  for (int i = 0; i < SERVO_SIZE; i++) {
    servos[i].attach(servo_pins[i]);
  }
}

long get_vib(int n) {
  long result = pulseIn(vib_pins[n], HIGH);
  if (result > 0) return result;
  return 0;
}

void a_change_direction (int a, int b, int c, int d){
  if (a > b && a > c && a > d ){
    move_servo(0, 180);
    move_servo(1, MAX_MOTOR);
    now_direction = 'a';
  }else if ( b > a && b > c && b > d ){
    move_servo(0, 90);
    move_servo(1, MIN_MOTOR);
    now_direction = 'b';
  }else if ( c > a && c > b && c > d ){
    move_servo(0, 180);
    move_servo(1, MIN_MOTOR);
    now_direction = 'a';
  }else {
    //( d > a && d > b && d > c )
    move_servo(0, 90);
    move_servo(1, MAX_MOTOR);
    now_direction = 'b';
  }
}

void b_change_direction (int a, int b, int c, int d){
  if (a > b && a > c && a > d ){
    move_servo(0, 90);
    move_servo(1, MAX_MOTOR);
    now_direction = 'b';
  }else if ( b > a && b > c && b > d ){
    move_servo(0, 180);
    move_servo(1, MAX_MOTOR);
    now_direction = 'a';
  }else if ( c > a && c > b && c > d ){
    move_servo(0, 90);
    move_servo(1, MIN_MOTOR);
    now_direction = 'b';
  }else{
    //( d > a && d > b && d > c )
    move_servo(0, 180);
    move_servo(1, MIN_MOTOR);
    now_direction = 'a';
  }
}


void move_servo(int n, int angle) {
  if (n == 1) {
    if (angle <= MIN_MOTOR) {
      servos[n].write(MIN_MOTOR);
      return;
    }
    if (angle >= MAX_MOTOR) {
      servos[n].write(MAX_MOTOR);
      return;
    }
  }
  servos[n].write(angle);
}
