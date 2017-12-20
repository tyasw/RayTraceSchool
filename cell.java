/* File: cell.java
 * Date: 4/20/17
 * Student: William Tyas
 * ID: W01203451
 * Class: CSCI 241, Spring 2017
 * Description: a cell containing an extent in the (x, y) plane
 */
public class cell {
   private nTuple lCorner;          // coordinates of the lower left corner
   private nTuple uCorner;          // coordinates of the upper right corner

   // Creates an empty cell
   public cell() {
      this.lCorner = new nTuple();
      this.uCorner = new nTuple();
   }

   // Creates a cell, setting the lower and upper corners
   public cell(nTuple lower, nTuple upper) {
      this.lCorner = lower;
      this.uCorner = upper;
   }
   
   public nTuple lCorner() {
      return this.lCorner;
   }
   
   public nTuple uCorner() {
      return this.uCorner;
   }
   
   // Sets the lower and upper corners of the cell
   public void setCell(nTuple newX, nTuple newY) {
      this.lCorner = newX;
      this.uCorner = newY;
   }
}