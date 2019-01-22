#define N 10
active proctype P() {
  int i;
  int sum = 0;
  for (i : 1 .. N) {
    sum = sum + i
  };
  printf("The sum is %d\n", sum)
}
