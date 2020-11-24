import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Gui extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
	

	private static final long serialVersionUID = 1L;

	static int width = 1200, height = 600, gap = 135, gapL = 25, distanceY = 30, distBetw = 10, buttonSize = 150;
	LinkedList<Box> band;
	LinkedList<State> allStates;
	LinkedList<Transition> allTransitions;
	LinkedList<String> inputAlph;
	LinkedList<String> outputAlph;
	
	String input = "";
	String[] bool = {"false","true"};
	String[] dir = {"N","R","L"};
	State selected;
	Transition selT = null;
	JButton b1,b2,b3,b4,b5;
	JLabel lxy,lname,lradius,lrgb,lfin,lTrRead,lTrWrite,lTrDir;
	JTextField t1,selX,selY,selName,radius,selColR,selColG,selColB,selTRead,selTWrite;
	JComboBox<String> fState;
	JComboBox<String> iState;
	JComboBox<String> trDir;
	boolean clickedt1 = false;
	boolean initialExists = false;
	boolean creatingState = false;
	boolean creatingTransition = false;
	boolean deleting = false;
	boolean toggleFinal = false;
	boolean dragged = false;
	boolean buttonsCreated = false;
	boolean transitionsCreated = false;
	Color labcol = new Color(220,20,60);
	Color fore = new Color(255,255,255);
	Color back = new Color(40,40,40);
	Color back2 = new Color(100,100,100);
	static Font f = new Font("Dialog",Font.BOLD,12);
	
	public static void main(String[] args) {
		Gui l = new Gui();
		JFrame f = new JFrame();
		f.setTitle("Turing Simulation");
		f.setContentPane(l);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.setResizable(true);
	}
	
	public Gui() {
		setPreferredSize(new Dimension(width,height));
		allStates = new LinkedList<State>();
		allTransitions = new LinkedList<Transition>();
		inputAlph = new LinkedList<String>();
		outputAlph = new LinkedList<String>();
		inputAlph.push("$");
		outputAlph.push("$");
		
		UIManager.put("Button.font", f);
		UIManager.put("Button.background", back);
		UIManager.put("Button.foreground", fore);
		UIManager.put("Button.border", 0);
		
		UIManager.put("Label.font", f);
		UIManager.put("Label.foreground", labcol);
		
		UIManager.put("TextField.font", f);
		UIManager.put("TextField.background", back);
		UIManager.put("TextField.foreground", fore);
		UIManager.put("TextField.caretForeground", fore);
		UIManager.put("TextField.border", 0);
		
		UIManager.put("ComboBox.font", f);
		UIManager.put("ComboBox.background", back);
		UIManager.put("ComboBox.foreground", fore);
		UIManager.put("ComboBox.selectionBackground", back2);
		UIManager.put("ComboBox.selectionForeground", fore);
		
		SwingUtilities.updateComponentTreeUI(this);
		
		setFocusable(true);
		requestFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
		createButtons();
		setLayout(null);
	}
	
	public State createState(int x, int y, boolean initial) {
		State newst;
		int auto = 0;
		if (allStates != null) {
			for(auto = 0; auto < allStates.size(); auto++) {
			}
		}
		String n = "q_" + auto;
		n = checkStateName(n);
		newst = new State(x, y, n, initial);
		newst.setX(checkDrawX(x, newst.getRadius()));
		newst.setY(checkDrawY(y, newst.getRadius()));
		return newst;
	}
	
	public String checkStateName(String n) {
		if(allStates != null) {
			for(State st : allStates) {
				if(st.getName().equals(n)) {
					n = n + "(1)";
					return n;
					}
			}
		}
		return n;
	}
	
	public Transition createTransition(State one, State two) {
		Transition newtr;
		int offsetMul = 0;
		for(Transition tr : allTransitions) {
			if((tr.getOne() == one && tr.getTwo() == two) || (tr.getOne() == two && tr.getTwo() == one)) {
				offsetMul++;
			}
		}
		newtr = new Transition(inputAlph.getLast(),outputAlph.getLast(),dir[0],one,two,offsetMul);
		return newtr;
	}
	
	public int checkDrawX(int x, int radius) {
		if (x < (gap + distBetw + buttonSize + radius)) {
			x = gap + distBetw + buttonSize + radius;
		}
		return x;
	}
	
	public int checkDrawY(int y, int radius) {
		if (y < distanceY*2 + 10 + radius) {
			y = distanceY*2 + 10 + radius;
		}
		return y;
	}
	
	public void createButtons() {
		b1 = new JButton("Simulate");
		b1.setBounds(gap, (int) (distanceY*2.5), buttonSize, distanceY);
		b1.addActionListener(this);
		add(b1);
		
		b2 = new JButton("Create State");
		b2.setBounds(gap*3, distanceY, buttonSize, distanceY);
		b2.addActionListener(this);
		add(b2);
		
		b3 = new JButton("Create Transition");
		b3.setBounds(gap*3 + buttonSize + distBetw, distanceY, buttonSize, distanceY);
		b3.addActionListener(this);
		add(b3);
		
		b4 = new JButton("Toggle Final State");
		b4.setBounds(gap*3 + (buttonSize + distBetw)*2, distanceY, buttonSize, distanceY);
		b4.addActionListener(this);
		add(b4);
		
		b5 = new JButton("Delete");
		b5.setBounds(gap*3 + (buttonSize + distBetw)*3, distanceY, buttonSize, distanceY);
		b5.addActionListener(this);
		add(b5);
		
		t1 = new JTextField("Input here");
		t1.setBounds(gap, distanceY, buttonSize, distanceY);
		t1.addMouseListener(this);
		add(t1);
		
	}
	
	public void setButtonsFalse() {
		b2.setBackground(back);
		b3.setBackground(back);
		b4.setBackground(back);
		b5.setBackground(back);
		
		creatingState = false;
		creatingTransition = false;
		toggleFinal = false;
		deleting = false;
	}
	
	public void createTransitionButtons() {
		if (selT != null) {
			if(!transitionsCreated) {
				transitionsCreated = true;
				
				lTrRead = new JLabel("Read");
				lTrRead.setBounds(gapL,distanceY*5,gap - gapL - distBetw,distanceY);
				
				selTRead = new JTextField(selT.getRead());
				selTRead.setBounds(gap,distanceY*5,buttonSize,distanceY);
				
				lTrWrite = new JLabel("Write");
				lTrWrite.setBounds(gapL,(int) (distanceY*6.5),gap - gapL - distBetw,distanceY);
				
				selTWrite = new JTextField(selT.getWrite());
				selTWrite.setBounds(gap, (int) (distanceY*6.5),buttonSize,distanceY);
				
				lTrDir = new JLabel("Direction");
				lTrDir.setBounds(gapL, distanceY*8,gap - gapL - distBetw,distanceY);
				
				trDir = new JComboBox<String>(dir);
				trDir.setBounds(gap,distanceY*8,(buttonSize - distBetw)/2,distanceY);
				if (selT.getDirection() == dir[0]) {
					trDir.setSelectedIndex(0);
				}
				else if (selT.getDirection() == dir[1]){
					trDir.setSelectedIndex(1);
				}
				else {
					trDir.setSelectedIndex(2);
				}
			
				selTRead.addActionListener(this);
				selTWrite.addActionListener(this);
				trDir.addActionListener(this);
				selTRead.addMouseListener(this);
				selTWrite.addMouseListener(this);
				trDir.addMouseListener(this);
				
				add(selTRead);
				add(selTWrite);
				add(trDir);
				add(lTrRead);
				add(lTrWrite);
				add(lTrDir);
				
				
			}
			else {
				selTRead.setText(selT.getRead());
				selTWrite.setText(selT.getWrite());
				
				if (selT.getDirection() == dir[0]) {
					trDir.setSelectedIndex(0);
				}
				else if (selT.getDirection() == dir[1]){
					trDir.setSelectedIndex(1);
				}
				else {
					trDir.setSelectedIndex(2);
				}
			}
			
		}
		else if(transitionsCreated == true) {
			remove(selTRead);
			remove(selTWrite);
			remove(trDir);
			remove(lTrRead);
			remove(lTrWrite);
			remove(lTrDir);
			
			transitionsCreated = false;
		}
		
	}
	
	public void createStateButtons() {
		if (selected != null) {
			if(!buttonsCreated) {
				buttonsCreated = true;
			
				lname = new JLabel("State name:");
				lname.setBounds(gapL,distanceY*5,gap - gapL - distBetw,distanceY);
				
				selName = new JTextField(selected.getName());
				selName.setBounds(gap,distanceY*5,buttonSize,distanceY);
				
				lxy = new JLabel("Position X, Y:");
				lxy.setBounds(gapL,(int) (distanceY*6.5),gap - gapL - distBetw,distanceY);
				
				selX = new JTextField(Integer.toString(selected.getX()));
				selX.setBounds(gap,(int) (distanceY*6.5),(buttonSize - distBetw)/2,distanceY);			
				
				selY = new JTextField(Integer.toString(selected.getY()));
				selY.setBounds(gap + distBetw + (buttonSize - distBetw)/2,(int) (distanceY*6.5),(buttonSize - distBetw)/2,distanceY);
				
				lradius = new JLabel("Radius:");
				lradius.setBounds(gapL,(int) (distanceY*8),gap - gapL - distBetw,distanceY);
				
				radius = new JTextField(Integer.toString(selected.getRadius()));
				radius.setBounds(gap,(int) (distanceY*8),buttonSize,distanceY);	
				
				lrgb = new JLabel("Red, Green, Blue:");
				lrgb.setBounds(gapL,(int) (distanceY*9.5),gap - gapL - distBetw,distanceY);
				
				selColR = new JTextField(Integer.toString(selected.getR()));
				selColR.setBounds(gap,(int) (distanceY*9.5),(buttonSize - distBetw*2)/3,distanceY);
				
				selColG = new JTextField(Integer.toString(selected.getG()));
				selColG.setBounds(gap + distBetw + (buttonSize - distBetw*2)/3,(int) (distanceY*9.5),(buttonSize - distBetw*2)/3,distanceY);
				
				selColB = new JTextField(Integer.toString(selected.getB()));
				selColB.setBounds(gap + distBetw*2 + ((buttonSize - distBetw*2)/3)*2,(int) (distanceY*9.5),(buttonSize - distBetw*2)/3,distanceY);
				
				lfin = new JLabel("Final, Initial:");
				lfin.setBounds(gapL,(int) (distanceY*11),gap - gapL - distBetw,distanceY);
				
				fState = new JComboBox<String>(bool);
				fState.setBounds(gap,distanceY*11,(buttonSize - distBetw)/2,distanceY);
				if(selected.finalState) {
					fState.setSelectedIndex(1);
				}
				else {
					fState.setSelectedIndex(0);
				}
				
				iState = new JComboBox<String>(bool);
				iState.setBounds(gap + distBetw + (buttonSize - distBetw)/2,distanceY*11,(buttonSize - distBetw)/2,distanceY);
				if(selected.initialState) {
					iState.setSelectedIndex(1);
				}
				else {
					iState.setSelectedIndex(0);
				}
				
	
				selName.addActionListener(this);
				selX.addActionListener(this);
				selY.addActionListener(this);
				radius.addActionListener(this);
				selColR.addActionListener(this);
				selColG.addActionListener(this);
				selColB.addActionListener(this);
				fState.addActionListener(this);
				iState.addActionListener(this);
				selName.addMouseListener(this);
				selX.addMouseListener(this);
				selY.addMouseListener(this);
				radius.addMouseListener(this);
				selColR.addMouseListener(this);
				selColG.addMouseListener(this);
				selColB.addMouseListener(this);
				fState.addMouseListener(this);
				iState.addMouseListener(this);
				
				
				add(lname);
				add(selName);
				add(lxy);
				add(selX);
				add(selY);
				add(lradius);
				add(radius);
				add(lrgb);
				add(selColR);
				add(selColG);
				add(selColB);
				add(lfin);
				add(fState);
				add(iState);
			}
			else {
				selName.setText(selected.getName());
				selX.setText(Integer.toString(selected.getX()));
				selY.setText(Integer.toString(selected.getY()));
				radius.setText(Integer.toString(selected.getRadius()));
				selColR.setText(Integer.toString(selected.getR()));
				selColG.setText(Integer.toString(selected.getG()));
				selColB.setText(Integer.toString(selected.getB()));
				
				if(selected.finalState) {
					fState.setSelectedIndex(1);
				}
				else {
					fState.setSelectedIndex(0);
				}
				
				if(selected.initialState) {
					iState.setSelectedIndex(1);
				}
				else {
					iState.setSelectedIndex(0);
				}
			}
		}
		else if(buttonsCreated == true){
			remove(lname);
			remove(selName);
			remove(lxy);
			remove(selX);
			remove(selY);
			remove(lradius);
			remove(radius);
			remove(lrgb);
			remove(selColR);
			remove(selColG);
			remove(selColB);
			remove(lfin);
			remove(fState);
			remove(iState);
			buttonsCreated = false;
		}
	}

	public void addValues(String s) {
		band = new LinkedList<Box>();
		String temp;
		for (int i = 0; i < s.length(); i++) {
			temp = Character.toString(s.charAt(i));
			Box b = new Box(temp);
			band.addLast(b);
		}
	}
	
	
	public void outPrint(LinkedList<Box> lli) {
		for(Box b : lli) {
			System.out.print(b.getS());
		}
		System.out.println("");
	}
	
	public boolean checkDuplicateInputs(LinkedList<Transition> t) {
		boolean bool = false;
		for (int i = 0; i < t.size() - 1; i++) {
			for (int j = i + 1; j < t.size(); j++) {
				if (t.get(i).getOne() == t.get(j).getOne() && t.get(i).getRead().equals(t.get(j).getRead())) {
					bool = true;
					break;
				}
			}
		}
		return bool;
	}
	
	public void initiateLogic() {
		if(allStates.size() != 0 && allTransitions.size() != 0) {
			if (band.size() == 0) {
				System.out.println("no input detected");
				return;
			}
			State temp = null;
			for(State st : allStates) {
				if(st.getInitialState())
					temp = st;
					break;
			}
			if(temp == null) {
				System.out.println("no Intitial State detected");
				return;
			}
			if (checkDuplicateInputs(allTransitions)) {
				System.out.println("invalid TM");
			}
			else {
				logic(temp, 0);
			}
			
		}
		else {
			System.out.println("no States or Transitions detected");
		}
	}
	
	public int moveLeft(int boxIndex) {
		boxIndex--;
		if(boxIndex < 0) {
			band.push(new Box("$"));
			boxIndex = 0;
		}
		return boxIndex;
	}
	
	public int moveRight(int boxIndex) {
		boxIndex++;
		if(boxIndex > band.size() - 1) {
			band.addLast(new Box("$"));
		}
		return boxIndex;
	}
	
	public LinkedList<Box> copyList(LinkedList<Box> llist) {
		LinkedList<Box> templist = new LinkedList<Box>();
		int i = 0;
		while (i < llist.size()) {
			Box tempbox = new Box(llist.get(i).getS());
			templist.addLast(tempbox);
			i++;
		}
		return templist;
	}
	
	public boolean isFinished(State temp, int index) {
		boolean done = true;
		String s = band.get(index).getS();
		for (Transition t : allTransitions) {
			if(t.getOne() == temp) {
				if(s.equals(t.getRead())) {
					done = false;
					break;
				}
				
			}
		}
		return done;
	}
	
	public void logic(State temp, int boxIndex) {
		for(Transition tr : allTransitions) {
			if(tr.getOne() == temp) {
				if(band.get(boxIndex).getS().equals(tr.getRead())) {
					band.get(boxIndex).setS(tr.getWrite());
					if(tr.getDirection().equals("L")) {
						logic(tr.getTwo(),moveLeft(boxIndex));
					}
					else if (tr.getDirection().equals("R")){
						logic(tr.getTwo(),moveRight(boxIndex));
					}
					else if (tr.getDirection().equals("N")){
						logic(tr.getTwo(),boxIndex);
					}
					break;
				}
			}
		}
		if (isFinished(temp, boxIndex)) {
			if (temp.getFinalState()) {
				System.out.println("Correct End in Final");
				outPrint(band);
			}
			else if (!temp.getFinalState()){
				System.out.println("Did not end in a Final State" + " " + temp.getName());
				outPrint(band);
			}
		}
	}
	
	public State statePressed(MouseEvent e) {
		State newSt = null;
		if(allStates.size() != 0) {
			for(int i = 0; i < allStates.size(); i++) {
				if(allStates.get(i).circle.contains(e.getX(),e.getY())) {
					newSt = allStates.get(i);
					if(!deleting && !toggleFinal) {
						setButtonsFalse();
					}
					dragged = true;
					return newSt;
				}
			}
		}
		return newSt;
	}
	
	public Transition transitionPressed(MouseEvent e) {
		Transition tempTr = null;
		if(allTransitions.size() != 0) {
			for(Transition tr : allTransitions) {
				if(tr.rect.contains(e.getX(),e.getY())) {
					tempTr = tr;
					return tempTr;
				}
			}
		}
		return tempTr;
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		setBackground(new Color(23,23,23));
		if(allTransitions.size() != 0) {
			for(Transition tr : allTransitions) {
				g2.setColor(fore);
				g2.drawLine(tr.xOne, tr.yOne, tr.xTwo, tr.yTwo);
				tr.show(g2);
			}
		}
		if(allStates.size() != 0) {
			for(State st : allStates) {
				st.show(g2);
			}
		}
		if(initialExists) {
			State temp = null;
			for(State st : allStates) {
				if(st.getInitialState()) {
					temp = st;
				}
			}
			g2.setColor(labcol);
			g2.setFont(f);
			g2.drawString("Initial State:" + " " + temp.getName(),gap, distanceY*13);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) {
			input = t1.getText();
			addValues(input);
			initiateLogic();
			clickedt1 = false;
		}
		
		else if (e.getSource() == b2) {
			if(creatingState) {
				creatingState = false;
				b2.setBackground(back);
			}
			else {
				setButtonsFalse();
				creatingState = true;
				b2.setBackground(back2);
			}
		}
		
		else if (e.getSource() == b3) {
			if(creatingTransition) {
				creatingTransition = false;
				b3.setBackground(back);
			}
			else {
				setButtonsFalse();
				creatingTransition = true;
				b3.setBackground(back2);
			}
		}
		
		else if (e.getSource() == b4) {
			if(toggleFinal) {
				toggleFinal = false;
				b4.setBackground(back);
			}
			else {
				setButtonsFalse();
				toggleFinal = true;
				b4.setBackground(back2);
			}
		}
		
		else if (e.getSource() == b5) {
			if(deleting) {
				deleting = false;
				b5.setBackground(back);
			}
			else {
				setButtonsFalse();
				deleting = true;
				selected = null;
				b5.setBackground(back2);
			}
		}
		
		if (selected != null && allStates.size() != 0) {
			if (e.getSource() == iState) {
				if(iState.getSelectedItem() == "true") {
					if(!selected.initialState && !initialExists) {
						selected.setInitialState(true);
						initialExists = true;
					}
				}
				
				else {
					 if(selected.initialState && initialExists) {
						selected.setInitialState(false);
						initialExists = false;
					 }
				}
			}
			
			else if (e.getSource() == fState) {
				if(fState.getSelectedItem() == "true") 
					selected.setFinalState(true);
				else 
					selected.setFinalState(false);
				
			}
			
			else if (e.getSource() == radius) {
				selected.setRadius(Integer.parseInt(radius.getText()));
				selected.setX(checkDrawX(selected.getX(), selected.getRadius()));
				selected.setY(checkDrawY(selected.getY(), selected.getRadius()));
			}
			
			else if (e.getSource() == selX) {
				int x = Integer.parseInt(selX.getText());
				selected.setX(checkDrawX(x, selected.getRadius()));
			}
			
			else if (e.getSource() == selY) {
				int y = Integer.parseInt(selY.getText());
				selected.setY(checkDrawY(y, selected.getRadius()));
			}
			
			else if (e.getSource() == selColR) {
				int r = Integer.parseInt(selColR.getText());
				if(r > 255) {
					r = 255;
				}
				selected.updateColor(r, selected.getG(),selected.getB());
			}
			
			else if (e.getSource() == selColG) {
				int gg = Integer.parseInt(selColG.getText());
				if(gg > 255) {
					gg = 255;
				}
				selected.updateColor(selected.getR(), gg, selected.getB());
			}
			
			else if (e.getSource() == selColB) {
				int b = Integer.parseInt(selColB.getText());
				if(b > 255) {
					b = 255;
				}
				selected.updateColor(selected.getR(), selected.getG(), b);
			}
			
			else if (e.getSource() == selName) {
				selected.setName(checkStateName(selName.getText()));
			}
			
			//allStates.set(selectedIndex, selected);		//Funktioniert von alleine. Wahrscheinlich weil selected einen Pointer zu dem LinkedList item ist
			
		}
		
		if(selT != null && allTransitions.size() != 0) {
			if(e.getSource() == selTRead) {
				boolean exists = false;
				selT.setRead(selTRead.getText());
				for(Transition tr : allTransitions) {
					if(tr.getRead() == selTRead.getText()) {
						exists = true;
					}
				}
				if(!exists)
					inputAlph.push(selT.getRead());
			}
			
			else if(e.getSource() == selTWrite) {
				boolean exists = false;
				selT.setWrite(selTWrite.getText());
				for(Transition tr : allTransitions) {
					if(tr.getWrite() == selTWrite.getText()) {
						exists = true;
					}
				}
				if(!exists)
					outputAlph.push(selT.getWrite());
			}
			else if(e.getSource() == trDir) {
				if(trDir.getSelectedItem() == dir[0]) {
					selT.setDirection(dir[0]);
				}
				else if(trDir.getSelectedItem() == dir[1]) {
					selT.setDirection(dir[1]);
				}
				else if(trDir.getSelectedItem() == dir[2]) {
					selT.setDirection(dir[2]);
				}
			}
		}
		
		
		repaint();
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == t1) {
			if(!clickedt1) {
				clickedt1 = true;
				t1.setText("");
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getSource() == t1 || e.getSource() == selX || e.getSource() == selY || e.getSource() == selName || e.getSource() == radius || e.getSource() == selColR || e.getSource() == selColG || e.getSource() == selColB || e.getSource() == iState || e.getSource() == fState || e.getSource() == selTRead || e.getSource() == selTWrite || e.getSource() == trDir) {
			setButtonsFalse();
		}
		
		else if(deleting) {
			
			if(allStates.size() != 0) {
				selected = statePressed(e);
				if(selected == null) {
					setButtonsFalse();
				}
				else {
					int i = allStates.indexOf(selected);
					if(i >= 0) {
						if(selected.getInitialState()) {
							initialExists = false;
						}
						LinkedList<Transition> toRemove = new LinkedList<Transition>();
						for(Transition tr : allTransitions) {
							if (selected == tr.one || selected == tr.two) {
								toRemove.add(tr);
							}
						}
						allTransitions.removeAll(toRemove);
						allStates.remove(i);
						selected = null;
						dragged = false;
						repaint();
					}
				}
			}
			if(allTransitions.size() != 0) {
				selT = transitionPressed(e);
				if(selT != null) {
					deleting = true;
					b5.setBackground(back2);
				}
				allTransitions.remove(selT);
				selT = null;
				createTransitionButtons();
			}
			createStateButtons();
		}
		
		else if(toggleFinal) {
			selected = statePressed(e);
			if(selected != null) {
				if(selected.getFinalState()) {
					selected.setFinalState(false);
				}
				else {
					selected.setFinalState(true);
				}
			}
			else {
				setButtonsFalse();
			}
			createStateButtons();
			
		}
		
		else if(creatingTransition) {
			if(selected == null) {
				creatingTransition = false;
				setButtonsFalse();
				createStateButtons();
			}
			else {
				State temp;
				temp = statePressed(e);
				if(temp == null) {
					creatingTransition = false;
					setButtonsFalse();
					createStateButtons();
					return;
				}
				selT = createTransition(selected,temp);
				allTransitions.addLast(selT);
				creatingTransition = false;
				repaint();
			}
			
		}
		
		else {
		selected = statePressed(e);
		selT = transitionPressed(e);
		createStateButtons();
		createTransitionButtons();			
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		dragged = false;
		if (creatingState) {
			if(initialExists) {
				State temp;
				temp = createState(e.getX(), e.getY(), false);
				allStates.addLast(temp);
			}
			else {
				State temp;
				temp = createState(e.getX(), e.getY(), true);
				allStates.addLast(temp);
				initialExists = true;
			}
			selected = allStates.getLast();
			createStateButtons();
		}
		repaint();
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(dragged) {
			selected.setX(checkDrawX(e.getX(), selected.getRadius()));
			selected.setY(checkDrawY(e.getY(), selected.getRadius()));
			selX.setText(Integer.toString(selected.getX()));
			selY.setText(Integer.toString(selected.getY()));
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}
