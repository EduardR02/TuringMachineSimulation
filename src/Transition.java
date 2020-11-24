import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Transition {

	int yOne ,xOne, yTwo, xTwo, offset;
	String direction,read,write;
	State one, two;
	Rectangle2D rect;

	Color c = new Color(255,255,255);
	
	
	public Transition(String read, String write, String direction, State one, State two, int offsetMul) {
		this.read = read;
		this.write = write;
		this.direction = direction;
		this.one = one;
		this.two = two;
		this.offset = offsetMul*15;
		setPos();
		
	}
	
	public void setPos() {
		yOne = one.getY();
		xOne = one.getX();
		yTwo = two.getY();
		xTwo = two.getX();
		rect = new Rectangle2D.Double((xOne + xTwo)/2 - 75, (yOne + yTwo)/2 - one.getRadius() - 15 - offset, 150,15);
	}
	
	public void show(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(c);
		g2.setFont(Gui.f);
		setPos();
		g2.drawString( "(" + read + "|" + write + ")" + " " + direction + "      " + " (from " + one.getName() + " to " + two.getName() + ")", (xOne + xTwo)/2 - 75,(yOne + yTwo)/2 - one.getRadius() - 5 - offset);
	}
	
	public State getOne() {
		return one;
	}

	public State getTwo() {
		return two;
	}

	public void setOne(State one) {
		this.one = one;
	}

	public void setTwo(State two) {
		this.two = two;
	}

	public String getDirection() {
		return direction;
	}

	public String getRead() {
		return read;
	}

	public String getWrite() {
		return write;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public void setWrite(String write) {
		this.write = write;
	}
	
}
