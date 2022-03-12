import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class assign1 {
	private static int[] random(int min,int max,int size) {
		int[] arr = new int[size];
		int upper = max;
		int lower = min;
		for(int i =0;i<arr.length;i++) {
			arr[i] = (int)(Math.random() * (upper - lower))+lower;
		}
		return arr;
	} 
	

	private static int[] insertionSort(int[] arr,AtomicLong swap,AtomicLong compare) {
		int[] myarr = arr.clone();
		if(myarr.length < 2) {
			return myarr;
		}
		for(int i=1;i<myarr.length;i++) {
		   int target = myarr[i];
		   int j = i-1;
		   while(j>=0 && target < myarr[j]){
			   myarr[j+1] = myarr[j];
			   swap.incrementAndGet();
			   compare.incrementAndGet();
			   j--;
		   }
		   myarr[j+1] = target;
		   swap.incrementAndGet();
		}

		return myarr;
	}
	
	private static int[] heapSort(int[] arr,AtomicLong counter,AtomicLong compare) {
		int[] myarr = arr.clone();
		heapSortCore(myarr,counter,compare);
	 
		return myarr;
	}
	
	private static void heapSortCore(int[] arr,AtomicLong counter,AtomicLong compare) {
		int size = arr.length;
		
		for (int i = size / 2 - 1; i >=0; i--) {
			 heapify(arr, size, i,counter,compare);
		}
		for(int i= size-1;i>0;i--) {
			int max = arr[0];
			arr[0] = arr[i];
			arr[i] = max;
			heapify(arr,i,0,counter,compare);
		}
		
	}
	
	private static void heapify(int[] arr,int size,int i,AtomicLong counter,AtomicLong compare){
		if(2*i+1 > size-1) {
			return;
		}
		int l = 2 * i + 1; // left = 2*i + 1
	    int r = 2 * i + 2; // right = 2*i + 2
	    int max = i;
	    if (l < size && arr[l] > arr[max]) {
            max = l;
	        compare.incrementAndGet();
	    }
 
        if (r < size && arr[r] > arr[max]) {
            max = r;
            compare.incrementAndGet();
        }
        if (max != i) {
            int swap = arr[i];
            arr[i] = arr[max];
            arr[max] = swap;
            compare.incrementAndGet();
            counter.incrementAndGet();
            heapify(arr, size, max,counter,compare);
        }
	}
	
	private static int[] quickSort(int[] arr,AtomicLong counter,AtomicLong compare) {
		int[] myarr = arr.clone();
		quickSortCore(myarr,0,myarr.length-1,counter,compare);
		return myarr;
	}
	
	private static void quickSortCore(int[] arr,int start,int end,AtomicLong counter,AtomicLong compare) {
	
		if(end > start) {
			compare.incrementAndGet();
	    	int part = partition(arr,start,end,counter,compare);
	    	quickSortCore(arr,start,part,counter,compare);
	    	quickSortCore(arr,part+1,end,counter,compare);
	    }
		 
	}
	
	private static int partition(int[] arr,int start,int end,AtomicLong counter,AtomicLong compare) {
		int i = (start-1);
		int j = end+1;
		int k = arr[start];
		
		while(true) {
			while(++i < end && arr[i] <k)
				compare.incrementAndGet();
			while(--j > start && arr[j] > k)
				compare.incrementAndGet();
			if(i < j) {
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
				counter.incrementAndGet();
			}else {
				return j;
			}
		}
		/*int pivot = arr[end];
		int i = (start-1);
		for(int j=start;j<=end-1;j++) {
			if(arr[j] < pivot) {
				compare.incrementAndGet();
				counter.incrementAndGet();
				int temp = arr[++i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}
		
		int temp = arr[i+1];
		arr[i+1] = arr[end];
		arr[end] = temp;
		counter.incrementAndGet();
		return (i+1);*/
	}
	
	
	
	private static int[] mergeSort(int[] arr,AtomicLong counter,AtomicLong compare,AtomicLong tempA) {
		int[] myarr = arr.clone();
		mergeSortCore(myarr,counter,compare,tempA);
		return myarr;
	}
	
	private static void mergeSortCore(int[] arr,AtomicLong counter,AtomicLong compare,AtomicLong tempA) {
		int size = arr.length;
		if(size<2) {return;}
		int mid = size /2;
		int[] left = Arrays.copyOfRange(arr, 0, mid);
		int[] right = Arrays.copyOfRange(arr, mid, size);
		tempA.addAndGet(2);
		mergeSortCore(left,counter,compare,tempA);
		mergeSortCore(right,counter,compare,tempA);
		merge(left,right,arr,counter,compare);
		
	} 
	
	private static void merge(int[] left,int[] right,int[] main,AtomicLong counter,AtomicLong compare) {
		int i=0;
		int j =0;
		while(i+j < main.length) {
			compare.incrementAndGet();
			if(i == right.length || (j < left.length && left[j] < right[i] )) {
				compare.incrementAndGet();
				main[i+j] = left[j];
				j++;
			}else {
				main[i+j] = right[i];
				i++;
			}
		}
	}
	
	private static void testInsert(int times,int[][] arr,int control) {
		System.out.println("Measurment of insertion sort");
		long counterSum = 0;
		long compareSum = 0;
		double start=0;
		double finish=0;
		switch(control) {
		  case 0:
			  start = System.currentTimeMillis();
		      break;
		  case 1:
			  start = System.nanoTime();
		      break;
		}
		
		AtomicLong counter;
		AtomicLong compare;
		for(int i=0;i<times;i++) {
			counter = new AtomicLong(0);
			compare = new AtomicLong(0);
			insertionSort(arr[i],counter,compare);
			counterSum += counter.longValue();
			compareSum += compare.longValue();
		}
		switch(control) {
		  case 0:
			  finish = System.currentTimeMillis();
		      break;
		  case 1:
			  finish = System.nanoTime();
		      break;
		}
		
		double timeElapsed = finish - start;
		System.out.println("Selection Sort average comparison: "+compareSum/times);
		System.out.println("Selection Sort average swapping: "+counterSum/times);
		switch(control) {
		  case 0:
			  System.out.println("Merge Sort Clock timeElapsed: "+timeElapsed+" ms");
			  System.out.println("Average: Clock time "+timeElapsed/times+" ms"+"\n");
		      break;
		  case 1:
			  System.out.println("Merge Sort Clock timeElapsed: "+timeElapsed+" ns");
			  System.out.println("Average: Clock time "+timeElapsed/times+" ns"+"\n");
		}
	} 
	
	
	private static void testHeapSort(int times,int[][] arr,int control) {
		System.out.println("Measurment of Heap sort");
		long counterSum = 0;
		long compareSum = 0;
		double start=0;
		double finish=0;
		switch(control) {
		  case 0:
			  start = System.currentTimeMillis();
		      break;
		  case 1:
			  start = System.nanoTime();
		      break;
		}
		AtomicLong counter;
		AtomicLong compare;
		for(int i=0;i<times;i++) {
			counter = new AtomicLong(0);
			compare = new AtomicLong(0);
			heapSort(arr[i],counter,compare);
			counterSum += counter.longValue();
			compareSum += compare.longValue();
			
		}
		switch(control) {
		  case 0:
			  finish = System.currentTimeMillis();
		      break;
		  case 1:
			  finish = System.nanoTime();
		      break;
		}
		double timeElapsed = finish - start;
		System.out.println("Heap Sort average comparison: "+compareSum/times);
		System.out.println("Heap Sort average swapping: "+counterSum/times);
		switch(control) {
		  case 0:
			  System.out.println("Merge Sort Clock timeElapsed: "+timeElapsed+" ms");
			  System.out.println("Average: Clock time "+timeElapsed/times+" ms"+"\n");
		      break;
		  case 1:
			  System.out.println("Merge Sort Clock timeElapsed: "+timeElapsed+" ns");
			  System.out.println("Average: Clock time "+timeElapsed/times+" ns"+"\n");
		}
	}
	
	private static void testQuickSort(int times,int[][] arr,int control) {
		System.out.println("Measurment of Quicksort");
		long counterSum = 0;
		long compareSum = 0;
		double start=0;
		double finish=0;
		switch(control) {
		  case 0:
			  start = System.currentTimeMillis();
		      break;
		  case 1:
			  start = System.nanoTime();
		      break;
		}
		AtomicLong counter;
		AtomicLong compare;
		for(int i=0;i<times;i++) {
		    counter = new AtomicLong(0);
		    compare = new AtomicLong(0);
			quickSort(arr[i],counter,compare);
			counterSum += counter.longValue();
			compareSum += compare.longValue();
			
		}
		switch(control) {
		  case 0:
			  finish = System.currentTimeMillis();
		      break;
		  case 1:
			  finish = System.nanoTime();
		      break;
		}
		
		double timeElapsed = finish - start;
		System.out.println("Quick Sort average comparison: "+compareSum/times);
		System.out.println("Quick Sort average swapping: "+counterSum/times);
		switch(control) {
		  case 0:
			  System.out.println("Merge Sort Clock timeElapsed: "+timeElapsed+" ms");
			  System.out.println("Average: Clock time "+timeElapsed/times+" ms"+"\n");
		      break;
		  case 1:
			  System.out.println("Merge Sort Clock timeElapsed: "+timeElapsed+" ns");
			  System.out.println("Average: Clock time "+timeElapsed/times+" ns"+"\n");
		}
		
	}
	
	private static void testMergeSort(int times,int[][] arr,int control) {
		System.out.println("Measurment of MergeSort");
		long counterSum = 0;
		long compareSum = 0;
		long tempASum = 0;
		
		AtomicLong counter;
		AtomicLong compare;
		AtomicLong tempA;
		double start=0;
		double finish=0;
		switch(control) {
		  case 0:
			  start = System.currentTimeMillis();
		      break;
		  case 1:
			  start = System.nanoTime();
		      break;
		}
		for(int i=0;i<times;i++) {
		    counter = new AtomicLong(0);
			compare = new AtomicLong(0);
			tempA = new AtomicLong(0);
			mergeSort(arr[i],counter,compare, tempA);
			counterSum += counter.longValue();
			compareSum += compare.longValue();
			tempASum += tempA.longValue();
		}
		switch(control) {
		  case 0:
			  finish = System.currentTimeMillis();
		      break;
		  case 1:
			  finish = System.nanoTime();
		      break;
		}
		double timeElapsed = finish - start;
		System.out.println("Merge Sort average comparison: "+compareSum/times);
		System.out.println("Merge Sort average swapping: "+counterSum/times);
		System.out.println("Merge Sort temp array: "+tempASum/times);
		switch(control) {
		  case 0:
			  System.out.println("Merge Sort Clock timeElapsed: "+timeElapsed+" ms");
			  System.out.println("Average: Clock time "+timeElapsed/times+" ms"+"\n");
		      break;
		  case 1:
			  System.out.println("Merge Sort Clock timeElapsed: "+timeElapsed+" ns");
			  System.out.println("Average: Clock time "+timeElapsed/times+" ns"+"\n");
		}
		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
	
		//Testing
		
		switch(args[0]) {
		  case "0":
			    int[] test =  random(1,100,20);
			    System.out.println(Arrays.toString(quickSort(test,new AtomicLong(0),new AtomicLong(0))));
			    System.out.println(Arrays.toString(insertionSort(test,new AtomicLong(0),new AtomicLong(0))));
			    System.out.println(Arrays.toString(mergeSort(test,new AtomicLong(0),new AtomicLong(0),new AtomicLong(0))));
			    System.out.println(Arrays.toString(heapSort(test,new AtomicLong(0),new AtomicLong(0))));
			    System.out.println(Arrays.toString(test));
			    break;
		  case "1":
			    final int[][] test1 = new int[100][10000];
				for(int i = 0;i<test1.length;i++) {
					test1[i] = random(1,1000000,10000);
				}
				 System.out.println("Result of array size 10,000");
				testHeapSort(100,test1,0);
				testQuickSort(100,test1,0);
			    testMergeSort(100,test1,0);
				testInsert(100,test1,0);
		      break;
		  case "2":
				final int[][] test2 = new int[100][100000];
				
				for(int i = 0;i<test2.length;i++) {
					test2[i] = random(1,1000000,100000);
				}
			    System.out.println("Result of array size 100,000");
				testQuickSort(100,test2,0);
				testHeapSort(100,test2,0);
				testMergeSort(100,test2,0);
				testInsert(100,test2,0);
		      break;
		  case "3":
				int[] testOne = random(1,1000000,100000);; 
				int[] insert = insertionSort(testOne,new AtomicLong(0),new AtomicLong(0));
				System.out.println(Arrays.equals(insert,testOne));
				int[] quick = quickSort(insert,new AtomicLong(0),new AtomicLong(0));
				int[] merge = mergeSort(insert,new AtomicLong(0),new AtomicLong(0),new AtomicLong(0));
				System.out.println(Arrays.equals(quick,merge));
		      break;
		  case "4":
			  System.out.println("----Small arrays results-----------------");
				final int[][] test16 = new int[100][16];
				for(int i = 0;i<test16.length;i++) {
					test16[i] = random(1,1000,16);
				}
				
				testHeapSort(100,test16,1);
				testQuickSort(100,test16,1);
			    testMergeSort(100,test16,1);
				testInsert(100,test16,1);
				
				System.out.println("----Results of 16 elements-----------------");
				
				final int[][] test32 = new int[100][32];
				for(int i = 0;i<test32.length;i++) {
					test32[i] = random(1,1000,32);
				}
				
				testHeapSort(100,test32,1);
				testQuickSort(100,test32,1);
			    testMergeSort(100,test32,1);
				testInsert(100,test32,1);
				
				System.out.println("----Results of 32 elements-----------------");
				
				final int[][] test64 = new int[100][64];
				for(int i = 0;i<test64.length;i++) {
					test64[i] = random(1,1000,64);
				}
				testHeapSort(100,test64,1);
				testQuickSort(100,test64,1);
			    testMergeSort(100,test64,1);
				testInsert(100,test64,1);
				System.out.println("----Results of 64 elements-----------------");
				
				final int[][] test128 = new int[100][128];
				for(int i = 0;i<test128.length;i++) {
					test128[i] = random(1,1000,128);
				}
			
				testHeapSort(100,test128,1);
				testQuickSort(100,test128,1);
			    testMergeSort(100,test128,1);
				testInsert(100,test128,1);
				
				System.out.println("----Results of 128 elements-----------------");
				final int[][] test256 = new int[100][256];
				for(int i = 0;i<test256.length;i++) {
					test256[i] = random(1,1000,256);
				}
				
				testHeapSort(100,test256,1);
				testQuickSort(100,test256,1);
			    testMergeSort(100,test256,1);
				testInsert(100,test256,1);
				
				System.out.println("----Results of 256 elements-----------------");
		      break;
		}
		 
	
	
		
		
		//part 1



		//part 2
		
	
   
	

	
	
		//part 3 
	
	

	
		//part 4
		
		
		
	}

}
