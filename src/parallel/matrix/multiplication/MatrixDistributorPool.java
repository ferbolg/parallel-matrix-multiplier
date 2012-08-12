package parallel.matrix.multiplication;

/*
 * Author : Naveen Nandan
 * 
 * Description : This program performs scalable parallel row-wise matrix
 * multiplication by making use of multi-threading. A thread pool is used for
 * space efficiency (to limit the maximum number of threads spawned).
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class MatrixDistributorPool {

   // thread-safe data structure for matrices
   private static AtomicIntegerArray[] matrixA;
   private static AtomicIntegerArray[] matrixB;
   private static AtomicIntegerArray[] product;

   public static void initialize() throws Exception {
      // get matrixA dimensions
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      String line = br.readLine();
      int rowA = Integer.parseInt(line.split(" ")[0]);
      int colA = Integer.parseInt(line.split(" ")[1]);

      // get matrixB dimensions
      line = br.readLine();
      int rowB = Integer.parseInt(line.split(" ")[0]);
      int colB = Integer.parseInt(line.split(" ")[1]);

      // check if product exists
      if (colA != rowB) {
         throw new Exception("invalid matrix dimensions : cannot multiply");
      }

      // load matrixA
      matrixA = new AtomicIntegerArray[rowA];
      for (int i = 0; i < rowA; i++) {
         AtomicIntegerArray row = new AtomicIntegerArray(colA);
         line = br.readLine();
         for (int j = 0; j < colA; j++) {
            row.set(j, Integer.parseInt(line.split(" ")[j]));
         }
         matrixA[i] = row;
      }

      // load matrixB
      matrixB = new AtomicIntegerArray[rowB];
      for (int i = 0; i < rowB; i++) {
         AtomicIntegerArray row = new AtomicIntegerArray(colB);
         line = br.readLine();
         for (int j = 0; j < colB; j++) {
            row.set(j, Integer.parseInt(line.split(" ")[j]));
         }
         matrixB[i] = row;
      }

      // initialize product matrix
      product = new AtomicIntegerArray[rowA];
      for (int i = 0; i < rowA; i++) {
         AtomicIntegerArray row = new AtomicIntegerArray(colB);
         for (int j = 0; j < colB; j++) {
            row.set(j, 0);
         }
         product[i] = row;
      }
   }

   public static void main(String[] args) {
      try {
         initialize();

         // thread pool for space efficiency (maximum of 10 threads spawned at a
         // given time)
         ExecutorService executor = Executors.newFixedThreadPool(10);

         // row-wise parallel distribution to multiple threads
         for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[i].length(); j++) {
               // submit thread to pool for row-wise multiplication
               ParallelMultiplier multiplyThread = new ParallelMultiplier(i, j,
                     matrixA[i], matrixB, product);
               executor.execute(multiplyThread);
            }
         }

         // row-wise parallel display
         for (int i = 0; i < product.length; i++) {
            // submit thread to pool for row-wise display
            ParallelPrinter displayThread = new ParallelPrinter(i, product);
            executor.execute(displayThread);
            System.out.println();
         }

         executor.shutdown();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
