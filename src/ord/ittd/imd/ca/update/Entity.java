package ord.ittd.imd.ca.update;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.ittd.imd.ca.Ball;
import org.ittd.imd.ca.Boundary;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

import pbox2d.PBox2D;
import processing.core.PApplet;

@XmlJavaTypeAdapter(EntityMethodAdapter.class)
public abstract class Entity 
{
	@XmlElement(name="xposition")
	private float xposition;
	@XmlElement(name="yposition")
	private float yposition;
	@XmlElement(name="width")
	private float width;
	@XmlElement(name="height")
	private float height;
	@XmlElement(name="move")
	private boolean move;

	
	@XmlTransient
	private Body body;
	@XmlTransient
	private BodyDef bodyDef;
	@XmlTransient
	private PBox2D box2d;
	@XmlTransient
	private  PApplet parent;
	
	
	public Entity(){}
	public Entity(float x, float y, float w, float h, boolean m)
	{
		this.xposition = x;
		this.yposition = y;
		this.width = w;
		this.height = h;
		this.move = m;
	}

	public Entity(float x, float y, float w, float h, boolean m, PBox2D b, PApplet child) 
	{
		this.xposition = x;
		this.yposition = y;
		this.width = w;
		this.height = h;
		this.move = m;
		this.box2d = b;
		this.parent = child;
		
	}
	
	public abstract String toString();
	public abstract void display();
	public abstract void makeBody();

	public void killBody()
	{
		getBox2d().destroyBody(getBody());
	}
	
	public float getXposition() {
		return xposition;
	}

	public void setXposition(float xposition) {
		this.xposition = xposition;
	}

	public float getYposition() {
		return yposition;
	}

	public void setYposition(float yposition) {
		this.yposition = yposition;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public boolean isMove() {
		return move;
	}

	public void setMove(boolean move) {
		this.move = move;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public BodyDef getBodyDef() {
		return bodyDef;
	}

	public void setBodyDef(BodyDef bodyDef) {
		this.bodyDef = bodyDef;
	}

	public PBox2D getBox2d() {
		return box2d;
	}

	public void setBox2d(PBox2D box2d) {
		this.box2d = box2d;
	}

	public PApplet getParent() {
		return parent;
	}

	public void setParent(PApplet parent) {
		this.parent = parent;
	}
	
	
	
}
