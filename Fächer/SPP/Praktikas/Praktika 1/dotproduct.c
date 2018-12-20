# include <stdlib.h>
# include <stdio.h>
# include <math.h>
# include <omp.h>

int main ( int argc, char *argv[] );
double test01 ( int n, double x[], double y[] );
double test02 ( int n, double x[], double y[] );

/******************************************************************************/

int main ( int argc, char *argv[] )
{
  double factor;
  int i;
  int n;
  double wtime;
  double *x;
  double xdoty;
  double *y;

  n = 100;

  while ( n < 1000000 )
  {
    n = n * 10;

    x = ( double * ) malloc ( n * sizeof ( double ) );
    y = ( double * ) malloc ( n * sizeof ( double ) );

    factor = ( double ) ( n );
    factor = 1.0 / sqrt ( 2.0 * factor * factor + 3 * factor + 1.0 );

    for ( i = 0; i < n; i++ )
    {
      x[i] = ( i + 1 ) * factor;
    }

    for ( i = 0; i < n; i++ )
    {
    y[i] = ( i + 1 ) * 6 * factor;
    }

    printf ( "\n" );
/*
  Test #1
*/
    //...YOU NEED TO FILL HERE ...
    xdoty = test01(n,x,y);
    wtime = omp_get_wtime();
    test01(n , x ,  y);
    wtime = omp_get_wtime() - wtime;
    printf ( "  Threadanzahl = 1 Sequential  %8d  %14.6e  %15.10f\n", n, xdoty, wtime );


/*
  Test #2
*/
    //...YOU NEED TO FILL HERE ...
    xdoty = test02(n,x,y);
    omp_set_num_threads(1);
    wtime = omp_get_wtime();
    test02(n , x ,  y);
    wtime = omp_get_wtime() - wtime;
    printf ( "  Threadanzahl = 1 Parallel  %8d  %14.6e  %15.10f\n", n, xdoty, wtime );
    omp_set_num_threads(2);
    wtime = omp_get_wtime();
    test02(n , x ,  y);
    wtime = omp_get_wtime() - wtime;
    printf ( "  Threadanzahl = 2 Parallel  %8d  %14.6e  %15.10f\n", n, xdoty, wtime );
    omp_set_num_threads(4);
    wtime = omp_get_wtime();
    test02(n , x ,  y);
    wtime = omp_get_wtime() - wtime;
    printf ( "  Threadanzahl = 4 Parallel  %8d  %14.6e  %15.10f\n", n, xdoty, wtime );
    omp_set_num_threads(8);
    wtime = omp_get_wtime();
    test02(n , x ,  y);
    wtime = omp_get_wtime() - wtime;
    printf ( "  Threadanzahl = 8 Parallel  %8d  %14.6e  %15.10f\n", n, xdoty, wtime );
    omp_set_num_threads(16);
    wtime = omp_get_wtime();
    test02(n , x ,  y);
    wtime = omp_get_wtime() - wtime;
    printf ( "  Threadanzahl = 16 Parallel  %8d  %14.6e  %15.10f\n", n, xdoty, wtime );

    free ( x );
    free ( y );
  }
/*
  Terminate.
*/
  printf ( "\n" );
  printf ( "DOT_PRODUCT\n" );
  printf ( "  Normal end of execution.\n" );

  return 0;
}

//Sequential version
double test01 ( int n, double x[], double y[] )

{
  int i;
  double xdoty;

  xdoty = 0.0;

  //...YOU NEED TO FILL HERE...

  for(i = 0; i < n; i++){
     xdoty = xdoty + x[i] * y[i];
   }
  return xdoty;
}

//Parallel version
double test02 ( int n, double x[], double y[] )

{
  int i;
  double xdoty;

  xdoty = 0.0;

  //...YOU NEED TO FILL HERE...
  #pragma omp parallel for reduction(+:xdoty)
    for(i = 0; i < n; i++){
       xdoty = xdoty + x[i] * y[i];
     }

  return xdoty;
}
double Threadtesting01 (int n, double xy[], double yx[], int numThread){
  double time;
  omp_set_num_threads(numThread);
  time = omp_get_wtime();
  test01(n , xy ,  yx);
  return time = omp_get_wtime() - time;
}

double Threadtesting02(int n, double xy[], double yx[], int numThread){
  double time;
  omp_set_num_threads(numThread);
  time = omp_get_wtime();
  test02(n , xy ,  yx);
  return time = omp_get_wtime() - time;
}
