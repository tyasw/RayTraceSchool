/* File: World.java
 * Date: 4/20/17
 * Student: William Tyas
 * ID: W01203451
 * Class: CSCI 241, Spring 2017
 * Description: A world object. World objects contain the camera, the image
 *    plane, a list of objects located in the world, and an n-tuple representing
 *    the direction of the light source.
 */
import java.awt.*;
import java.util.*;

public class World {
   private float cameraDistance;     // distance of the camera from the image plane
   private nTuple camera;            // camera located at (0,0,cameraDistance)
   private int imagePlaneSize;       // size of the image plane
   private nTuple image_Plane;       // the image plane
   private nTuple lightDirection;    // vector describing light source direction
   private ArrayList<Sphere> objectList;       // array of spheres to be rendered, having center c, radius r, and color (R,G,B)

   // Creates a new world object. Camera location calculated from the camera distance.
   // Image plane created from specified imagePlaneSize.
   public World(float cameraDistance, int imagePlaneSize, nTuple lightDirection, ArrayList<Sphere> objectList) {
      this.cameraDistance = cameraDistance;
      this.camera = new nTuple(0.0f, 0.0f, cameraDistance);
      this.imagePlaneSize = imagePlaneSize;
      this.lightDirection = lightDirection;
      lightDirection.normalize();
      this.objectList = objectList;
      this.image_Plane = new nTuple(imagePlaneSize, imagePlaneSize, 0);    //WHAT ABOUT +- IMAGEPLANESIZE?
   }

   public nTuple camera() {
      return this.camera;
   }

   public int imagePlaneSize() {
      return this.imagePlaneSize;
   }

   public nTuple imagePlane() {
      return this.image_Plane;
   }

   public nTuple lightDirection() {
      return this.lightDirection;
   }

   public ArrayList<Sphere> getObjectList() {
      return this.objectList;
   }

   public Sphere getObject(int i) {
      return this.objectList.get(i);
   }

   // Insert spheres into quadtree to optimize ray tracing, down to specified level
   public Quadtree setUpQuadtree(int level) {
        float lLXCoord = this.imagePlaneSize * -1;
        float lLYCoord = this.imagePlaneSize * -1;
        cell cell = new cell(new nTuple(), new nTuple());
        Quadtree quadtree = new Quadtree(cell);       // setting quadtree to be nonempty so null pointer exception not thrown
        createQuadTree(quadtree, cell, level, this.imagePlaneSize, lLXCoord, lLYCoord);
        for (int i = 0; i < this.getObjectList().size(); i++) {               // Insert each sphere into quadtree
            sphereInsert(quadtree, quadtree.getCell(), level, 0, i);
        }
        return quadtree;
   }

