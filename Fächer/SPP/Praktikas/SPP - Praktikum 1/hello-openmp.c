#include <omp.h>
#include <stdio.h>

int main ( int argc, char *argv[] ) {
  int max;
  max = omp_get_max_threads();
  printf("%d\n",max);
  omp_set_num_threads(max - max * 0.4);
#pragma omp parallel
  {
    printf("Hello, %d\n", omp_get_thread_num());
  }

}
