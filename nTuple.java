/* File: nTuple.java
 * Date: 4/20/17
 * Student: William Tyas
 * ID: W01203451
 * Class: CSCI 241, Spring 2017
 * Description: a three-dimensional n-Tuple
 */
public class nTuple {
   private float x;
   private float y;
   private float z;
   private double vectorLength;
   private nTuple p;
   private nTuple v;

   public nTuple() {
       this.x = 0.0f;
       this.y = 0.0f;
       this.z = 0.0f;
   }

   public nTuple(float x, float y) {
       this.x = x;
       this.y = y;
   }

   public nTuple(float x, float y, float z) {
       this.x = x;
       this.y = y;
       this.z = z;
   }

   public nTuple(nTuple p, nTuple v) {
       this.p = p;     //somehow set x to p
       this.v = v;     //somehow set y to v
   }

   public float x() {
       return this.x;
   }

   public float y() {
       return this.y;
   }

   public float z() {
       return this.z;
   }
    
   // The next two methods return an nTuple stored within this nTuple
   public nTuple getP() {
     return this.p;
   }
    
   public nTuple getV() {
     return this.v;
   }

   public nTuple getNTuple() {
       return this;
   }
    
   public void setNTuple(float newX, float newY) {
      this.x = newX;
      this.y = newY;
      this.z = 0.0f;
   }
   
   public void setNTuple(float newX, float newY, float newZ) {
       this.x = newX;
       this.y = newY;
       this.z = newZ;
   }

   public void setX(float newX) {
       this.x = newX;
   }

   public void setY(float newY) {
       this.y = newY;
   }

   public void setZ(float newZ) {
       this.z = newZ;
   }

    // Normalize the nTuple
   public void normalize() {
       if (this.x != 0 | this.y != 0 | this.z != 0) {
           vectorLength = Math.sqrt(Math.pow((double) x, (double) 2) + Math.pow((double) y, (double) 2) + Math.pow((double) z, (double) 2));
           this.x = (float) (x / vectorLength);
           this.y = (float) (y / vectorLength);
           this.z = (float) (z / vectorLength);
       }
   }
    
   // Multiply this nTuple by a factor
   public nTuple multiply(float factor) {
       this.setNTuple(factor * this.x(), factor * this.y(), factor * this.z());
       return this;
   }
}