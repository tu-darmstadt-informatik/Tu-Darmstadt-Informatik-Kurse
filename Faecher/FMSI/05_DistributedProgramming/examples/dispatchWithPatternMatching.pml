mtype = {hi, bye};
chan ch = [0] of {mtype};
  
active proctype Server () {
  int i;
  do
    :: ch ? hi  -> printf("Hello.\n")
    :: ch ? bye -> printf("See you.\n"); break
  od
}

active proctype Client() {
  ch ! hi; ch ! bye
}

