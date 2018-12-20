byte turn = 0;

active proctype p() {
     turn = 1; 
     turn == 2; 
     // enter critical section
     // leave critical section
}

active proctype q() {
    turn = 2;
    turn == 1;
     // enter critical section
     // leave critical section
}
