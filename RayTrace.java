/* File: RayTrace.java
 * Date: 4/20/17
 * Student: William Tyas
 * ID: W01203451
 * Class: CSCI 241, Spring 2017
 * Description: Draws spheres on the screen using ray tracing.
 */
import java.awt.*;
import java.util.*;
import java.io.*;

public class RayTrace {
   public static void main(String args[]) throws FileNotFoundException {
      // Set up world
      int imagePlaneSize = 10;
      int s = imagePlaneSize;
      
      World world = readWrlFile(s, args);
      nTuple backgroundColor = new nTuple(0.5f, 0.6f, 0.8f);
        
       // Create an image
       Image image = new Image();

       // Insert spheres into quadtree prior to ray tracing
       int quadtreeLevels = 3;     // levels in the quadtree
       Quadtree quadtree = world.setUpQuadtree(quadtreeLevels);
        
       // Draw pixel
       drawPixel(world, image, backgroundColor, quadtree);
   }
    
   // Reads a world file from the command line and sets up the world
   public static World readWrlFile(int imagePlaneSize, String[] args) throws FileNotFoundException{
      // Read from world file specified by command line argument
      Scanner input = new Scanner(new File(args[0]));
      nTuple lightDirection = new nTuple();
      ArrayList<Sphere> objectList = new ArrayList<Sphere>();
        
      //first line is camera
      String camera = input.next();
      double cameraDistance = input.nextDouble();

      //second line is light
      String light = input.next();
      lightDirection.setX((float) input.nextDouble());
      lightDirection.setY((float) input.nextDouble());
      lightDirection.setZ((float) input.nextDouble());
         
      // Read spheres from file
      while (input.hasNextLine()) {
         //rest of the lines are spheres
         nTuple sphereCenter = new nTuple();
         String sphere = input.next();
         sphereCenter.setX((float) input.nextDouble());
         sphereCenter.setY((float) input.nextDouble());
         sphereCenter.setZ((float) input.nextDouble());
         
         float sphereRadius = (float) input.nextDouble();
         
         nTuple sphereColor = new nTuple();
         sphereColor.setX((float) input.nextDouble());
         sphereColor.setY((float) input.nextDouble());
         sphereColor.setZ((float) input.nextDouble());
         
         Sphere nextSphere = new Sphere(sphereCenter, sphereRadius, sphereColor);
         
         objectList.add(nextSphere);
      }
      return new World((float) cameraDistance, imagePlaneSize, lightDirection, objectList);    
    }
    
    // Determine what color the pixel should be and draw it
    public static void drawPixel(World world, Image image, nTuple backgroundColor, Quadtree quadtree) {
         int w = image.getWIDTH();
         int h = image.getHEIGHT();
         nTuple p = world.camera();
         int s = world.imagePlaneSize();
         nTuple lightDirection = world.lightDirection();
         ArrayList<Sphere> objectList = world.getObjectList();         
         nTuple v = new nTuple();
         
         Sphere closestSphere = new Sphere();
         float closestZ = (float) Double.NEGATIVE_INFINITY;

         // Iterate over pixels in image
         for (int u = 0; u < w; u++) {
             for (int d = 0; d < h; d++) {
               // Keep track of intersections
               boolean intersection = false;
               boolean currentIntersection = false;
                
               ray ray = new ray();
               ray.imagePlaneCoords(world, s, u, d, w, h); // get image plane coordinates   
               if (!world.sphereIntersects(quadtree)) {    // no intersection with sphere, paint background color
                  Color pixelColor = new Color(backgroundColor.x(), backgroundColor.y(), backgroundColor.z());
                  image.paint(image.getGraphics(), pixelColor, u, d, u, d);                  
               } else {
                  // Shoot ray towards image plane
                  ray = ray.shootRay(world, v, p, s, u, d, w, h);
                  for (int i = 0; i < objectList.size(); i++) {
                      //Move sphere to origin and camera by same translation vector to simplify math
                      nTuple q = new nTuple(ray.camera().x() - objectList.get(i).center().x(), ray.camera().y() - objectList.get(i).center().y(), ray.camera().z() - objectList.get(i).center().z());
                      Sphere translatedSphere = new Sphere(new nTuple(0.0f, 0.0f, 0.0f), objectList.get(i).r(), objectList.get(i).color());
                       
                      // Check if ray intersects sphere
                      intersection = ray.isIntersect(q, v, translatedSphere, intersection);
                      if (intersection) {
                          currentIntersection = true;
                      } 
                                          
                      // If current sphere intersected, check if closest sphere
                      if (currentIntersection && ray.direction().z() > closestZ) {
                          closestZ = ray.direction().z();
                          closestSphere = world.getObject(i);
                      }
                      currentIntersection = false;
                  }
                  closestZ = (float) Double.NEGATIVE_INFINITY;
                   
                  // Determine pixel color and draw pixel
                  Color pixelColor = world.getColor(intersection, image, backgroundColor, closestSphere);
                  image.paint(image.getGraphics(), pixelColor, u, d, u, d);
               }
            }
        }
    }
}
