#define N 10
active proctype P() {
  byte sum = 0, i = 0;
  do 
    :: i > N -> goto exitloop;
    :: else  -> sum = sum + i;
		i++
  od;
exitloop:
  printf("The sum is %d\n", sum)
}
