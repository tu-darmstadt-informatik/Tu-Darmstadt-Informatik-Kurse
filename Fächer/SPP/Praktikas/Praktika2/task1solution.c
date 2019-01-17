#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <math.h>
#include <stdint.h>
#include <limits.h>
#include <string.h>
#include <unistd.h>

#include "timing.h"


#define MAX_NUM_LOCAL_ELEMS   3

#define DEBUG 2
#define debug_print(args ...) if (DEBUG) { fprintf(stderr, args);fflush(stdout);}

#define KNRM  "\x1B[0m"
#define KRED  "\x1B[31m"
#define KGRN  "\x1B[32m"
#define KYEL  "\x1B[33m"
#define KBLU  "\x1B[34m"
#define KMAG  "\x1B[35m"
#define KCYN  "\x1B[36m"
#define KWHT  "\x1B[37m"

MPI_Comm rowComm;
MPI_Comm colComm;


int mod(int a, int b) {
    int r = a % b;
    return r < 0 ? r + b : r;
}

/**
* Checks whether arr is sorted locally
**/
int is_arr_sorted(int *arr, int len) {

    int i;
    for (i = 0; i < len - 1; ++i)
        if (arr[i] > arr[i + 1])
            return 0;
    return 1;
}

/*
* returns the Max Number in an array given the length
**/

void sumBeforePos(int *array, int length, int *outputArray) {
    int sum = 0;
    for (int i = 0; i < length; i++) {
        outputArray[i] = sum;
        sum += array[i];
    }
}

/*
* returns the sum of Numbers in an array given the length
**/

int sumArray(int *array, int length) {
    int sum = 0;
    for (int i = 0; i < length; i++)
        sum += array[i];

    return sum;
}

/**
* Checks whether arr is sorted globally.
* len is the total_n
**/
int verify_results(int *arr, int len, int myrank, int nprocs) {
    debug_print("Phase 8 -> verify result\n");
    int is_sorted_from_POV = 0;
    int is_sorted_row_vise = 0;
    int is_sorted_global = 0;
    int d = sqrt(nprocs);
    MPI_Status recieved_status;
    int recieved_value = 0;
    int *const one = malloc(sizeof(int));
    *one = 1;
    int doIHaveVotRight = myrank < (len -1); //-1 because last Element doesn't count

    debug_print("Phase 8 -> validating in chain\n");
    MPI_Sendrecv(arr,
                 1,
                 MPI_INT,
                 myrank == 0 ? MPI_PROC_NULL : myrank - 1,
                 0,
                 &recieved_value,
                 1,
                 MPI_INT,  //because in c % is the remainder and not modulo
                 myrank == nprocs - 1 ? MPI_PROC_NULL : myrank + 1,
                 0,
                 MPI_COMM_WORLD,
                 &recieved_status);

    debug_print("[%d] %s, comparing %d with %d\n", myrank, doIHaveVotRight ? "with voting right" : "with no voting right",
                arr != NULL ? *arr : -1, recieved_value);

    is_sorted_from_POV = doIHaveVotRight? recieved_value > arr[0] : 1; //when len > 0
    //when from every Position the left and the right one are sorted,
    //the whole thing is sorted
    debug_print("Phase 8 -> res: %d\n", is_sorted_from_POV);
    debug_print("Phase 8 -> validating in row\n");
    MPI_Allreduce
            (&is_sorted_from_POV,
             &is_sorted_row_vise,
             1,
             MPI_INT,
             MPI_MIN,
             rowComm);
    debug_print("Phase 8 -> res: %d\n", is_sorted_row_vise);

    debug_print("Phase 8 -> determaning leading in Col\n");
    int leadingRankInCol = -1;
    MPI_Allreduce
            (&myrank,
             &leadingRankInCol,
             1,
             MPI_INT,
             MPI_MIN,
             colComm);

    if (leadingRankInCol == 0) {
        debug_print("Phase 8 -> determaning leading in col\n");
        MPI_Allreduce(
                &is_sorted_row_vise,
                &is_sorted_global,
                1,
                MPI_INT,
                MPI_MIN,
                colComm);
        debug_print("exeting with res %d\n", is_sorted_global);
    }

    debug_print("exeting\n");

    return is_sorted_global;
}


/**
* This function compares two integers.
*/
int comp_func(const void *a, const void *b) {
    return (*(int *) a - *(int *) b);
}


/**
* Returns unique random integer.
*/
int get_unique_rand_elem(unsigned int total_count_bits, unsigned int index, double scale) {
    int random_elem = (int) (scale * drand48());
    int unique_random_element = (random_elem << total_count_bits) | index;

    return unique_random_element;
}