   /* Creates the quadtree. Each node is a cell.
    * Arguments:
    *    quadtree: current quadtree
    *    cell: current cell
    *    level: current level in the tree
    *    imagePlaneSize: size of the image plane
    *    lLXCoord: x-coordinate of the lower corner of the current cell
    *    lLYCoord: y-coordinate of the lower corner of the current cell
    */
   private Quadtree createQuadTree(Quadtree quadtree, cell cell, int level, float imagePlaneSize, float lLXCoord, float lLYCoord) {   
        nTuple xExtentp = new nTuple();
        nTuple yExtentp = new nTuple();
        
        xExtentp.setNTuple(lLXCoord, lLYCoord);
        yExtentp.setNTuple(lLXCoord * -1, lLYCoord * -1);
        
        ArrayList<cell> cells = new ArrayList<cell>();         //Store children cells in array list
        
        if (quadtree.parent() == null) {                       // The overall parent of the quadtree
            quadtree.getCell().setCell(xExtentp, yExtentp);
        }

        // Create cells that contain the lower left and upper right coordinates of that cell
         for (int x = 0; x < 2; x++) {
             for (int y = 0; y < 2; y++) {
                 nTuple lCorner = new nTuple();    //lower corner
                 nTuple uCorner = new nTuple();    //upper corner
                 nTuple coordinates = new nTuple(0.0f, 0.0f);
                 coordinates.setNTuple((float) x, (float) y);
                 float xLLCorner = getCoords(coordinates, lLXCoord, lLYCoord, (float) imagePlaneSize, 1);
                 float yLLCorner = getCoords(coordinates, lLXCoord, lLYCoord, (float) imagePlaneSize, 2);
                 lCorner.setNTuple(xLLCorner, yLLCorner);
                 uCorner.setNTuple(xLLCorner + imagePlaneSize, yLLCorner + imagePlaneSize);
                 
                 cell thisCell = new cell(lCorner, uCorner);
                 cells.add(thisCell);
             }
         }

         // Create subtrees and connect to parent (quadtree)
         Quadtree ll = new Quadtree();
         ll.setParent(quadtree);
         ll.setCell(cells.get(0));
         
         Quadtree ul = new Quadtree();
         ul.setParent(quadtree);
         ul.setCell(cells.get(1));
         
         Quadtree lr = new Quadtree();
         lr.setParent(quadtree);
         lr.setCell(cells.get(2));

         Quadtree ur = new Quadtree();
         ur.setParent(quadtree);
         ur.setCell(cells.get(3));

         quadtree.setQuadtree(ll, ul, lr, ur);
         
         // Recursively create children
         if (level > 1) {
             createQuadTree(quadtree.ll(), quadtree.ll().getCell(), level - 1, (float) imagePlaneSize / 2, cells.get(0).lCorner().x(), cells.get(0).lCorner().y());
             createQuadTree(quadtree.ul(), quadtree.ul().getCell(), level - 1, (float) imagePlaneSize / 2, cells.get(1).lCorner().x(), cells.get(1).lCorner().y());
             createQuadTree(quadtree.lr(), quadtree.lr().getCell(), level - 1, (float) imagePlaneSize / 2, cells.get(2).lCorner().x(), cells.get(2).lCorner().y());
             createQuadTree(quadtree.ur(), quadtree.ur().getCell(), level - 1, (float) imagePlaneSize / 2, cells.get(3).lCorner().x(), cells.get(3).lCorner().y());
             level -= 1;
         }
        return quadtree;
   }

   /* Inserts spheres into a quadtree
    * Arguments:
    *   quadtree: quadtree we will insert spheres into
    *   cells: list of cells
    *   level: determines where in cells list we look
    *   i: current sphere being examined
    */
   private Quadtree sphereInsert(Quadtree tree, cell cell, int level, int currentLevel, int i) {
         // Place spheres whose bounding boxes intersect with cell in correct node
         if (currentLevel < level) {
            if (isIntersect(tree.getCell(), objectList.get(i)) && !tree.getSpheres().contains(objectList.get(i))) {
               tree.setValue(objectList.get(i));
            }   
            sphereInsert(tree.ll(), tree.ll().getCell(), level, currentLevel + 1, i);
            sphereInsert(tree.ul(), tree.ul().getCell(), level, currentLevel + 1, i);
            sphereInsert(tree.lr(), tree.lr().getCell(), level, currentLevel + 1, i);
            sphereInsert(tree.ur(), tree.ur().getCell(), level, currentLevel + 1, i);
         }
         
         // At bottom level of tree, set cells and don't create children
         if (currentLevel == level) {
            if (isIntersect(tree.getCell(), objectList.get(i)) && !tree.getSpheres().contains(objectList.get(i))) {
               tree.setValue(objectList.get(i));
            }            
         }
         return tree;
   }

   /* Checks if the CURRENT sphere is in cell
    * Arguments:
    *   cc: current cell
    *   s: sphere we're looking at for intersection
    */
   private boolean isIntersect(cell cc, Sphere s) {
      nTuple sBB = s.getBoundBox(this);       // bound box for sphere
      boolean isIntersectX = ((cc.lCorner().x() <= sBB.getP().x()) && (sBB.getP().x() < cc.uCorner().x())) || ((cc.lCorner().x() <= sBB.getV().x()) && (sBB.getV().x() < cc.uCorner().x())) || ((sBB.getP().x() <= cc.lCorner().x()) && (cc.uCorner().x() < sBB.getV().x()));
      boolean isIntersectY = ((cc.lCorner().y() <= sBB.getP().y()) && (sBB.getP().y() < cc.uCorner().y())) || ((cc.lCorner().y() <= sBB.getV().y()) && (sBB.getV().y() < cc.uCorner().y())) || ((sBB.getP().y() <= cc.lCorner().y()) && (cc.uCorner().y() < sBB.getV().y()));
      return (isIntersectX && isIntersectY);
   }
   
