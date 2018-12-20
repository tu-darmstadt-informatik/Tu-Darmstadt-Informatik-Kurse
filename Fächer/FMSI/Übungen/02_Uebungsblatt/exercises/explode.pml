short i = 0;

active proctype p() {
  endP:  do 
   :: i = i + 2
   :: i = i + 4
  od
}

active proctype q() {
  endQ: do
   :: i = i - 1
   :: i = i - 2;
  od
}

active proctype notMaxShort() {
  assert i != 32767
}

