import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

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
	

	private static int[] insertionSort(int[] arr,AtomicInteger swap) {
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
			   j--;
		   }
		   myarr[j+1] = target;
		   swap.incrementAndGet();
		}

		return myarr;
	}
	
	private static int[] heapSort(int[] arr,AtomicInteger counter) {
		int[] myarr = arr.clone();
		heapSortCore(myarr,counter);
	 
		return myarr;
	}
	
	private static void heapSortCore(int[] arr,AtomicInteger counter) {
		int size = arr.length;
		
		for (int i = size / 2 - 1; i >=0; i--) {
			 heapify(arr, size, i,counter);
		}
		for(int i= size-1;i>0;i--) {
			int max = arr[0];
			arr[0] = arr[i];
			arr[i] = max;
			heapify(arr,i,0,counter);
		}
		
	}
	
	private static void heapify(int[] arr,int size,int i,AtomicInteger counter){
		if(2*i+1 > size-1) {
			return;
		}
		int l = 2 * i + 1; // left = 2*i + 1
	    int r = 2 * i + 2; // right = 2*i + 2
	    int max = i;
	    if (l < size && arr[l] > arr[max])
            max = l;
 
        if (r < size && arr[r] > arr[max])
            max = r;
        
        if (max != i) {
            int swap = arr[i];
            arr[i] = arr[max];
            arr[max] = swap;
            counter.incrementAndGet();
            heapify(arr, size, max,counter);
        }
	}
	
	private static int[] quickSort(int[] arr) {
		int[] myarr = arr.clone();
		quickSortCore(myarr,0,myarr.length-1);
		return myarr;
	}
	
	private static void quickSortCore(int[] arr,int start,int end) {
		if(start > end) {
			return; //sorted
		}
	    if(end > start) {
	    	int part = partition(arr,start,end);
	    	quickSortCore(arr,start,part-1);
	    	quickSortCore(arr,part+1,end);
	    }
		 
	}
	
	private static int partition(int[] arr,int start,int end) {
		int pivot = arr[end];
		int i = (start-1);
		for(int j=start;j<=end-1;j++) {
			if(arr[j] < pivot) {
				int temp = arr[++i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}
		
		int temp = arr[i+1];
		arr[i+1] = arr[end];
		arr[end] = temp;
		return (i+1);
	}
	
	
	
	private static int[] mergeSort(int[] arr) {
		int[] myarr = arr.clone();
		mergeSortCore(myarr);
		return myarr;
	}
	
	private static void mergeSortCore(int[] arr) {
		int size = arr.length;
		if(size<2) {return;}
		int mid = size /2;
		int[] left = Arrays.copyOfRange(arr, 0, mid);
		int[] right = Arrays.copyOfRange(arr, mid, size);
		mergeSortCore(left);
		mergeSortCore(right);
		merge(left,right,arr);
		
	} 
	
	private static void merge(int[] left,int[] right,int[] main) {
		int i=0;
		int j =0;
		while(i+j < main.length) {
			if(i == right.length || (j < left.length && left[j] < right[i] )) {
				main[i+j] = left[j];
				j++;
			}else {
				main[i+j] = right[i];
				i++;
			}
		}
	}
	
	private static void testInsert(int times,int[] arr) {
		System.out.println("Measurment of insertion sort");
		long start = System.currentTimeMillis();
		AtomicInteger counter = new AtomicInteger(0);
		for(int i=0;i<times;i++) {
		
			insertionSort(arr,counter);
			if(i == 0) {
				System.out.println("Number of swapping: "+counter);
			}
			
		}
		long finish = System.currentTimeMillis();
		long timeElapsed = finish - start;
		System.out.println("InsertionSort Clock timeElapsed: "+timeElapsed);
		System.out.println("Average: Clock time "+timeElapsed/times);
	} 
	
	
	private static void testHeapSort(int times,int[] arr) {
		System.out.println("Measurment of Heap sort");
		long start = System.currentTimeMillis();
		AtomicInteger counter = new AtomicInteger(0);
		for(int i=0;i<times;i++) {
		
			heapSort(arr,counter);
			if(i == 0) {
				System.out.println("Number of swapping: "+counter);
			}
			
		}
		long finish = System.currentTimeMillis();
		long timeElapsed = finish - start;
		System.out.println("HeapSort Clock timeElapsed: "+timeElapsed);
		System.out.println("Average: Clock time "+timeElapsed/times);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final int[] test = random(1,1000000,10000);
		
		
		//System.out.println("InsertionSort: "+Arrays.toString(insertionSort(test,new AtomicInteger(0))));
		
		testInsert(100,test);
		testHeapSort(100,test);
		/*
		System.out.println("MergeSort: "+Arrays.toString(mergeSort(test)));
		System.out.println("QuickSort: "+Arrays.toString(quickSort(test)));
		System.out.println("HeapSort: "+Arrays.toString(heapSort(test)));
		System.out.println(System.currentTimeMillis());
		*/
		//System.out.println("Heap == Quick: "+Arrays.equals(quickSort(test),heapSort(test)));
	}

}
