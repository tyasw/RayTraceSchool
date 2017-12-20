/* File: Sphere.java
 * Date: 4/20/17
 * Student: William Tyas
 * ID: W01203451
 * Class: CSCI 241, Spring 2017
 * Description: a sphere object with a center, radius, and color
 */
public class Sphere {
   private nTuple center;
   private float r;        //radius
   private nTuple color;

   public Sphere() {
       this.center = new nTuple(0.0f, 0.0f, 0.0f);
       this.r = 0;
       this.color = new nTuple(0.0f, 0.0f, 0.0f);
   }

   public Sphere(nTuple c, float r, nTuple color) {
       this.center = c;
       this.r = r;
       this.color = color;
   }

   public nTuple center() {
       return this.center;
   }

   public float r() {
       return this.r;
   }

   public nTuple color() {
       return this.color;
   }

   //Sets the center of the sphere
   public void setCenter(float newX, float newY, float newZ) {
       this.center.setNTuple(newX, newY, newZ);
   }

   // Calculate the bounding box for the sphere
   public nTuple getBoundBox(World world) {
        nTuple lCorner = new nTuple();
        nTuple uCorner = new nTuple();

        // Calculate x extent
        float aX = (float) Math.sqrt(Math.pow(this.center().x(), 2) + Math.pow(this.center().z() - world.camera().z(), 2));
        float thetaX = (float) Math.atan(this.r() / aX);
        float omegaX = (float) Math.asin(this.center().x() / aX);
        float phiX = omegaX - thetaX;
        
        // Calculate y extent
        float aY = (float) Math.sqrt(Math.pow(this.center().y(), 2) + Math.pow(this.center().z() - world.camera().z(), 2));
        float thetaY = (float) Math.atan(this.r() / aY);
        float omegaY = (float) Math.asin(this.center().y() / aY);
        float phiY = omegaY - thetaY;

        lCorner.setNTuple(world.camera().z() * ((float) Math.tan(phiX)), world.camera().z() * ((float) Math.tan(phiY)), 0);
        uCorner.setNTuple(world.camera().z() * ((float) Math.tan(phiX + 2 * thetaX)), world.camera().z() * ((float) Math.tan(phiY + 2 * thetaY)), 0);

        return new nTuple(lCorner, uCorner);
   }
}