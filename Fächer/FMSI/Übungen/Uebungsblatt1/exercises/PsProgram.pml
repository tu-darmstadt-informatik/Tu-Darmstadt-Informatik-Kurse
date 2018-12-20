inline rollDiceTwice(result) {
   // stores sum of two dice throws
   result = 0;
   // loop counter
   times_ = 0;
   do 
     :: times_ <= 1 -> 
          if 
            :: result = result + 1; 
            :: result = result + 2;
            :: result = result + 3; 
            :: result = result + 4; 
            :: result = result + 5; 
            :: result = result + 6;  
          fi;           
          times_  =  times_ + 1
     :: times_ > 1 -> goto loopEnd
   od;
   loopEnd:
     printf("Result of two throws: %d\n", result) 
    
}


active proctype P() {
	 byte dice = 0; 
   byte times = 0;
   
   rollDiceTwice(dice);
    
   do
    :: dice == 12 -> printf("Won\n"); break;
    :: dice != 12 && times < 1 ->  
            rollDiceTwice(dice); 
            times = times + 1;
    :: else -> printf("Lost\n"); break;
  od;
}
