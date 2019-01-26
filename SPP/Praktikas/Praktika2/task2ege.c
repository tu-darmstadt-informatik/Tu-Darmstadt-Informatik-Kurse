#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <math.h>
#include <stdint.h>
#include <limits.h>
#include <string.h>

#include "timing.h"


#define MAX_NUM_LOCAL_ELEMS   3

#define SIZE_OF_ARRAY(x) (sizeof(x) / sizeof((x)[0]))
MPI_Comm rowComm; //neue Communicators
MPI_Comm colComm;


int k;
int total_elem_num;


/**
 * Checks whether arr is sorted locally
 **/
int is_arr_sorted( int* arr, int len ) {

    int i;
    for( i = 0; i < len - 1; ++i )
        if( arr[ i ] > arr[ i + 1 ] )
            return 0;
    return 1;
}


/**
 * Checks whether arr is sorted globally.
 **/
int verify_results( int* arr, int len, int myrank, int nprocs ) {

  int is_sorted_global = 0;

  // TODO
  int sortedpov = 0;
  int sortedrow = 0;
  int p = nprocs;
  int d = sqrt(p);
  MPI_Status recieved_status;
  int recieved_value = 0;
  int *const one = malloc(sizeof(int));
  *one = 1;
  int VotRight = 0;
  if( myrank < (len -1))
    VotRight = myrank;
  else
    VotRight = len-1;


if(myrank == 0)
MPI_Sendrecv(arr, 1, MPI_INT,MPI_PROC_NULL, 0, &recieved_value, 1, MPI_INT, myrank == nprocs - 1 ? MPI_PROC_NULL : myrank + 1, 0, MPI_COMM_WORLD, &recieved_status);
else
MPI_Sendrecv(arr, 1, MPI_INT, myrank - 1, 0, &recieved_value, 1, MPI_INT, myrank == nprocs - 1 ? MPI_PROC_NULL : myrank + 1, 0, MPI_COMM_WORLD, &recieved_status);

if(VotRight)
sortedpov= recieved_value > arr[0];
else
sortedpov= 1;


MPI_Allreduce (&sortedpov, &sortedrow, 1, MPI_INT, MPI_MIN, rowComm);

  int leadingRankInCol = -1;
  MPI_Allreduce(&myrank, &leadingRankInCol, 1, MPI_INT, MPI_MIN, colComm);

  if (leadingRankInCol == 0)
      MPI_Allreduce( &sortedrow, &is_sorted_global, 1, MPI_INT, MPI_MIN,  colComm);

  return is_sorted_global;
  /**int is_sorted_global = 1;

  int* isBad = calloc(nprocs, sizeof(int));




  if( len != total_elem_num) {

    isBad[myrank] = 1;

  }


  MPI_Barrier(MPI_COMM_WORLD);

  for(int i = 0; i < nprocs && myrank == 0; i++) {

    if(isBad[myrank] == 1)
      is_sorted_global = 0;


  }


  MPI_Barrier(MPI_COMM_WORLD);

  for(int i = 0; i < len - 1 && is_sorted_global == 1; i++) {

    if(arr[i] >= arr[i + 1]) {

      isBad[myrank] = 1;
      break;
    }

  }

  MPI_Barrier(MPI_COMM_WORLD);

  for(int i = 0; i < nprocs; i++) {

    if(isBad[myrank] == 1)
      is_sorted_global = 0;

  }


  // int isEqual[len];
  //
  // MPI_Allreduce(arr, isEqual, len, MPI_INT, MPI_SUM, MPI_COMM_WORLD);

  MPI_Barrier(MPI_COMM_WORLD);

  for(int i = 0; i < len; i++) {

    if(isBad[myrank] == 1)
      is_sorted_global = 0;
  }

  free(isBad);

  return is_sorted_global;
**/
}

/**
 * This function compares two integers.
 */
int comp_func( const void* a, const void* b ) {
    return ( *(int*)a - *(int*)b );
}


/**
 * Returns unique random integer.
 */
