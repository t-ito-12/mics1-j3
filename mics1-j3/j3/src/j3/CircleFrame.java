package j3;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;


class CirclePanel extends JPanel implements MouseListener {
  private int   radius = 5;
  private Color color  = Color.red;
  private int   x[],y[],r[];
  private Color c[];
  private int   num=0;
  final static int MAX=5000;
  
  public CirclePanel(){
    x=new int[MAX];
    y=new int[MAX];
    r=new int[MAX];
    c=new Color[MAX];
    this.addMouseListener(this);
    this.setRadius(radius);
    this.setPanelColor(color);
  }
  
  public void setRadius(int r){
	  radius = r;
  }
  public void setPanelColor(Color c){
	  color = c;
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    for(int i=0;i<num;i++){
      g.setColor(c[i]);
      g.fillOval(x[i]-r[i],y[i]-r[i],r[i]*2,r[i]*2);
    }
    
  }
  private void addCircle(int x0,int y0){
    if (num>=MAX) return; 
    x[num]=x0; y[num]=y0;
    r[num]=radius; c[num]=color;
    num++;
    this.repaint();
  }
     
  public void mousePressed(MouseEvent e) {
     addCircle(e.getX(),e.getY());
  }
  public void mouseClicked(MouseEvent e) { }
  public void mouseReleased(MouseEvent e){ }
  public void mouseEntered(MouseEvent e) { }
  public void mouseExited(MouseEvent e)  { }
}

class CircleFrame extends JFrame implements ActionListener, ChangeListener{
   private CirclePanel panel;
   // JPanelに貼り付けるためのJButtonを5つ生成
   JButton b1=new JButton("Red"),b2=new JButton("Blue");
   JButton b3=new JButton("Green"),b4=new JButton("10");
   JButton b5=new JButton("5");
   JSlider s1;
   public CircleFrame(){
      this.setTitle("CircleFrame");
      this.setSize(1000,500);
      s1 = new JSlider();
      panel=new CirclePanel();
      this.add(panel);
   // JPanelを２つ生成
	    JPanel  p1=new JPanel(),p2=new JPanel(), p3 = new JPanel();
	   this.setTitle("Panel Combination");
	    // 2つのJPanelをそれぞれ，3x1, 2x1 のGridLayoutに設定．
	    p1.setLayout(new GridLayout(3,1));
	    p2.setLayout(new GridLayout(2,1));
	    // ボタンをそれぞれのJPanelに貼付け
	    p1.add(b1); p1.add(b2); p1.add(b3);
	    p2.add(b4); p2.add(b5);
	    p3.add(s1);
	     
	    // JButtonが3つ張り付いたJPanelを左，
	    // JButtonが2つ張り付いたJPanelを右に貼り付ける．
	    this.add(p1,BorderLayout.WEST);
	    this.add(p2,BorderLayout.EAST);
	    this.add(p3,BorderLayout.NORTH);
	    b1.addActionListener(this);
	    b2.addActionListener(this);
	    b3.addActionListener(this);
	    b4.addActionListener(this);
	    b5.addActionListener(this);
	    s1.addChangeListener(this);
	    
	   
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setVisible(true);
	    }
   	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==b1) panel.setPanelColor(Color.red);
		else if(e.getSource()==b2) panel.setPanelColor(Color.blue);
		else if(e.getSource()==b3) panel.setPanelColor(Color.green);
		else if(e.getSource()==b4) panel.setRadius(10);
		else if(e.getSource()==b5) panel.setRadius(5);
	}
   	public void stateChanged(ChangeEvent e) {
   	    panel.setRadius(s1.getValue());
   	}
   	
    public static void main(String argv[]) {
      new CircleFrame();  
   }
}