/**
* Initilizes the input. Each process will have a random local array of numbers. The
* length of this array is anywhere between 0 to MAX_NUM_LOCAL_ELEMS
*/
void init_input(int w_myrank, int w_nprocs, int *input_arr,
                int *input_len, int *total_elems) {

    // Initialize random seed
    srand48(w_nprocs);

    // Total number of elements is 65% of the number of processes
    *total_elems = (int) (w_nprocs * 0.65);
    int *global_arr = NULL;
    int *sendcnts = NULL;
    int *displs = NULL;

    if (w_myrank == 0) {
        printf("Total number of input elements: %d\n", *total_elems);

        global_arr = malloc(*total_elems * sizeof(int));

        double scale = *total_elems * 5;
        int total_count_bits = (int) ceil(log(*total_elems) / log(2.0));

        // Init global array with random elements
        for (int i = 0; i < *total_elems; ++i){
            global_arr[i] = get_unique_rand_elem(total_count_bits, i, scale);
            debug_print("%d, ", global_arr[i]);
          }
          debug_print("\n");

        // Randomly decide how much elements each rank will get
        sendcnts = malloc(w_nprocs * sizeof(int));
        memset(sendcnts, 0, w_nprocs * sizeof(int));
        int total_elem_cnt = *total_elems;
        for (int i = 0; i < w_nprocs; ++i) {
            double coin_flip = drand48();
            if (coin_flip < 0.45) {
                sendcnts[i]++;
                total_elem_cnt--;
                if (total_elem_cnt == 0) break;
                coin_flip = drand48();
                if (coin_flip < 0.35) {
                    sendcnts[i]++;
                    total_elem_cnt--;
                    if (total_elem_cnt == 0) break;
                    if (coin_flip < 0.15) {
                        sendcnts[i]++;
                        total_elem_cnt--;
                        if (total_elem_cnt == 0) break;
                    }
                }
            }
        }

        // Redistribute remaining counts
        int curr_rank = 0;
        while (total_elem_cnt > 0) {
            while (sendcnts[curr_rank] >= MAX_NUM_LOCAL_ELEMS)
                ++curr_rank;
            sendcnts[curr_rank]++;
            total_elem_cnt--;
        }

        displs = malloc(w_nprocs * sizeof(int));
        displs[0] = 0;
        for (int i = 1; i < w_nprocs; ++i)
            displs[i] = displs[i - 1] + sendcnts[i - 1];
    }

    // Redistribute the input length
    MPI_Scatter(sendcnts, 1, MPI_INT, input_len, 1, MPI_INT, 0, MPI_COMM_WORLD);

    // Redistribute the input
    MPI_Scatterv(global_arr, sendcnts, displs, MPI_INT, input_arr, *input_len,
                 MPI_INT, 0, MPI_COMM_WORLD);

    free(global_arr);
    free(sendcnts);
    free(displs);
}

static void wait_for_debugger(int myrank)
{
  if(DEBUG == 2 && myrank == 0) {
    volatile int i=0;
    fprintf (stderr , "pid %ld waiting for debugger \n", (long) getpid ());
    while(i==0) { /* change ’i’ in the debugger */ }
      sleep(1);
    }
    MPI_Barrier ( MPI_COMM_WORLD );
}