int get_unique_rand_elem( unsigned int total_count_bits, unsigned int index, double scale ) {

    int random_elem = (int)( scale * drand48() );
    int unique_random_element = ( random_elem << total_count_bits ) | index;

    return unique_random_element;
}


/**
 * Merges arr_from into arr.
 */
void merge_arr( int* arr1, int len1, int* arr2, int len2, int* arr_out, int* len_out ) {

    int idx1 = 0, idx2 = 0, idx3 = 0;
    while( idx1 < len1 ) {
        while( idx2 < len2 &&
               arr2[ idx2 ] < arr1[ idx1 ] ) {
            arr_out[ idx3++ ] = arr2[ idx2++ ];
        }
        arr_out[ idx3++ ] = arr1[ idx1++ ];
    }
    while( idx2 < len2 ) {
        arr_out[ idx3++ ] = arr2[ idx2++ ];
    }
    *len_out = idx3;
}


/**
 * All-gather-merge using hypercube approach.
 */
void all_gather_merge( int* arr, int len, int** out_arr, int* out_len,
                       int nprocs, MPI_Comm comm ) {
    // TODO
    int w_myrank;
    MPI_Comm_rank(comm, &w_myrank);

    int j = k;


    int* array = arr;
    int len_main = len;
    printf("TEST \n");


    for(int i = 0; i < k; i++) {

      int partner = w_myrank ^ (int) pow(2, i);

      //printf("ITERATION : %d\n", i);

      j++;

      //MPI_Status* stat_arr = malloc(sizeof(MPI_Status) * len * 2);

      int toSend[1];
      toSend[0] = len_main;

      int toReceive[1];

      MPI_Status stat;


    //  int sndCnt;
      int rcvCnt;

      //MPI_Barrier(MPI_COMM_WORLD);


      MPI_Sendrecv(toSend, 1, MPI_INT, partner, w_myrank, toReceive, 1, MPI_INT, partner, partner, comm, &stat);

  //    MPI_Barrier(MPI_COMM_WORLD);

    //  if (i == 1) printf("%d hi %d\n", toReceive[0], toSend[0]);


      rcvCnt = toReceive[0];
        //sndCnt = len;

      int recvBuff[rcvCnt];



    //  MPI_Barrier(MPI_COMM_WORLD);

      MPI_Sendrecv(array, len_main, MPI_INT, partner, w_myrank, recvBuff, rcvCnt, MPI_INT, partner, partner, comm, &stat);

      //MPI_Barrier(MPI_COMM_WORLD);

      int out_length = len_main + rcvCnt;


      int temp_arr[out_length];


      MPI_Barrier(MPI_COMM_WORLD);

      merge_arr(array, len_main, recvBuff, rcvCnt, temp_arr, &len_main);


      MPI_Barrier(MPI_COMM_WORLD);

      array = temp_arr;

      if(w_myrank == 5) {

      printf("DIES IST LAUF %d\n", i);

      for(int i = 0; i < len; i++) {
        printf("Knoten: %d\n", array[i]);
      }

      printf("\n");
}

    }

    *out_len = len_main;
    *out_arr = array;
}


/**
 * Initilizes the input. Each process will have a random local array of numbers. The
 * length of this array is anywhere between 0 to MAX_NUM_LOCAL_ELEMS
 */
