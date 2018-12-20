chan ch = [0] of { byte, byte };

active proctype Sender() {
  printf("ready\n");
  ch ! 11, 45;
  printf("Sent\n")
}
 
active proctype Receiver() {
  byte  hour, minute;

  printf("steady\n");
  ch ? hour, minute;
  printf("Received\n")
}
