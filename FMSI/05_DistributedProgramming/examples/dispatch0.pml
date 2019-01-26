mtype = {hi, bye};
chan ch = [0] of {mtype};
  
active proctype Server () {
   mtype msg;
read:
  ch ? msg;
  do
    ::  msg == hi -> printf("Hello.\n"); goto read
    ::  msg == bye -> printf("See you.\n"); break
  od
}

active proctype Client() {
  ch ! hi; ch ! bye
}

