 /* File: ray.java
  * Date: 4/20/17
  * Student: William Tyas
  * ID: W01203451
  * Class: CSCI 241, Spring 2017
  * Description: A ray object, which describes a vector from a camera in some
  *   direction.
  */
 public class ray {
     private nTuple cameraPoint;       //location of camera
     private nTuple direction;         //normalized vector describing the ray direction

     public ray() {
         this.cameraPoint = new nTuple(0.0f, 0.0f, 0.0f);
         this.direction = new nTuple(0.0f, 0.0f, 0.0f);
     }
     
     public ray(nTuple cameraPoint, nTuple normalizedVector) {
         this.cameraPoint = cameraPoint;
         this.direction = normalizedVector;
     }

     public nTuple camera() {
         return this.cameraPoint;
     }

     public nTuple direction() {
         return this.direction;
     }

     public void setDirection(float newX, float newY, float newZ) {      //Can I just change direction this way (will it actually change it?)
         this.direction.setNTuple(newX, newY, newZ);
     }
     
     /* Sets the image plane coordinates that will be mapped to
      * Arguments:
      *     world: the world
      *     s: image plane size
      *     u: x-coord of pixel on screen
      *     d: y-coord of pixel on screen
      *     w: width of screen being drawn on
      *     h: height of screen being drawn on
      */
     public void imagePlaneCoords(World world, int s, int u, int d, int w, int h) {
         world.imagePlane().setNTuple(s * ((2 * (float) u)/w - 1), -s * ((2 * (float) d)/h - 1), 0); //q
     }       
 
     /* Shoot a ray from the camera through image plane
      * Arguments:
      *     world: the world
      *     v: ray from camera through point on image plane
      *     p: camera
      *     s: image plane size
      *     u: x-coord of pixel on screen
      *     d: y-coord of pixel on screen
      *     w: width of screen being drawn on (not image plane, but scree)
      *     h: height of screen being drawn on
      */
     public ray shootRay(World world, nTuple v, nTuple p, int s, int u, int d, int w, int h) {
        //subtract two vectors and normalize
        world.imagePlane().setNTuple(s * ((2 * (float) u)/w - 1), -s * ((2 * (float) d)/h - 1), 0); //q
        v.setNTuple(world.imagePlane().x() - p.x(), world.imagePlane().y() - p.y(), world.imagePlane().z() - p.z());
        v.normalize();
   
        nTuple rayVector = new nTuple(p, v);
        ray ray = new ray(p, rayVector);    //ray from camera, need to access parts of rayVector
        return ray;
     }
     
     /* Check if ray intersects with current sphere
      * Arguments:
      *     q: coordinates of translated camera
      *     v: ray from camera through point on image plane
      *     translatedSphere: sphere moved to origin
      *     intersection: whether sphere intersects or not 
      */
     public boolean isIntersect(nTuple q, nTuple v, Sphere translatedSphere, boolean intersection) {           
        // Intersect the ray with the sphere
        float a = 1.0f;         //dot product of v with itself
        float b = 2.0f * (q.x() * v.x() + q.y() * v.y() + q.z() * v.z());
        float c = (q.x() * q.x() + q.y() * q.y() + q.z() * q.z()) - (translatedSphere.r() * translatedSphere.r());

        float t1 = 0.0f;
        float t2 = 0.0f;
        float minT = 0.0f;

        // Solve quardatic to find intersection with sphere
        if ((b*b - 4*a*c) >= 0) {
            t1 = (float) ((-b + Math.sqrt((double) ((b*b) - 4*a*c))) / (2*a));
            t2 = (float) ((-b - Math.sqrt((double) ((b*b) - 4*a*c))) / (2*a));
            intersection = true;

            minT = t1;
            if (t2 < t1) {          //keep smallest t
                minT = t2;
            }
            this.direction().setNTuple(this.camera().x() + minT * v.x(), this.camera().y() + minT * v.y(), this.camera().z() + minT * v.z());
        }
        return intersection;
     }
 }