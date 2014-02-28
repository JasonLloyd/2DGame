package org.ittd.imd.ca;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import pbox2d.PBox2D;
import processing.core.PApplet;



/**
 * @author ______
 * Found in the library examples for JBOX2d (Windmill)
 * It has been edited so it can be used as a see-saw and not a windmill 
 * 
 */
public class SeeSaw 
{

  // Our object is two Boundaries and one joint
  RevoluteJoint joint;
  SeeSawPart Boundary1;
  SeeSawPart Boundary2;
  SeeSawPart boundary3;
  SeeSawPart boundary4;

  PApplet parent;	// Variable to hold the parent element 
  
  PBox2D box2d;		// holds the box2d world
  
  /**
 * @param x: This is the X position of the see-saw. 
 * @param y: This is the Y position of the see-saw.
 * @param p: The parent element so we can attach it to our PApplet.
 * @param b: The box2d element so we can attach it to our physics world.
 */
  
  public SeeSaw(float x, float y, PApplet p, PBox2D b)
  {

	parent = p;
	box2d = b;
	
    // Initialize locations of two Boundaries
    Boundary1 = new SeeSawPart(x, y-20, 120, 10, false,box2d,parent);
    Boundary2 = new SeeSawPart(x, y, 10, 40, true,box2d,parent);
    boundary3 = new SeeSawPart(x + 40, y + 20,20,10,true,box2d,parent);

    // Define joint as between two bodies
    RevoluteJointDef rjd = new RevoluteJointDef();

    rjd.initialize(Boundary1.getBody(), Boundary2.getBody(), Boundary1.getBody().getWorldCenter());
      // Create the joint
    joint = (RevoluteJoint) box2d.world.createJoint(rjd);
  }

  void display() 
  {
	  
    Boundary2.display();
    Boundary1.display();
    boundary3.display();

    // Draw anchor just for debug
    Vec2 anchor = box2d.coordWorldToPixels(Boundary1.getBody().getWorldCenter());
    parent.pushMatrix();
    parent.fill(255, 0, 0);
    parent.stroke(0);
    parent.ellipse(anchor.x, anchor.y, 4, 4);
    parent.popMatrix();
  }
}