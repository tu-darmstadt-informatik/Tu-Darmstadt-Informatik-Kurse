// events
mtype = {login, logout} 

// select arbitrarily an event (login, logout) and 
// assigns it to the variable given as parameter
inline selectEvent(event) {
   if
		:: event = login;
		:: event = logout;
	fi
}

active proctype Authentication() {
  // counts failed authentication tries

 byte fail = 0;

  // current state
  byte currentState = 0;

  // received event
  mtype ev;

  // TODO: IMPLEMENT
do
::currentState = 0 ->
	selectEvent(ev)
	if
		:: fail < 3 && ev =login && currentState = 0 -> currentState = 1 && fail = 0;
		:: fail= fail +1 && currentState = 0;
		:: ev = logout && currentState = 1-> currentState = 0;
		::fail >=3 && event = login && currentState = 0 -> currentState = 2;
		::currentState = 2 -> printf("Passwort zu oft falsch eingegeben")
	

}
