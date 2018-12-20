bool q1 = false;
bool q2 = false;

active proctype p() {
  q1 = true;
  q2 == false;  
  // enter critical section
  // leave critical section  
  q1 = false;
}

active proctype q() {
  q2 = true;
  q1 == false;
  // enter critical section
  // leave critical section
  q2 = false;
}
