#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

// This function partitions array A into two subarrays A_1 and A_2
// Input:
//     *l is the index of the first element in array A
//     *r is the index of the last element in array A
//
//     A
//     [  |  |  |  |  ...  |  |  |  |  ]
//      *l                           *r
//
// Output:
//     *l is now the index of the first element of array A_2, which still needs to be sorted
//     *r is now the index of the last  element of array A_1, which still needs to be sorted
//
//     A_1              A_2
//     [  |  | ... |  ] [  |  | ... |  ]
//                  *r   *l


void partition ( int* A, int* l, int* r )
{
  int x = A[*r];
  int i = *l - 1;
  for(int j = *l; j <= *r-1; j++){
    if(A[j] <= x){
      i++;
      int temp = A[i];
       A[i] = A[j];
       A[j] = temp;
    }
  }
  int temp = A[i+1];
  A[i+1] = A[*r];
  A[*r] = temp;
  *r = i + 1;
  *l = i + 1;
}

// Input:
//     l is the index of the first element in array A
//     r is the index of the last element in array A
//
//     A
//     [  |  |  |  |  ...  |  |  |  |  ]
//      l                            r
void quicksort( int* A, int l, int r )
{
  if(l < r){
    int oldL = l;
    int oldR = r;
    partition(A, &l, &r);
    quicksort(A, oldL, r-1);
    quicksort(A, l+1, oldR);
  }
}

int main( int argc, char** argv )
{
    if( argc < 2 )
    {
        printf( "Usage: %s <array length>\n", argv[0] );
        return 1;
    }
    // Read in number of elements
    int numOfElem = atoi(argv[1]);



    srand( 14811 );

    // Allocate array
    int* arr = ( int* ) malloc ( numOfElem * sizeof ( int) );

    // Initialize array
    for(int i = 0; i < numOfElem; i++) {
      arr[i] = rand();
    }


    // Time the execution
    double start = omp_get_wtime();

    quicksort(arr, 0, numOfElem-1);

    double end = omp_get_wtime();
    double time = end - start;

    printf("Time of execution : %f\n", time);


    // Verify sorted order
    for(int i = 0; i < numOfElem - 1; i++){
      if(arr[i]>arr[i+1]){
        fprintf(stderr, "Array not sorted\n");
        exit(-1);
      }
    }
    free(arr);
    return 0;
}
