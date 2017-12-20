/* File: Quadtree.java
 * Date: 4/20/17
 * Student: William Tyas
 * ID: W01203451
 * Class: CSCI 241, Spring 2017
 * Description: A tree that stores objects for the purpose of optimization
 */
import java.util.*;

public class Quadtree {
   private ArrayList<Sphere> intersectSpheres;  //spheres associated with node
   private cell cell;                           //cell describing
   private Quadtree parent;
   private Quadtree current;
   private Quadtree ul, ur, ll, lr;  //children

   //constructor that generates an empty node
   public Quadtree() {
      this.cell = null;
      this.intersectSpheres = new ArrayList<Sphere>();
      this.parent = null;
      this.current = this;
      this.ul = null;
      this.ur = null;
      this.ll = null;
      this.lr = null;
   }

   // Returns a tree referencing a cell and four empty subtrees
   public Quadtree(cell cell) {
      this.cell = cell;
      this.intersectSpheres = new ArrayList<Sphere>();
      this.parent = null;
      this.ul = null;
      this.ur = null;
      this.ll = null;
      this.lr = null;
   }

   // Returns a tree referencing four subtrees
   public Quadtree(Quadtree ul, Quadtree ur, Quadtree ll, Quadtree lr) {
      this(null, ul, ur, ll, lr);
   }

   // Returns a tree referencing value and four subtrees
   public Quadtree(Sphere value, Quadtree ul, Quadtree ur, Quadtree ll, Quadtree lr) {
      if (ul == null) {
         this.ul = new Quadtree();
      }
      setUL(ul);

      if (ur == null) {
         this.ur = new Quadtree();
      }
      setUR(ur);

      if (ll == null) {
         this.ll = new Quadtree();
      }
      setLL(ll);
      
      if (lr == null) {
         this.lr = new Quadtree();
      }
      setLR(lr);
   }

   // returns reference to (possibly empty) ul subtree
   public Quadtree ul() {
      return this.ul;
   }

   // returns reference to (possibly empty) ur subtree
   public Quadtree ur() {
      return this.ur;
   }

   // returns reference to (possibly empty) ll subtree
   public Quadtree ll() {
      return this.ll;
   }

   // returns reference to (possibly empty) lr subtree
   public Quadtree lr() {
      return this.lr;
   }

   //returns reference to parent node, or null
   public Quadtree parent() {
      return this.parent;
   }

   public cell getCell() {
      return this.cell;
   }
   
   // Set the cell of this quadtree
   public void setCell(cell cell) {
      this.cell = cell;
   }
   
   // Set the children of the current quadtree
   public void setQuadtree(Quadtree newLL, Quadtree newUL, Quadtree newLR, Quadtree newUR) {
      setLL(newLL);
      setUL(newUL);
      setLR(newLR);
      setUR(newUR);
   }
   
   //re-parents this node to parent reference, or null
   public void setParent(Quadtree newParent) {
      if (newParent.getCell() == null) {
         this.parent = newParent;
      }
      
      this.parent = newParent;
   }

   //post: sets ul subtree to newUL and re-parents newUL if not null
   public void setUL(Quadtree newUL) {
      if (newUL.getCell() == null) return;
      this.ul = newUL;
      this.ul.setParent(this);
   }

   //post: sets ur subtree to newUR and re-parents newUR if not null
   public void setUR(Quadtree newUR) {
      if (newUR.getCell() == null) return;
      this.ur = newUR;
      this.ur.setParent(this);
   }

   //post: sets ll subtree to newLL and re-parents newLL if not null
   public void setLL(Quadtree newLL) {
      if (newLL.getCell() == null) return;
      this.ll = newLL;
      this.ll.setParent(this);
   }

   //post: sets lr subtree to newLR and re-parents newLR if not null
   public void setLR(Quadtree newLR) {
      if (newLR.getCell() == null) return;
      this.lr = newLR;
      this.lr.setParent(this);
   }
   
   // returns spheres associated with this node
   public ArrayList<Sphere> getSpheres() {
      return this.intersectSpheres;
   }

   // sets the value associated with this node
   public void setValue(Sphere value) {
      this.intersectSpheres.add(value);
   }
   
   // Checks if quadtree is empty
   public boolean isEmpty() {
      return (this.parent == null && this.ul == null && this.ur == null && this.ll == null && this.lr == null);
   }
}