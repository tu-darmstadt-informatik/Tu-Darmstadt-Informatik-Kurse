chan request = [0] of { byte };

active proctype Client0() {
  request ! 0;
  printf("Here")
}


