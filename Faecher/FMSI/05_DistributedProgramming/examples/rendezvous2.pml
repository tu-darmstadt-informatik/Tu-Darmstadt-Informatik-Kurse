chan request = [0] of { byte };
chan reply = [0] of { bool };

active proctype Server() {
  byte num;
end:
  do :: request ? num -> 
	printf("serving client %d\n", num);
	reply ! true
  od
}

active proctype Client0() {
  request ! 0;
  reply ? _
}

active proctype Client1() {
  request ! 1;
  reply ? _
}
