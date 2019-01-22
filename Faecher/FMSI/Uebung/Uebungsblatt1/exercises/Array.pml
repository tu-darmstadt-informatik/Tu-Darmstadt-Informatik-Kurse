/*
     template to start with
 */
#define N 5

active proctype ARRAY() {
     int i;
     int prod = 1;
     int a[N]; 

     /* fill in */
int random
    for (i in a) {
	//hier zufällige Auswahl treffen an möglivhen if fällen
	if
		::random = 1
		::random = 2
		::random = 3
		::random = 4
		::random = 5
	fi
	a[i] = random
  }


  for (i in a) {
    prod = prod * a[i]
  }
     /* Print something nice */
     printf("The product is: %d\n", prod)
}
