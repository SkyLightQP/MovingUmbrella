#include <Servo.h> 
#include <SoftwareSerial.h>

#define VIB_SIZE 8
#define SERVO_SIZE 2

#define MAX_MOTOR 60
#define MIN_MOTOR 0
#define DEFAULT_MOTOR 26

int vib_pins[] = { 2, 3, 4, 5, 6, 7, 8, 9 };
int servo_pins[] = { 10, 11 };
int bt_tx = 12;
int bt_rx = 13;
Servo servos[SERVO_SIZE];
SoftwareSerial bt_serial(bt_tx, bt_rx);

void setup() {
  init_sensors();

  Serial.begin(9600);
  bt_serial.begin(9600);
  
  move_servo(0, 180);
  move_servo(1, DEFAULT_MOTOR);
}

void loop() {
  Serial.println();
  long a = (get_vib(0) + get_vib(1)) / 2;
  long b = (get_vib(2) + get_vib(3)) / 2;
  long c = (get_vib(4) + get_vib(5)) / 2;
  long d = (get_vib(6) + get_vib(7)) / 2;
  delay(30);
  Serial.println(a);
  Serial.println(b);
  Serial.println(c);
  Serial.println(d);

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
