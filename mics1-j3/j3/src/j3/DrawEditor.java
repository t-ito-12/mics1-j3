package j3;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

// 描画した図形を記録する Figure クラス (継承して利用する)
class Figure {
  protected int x,y,width,height,nowx,nowy,prex,prey,radius;
  protected Color color;
  public Figure(int x,int y,int w,int h,Color c, int nx, int ny, int px, int py, int r) {
    this.x = x; this.y = y;
    width = w; height = h;
    color = c;
    nowx = nx; nowy = ny;
    prex = px; prey = py;
    radius = r;
  }
  public void setSize(int w,int h) {
    width = w; height = h;
  }
  public void setLocation(int x,int y) {
    this.x = x; this.y = y;
  }
  public void setnow(int x, int y){
	  nowx = x; nowy = y;
  }
  public void setprev(int x, int y){
	  prex = x; prey = y;
  }
  public void setRadius(int r){
	  radius = r;
  }
  /* 位置と大きさ、マウスドラッグの際の押しはじめの位置と、現在の
      マウスの位置を変更するメソッド */
  public void reshape(int x1,int y1,int x2,int y2) {
    int newx = Math.min(x1,x2);
    int newy = Math.min(y1,y2);
    int neww = Math.abs(x1-x2);
    int newh = Math.abs(y1-y2);
    setLocation(newx,newy);
    setSize(neww,newh);
    setnow(x2, y2);
    setprev(x1,y1);
  }
  public void draw(Graphics g) {}
}
/* フリーハンド */
class FreeFigure extends Figure   {
	private Stroke STROKE = new BasicStroke(
		    radius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	private List<Shape> list;
	public FreeFigure(int x,int y,int w,int h,Color c, int nx, int ny, int px, int py, int r,
			List<Shape> list){
		super(x,y,w,h,c,nx,ny,px,py,r);
		this.list = list;
	}
	/* 自由曲線を描画 */
	public void draw(Graphics g) {
	    	if (list != null) {
	    		Graphics2D g2 = (Graphics2D) g.create();
	    		g2.setStroke(STROKE);
	    		for (Shape s : list) {
	    			g2.setPaint(color);
	    			g2.draw(s);
	    		}
	    		g2.dispose();
	    	}
	}
}
/* 長方形 */
class RectangleFigure extends Figure {
  public RectangleFigure(int x,int y,int w,int h,Color c, int nx, int ny, int px, int py, int r) {
    super(x,y,w,h,c,nx,ny,px,py,r);
  }
  /* 長方形を描画 */
  public void draw(Graphics g) {
	  Graphics2D g2 = (Graphics2D)g;
	  g2.setColor(color);
	  g2.setStroke(new BasicStroke(radius));
    g2.draw(new Rectangle2D.Double(x, y, width, height));
  }
}
/* 楕円 */
class CircleFigure extends Figure{
	public CircleFigure(int x,int y,int w,int h,Color c, int nx, int ny, int px, int py, int r){
		super(x,y,w,h,c,nx,ny,px,py,r);
	}
	/* 楕円を描画 */
	public void draw(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		  g2.setColor(color);
		  g2.setStroke(new BasicStroke(radius));
		  g2.drawOval(x, y, width, height);
	}
}
/* 直線 */
class LineFigure extends Figure{
	public LineFigure(int x,int y,int w,int h,Color c, int nx, int ny, int px, int py, int r){
		super(x,y,w,h,c,nx,ny,px,py,r);
	}
	/* 直線を描画 */
	public void draw(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		  g2.setColor(color);
		  g2.setStroke(new BasicStroke(radius));
		int nx = nowx;
		int ny = nowy;
		int x0 = prex;
		int y0 = prey;
		g2.drawLine(x0, y0, nx, ny);
	}
}

////////////////////////////////////////////////
// Model (M)

// modelは java.util.Observableを継承する．Viewに監視される．
class DrawModel extends Observable{
  protected ArrayList<Figure> fig;
  protected Figure drawingFigure;
  protected Color currentColor;
  public DrawModel() {
    fig = new ArrayList<Figure>();
    drawingFigure = null;
    currentColor = Color.BLACK;  // 初期色
  }
  /* 図形を記録している ArrayList を返すメソッド */
  public ArrayList<Figure> getFigures() {
    return fig;
  }
  /* 何番目かを指定して図形を取り出すメソッド */
  public Figure getFigure(int idx) {
    return fig.get(idx);
  }
  /* 操作中の図形の形を変更するメソッド */
  public void reshapeFigure(int x1,int y1,int x2,int y2) {
	      drawingFigure.reshape(x1,y1,x2,y2);
	      setChanged();
	      notifyObservers();
	    }
  /* 新たに図形を作って追加するメソッド */
  public void createFigure(int x,int y, int type, int r, int mx, int my, List<Shape> list) {
    Figure f = new RectangleFigure(x,y,0,0,currentColor,x,y,x,y,r);
	switch(type){
	case 0:
		/* フリーハンド */
		f = new FreeFigure(x,y,0,0,currentColor,x,y,x,y,r,list);
    	break;
	case 1:
    	/* 四角 */
    	f = new RectangleFigure(x,y,0,0,currentColor,x,y,x,y,r);
    	break;
    case 2:
    	/* 楕円 */
    	f = new CircleFigure(x, y, 0, 0, currentColor,x,y,x,y,r);
    	break;
    case 3:
    	/* 直線 */
    	f = new LineFigure(x, y, 0, 0, currentColor,x,y,x,y,r);
    	break;
    }
	fig.add(f);
    drawingFigure = f;
    setChanged();
    notifyObservers();
  }

}

////////////////////////////////////////////////
// View (V)

// Viewは，Observerをimplementsする．Modelを監視して，
// モデルが更新されたupdateする．実際には，Modelから
// update が呼び出される．
class ViewPanel extends JPanel implements Observer{
	private static final long serialVersionUID = 1L;
	protected DrawModel model;
	public ViewPanel(DrawModel m,DrawController c) {
	this.setBackground(Color.white);
    this.addMouseListener(c);
    this.addMouseMotionListener(c);
    model = m;
    model.addObserver(this);
  }
  /* DrawModel が記録しているすべての Figure を描画する。*/
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    ArrayList<Figure> fig = model.getFigures();
    for(int i = 0; i < fig.size(); i++) {
      Figure f = fig.get(i);
      f.draw(g);
    }
  }
  /* 再描画 */
  public void update(Observable o,Object arg){
    repaint();
  }
}
/* JPanel を拡張 */
class exJPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	protected int x,y,r;
	protected Color color;

	public exJPanel(int x, int y, int r, Color color){
      this.setBackground(Color.WHITE);
      this.x = x; this.y = y; this.r= r;
      this.color = color;

    }
    /* 円を描画するメソッド */
    public void paintComponent( Graphics g ){
      super.paintComponent(g);
      if(color == Color.WHITE){
    	  g.setColor(Color.BLACK);
          g.drawOval(x, y, r, r);
      }else{
    	  g.setColor(color);
    	  g.fillOval(x, y, r, r);
      }
    }
    /* x,y を変更するメソッド */
    public void changeposition(int x, int y){
    	this.x = x; this.y = y;
    }
    /* r と color を変更するメソッド */
    public void addCircle(int r, Color color){
    	this.r = r; this.color = color;
    	this.repaint();
    }
    /* gを破棄するメソッド */
  }

