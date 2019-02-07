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
void cuda_grayscale(int width, int height, BYTE *image, BYTE *image_out)
{
    //TODO (9 pt): implement grayscale filter kernel
		int bx = blockIdx.x;
		int by = blockIdx.y;





		for (int h = 0; h < height; h++)
		{
				int offset_out = h * width;      // 1 color per pixel
				int offset     = offset_out * 3; // 3 colors per pixel

				for (int w = 0; w < width; w++)
				{
						BYTE *pixel = &image[offset + w * 3];

						// Convert to grayscale following the "luminance" model
						image_out[offset_out + w] = pixel[0] * 0.0722f + // B
						pixel[1] * 0.7152f + // G
						pixel[2] * 0.2126f;  // R
				}
		}
}


// 1D Gaussian kernel array values of a fixed size (make sure the number > filter size d)
//TODO: Define the cGaussian array on the constant memory (2 pt)
__constant__
float cGaussian[64];

void cuda_updateGaussian(int r, double sd)
{
	float fGaussian[64];
	for (int i = 0; i < 2*r +1 ; i++)
	{
		float x = i - r;
		fGaussian[i] = expf(-(x*x) / (2 * sd*sd));
	}
	//TODO: Copy computed fGaussian to the cGaussian on device memory (2 pts)
	cudaMemcpyToSymbol(&cGaussian, &fGaussian, 0, cudaMemcpyDeviceToDevice);
}

//TODO: implement cuda_gaussian() kernel (3 pts)
__device__
void cuda_gaussian(float x, double sigma, float a, float b, double* out)
{
	double output = a * b *	expf(-(powf(x, 2)) / (2 * powf(sigma, 2)));
	out = &output;
}


/*********** Bilateral Filter  *********/
// Parallel (GPU) Bilateral filter kernel
__global__ void cuda_bilateral_filter(BYTE* input, BYTE* output,
	int width, int height,
	int r, double sI, double sS)
	{
		for(int h = 0; h < height; h++){
			for(int w = 0; w < width; w++){
				double iFiltered = 0;
				double wP = 0;
				// Get the centre pixel value
				unsigned char centrePx = input[h*width+w];
				// Iterate through filter size from centre pixel
				for (int dy = -r; dy <= r; dy++) {
					int neighborY = h+dy;
					if (neighborY < 0)
	                    neighborY = 0;
	                else if (neighborY >= height)
	                    neighborY = height - 1;
					for (int dx = -r; dx <= r; dx++) {
						int neighborX = w+dx;
						if (neighborX < 0)
		                    neighborX = 0;
		                else if (neighborX >= width)
		                    neighborX = width - 1;
						// Get the current pixel; value
						unsigned char currPx = input[neighborY*width+neighborX];
						// Weight = 1D Gaussian(x_axis) * 1D Gaussian(y_axis) * Gaussian(Range or Intensity difference)
						//TODO: implement bilateral filter kernel (9 pts)
						double w;
						cuda_gaussian(centrePx - currPx, sI, cGaussian[dy + r], cGaussian[dx + r], &w);

						iFiltered += w * currPx;
						wP += w;
					}
				}
				output[h*width + w] = iFiltered / wP;
			}
		}
	}

	//<<<bilateral_grid, bilateral_block>>>


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

        int block_dim_x, block_dim_y;
        block_dim_x = block_dim_y = (int) sqrt(suggested_blockSize);

        dim3 gray_block(block_dim_x, block_dim_y); // 2 pts

				int grid_x, grid_y;
				grid_x = (input.cols + block_dim_x - 1) / block_dim_x;
				grid_y = (input.rows + block_dim_x - 1) / block_dim_y;


        //TODO: Calculate grid size to cover the whole image - 2 ptsd
				dim3 gray_grid(grid_x, grid_y);
				printf("HIII\n");

        // Allocate the intermediate image buffers for each step
        Image img_out(input.cols, input.rows, 1, "P5");
        for (int i = 0; i < 2; i++)
        {
            //TODO: allocate memory on the device (2 pts)
						cudaMalloc((void**) &d_image_out[i], sizeof(BYTE) * image_size);
            //TODO: intialize allocated memory on device to zero (2 pts)
						cudaMemset(/*(void**)*/ &d_image_out[i], 0, sizeof(BYTE) * image_size);
        }

        //copy input image to device
        //TODO: Allocate memory on device for input image (2 pts)
				cudaMalloc((void**) &d_input, sizeof(BYTE) * image_size * 3);
        //TODO: Copy input image into the device memory         dim3 gray_block(block_dim_x, block_dim_y); // 2 pts
				cudaMemcpy(d_input, input.pixels, image_size * 3, cudaMemcpyHostToDevice);



        cudaEventRecord(start, 0); // start timer
        // Convert input image to grayscale
        //TODO: Launch cuda_grayscale() (2 pts)
				cuda_grayscale<<<gray_grid, gray_block>>>(input.cols, input.rows, d_input, d_image_out[0]);
				cuda_grayscale<<<gray_grid, gray_block>>>(input.cols, input.rows, d_input, d_image_out[1]);


        cudaEventRecord(stop, 0); // stop timer
        cudaEventSynchronize(stop);

        // Calculate and print kernel run time
	cudaEventElapsedTime(&time, start, stop);
	cout << "GPU Grayscaling time: " << time << " (ms)\n";
	cout << "Launched blocks of size " << gray_block.x * gray_block.y << endl;

        //TODO: transfer image from device to the main memory for saving onto the disk (2 pts)
				cudaMemcpy(output.pixels, d_image_out[0], image_size, cudaMemcpyDeviceToHost);
        savePPM(output, "image_gpu_gray.ppm");


	// ******* Bilateral filter kernel launch *************

	//Creating the block size for grayscaling kernel
	cudaOccupancyMaxPotentialBlockSize( &suggested_minGridSize, &suggested_blockSize, cuda_bilateral_filter);

        block_dim_x = block_dim_y = (int) sqrt(suggested_blockSize);

        dim3 bilateral_block(block_dim_x, block_dim_y/* TODO */); // 2 pts

        //TODO: Calculate grid size to cover the whole image - 2pts
				dim3 bilateral_grid((input.cols + block_dim_x - 1)/block_dim_x, (input.rows + block_dim_x - 1)/block_dim_y);

        // Create gaussain 1d array
        cuda_updateGaussian(r,sS);

        cudaEventRecord(start, 0); // start timer
				//TODO: Launch cuda_bilateral_filter() (2 pts)
				cuda_bilateral_filter<<<bilateral_grid, bilateral_block>>>(input.pixels, output.pixels, input.cols, input.rows, r, sI, sS);

				//cpu_bilateral_filter(BYTE* input, BYTE* output, int width, int height, int r, double sI, double sS)

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
