byte b=0; 

proctype p() {
  b++
}



init { 
atomic{
	 run p();
	  run p();
		}
_nr_pr == 1;
-> assert(b==2);
	}








































/*
proctype p() {
  byte tmp = b;
  tmp ++;
  b = tmp
}

*/
