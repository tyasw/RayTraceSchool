Project: Ray Trace
Author: William Tyas
Last updated: 12/19/17

NOTE: THIS PROJECT IS INCOMPLETE, AND THERE ARE NUMEROUS BUGS.

Ray Trace is a simple ray tracer. It reads information from a .wrl file.
Each line in the .wrl file describes either a sphere, a camera, or the
light source. The Java Graphics package is used for drawing the spheres.

How to run:
Compile by running the following command:

	javac RayTrace.java

To run the program, run the following command:

	java RayTrace <.wrl file name>

Make sure that a plaintext file with the .wrl extension is included in the
same directory as lab02.java. The file should be formatted as follows:

camera: <distance away from image plane>
light: <coordinates of light source>
sphere: <coordinates of light source> <radius of sphere> <color in rgb format>
sphere: <coordinates of light source> <radius of sphere> <color in rgb format>
...

Here is an example of a .wrl file:

camera: 20
light: 0.577 0.577 0.577
sphere: 2.0 2.0 -1.0 3.0 1.0 0.0 0.0
sphere: 2.0 -2.0 0.0 3.0 0.0 1.0 0.0
sphere: -2.0 0.0 1.0 2.0 0.0 0.0 1.0
