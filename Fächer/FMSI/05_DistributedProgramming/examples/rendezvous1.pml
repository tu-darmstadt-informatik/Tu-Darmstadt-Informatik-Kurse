chan request = [0] of { byte };

active proctype Client0() {
  request ! 0
}

active proctype Client1() {
  request ! 1
}

active proctype Server() {
  byte num;
  do
    :: request ? num;
       printf("serving client %d\n", num)
  od
}