   /* Checks if pixel intersects the current cell to determine if we're in the correct cell
    * Arguments:
    *    tree: current tree being examined
    *    pixel: current pixel in image plane
    */
   private boolean pixelIsIntersect(Quadtree tree, nTuple pixel) {
      cell cc = tree.getCell();
      boolean isIntersectX = (cc.lCorner().x() <= pixel.x()) && (pixel.x() <= cc.uCorner().x());
      boolean isIntersectY = (cc.lCorner().y() <= pixel.y()) && (pixel.y() <= cc.uCorner().y());
      return (isIntersectX && isIntersectY);
   }

   /* Determines lower left image plane coordinates for current cell
    * Arguments:
    *   coordinates: coordinates of cell ((0,0), (0,1), (0,2), etc.), not the same as image plane coordinates
    *   imageLLCoords: coordinates of lower left corner of image plane
    *   imagePlaneSize: size of image plane of parent
    *   value: number representing whether to calculate x or y coordinate
    */
   private float getCoords(nTuple coordinates, float lLXCoord, float lLYCoord, float imagePlaneSize, int value) {
      float cellCoord = 0.0f;
      if (value == 1) {
          cellCoord = lLXCoord + imagePlaneSize * coordinates.x();
      } else {
          cellCoord = lLYCoord + imagePlaneSize * coordinates.y();
      }
      return cellCoord;
   }
   
   /* Determines whether ANY sphere is in a cell where pixel is located
    * Arguments:
    *    tree: the quadtree
    */
   public boolean sphereIntersects(Quadtree tree) {
      // Go to bottom of tree where pixel is located
      while (tree.ll() != null && tree.ul() != null && tree.lr() != null && tree.ur() != null) {
         if (pixelIsIntersect(tree.ll(), this.image_Plane)) {
            tree = tree.ll();
         } else if (pixelIsIntersect(tree.ul(), this.image_Plane)) {
            tree = tree.ul();
         } else if (pixelIsIntersect(tree.lr(), this.image_Plane)) {
            tree = tree.lr();
         } else if (pixelIsIntersect(tree.ur(), this.image_Plane)) {
            tree = tree.ur();
         }
      }
      cell currentCell = tree.getCell();
      
      // Check if any spheres exist in cell
      for (int i = 0; i < objectList.size(); i++) {
         if (tree.getSpheres().contains(objectList.get(i))) {
            return true;
         }      
      }
      return false;
   }
   
   /* Determine pixel color
    * Arguments:
    *    intersection: does current ray intersect with any spheres?
    *    image: image to draw on
    *    backgroundColor: specified background color
    *    closestSphere: closestSphere to camera along the ray
    */
   public Color getColor(boolean intersection, Image image, nTuple backgroundColor, Sphere closestSphere) {
      if (!intersection) {
         Color pixelColor = new Color(backgroundColor.x(), backgroundColor.y(), backgroundColor.z());
         return pixelColor;
      } else {
         Color pixelColor = lambertianShading(imagePlane(), closestSphere, new nTuple(closestSphere.color().x(), closestSphere.color().y(), closestSphere.color().z()));
         return pixelColor;
      }
   }
   
   /* Modify pixel to implement lambertian shading
    * Arguments:
    *    point: the point to shade
    *    s: current sphere
    *    sphereColor: sphere's color
    */
   public Color lambertianShading(nTuple point, Sphere s, nTuple sphereColor) {
       //Find surface normal
       nTuple n = new nTuple(point.x() - s.center().x(), point.y() - s.center().y(), point.z() - s.center().z());
       n.normalize();

       //Multiply color of sphere by cos of angle between surface normal and light vector
       float cosVectors = n.x() * lightDirection.x() + n.y() * lightDirection.y() + n.z() * lightDirection.z();
       if (cosVectors > 0.1) {
           sphereColor.multiply(cosVectors);
       } else {
           sphereColor.multiply(0.1f);
       }
       Color lambertianShadingColor = new Color((float) sphereColor.x(), (float) sphereColor.y(), (float) sphereColor.z());
       return lambertianShadingColor;
   }
}
