chan request = [0] of { byte };
chan reply = [0] of { bool };

active proctype Server() {
  byte client;
end:
  do
    :: request ? client -> 
       if
	 :: printf("Client %d\n", client);
	    reply ! true
	 :: skip /* loosing a request */
       fi
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
