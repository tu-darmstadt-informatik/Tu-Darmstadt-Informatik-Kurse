mtype = {login, logout} 

inline selectEvent(event) {
  if
   :: event = login
   :: event = logout
   fi   
}

// counts failed authentication tries
byte fail = 0;

// current state
byte currentState = 0;

active proctype Authentication() {

// received event
mtype ev;

do
 :: currentState == 0 ->
           printf("Init");
		   selectEvent(ev);
           if
            :: fail >= 3 -> currentState = 2
            :: fail < 3  && ev == login  ->  fail = 0; currentState = 1;  
            :: fail < 3  && ev == login  ->  fail = fail + 1; currentState = 0 // as we stay in the same state 
                                                                               // the last assignment is not necessary
                                                                               // only to keep the code a bit more 
                                                                               // understandable
           fi
 :: currentState == 1 ->  assert(fail = 0);
           printf("Authenticated"); 
           selectEvent(ev); 
           if
            :: ev == logout -> currentState = 0
            fi 
 :: currentState == 2 ->assert(fail == 3); printf("Locked"); selectEvent(ev)        
od
}

active prototype invariant(){
	assert((currentstate == 2 && fail >= 3) || (currentstate != 2 && fail >= 3))
}