void init_input( int w_myrank, int w_nprocs, int* input_arr,
                 int* input_len, int* total_elems ) {

    // Initialize random seed
    srand48( w_nprocs );

    // Total number of elements is 65% of the number of processes
    *total_elems = (int)( w_nprocs * 0.65 );
    int* global_arr = NULL;
    int* sendcnts = NULL;
    int* displs = NULL;

    if( w_myrank == 0 ) {
        printf( "Total number of input elements: %d\n", *total_elems );

        global_arr = malloc( *total_elems * sizeof(int) );

        double scale = *total_elems * 5;
        int total_count_bits = (int)ceil( log( *total_elems ) / log( 2.0 ) );

        // Init global array with random elements
        for( int i = 0; i < *total_elems; ++i )
            global_arr[i] = get_unique_rand_elem( total_count_bits, i, scale );

        // Randomly decide how much elements each rank will get
        sendcnts = malloc( w_nprocs * sizeof(int) );
        memset( sendcnts, 0, w_nprocs * sizeof(int) );
        int total_elem_cnt = *total_elems;
        for( int i = 0; i < w_nprocs; ++i ) {
            double coin_flip = drand48();
            if( coin_flip < 0.45 ) {
                sendcnts[i]++;
                total_elem_cnt--;
                if( total_elem_cnt == 0 ) break;
                coin_flip = drand48();
                if( coin_flip < 0.35 ) {
                    sendcnts[i]++;
                    total_elem_cnt--;
                    if( total_elem_cnt == 0 ) break;
                    if( coin_flip < 0.15 ) {
                        sendcnts[i]++;
                        total_elem_cnt--;
                        if( total_elem_cnt == 0 ) break;
                    }
                }
            }
        }

        // Redistribute remaining counts
        int curr_rank = 0;
        while( total_elem_cnt > 0 ) {
            while( sendcnts[curr_rank] >= MAX_NUM_LOCAL_ELEMS )
                ++curr_rank;
            sendcnts[curr_rank]++;
            total_elem_cnt--;
        }

        displs = malloc( w_nprocs * sizeof(int) );
        displs[0] = 0;
        for( int i = 1; i < w_nprocs; ++i )
            displs[i] = displs[i - 1] + sendcnts[i - 1];
    }

    // Redistribute the input length
    MPI_Scatter( sendcnts, 1, MPI_INT, input_len, 1, MPI_INT, 0, MPI_COMM_WORLD );

    // Redistribute the input
    MPI_Scatterv( global_arr, sendcnts, displs, MPI_INT, input_arr, *input_len, MPI_INT,
                  0, MPI_COMM_WORLD );

    free( global_arr );
    free( sendcnts );
    free( displs );
}

