package org.ittd.imd.ca.sprites;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

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

/**
 * <p>Java class for SeeSawPartT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SeeSawPartT">
 *   &lt;complexContent>
 *     &lt;extension base="{}BaseEntity">
 *       &lt;sequence>
 *         &lt;element name="x" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="y" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SeeSaw", propOrder = {

})
public class SeeSaw extends Entity
{

  // Our object is two Boundaries and one joint
  @XmlTransient
  RevoluteJoint joint;
  @XmlTransient
  SeeSawPart Boundary1;
  @XmlTransient
  SeeSawPart Boundary2;
  @XmlTransient
  SeeSawPart boundary3;
  @XmlTransient
  SeeSawPart boundary4;
  @XmlTransient
  PApplet parent;	// Variable to hold the parent element 
  @XmlTransient
  PBox2D box2d;		// holds the box2d world
  
  @XmlTransient
	public static List<SeeSaw> entities = new ArrayList<SeeSaw>();
  
  /**
 * @param x: This is the X position of the see-saw. 
 * @param y: This is the Y position of the see-saw.
 * @param p: The parent element so we can attach it to our PApplet.
 * @param b: The box2d element so we can attach it to our physics world.
 */
  
  public SeeSaw(){}
  public SeeSaw(float x, float y, PApplet p, PBox2D b)
  {
	setXposition(x);
	setYposition(y);
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

  public void display() 
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

@Override
public String toString() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void makeBody() {
	// TODO Auto-generated method stub
	
}

public void addSeeSawItem(SeeSaw item)
{
	SeeSaw.entities.add(item);
}

public List<SeeSaw> getSeeSawEntities()
{
	return entities;
}
}