package parallel.matrix.multiplication;

/*
 * Author : Naveen Nandan
 * 
 * Description : O(n) row-wise matrix multiplication
 */
import java.util.concurrent.atomic.AtomicIntegerArray;

public class ParallelMultiplier extends Thread {
   private int row;
   private int column;
   private AtomicIntegerArray matrixA;
   private AtomicIntegerArray[] matrixB;
   private AtomicIntegerArray[] product;

   public ParallelMultiplier(final int row, final int column,
         final AtomicIntegerArray matrixA, final AtomicIntegerArray[] matrixB,
         final AtomicIntegerArray[] product) {
      this.row = row;
      this.column = column;
      this.matrixA = matrixA;
      this.matrixB = matrixB;
      this.product = product;
   }

   public void run() {
      int value = 0;
      for (int i = 0; i < matrixA.length(); i++) {
         value = value + (matrixA.get(i) * matrixB[i].get(column));
      }
      product[row].set(column, value);
   }
}