int main(int argc, char **argv) {
    //debug_print("starting\n");
    int w_myrank, w_nprocs;
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &w_myrank);
    MPI_Comm_size(MPI_COMM_WORLD, &w_nprocs);
    init_clock_time();

    //
    // Initialization phase
    //

    int d = (int) sqrt(w_nprocs); //d is defined on the Worksheet
    wait_for_debugger(w_myrank);

    //debug_print("Phase 1 -> Comm split\n");
    MPI_Comm_split(MPI_COMM_WORLD, w_myrank % d, w_myrank, &colComm); //
    MPI_Comm_split(MPI_COMM_WORLD, w_myrank/d, w_myrank, &rowComm);
    MPI_Barrier(MPI_COMM_WORLD);
    debug_print("[%d]/%d = %d, %d mod %d = %d\n", w_myrank, d, w_myrank/d, w_myrank,d,w_myrank%d);

    MPI_Barrier(MPI_COMM_WORLD);

    int n = 0;
    int total_n;
    int elem_arr[MAX_NUM_LOCAL_ELEMS];
    init_input(w_myrank, w_nprocs, elem_arr, &n, &total_n);
    double start = get_clock_time();

    //
    // TODO
    //
    debug_print("Phase 2 -> gatherElementCountInCol\n");

    for(int i = 0; i < n; i++) debug_print("\t[%d] owns %d \n", w_myrank, elem_arr[i]);
    int *elemCountAmongProcessorCol = (int *) malloc(sizeof(int) * d);
    int *elemCountAmongProcessorRow = (int *) malloc(sizeof(int) * d);
    MPI_Allgather(
            &n,
            1,
            MPI_INT,
            elemCountAmongProcessorCol,
            1,
            MPI_INT,
            colComm
    );

    debug_print("Phase 2 -> gatherElementCountInRow\n");
  MPI_Barrier(MPI_COMM_WORLD);
  int * sizeOfComm = malloc(sizeof(int));
  MPI_Comm_size(rowComm, sizeOfComm);
    MPI_Allgather(
            &n,
            1,
            MPI_INT,
            elemCountAmongProcessorRow,
            1,
            MPI_INT,
            rowComm
    );
    MPI_Barrier(MPI_COMM_WORLD);
  //  debug_print("Phase 2 -> gatherElementCountSumUp\n");
    int numOfElementsInRow = sumArray(elemCountAmongProcessorRow, d);
    int numOfElementsInCol = sumArray(elemCountAmongProcessorCol, d);

    for(int i =0; i < d; i++){
      debug_print("[%d] #EleAlongprocessorRow: %d\n", w_myrank, elemCountAmongProcessorRow[i]);
    }

    debug_print("[%d] elements by commSize of %d; in row: %d\n", w_myrank, *sizeOfComm, numOfElementsInRow);
    debug_print("[%d] elements in col: %d\n", w_myrank, numOfElementsInCol);

    //debug_print("Phase 2 -> calculatingOffsets\n");
    int *rowOffsets = (int *) calloc(d, sizeof(int));
    int *colOffsets = (int *) calloc(d, sizeof(int));
    //When A Processor doesn't deliver any Element it needs to be Skipped in
    //Array insertion. This is used to determante, how much cells in the array
    //must be skipped

    sumBeforePos(elemCountAmongProcessorRow, d, rowOffsets);
    sumBeforePos(elemCountAmongProcessorCol, d, colOffsets);

    int* recvBufRow = malloc(sizeof(int) * numOfElementsInRow);
    int* recvBufCol = malloc(sizeof(int) * numOfElementsInCol);


    //debug_print("Phase 3 -> Gather rowElements\n");

    if(n > MAX_NUM_LOCAL_ELEMS) debug_print("%sPhase 3 -> n (%d) > MAX_NUM_LOCAL_ELEMS!!!", n, KRED);

    debug_print("Allgather Row: elem_arr[0..%d -1] = [", n);

    for(int i = 0; i < n; i++){
      debug_print("%d, ", elem_arr[i]);
    }
    MPI_Allgatherv(
            elem_arr,
            n,
            MPI_INT,
            recvBufRow,
            elemCountAmongProcessorRow,
            rowOffsets,
            MPI_INT,
            rowComm
    );

    //debug_print("]\n waited for %d answers: \n", d);
    //for(int i = 0; i < d;i++){
    //  debug_print("[elements: %d, offset: %d, recieved: {", elemCountAmongProcessorRow[i], rowOffsets[i]);
    //  for(int j = rowOffsets[i]; j < elemCountAmongProcessorRow[i] + rowOffsets[i]; j++){
    //    debug_print("%d, ", recvBufRow[j]);
    //  }
    //  debug_print("}]\n")
    //}

    debug_print("Phase 3 -> Gather colElements\n");
    MPI_Allgatherv(
            elem_arr,
            n,
            MPI_INT,
            recvBufCol,
            elemCountAmongProcessorCol,
            colOffsets,
            MPI_INT,
            colComm
    );
    debug_print("Phase 4 -> QSort\n");
    qsort(recvBufRow, numOfElementsInRow, sizeof(int), comp_func);
    qsort(recvBufCol, numOfElementsInCol, sizeof(int), comp_func);

    debug_print("col = [");
    for(int i = 0; i < numOfElementsInCol;i++){
      debug_print("%d,", recvBufCol[i]);
    }
    debug_print("], num of Elements = %d\n", numOfElementsInCol);
    debug_print("row = [");
    for(int i = 0; i < numOfElementsInRow;i++){
      debug_print("%d,", recvBufRow[i]);
    }
    debug_print("], num of Elements = %d\n", numOfElementsInRow);

    //debug_print("Phase 5 -> det. loc ranks\n");
    int *localRanksCol = (int *) calloc(numOfElementsInCol, sizeof(int));

    for (int i = 0; i < numOfElementsInCol; i++) {
      localRanksCol[i] = 0;
        for (int j = 1; j < numOfElementsInRow + 1; j++) { // +1 bc more spaces inbetween than elements!
            if(recvBufCol[i] <= recvBufRow[j-1]){
              break;
            }
            localRanksCol[i] = j;
        }
        debug_print("%d has local rank %d \n", recvBufCol[i], localRanksCol[i]);
    }

    qsort(localRanksCol, numOfElementsInCol, sizeof(int), comp_func);

    debug_print("Phase 6 -> det. global ranks\n");
    MPI_Barrier(MPI_COMM_WORLD);
    int *globalRanksCol = malloc(sizeof(int) * numOfElementsInCol);

    MPI_Allreduce(
            localRanksCol,
            globalRanksCol,
            numOfElementsInCol,
            MPI_INT,
            MPI_SUM,
            colComm);
    for(int i = 0; i < numOfElementsInCol; i++){
      debug_print("[%d] global rank of %d is %d \n",w_myrank, recvBufCol[i], globalRanksCol[i]);
    }

    MPI_Barrier(MPI_COMM_WORLD);
    //
    // Redistribute data
    // Adjust this code to your needs
    //
    MPI_Request req_arr[MAX_NUM_LOCAL_ELEMS];
    MPI_Status stat_arr[MAX_NUM_LOCAL_ELEMS];

    int n_req = n + (w_myrank < total_n);
    // +1 for recieving a value


    //MPI_Isend( &(elem_arr[i]), 1, MPI_INT, arr_global_ranks[j], 0,
    //             MPI_COMM_WORLD, req_arr + n_req );
    debug_print("Phase 7 -> det global rank of loc # %d Elements ...\n", n);
    int *globalRanksOfLocElements = malloc(n * sizeof(int));

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < numOfElementsInCol; j++) {
            if (elem_arr[i] == recvBufCol[j]) {
                globalRanksOfLocElements[i] = globalRanksCol[j];
                debug_print("[%d] %d has global rank %d\n",w_myrank, elem_arr[i], globalRanksOfLocElements[i]);
            }
        }
    }

    //debug_print("Phase 7 -> send Elements to processors\n");
    for (int i = 0; i < n; i++) {
        debug_print("[%d] sending %d to %d\n", w_myrank, elem_arr[i], globalRanksOfLocElements[i]);
        MPI_Isend(
                &(elem_arr[i]),
                1,
                MPI_INT,
                globalRanksOfLocElements[i],
                0,
                MPI_COMM_WORLD,
                &(req_arr[i])
        );
    }
    //debug_print("Phase 8 -> generating recv\n");
    int *myElement = malloc(sizeof(int));
    if (w_myrank < total_n) {
        MPI_Irecv(myElement, 1, MPI_INT, MPI_ANY_SOURCE,
                  0, MPI_COMM_WORLD, &(req_arr[n_req - 1]));
    }
    debug_print("Sending... \n");

    MPI_Waitall(n_req, req_arr, stat_arr);
    // Receive element
    // TODO
    MPI_Barrier(MPI_COMM_WORLD);
    debug_print("finished Sending... \n");

    //
    // Measure the execution time after all the steps are finished,
    // but before verifying the results
    //
    double elapsed = get_clock_time() - start;

    //
    // Verify the data is sorted globally
    //
    int *minOne = malloc(sizeof(int));
    *minOne = -1;

    elem_arr[0] = (w_myrank < total_n)? *myElement: *minOne;

    if(DEBUG) MPI_Barrier(MPI_COMM_WORLD);
    debug_print("[%d] finally has %d \n", w_myrank, *myElement);

    int res = verify_results(elem_arr, total_n, w_myrank, w_nprocs);
    if(DEBUG) MPI_Barrier(MPI_COMM_WORLD);
    if (w_myrank == 0) {
        if (res) {
            printf("Results correct!\n");
        } else {
            printf("Results incorrect!\n");
        }
    }

    // Get timing - max across all ranks
    double elapsed_global;
    MPI_Reduce(&elapsed, &elapsed_global, 1, MPI_DOUBLE,
               MPI_MAX, 0, MPI_COMM_WORLD);

    if (w_myrank == 0) {
        printf("Elapsed time (ms): %f\n", elapsed_global);
    }

    MPI_Finalize();

    return 0;
}

//PS: Felix stinkt mehr
