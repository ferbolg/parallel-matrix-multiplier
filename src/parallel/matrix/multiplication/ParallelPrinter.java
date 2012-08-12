package parallel.matrix.multiplication;

/*
 * Author : Naveen Nandan
 * 
 * Description : O(n) row-wise matrix display
 */
import java.util.concurrent.atomic.AtomicIntegerArray;

public class ParallelPrinter extends Thread {
   private int row;
   private AtomicIntegerArray[] matrix;

   public ParallelPrinter(final int row, final AtomicIntegerArray[] matrix) {
      this.row = row;
      this.matrix = matrix;
   }

   public synchronized void run() {
      for (int i = 0; i < matrix[row].length(); i++) {
         System.out.print(matrix[row].get(i));
         System.out.print(" ");
      }
   }
}