//////////////////////////////////////////////////
// main class
//   (GUIを組み立てているので，view の一部と考えてもよい)
class DrawFrame extends JFrame implements ActionListener, ChangeListener{
  private static final long serialVersionUID = 1L;
  Stack<Figure> stack;
  DrawModel model;
  ViewPanel view;
  DrawController cont;
  Color nowcolor;
  JMenuBar menubar = new JMenuBar();
  exJPanel panel;
  JSlider s1;
  JLabel label;
  Graphics g;
  JMenu menu1 = new JMenu("ツール");
  JMenu menu2 = new JMenu("図形");
  JMenuItem menuitem1_1 = new JMenuItem("色");
  JMenuItem menuitem2_1 = new JMenuItem("手書き");
  JMenuItem menuitem2_2 = new JMenuItem("四角形");
  JMenuItem menuitem2_3 = new JMenuItem("楕円");
  JMenuItem menuitem2_4 = new JMenuItem("直線");

  JButton b1=new JButton("ペン");
  JButton b2=new JButton("消しゴム");
  JButton b3 = new JButton("←戻る");
  JButton b4 = new JButton("進む→");


   public DrawFrame(){
	  stack = new Stack<Figure>();
      model=new DrawModel();
      cont =new DrawController(model);
      view=new ViewPanel(model,cont);
      panel=new exJPanel(50,20,cont.radius,model.currentColor);
      nowcolor = model.currentColor;
      s1 = new JSlider(1,100,5);
      s1.setMinorTickSpacing(5);
      s1.setPaintTicks(true);
      JPanel p1 = new JPanel();
      JPanel p2 = new JPanel();
      label = new JLabel();
      label.setText("太さ:" + s1.getValue());
      this.add(view);
      p1.add(b3);
      p1.add(b4);
      p2.setLayout(new GridLayout(5, 5));
      p2.add(label);
      p2.add(panel);
      p2.add(s1);
      p2.add(b1);
      p2.add(b2);
      this.add(p1,BorderLayout.NORTH);
      this.add(p2,BorderLayout.WEST);
      this.setBackground(Color.black);
      this.setTitle("Draw Editor");
      this.setSize(800,800);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      /* メニューバー */
      menu1.setFont(new Font("Dialog", Font.PLAIN, 20));
      menu2.setFont(new Font("Dialog", Font.PLAIN, 20));
      menubar.add(menu1);
      menubar.add(menu2);
      /* サブメニューバー */
      menuitem1_1.setFont(new Font("Dialog", Font.PLAIN, 20));
      menuitem2_1.setFont(new Font("Dialog", Font.PLAIN, 20));
      menuitem2_2.setFont(new Font("Dialog", Font.PLAIN, 20));
      menuitem2_3.setFont(new Font("Dialog", Font.PLAIN, 20));
      menuitem2_4.setFont(new Font("Dialog", Font.PLAIN, 20));
      menu1.add(menuitem1_1);
      menu2.add(menuitem2_1);
      menu2.add(menuitem2_2);
      menu2.add(menuitem2_3);
      menu2.add(menuitem2_4);
      setJMenuBar(menubar);
      menuitem1_1.addActionListener(this);
      menuitem2_1.addActionListener(this);
      menuitem2_2.addActionListener(this);
      menuitem2_3.addActionListener(this);
      menuitem2_4.addActionListener(this);
      b1.addActionListener(this);
      b2.addActionListener(this);
      b3.addActionListener(this);
      b4.addActionListener(this);
      
      s1.addChangeListener(this);
      this.setVisible(true);
    }
   /* スライダー */
	public void stateChanged(ChangeEvent e) {
		cont.radius = s1.getValue();
		label.setText("太さ:" + s1.getValue());
		panel.addCircle(cont.radius, model.currentColor);
	}
	/* ボタン */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==menuitem1_1){
			 Color color = JColorChooser.showDialog(this, "色の選択", Color.white);

			    if(color != null){
			    	model.currentColor = color;
			    	panel.addCircle(cont.radius, model.currentColor);
			    }
			  }
		else if(e.getSource()==menuitem2_1){ cont.f_type = 0;}
		else if(e.getSource()==menuitem2_2){ cont.f_type = 1;}
		else if(e.getSource()==menuitem2_3){ cont.f_type = 2;}
		else if(e.getSource()==menuitem2_4){ cont.f_type = 3;}
		else if(e.getSource()==b1){
			model.currentColor = nowcolor;
			cont.f_type = 0;
			panel.addCircle(cont.radius, model.currentColor);
			}
		else if(e.getSource()==b2){
			if(model.currentColor != Color.WHITE) nowcolor = model.currentColor;
			model.currentColor = Color.WHITE;
			panel.addCircle(cont.radius, model.currentColor);
			}
		else if(e.getSource()==b3){
			if(model.fig.size() > 0){
				stack.push(model.fig.get(model.fig.size() - 1));
				model.fig.remove(model.fig.size() - 1);
				repaint();
			}
			}
		else if(e.getSource() == b4){
			if(stack.empty() != true){
				model.fig.add(stack.pop());
				repaint();
			}
		}
		}
	/* main */
	public static void main(String argv[]) {
	      new DrawFrame();
	   }
}

////////////////////////////////////////////////
// Controller (C)

class DrawController extends JPanel implements MouseListener,MouseMotionListener {
  private static final long serialVersionUID = 1L;
  private transient List<Shape> list;
  private transient Path2D path;
  protected DrawModel model;
  protected int dragStartX,dragStartY;
  protected int f_type = 0;
  protected int radius = 5;

  public DrawController(DrawModel a) {
    model = a;
  }
  public void mouseClicked(MouseEvent e) {}
  /* マウスが押されたとき */
  public void mousePressed(MouseEvent e) {
	  list = new ArrayList<Shape>();
		path = new Path2D.Double();
		list.add(path);
	  Point p = e.getPoint();
    path.moveTo(p.x, p.y);
	dragStartX = e.getX(); dragStartY = e.getY();
    model.createFigure(dragStartX,dragStartY, f_type, radius, p.x, p.y, list);
  }
  /* マウスがドラッグされたとき */
  public void mouseDragged(MouseEvent e) {
	  Point p = e.getPoint();
      path.lineTo(p.x, p.y);
	  model.reshapeFigure(dragStartX,dragStartY,e.getX(),e.getY());
  }
  public void mouseReleased(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseMoved(MouseEvent e) {}
}