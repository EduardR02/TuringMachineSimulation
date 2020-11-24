import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class State {

	boolean initialState, finalState;
	int x, y, r, gg, b;
	String name;
	Color c;
	int radius;
	int finalOffset = 4;
	Ellipse2D circle;
	
	public State(int x, int y, String name, boolean initialState) {
		radius = 25;
		setX(x);
		setY(y);
		this.name = name;
		this.initialState = initialState;
		finalState = false;
		pickColor(this.initialState);
		c = new Color(r,gg,b);
		updateShape();
	}
	
	public void pickColor(boolean initial) {
		if (initial) 
			updateColor(0,100,0);
		else
			updateColor(100,0,0);
	}
	
	public void updateColor(int r, int gg, int b) {
		this.r = r;
		this.gg = gg;
		this.b = b;
	}
	
	public void updateShape() {
		circle = new Ellipse2D.Double(x - radius, y - radius, radius*2, radius*2);
	}
	
	public void show(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		c = new Color(r,gg,b);
		updateShape();
		g2.setColor(c);
		g2.fill(circle);
		g2.setColor(new Color(255,255,255));
		g2.draw(circle);
		if(finalState) {
			g2.setColor(new Color(255,255,255));
			g2.drawOval(x + finalOffset - radius, y + finalOffset - radius, radius*2 - finalOffset*2, radius*2 - finalOffset*2);
		}
		g2.setColor(new Color(0,0,0));
		g2.drawString(name,x - radius/2, y + Gui.f.getSize()/2);
	}
	
	public void setFinalState(boolean finalState) {
		this.finalState = finalState;
	}
	
	public void setInitialState(boolean initialState) {
		this.initialState = initialState;
		if(initialState) 
			updateColor(0,100,0);
		else
			updateColor(100,0,0);
		
	}
	
	public boolean getFinalState() {
		return finalState;
	}
	
	public boolean getInitialState() {
		return initialState;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getR() {
		return r;
	}

	public int getG() {
		return gg;
	}

	public int getB() {
		return b;
	}
	
}
