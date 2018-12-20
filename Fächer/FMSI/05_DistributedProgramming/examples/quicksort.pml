#define ARRAY_SIZE 4
#define MAX_VALUE 5
chan stack = [255] of { byte, byte};

byte array[ARRAY_SIZE];

/*
  non-deterministic choose of a pivot element
*/
inline ChoosePivot(I,L,H) {
  I = L;
  do
    :: I < H -> I++
    :: break
  od;
  pivot = array[I]
}

/*
  swap the FST and SND entry of the array in one atomic step
  Control question: 
        * Do we need the atomic block here? 
              (try verification with and without SPIN)
        * Explain why resp. why not?
*/
inline swap(FST, SND) {
  atomic {
    array[FST] = array[FST] - array[SND];
    array[SND] = array[FST] + array[SND];
    array[FST] = array[SND] - array[FST]
  }
}

/*
   implements the actual quicksort with inplace swapping
   process blocks if channel empty, if all processes 
   are blocked the array is sorted
*/
proctype quicksort() {
  byte pivot;
  byte low;
  byte high;
  
  byte i;
  byte j; 
  endStart:
    stack ? low, high;
 
    ChoosePivot(i, low, high); 
 
    i = 0; 
    j = 0;
    do
      :: low + i < high - j && array[low  + i] < pivot -> i++
      :: low + i < high - j && array[high - j] > pivot -> j++
      :: low + i < high - j && array[low+i] > pivot 
           && array[high - j] <= pivot -> swap(low+i, high-j); j++
	  :: low + i < high - j &&
          array[low+i] >= pivot &&  array[high - j] < pivot -> swap(low+i, high-j); i++
      :: low + i == high - j -> break
      :: else -> j++
    od;
    if 
      :: high - low > 0 ->
  		  stack ! low, low + i;
  		  if
            :: low + i + 1 <= high -> stack ! low + i + 1, high
		    :: else -> skip
	      fi
      :: else -> skip
    fi;
    goto endStart;
}


/*
  fill V-th array component with non-deterministaically
 chosen value between 0 and MAX_VALUE
*/
inline Input(V) {
   i=0;
   do
	:: i<MAX_VALUE -> i++
    :: break
   od;
   array[V] = i 
}

init {
  int i;
  int j;
  do 
   :: j < ARRAY_SIZE -> Input(j); j++
   :: else -> break
  od;
  stack ! 0, ARRAY_SIZE - 1;
  atomic { 
    run quicksort();
    run quicksort();
  }
  printf("Result: ");
  i = 0;
  do
   :: timeout -> 
         do 
           :: i < ARRAY_SIZE - 1 -> 
                 printf("%d <= %d \n\n", array[i], array[i+1]); 
                 assert (array[i] <= array[i+1]); 
                 i++
           :: else -> break
         od;
         break
  od 
}