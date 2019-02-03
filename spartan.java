import java.util.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import java.io.BufferedReader;

//main method at end of this class
@SuppressWarnings("unchecked")
class Heap<AnyType extends Comparable<AnyType>> {
     
    //basic data fields
   private static final int CAPACITY = 2;
   private int size;            // Number of elements in heap
   private AnyType[] heap;     // The heap array

    //default constructor
   public Heap(){
      size = 0;
      heap = (AnyType[]) new Comparable[CAPACITY];
   }

     //constructor when given an array 
   public Heap(AnyType[] array){
      size = array.length;
      heap = (AnyType[]) new Comparable[array.length+1];
      System.arraycopy(array, 0, heap, 1, array.length);//we do not use 0 index
      buildHeap();
   }

     //basic build heap method
   private void buildHeap() {
      for (int i = size/2; i > 0; i--){
         moveDown(i);
      }
   }
     
     //method used for sorting the heap properly
   public void moveDown(int k) {
      AnyType tmp = heap[k];
      int child;

      for (; 2*k <= size; k = child) {
         child = 2*k;

         if(child != size &&
            heap[child].compareTo(heap[child + 1]) > 0) child++;

         if(tmp.compareTo(heap[child]) > 0)  {
             heap[k] = heap[child];
             ((Data)heap[k]).pos=k;

        }
         else
            break;
      }
      heap[k] = tmp;
      ((Data)tmp).pos = k;
  }

    // heap sort implementation on an array
    public void heapSort(AnyType[] arr) {
        size = arr.length;
        heap = (AnyType[]) new Comparable[size+1];
        System.arraycopy(arr, 0, heap, 1, size);
        buildHeap();

        for (int i = size; i > 0; i--) {
            AnyType temp = heap[i];
            heap[i] = heap[1];
            heap[1] = temp;
            size--;
            moveDown(1);
        }

        for (int i = 0; i < heap.length-1; i++) {
            arr[i] = heap[heap.length-1-i];
        }
    }

     //
   public int heapInsert(AnyType x) {
       
      if(size == heap.length - 1) {
          doubleSize();
      }
       
      //Insert a new item to the end of the array
      int pos = ++size;
       
      //move up
      for(; pos > 1 && x.compareTo(heap[pos/2]) < 0; pos = pos/2) {
         heap[pos] = heap[pos/2];
		 ((Data)heap[pos]).pos=pos;
	  }
       
      heap[pos] = x;
	  return pos;
   }
     
       //delete the top or root (minimum value in the heap)
   public AnyType deleteMin() throws RuntimeException {
       
      if (size == 0) {
          throw new RuntimeException();
      }
       
      AnyType key = heap[1];
      heap[1] = heap[size--];
      moveDown(1);
       
      return key;
	}
     
     //easy way to find minimum element through array indexing
	public AnyType findMin(){
		return heap[1];
	}

    public String toString() {
        String answer = "";
        for(int i = 1; i <= size; i++){
             answer += heap[i]+" ";
        }
        return answer;
    }
     
    //Method that gives the size
   public int getSize() {
	   return size;
    }

    //Private method for doubling the size 
    private void doubleSize() {
        AnyType [] og = heap;
        heap = (AnyType []) new Comparable[heap.length * 2];
        System.arraycopy(og, 1, heap, 1, size);
    }

   //Method for inserting into the hashtable
    public static void htInsert(String name, long score, Heap<Data> hp, Hashtable htable) {
        //create hashtable entries
        Data d = new Data();
        d.name = name;
        d.score = score;
        
        //set the position
        int pos = 0;
        pos = hp.heapInsert(d);
        d.pos = pos;
        
        //add to the hashtable
        htable.put(name, d);
    }

    //Method for updating items in the heap
    public static void update(String name, long n, Hashtable htable, Heap hp){
        Data d = new Data();
        d = (Data)htable.get(name);
        d.score += n;
        int p = d.pos;
        hp.moveDown(p);
    }

    //Method for deleting items from the heap
    public static void delete(long n, Hashtable htable, Heap hp){
        Data d = new Data();  
        d = (Data)hp.findMin();
        while(n > d.score) {
            htable.remove(d.name);
            hp.deleteMin();
            d = (Data)hp.findMin();
	   }
	   System.out.println(hp.getSize());
    }
    
     public static void main(String[] args) throws IOException {
      //create heap and hashtable
		Heap<Data> heap = new Heap<Data>();
		Hashtable<String, Data> ht = new Hashtable<String, Data>();
		//initialize BufferReader
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			//take in total number of candidates
			int n = Integer.parseInt(in.readLine());
			for (int i = 0; i < n; i++) {
				String [] arr = in.readLine().split(" ");
				htInsert(arr[0], Long.parseLong(arr[1]), heap, ht);
			}
			
			n = Integer.parseInt(in.readLine());
			for (int i = 0; i < n; i++) {
				String[] arr = in.readLine().split(" ");
				int option = Integer.parseInt(arr[0]);
				if (option == 1) {
					update(arr[1], Long.parseLong(arr[2]), ht, heap);
				} else if (option == 2) {
					delete(Long.parseLong(arr[1]), ht, heap);
				}
			}
		} catch (IOException e) {
			System.err.println("IOException");
		}
	}
}

//Data class
class Data implements Comparable<Data>{
    //necessary data fields
	public String name;
	public long score;
	public int pos;
	
    //compareTo method to use for comparing within the heap
	public int compareTo(Data d){
        return Long.valueOf(score).compareTo(Long.valueOf(d.score));
    }
  
}
