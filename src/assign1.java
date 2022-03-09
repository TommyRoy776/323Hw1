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
	

	private static int[] insertionSort(int[] arr,AtomicInteger swap,AtomicInteger compare) {
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
	
	private static int[] heapSort(int[] arr,AtomicInteger counter,AtomicInteger compare) {
		int[] myarr = arr.clone();
		heapSortCore(myarr,counter,compare);
	 
		return myarr;
	}
	
	private static void heapSortCore(int[] arr,AtomicInteger counter,AtomicInteger compare) {
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
	
	private static void heapify(int[] arr,int size,int i,AtomicInteger counter,AtomicInteger compare){
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
	
	private static int[] quickSort(int[] arr,AtomicInteger counter,AtomicInteger compare) {
		int[] myarr = arr.clone();
		quickSortCore(myarr,0,myarr.length-1,counter,compare);
		return myarr;
	}
	
	private static void quickSortCore(int[] arr,int start,int end,AtomicInteger counter,AtomicInteger compare) {
		if(start > end) {
			compare.incrementAndGet();
			return; //sorted
		}
	    if(end > start) {
	    	int part = partition(arr,start,end,counter,compare);
	    	quickSortCore(arr,start,part-1,counter,compare);
	    	quickSortCore(arr,part+1,end,counter,compare);
	    }
		 
	}
	
	private static int partition(int[] arr,int start,int end,AtomicInteger counter,AtomicInteger compare) {
		int pivot = arr[end];
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
		return (i+1);
	}
	
	
	
	private static int[] mergeSort(int[] arr,AtomicInteger counter,AtomicInteger compare,AtomicInteger tempA) {
		int[] myarr = arr.clone();
		mergeSortCore(myarr,counter,compare,tempA);
		return myarr;
	}
	
	private static void mergeSortCore(int[] arr,AtomicInteger counter,AtomicInteger compare,AtomicInteger tempA) {
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
	
	private static void merge(int[] left,int[] right,int[] main,AtomicInteger counter,AtomicInteger compare) {
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
	
	private static void testInsert(int times,int[][] arr) {
		System.out.println("Measurment of insertion sort");
		long counterSum = 0;
		long compareSum = 0;
		double start = System.currentTimeMillis();
		AtomicInteger counter = new AtomicInteger(0);
		AtomicInteger compare = new AtomicInteger(0);
		for(int i=0;i<times;i++) {
		
			insertionSort(arr[i],counter,compare);
			counterSum += counter.longValue();
			compareSum += compare.longValue();
		}
		double finish = System.currentTimeMillis();
		double timeElapsed = finish - start;
		System.out.println("Selection Sort comparison: "+compareSum/times);
		System.out.println("Selection Sort swapping: "+counterSum/times);
		System.out.println("InsertionSort Clock timeElapsed: "+timeElapsed+" ms");
		System.out.println("Average: Clock time "+timeElapsed/times+" ms"+"\n");
	} 
	
	
	private static void testHeapSort(int times,int[][] arr) {
		System.out.println("Measurment of Heap sort");
		long counterSum = 0;
		long compareSum = 0;
		double start = System.currentTimeMillis();
		AtomicInteger counter = new AtomicInteger(0);
		AtomicInteger compare = new AtomicInteger(0);
		for(int i=0;i<times;i++) {
		
			heapSort(arr[i],counter,compare);
			counterSum += counter.longValue();
			compareSum += compare.longValue();
			
		}
		double finish = System.currentTimeMillis();
		double timeElapsed = finish - start;
		System.out.println("Heap Sort comparison: "+compareSum/times);
		System.out.println("Heap Sort swapping: "+counterSum/times);
		System.out.println("HeapSort Clock timeElapsed: "+timeElapsed+" ms");
		System.out.println("Average: Clock time "+timeElapsed/times+" ms"+"\n");
	}
	
	private static void testQuickSort(int times,int[][] arr) {
		System.out.println("Measurment of Quicksort");
		long counterSum = 0;
		long compareSum = 0;
		double start = System.currentTimeMillis();
		AtomicInteger counter = new AtomicInteger(0);
		AtomicInteger compare = new AtomicInteger(0);
		for(int i=0;i<times;i++) {
		
			quickSort(arr[i],counter,compare);
			counterSum += counter.longValue();
			compareSum += compare.longValue();
			
		}
		double finish = System.currentTimeMillis();
		double timeElapsed = finish - start;
		System.out.println("Quick Sort comparison: "+compareSum/times);
		System.out.println("Quick Sort swapping: "+counterSum/times);
		System.out.println("HeapSort Clock timeElapsed: "+timeElapsed+" ms");
		System.out.println("Average: Clock time "+timeElapsed/times+" ms"+"\n");
		
	}
	
	private static void testMergeSort(int times,int[][] arr) {
		System.out.println("Measurment of MergeSort");
		long counterSum = 0;
		long compareSum = 0;
		long tempASum = 0;
		
		AtomicInteger counter;
		AtomicInteger compare;
		AtomicInteger tempA;
		double start = System.currentTimeMillis();
		for(int i=0;i<times;i++) {
		    counter = new AtomicInteger(0);
			compare = new AtomicInteger(0);
			tempA = new AtomicInteger(0);
			mergeSort(arr[i],counter,compare, tempA);
			counterSum += counter.longValue();
			compareSum += compare.longValue();
			tempASum += tempA.longValue();
		}
		double finish = System.currentTimeMillis();
		double timeElapsed = finish - start;
		System.out.println("Merge Sort comparison: "+compareSum/times);
		System.out.println("Merge Sort swapping: "+counterSum/times);
		System.out.println("Merge Sort temp array: "+tempASum/times);
		System.out.println("Merge Sort Clock timeElapsed: "+timeElapsed+" ms");
		System.out.println("Average: Clock time "+timeElapsed/times+" ms"+"\n");
		
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//final int[] test = random(1,1000000,10000);
		
		final int[][] test1 = new int[100][10000];
		for(int i = 0;i<test1.length;i++) {
			test1[i] = random(1,1000000,10000);
		}
		
	
		
		
		//part 1
		
		testHeapSort(100,test1);
		testQuickSort(100,test1);
	    testMergeSort(100,test1);
		testInsert(100,test1);

		
		//part 2
		
		final int[][] test2 = new int[100][100000];
		for(int i = 0;i<test1.length;i++) {
			test1[i] = random(1,1000000,100000);
		}
		
		testInsert(100,test2);
		testHeapSort(100,test2);
		testMergeSort(100,test2);
		testQuickSort(100,test2);
		
		
		/*int[] insert = insertionSort(test,new AtomicInteger(0),new AtomicInteger(0));
		int[] quick = quickSort(test,new AtomicInteger(0),new AtomicInteger(0));
		int[] merge = mergeSort(test,new AtomicInteger(0),new AtomicInteger(0),new AtomicInteger(0));
		System.out.println(Arrays.equals(quick,merge));*/
		
	}

}
