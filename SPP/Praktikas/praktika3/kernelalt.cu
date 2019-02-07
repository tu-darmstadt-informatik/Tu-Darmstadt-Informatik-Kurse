//
//  kernel.cu
//
//  Created by Arya Mazaheri on 01/12/2018.
//

#include <iostream>
#include <algorithm>
#include <cmath>
#include "ppm.h"

using namespace std;

/*********** Gray Scale Filter  *********/

/**
 * Converts a given 24bpp image into 8bpp grayscale using the GPU.
 */
__global__
void cuda_grayscale(int width, int height, BYTE *image, BYTE *image_out) //Kernel
{
    //TODO (9 pt): implement grayscale filter kernel AUFGABE 4
		  int idx = threadIdx.x + blockIdx.x * blockDim.x;
}


// 1D Gaussian kernel array values of a fixed size (make sure the number > filter size d)
//TODO: Define the cGaussian array on the constant memory (2 pt)

void cuda_updateGaussian(int r, double sd)
{
	float fGaussian[64];
	for (int i = 0; i < 2*r +1 ; i++)
	{
		float x = i - r;
		fGaussian[i] = expf(-(x*x) / (2 * sd*sd));
	}
	//TODO: Copy computed fGaussian to the cGaussian on device memory (2 pts)
	cudaMemcpyToSymbol(cGaussian, /* TODO */);
}

//TODO: implement cuda_gaussian() kernel (3 pts)


/*********** Bilateral Filter  *********/
// Parallel (GPU) Bilateral filter kernel
__global__ void cuda_bilateral_filter(BYTE* input, BYTE* output,
	int width, int height,
	int r, double sI, double sS)
{
	//TODO: implement bilateral filter kernel (9 pts)
}


void gpu_pipeline(const Image & input, Image & output, int r, double sI, double sS)
{
	// Events to calculate gpu run time
	cudaEvent_t start, stop;
	float time;
	cudaEventCreate(&start);
	cudaEventCreate(&stop);


	// GPU related variables
	BYTE *d_input = NULL;
	BYTE *d_image_out[2] = {0}; //temporary output buffers on gpu device
	int image_size = input.cols*input.rows;
	int suggested_blockSize;   // The launch configurator returned block size
	int suggested_minGridSize; // The minimum grid size needed to achieve the maximum occupancy for a full device launch

	// ******* Grayscale kernel launch *************

	//Creating the block size for grayscaling kernel
	cudaOccupancyMaxPotentialBlockSize( &suggested_minGridSize, &suggested_blockSize, cuda_grayscale);
//Jeder Thread soll einen Pixelbearbeiten
        int block_dim_x, block_dim_y;
        block_dim_x = block_dim_y = (int) sqrt(suggested_blockSize);

    //    dim3 gray_block(/* TODO */suggested_blockSize, TODOoccupancy ); // 2 pts
				dim3 gray_block(/* TODO */block_dim_x, block_dim_y ); // 2 pts
				//TODO: Calculate grid size to cover the whole image - 2 pts
				//dim3 gray_grid(input.cols / block_dim_x , input.rows / block_dim_y);
				dim3 gray_grid((input.cols+ suggested_blockSize -1) /suggested_blockSize, (input.rows+ suggested_blockSize -1) /suggested_blockSize);
				int AnzahlderThreadsproblock = gray_grid.x * gray_grid.y; //Beispiel ka welchen man nehmen soll //Variable zum besseren Verständniss
        //Funktioniert nur für quadrat zahleń int gridsize = (image_size+ suggested_blockSize -1) /suggested_blockSize;
				//anzahl der Threads im Block könnten ja prizipiell eine ganze Zeile abdecken



        // Allocate the intermediate image buffers for each step
        Image img_out(input.cols, input.rows, 1, "P5");
				BYTE *puffer[2];
				int InitByte = 0;
				size_t gesamtgroeße = i; //TODO wert reinschreiben
				int anzahlbytes; //TODO wert reinschreiben
        for (int i = 0; i < 2; i++)
        {
            //TODO: allocate memory on the device (2 pts)
						cudaMalloc((void**) &puffer[i], (sizeof(BYTE)) * image_size);
            //TODO: intialize allocated memory on device to zero (2 pts)
						cudaMemset(&puffer[i], InitByte, image_size);
        }

        //copy input image to device
        //TODO: Allocate memory on device for input image (2 pts)
				size_t gesamtgrößeBildpuffer;
				BYTE *pufferEingabebild;

				//cudaMalloc((void**) &pufferEingabebild, sizeof(BYTE) * image_size);
				cudaMalloc((void**) &pufferEingabebild, 3* image_size); // r g b
				//evtl doch das		 cudaMemset2D ( &pufferEingabebild, size_t pitch, int  value, size_t width, size_t height ) //aus den duration_cast
//TODO: Copy input image into the device memory (2 pts)
				Image img = readPPM("test_input.ppm");
				cudamemcpy(&pufferEingabebild, &input.pixels /** holt den Bytearr welcher alle werte vom img beinhaltet */, image_size * (sizeof(int)), cudaMemcpyHostToDevice);

        cudaEventRecord(start, 0); // start timer
        // Convert input image to grayscale
        //TODO: Launch cuda_grayscale() (2 pts)
					cuda_grayscale<<< gray_size, gray_block>>>( input.cols, input.rows, &input.pixels,&pufferEingabebild ); //Das sind die Parameter der grayscale methode//Folie 69

        cudaEventRecord(stop, 0); // stop timer
        cudaEventSynchronize(stop);

        // Calculate and print kernel run time
	cudaEventElapsedTime(&time, start, stop);
	cout << "GPU Grayscaling time: " << time << " (ms)\n";
	cout << "Launched blocks of size " << gray_block.x * gray_block.y << endl; //ist sowas wie ein printf befehl denke ich

        //TODO: transfer image from device to the main memory for saving onto the disk (2 pts)
				cudamemcpy(img_out, &pufferEingabebild,  image_size * (sizeof(int)), cudaMemcpyDeviceToHost);
		    savePPM(img_out, "image_gpu_gray.ppm");


	// ******* Bilateral filter kernel launch *************

	//Creating the block size for grayscaling kernel
	cudaOccupancyMaxPotentialBlockSize( &suggested_minGridSize, &suggested_blockSize, cuda_bilateral_filter); //cuda_bil.. ist kernel

        block_dim_x = block_dim_y = (int) sqrt(suggested_blockSize);

        dim3 bilateral_block(/* TODO */ suggested_blockSize, TODOoccupancy); // 2 pts steht im Internetz so

        //TODO: Calculate grid size to cover the whole image - 2pts

        // Create gaussain 1d array
        cuda_updateGaussian(r,sS);

        cudaEventRecord(start, 0); // start timer
	//TODO: Launch cuda_bilateral_filter() (2 pts)
        cudaEventRecord(stop, 0); // stop timer
        cudaEventSynchronize(stop);

        // Calculate and print kernel run time
        cudaEventElapsedTime(&time, start, stop);
        cout << "GPU Bilateral Filter time: " << time << " (ms)\n";
        cout << "Launched blocks of size " << bilateral_block.x * bilateral_block.y << endl;

        // Copy output from device to host
	//TODO: transfer image from device to the main memory for saving onto the disk (2 pts)


        // ************** Finalization, cleaning up ************

        // Free GPU variables
	//TODO: Free device allocated memory (3 pts)
}