int main( int argc, char** argv ) {



  	int w_myrank, w_nprocs;
      MPI_Init( &argc, &argv );
      MPI_Comm_rank( MPI_COMM_WORLD, &w_myrank );
      MPI_Comm_size( MPI_COMM_WORLD, &w_nprocs );

      init_clock_time();

      //
      // Initialization phase
      //
      int r = (int) w_nprocs;
      int d = (int) sqrt(r);
      //Einmmal mod und einmal /, damit nicht die gleichen Farben-Tags herauskommen
      MPI_Comm_split(MPI_COMM_WORLD, w_myrank % d, w_myrank, &colComm);
      MPI_Comm_split(MPI_COMM_WORLD, w_myrank / d, w_myrank, &rowComm);


      int n = 0;
      int total_n;
      int elem_arr[MAX_NUM_LOCAL_ELEMS];

      init_input( w_myrank, w_nprocs, elem_arr, &n, &total_n );

      double start = get_clock_time() ;
      //
      // TODO
      //
      int *elemCntCol = (int *) malloc(sizeof(int) * d);
      int *elemCntRow = (int *) malloc(sizeof(int) * d);
      //MPI_Allgather(  &n, 1, MPI_INT, elemCntCol, 1, MPI_INT, colComm);



      int *sizeOfComm = malloc(sizeof(int));
      MPI_Comm_size(rowComm, sizeOfComm);

      //MPI_Allgather( &n, 1, MPI_INT, elemCntRow, 1, MPI_INT, rowComm);
      //MPI_Barrier(MPI_COMM_WORLD);
      int numElemRow = 0;
      int numElemCol = 0;

      int *rowOffsets = (int *) calloc(d, sizeof(int));
      int *colOffsets = (int *) calloc(d, sizeof(int));

      int sum = 0;
      int adder_Row = 0;
        int adder_Col = 0;


        for(int i = 0; i < d; i++) {
          numElemRow += elemCntRow[i];
          numElemCol += elemCntCol[i];

          rowOffsets[i] = adder_Row;
          colOffsets[i] = adder_Col;

          adder_Row += elemCntRow[i];
          adder_Col += elemCntCol[i];
        }
  //    sumBeforePos(elemCntRow, d, rowOffsets);
    //  sumBeforePos(elemCntCol, d, colOffsets);

      int* recvBufRow = malloc(sizeof(int) * numElemRow);
      int* recvBufCol = malloc(sizeof(int) * numElemCol);


      //MPI_Allgatherv( elem_arr, n, MPI_INT, recvBufRow, elemCntRow, rowOffsets, MPI_INT, rowComm);

      //MPI_Allgatherv(elem_arr, n, MPI_INT, recvBufCol, elemCntCol, colOffsets, MPI_INT, colComm);
      //MPI_Barrier(MPI_COMM_WORLD);
      //1.c
      //qsort(recvBufCol, numElemCol, sizeof(int), comp_func);
      //qsort(recvBufRow, numElemRow, sizeof(int), comp_func);


      int *IndexCol = (int *) malloc(numElemCol * sizeof(int));
      //Indexe von Col sortieren
      for(int i = 0; i < numElemCol; i++) {
    IndexCol[i] = 0;
    for(int j = numElemRow - 1; j >= 0; j--) {
      if(recvBufCol[i] > recvBufRow[j]) {
        IndexCol[i] = j + 1;
        break;
      }
      else if(recvBufCol[i] == recvBufRow[j]) {
        IndexCol[i] = j;
        break;
      }
    }
  }


      // Nochmal Index sort
      qsort(IndexCol, numElemCol, sizeof(int), comp_func);

      int *globalIndexCol = malloc(sizeof(int) * numElemCol);


      MPI_Allreduce( IndexCol, globalIndexCol,  numElemCol, MPI_INT, MPI_SUM, colComm);
      MPI_Barrier(MPI_COMM_WORLD);

      //
      // Redistribute data
      // Adjust this code to your needs
      //

      int nhelp = 0;
      if(w_myrank < total_n)
        nhelp = n + 1;
      else
        nhelp= n;


      MPI_Request* req_arr = malloc(sizeof(MPI_Request) * nhelp);
      MPI_Status* stat_arr = malloc(sizeof(MPI_Status) * nhelp);

      int *globalIndexLocElements = calloc(total_n,sizeof(int));

      for (int i = 0; i < n; i++) {
          for (int j = 0; j < numElemCol; j++) {
              if (elem_arr[i] == recvBufCol[j]) {
                  globalIndexLocElements[i] = globalIndexCol[j];
              }
          }
      }


      for (int i = 0; i < n; i++) {
        MPI_Isend( &(elem_arr[i]), 1, MPI_INT, globalIndexLocElements[i], 0, MPI_COMM_WORLD, &(req_arr[i]));

  }
      MPI_Barrier(MPI_COMM_WORLD);

      int *myElement = malloc(sizeof(int));
      if (w_myrank < total_n) {
          MPI_Irecv(myElement, 1, MPI_INT, MPI_ANY_SOURCE, 0, MPI_COMM_WORLD, &(req_arr[nhelp - 1]));
      }
      MPI_Barrier(MPI_COMM_WORLD);

      // Receive element
      // TODO
      if(w_myrank < total_n){
        elem_arr[0] = *myElement;
      }
      else{
        elem_arr[0] = -1;
      }


      MPI_Waitall( nhelp, req_arr, stat_arr );




      //
      // Measure the execution time after all the steps are finished,
      // but before verifying the results
      //
      double elapsed = get_clock_time() - start;

      //
      // Verify the data is sorted globally
      //
      int res = verify_results( elem_arr, n, w_myrank, w_nprocs );
      if( w_myrank == 0 ) {
          if( res ) {
              printf( "Results correct!\n" );
          }
          else {
              printf( "Results incorrect!\n" );
          }
      }

      // Get timing - max across all ranks
      double elapsed_global;
      MPI_Reduce( &elapsed, &elapsed_global, 1, MPI_DOUBLE,
                  MPI_MAX, 0, MPI_COMM_WORLD );

      if( w_myrank == 0 ) {


	int w_myrank, w_nprocs;
    MPI_Init( &argc, &argv );
    MPI_Comm_rank( MPI_COMM_WORLD, &w_myrank );
    MPI_Comm_size( MPI_COMM_WORLD, &w_nprocs );

    init_clock_time();

    //
    // Initialization phase
    //
    int r = (int) w_nprocs;
    int d = (int) sqrt(r);
    //Einmmal mod und einmal /, damit nicht die gleichen Farben-Tags herauskommen
    MPI_Comm_split(MPI_COMM_WORLD, w_myrank % d, w_myrank, &colComm);
    MPI_Comm_split(MPI_COMM_WORLD, w_myrank / d, w_myrank, &rowComm);


    int n = 0;
    int total_n;
    int elem_arr[MAX_NUM_LOCAL_ELEMS];

    init_input( w_myrank, w_nprocs, elem_arr, &n, &total_n );

    double start = get_clock_time() ;
    //
    // TODO
    //
    int *elemCntCol = (int *) malloc(sizeof(int) * d);
    int *elemCntRow = (int *) malloc(sizeof(int) * d);
    //MPI_Allgather(  &n, 1, MPI_INT, elemCntCol, 1, MPI_INT, colComm);



    int *sizeOfComm = malloc(sizeof(int));
    MPI_Comm_size(rowComm, sizeOfComm);

    //MPI_Allgather( &n, 1, MPI_INT, elemCntRow, 1, MPI_INT, rowComm);
    MPI_Barrier(MPI_COMM_WORLD);
    int numElemRow = 0;
    int numElemCol = 0;

    int *rowOffsets = (int *) calloc(d, sizeof(int));
    int *colOffsets = (int *) calloc(d, sizeof(int));

    int sum = 0;
    int adder_Row = 0;
      int adder_Col = 0;


      for(int i = 0; i < d; i++) {
        numElemRow += elemCntRow[i];
        numElemCol += elemCntCol[i];

        rowOffsets[i] = adder_Row;
        colOffsets[i] = adder_Col;

        adder_Row += elemCntRow[i];
        adder_Col += elemCntCol[i];
      }
//    sumBeforePos(elemCntRow, d, rowOffsets);
  //  sumBeforePos(elemCntCol, d, colOffsets);

    int* recvBufRow = malloc(sizeof(int) * numElemRow);
    int* recvBufCol = malloc(sizeof(int) * numElemCol);


  //  MPI_Allgatherv( elem_arr, n, MPI_INT, recvBufRow, elemCntRow, rowOffsets, MPI_INT, rowComm);

    //MPI_Allgatherv(elem_arr, n, MPI_INT, recvBufCol, elemCntCol, colOffsets, MPI_INT, colComm);
    qsort(&elem_arr, n, sizeof(int), comp_func);

    int out_len;
    int* out_arr[total_n];
    printf("HELLO\n");
    all_gather_merge( elem_arr, n, out_arr, elem_arr, d, rowComm );

    all_gather_merge( elem_arr, n, out_arr, elem_arr, d, colComm );


    MPI_Barrier(MPI_COMM_WORLD);
    //1.c
    qsort(recvBufCol, numElemCol, sizeof(int), comp_func);
    qsort(recvBufRow, numElemRow, sizeof(int), comp_func);


    int *IndexCol = (int *) malloc(numElemCol * sizeof(int));
    //Indexe von Col sortieren
    for(int i = 0; i < numElemCol; i++) {
  IndexCol[i] = 0;
  for(int j = numElemRow - 1; j >= 0; j--) {
    if(recvBufCol[i] > recvBufRow[j]) {
      IndexCol[i] = j + 1;
      break;
    }
    else if(recvBufCol[i] == recvBufRow[j]) {
      IndexCol[i] = j;
      break;
    }
  }
}


    // Nochmal Index sort
    qsort(IndexCol, numElemCol, sizeof(int), comp_func);

    int *globalIndexCol = malloc(sizeof(int) * numElemCol);


    MPI_Allreduce( IndexCol, globalIndexCol,  numElemCol, MPI_INT, MPI_SUM, colComm);
    MPI_Barrier(MPI_COMM_WORLD);

    //
    // Redistribute data
    // Adjust this code to your needs
    //

    int nhelp = 0;
    if(w_myrank < total_n)
      nhelp = n + 1;
    else
      nhelp= n;


    MPI_Request* req_arr = malloc(sizeof(MPI_Request) * nhelp);
    MPI_Status* stat_arr = malloc(sizeof(MPI_Status) * nhelp);

    int *globalIndexLocElements = calloc(total_n,sizeof(int));

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < numElemCol; j++) {
            if (elem_arr[i] == recvBufCol[j]) {
                globalIndexLocElements[i] = globalIndexCol[j];
            }
        }
    }


    for (int i = 0; i < n; i++) {
      MPI_Isend( &(elem_arr[i]), 1, MPI_INT, globalIndexLocElements[i], 0, MPI_COMM_WORLD, &(req_arr[i]));

}
    MPI_Barrier(MPI_COMM_WORLD);

    int *myElement = malloc(sizeof(int));
    if (w_myrank < total_n) {
        MPI_Irecv(myElement, 1, MPI_INT, MPI_ANY_SOURCE, 0, MPI_COMM_WORLD, &(req_arr[nhelp - 1]));
    }
    MPI_Barrier(MPI_COMM_WORLD);

    // Receive element
    // TODO
    if(w_myrank < total_n){
      elem_arr[0] = *myElement;
    }
    else{
      elem_arr[0] = -1;
    }


    MPI_Waitall( nhelp, req_arr, stat_arr );




    //
    // Measure the execution time after all the steps are finished,
    // but before verifying the results
    //
    double elapsed = get_clock_time() - start;

    //
    // Verify the data is sorted globally
    //
    int res = verify_results( elem_arr, n, w_myrank, w_nprocs );
    if( w_myrank == 0 ) {
        if( res ) {
            printf( "Results correct!\n" );
        }
        else {
            printf( "Results incorrect!\n" );
        }
    }

    // Get timing - max across all ranks
    double elapsed_global;
    MPI_Reduce( &elapsed, &elapsed_global, 1, MPI_DOUBLE,
                MPI_MAX, 0, MPI_COMM_WORLD );

    if( w_myrank == 0 ) {
        printf( "Elapsed time (ms): %f\n", elapsed_global );
    }

    MPI_Finalize();

    return 0;
}


  /**  printf("START\n");
    int w_myrank, w_nprocs;
    MPI_Init( &argc, &argv );
    MPI_Comm_rank( MPI_COMM_WORLD, &w_myrank );
    MPI_Comm_size( MPI_COMM_WORLD, &w_nprocs );

    //
    // Initialization phase
    //
    int n = 0;
    int total_n;
    int elem_arr[MAX_NUM_LOCAL_ELEMS];

    k = (int) log2(w_nprocs);
    int d = (int) sqrt(w_nprocs);

    init_input( w_myrank, w_nprocs, elem_arr, &n, &total_n );


    //
    // TODO
    //



    //printf("%ld %d\n",SIZE_OF_ARRAY(elem_arr), n);

    //MPI_Barrier(MPI_COMM_WORLD);

    // printf("%d\n", n);

    //MPI_Barrier(MPI_COMM_WORLD);
    // if(w_myrank == 0) printf("\n\n\n\n\n\n\n\n\n\n\n\n");
    qsort(&elem_arr, n, sizeof(int), comp_func);

    int out_len;
    int* out_arr[total_n];
    printf("HELLO\n");
    all_gather_merge( elem_arr, n, out_arr, elem_arr, d, rowComm );

    all_gather_merge( elem_arr, n, out_arr, elem_arr, d, colComm );

  //  printf("%d\n", out_len);


    if(w_myrank == 0)
    total_elem_num = total_n;


    //printf("ÖLSKDGJÖSLKDGJÖSLKDJGÖLSKDJGÖLSKDGJÖSKDJGÖSDLKGJSDÖLKJSDÖLKGJ\n");
    //
    // Verify the data is sorted globally
    //
    int res = verify_results( elem_arr, n, w_myrank, w_nprocs );
    if( w_myrank == 0 ) {
        if( res ) {
            printf( "Results correct!\n" );
        }
        else {
            printf( "Results incorrect!\n" );
        }
    }

    MPI_Finalize();

    return 0;   **/
}