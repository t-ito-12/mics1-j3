package j3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
 
class HelloFrame extends JFrame implements ActionListener { 
    private JLabel label, count, pic;
    int g = 0;
    int s = 0;
    int b = 0;
    
    public HelloFrame(){
      this.setSize(400,200);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      JButton b = new JButton("おみくじ");
      this.add(b,BorderLayout.SOUTH);
      label = new JLabel(" ",JLabel.CENTER);
      count = new JLabel(" ",JLabel.CENTER);
      pic = new JLabel(" ",JLabel.CENTER);
      this.add(label,BorderLayout.WEST);
      this.add(pic,BorderLayout.CENTER);
      this.add(count,BorderLayout.NORTH);
      b.addActionListener(this);
      this.setVisible(true);
    }
    public void actionPerformed(ActionEvent ev) {
    	double r = Math.random();
    	if(r > 0.7){
    		label.setText("good");
    		//pic.setText("<html><img src=http://www.d-beam.net/bkst/2008/2008-04-07-2_files/0009-f6de5.jpg  width=100 height=100></html>");
    		g++;
    	}else if(r > 0.2){
    		label.setText("so so");
    		s++;
    		//pic.setText("<html><img src=http://cdn-ak.f.st-hatena.com/images/fotolife/t/tosen0302/20160518/20160518223334.jpg  width=100 height=100></html>");
    	}else{
    		label.setText("bad");
    		b++;
    		//pic.setText("<html><img src=http://sublo.ocnk.net/data/sublo/product/1ce212c77d.jpg width=100 height=100></html>");
    	}
    	count.setText("good:" + String.valueOf(100*g/(g + s + b)) + "%  so so:" + String.valueOf(100*s/(g + s + b))
    			+ "%  bad:" + String.valueOf(100*b/(g + s + b)) + "%  試行回数:" + String.valueOf(g + s + b));
    }
    public static void main(String argv[]) {
      new HelloFrame();
    }
}