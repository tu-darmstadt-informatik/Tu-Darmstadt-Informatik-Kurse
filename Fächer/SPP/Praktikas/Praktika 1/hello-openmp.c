#include <omp.h>
#include <stdio.h>
  int main ( int argc, char *argv[] ) {
    int max;
    max = omp_get_max_threads();
    printf("%d\n",max);
    omp_set_num_threads(6);
  #pragma omp parallel
    {
      printf("Hello, %d\n", omp_get_thread_num());
    }

  }
