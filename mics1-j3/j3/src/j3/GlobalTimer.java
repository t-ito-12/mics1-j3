package j3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class TimeObservable extends Observable implements ActionListener {
	  private javax.swing.Timer timer;
	  private int hour, min, sec; // 現在時刻を保持するインスタンス変数
	  public TimeObservable() {
	    timer = new javax.swing.Timer(1000, this); // 1秒毎にactionPerformedを呼び出し
	    timer.start();
	  }
	  public void actionPerformed(ActionEvent arg0) {
	    this.setValue();
	    setChanged();
	    notifyObservers();
	  }
	  void setValue()  {
		  Calendar cal = Calendar.getInstance(); 
		  hour= cal.get(Calendar.HOUR_OF_DAY); 
		  min = cal.get(Calendar.MINUTE); 
		  sec = cal.get(Calendar.SECOND); 
	  }
	  String getValue(int diff) {
		  return String.format("%02d:%02d:%02d",(hour+(diff)+24)%24 ,min,sec);
	  }
}
class ClockPanel extends JPanel implements Observer {
	  private TimeObservable timeObservable;
	  private int diff;
	  private JLabel timeLabel;
	  public ClockPanel(TimeObservable to, String place, int diff) {
	    timeObservable = to;
	    this.diff = diff;
	    timeObservable.addObserver(this);
	    timeLabel = new JLabel(to.getValue(diff), JLabel.CENTER);
	    this.setLayout(new GridLayout(1,2));
	    this.add(new JLabel(place));
	    this.add(timeLabel);
	  }
	  public void update(Observable o, Object arg) {
	    timeLabel.setText(timeObservable.getValue(diff));
	  }
	}
class ClockFrame extends JFrame{
	private ClockPanel Tokyo, Beijing, Paris, London, NewYork, LosAngels;
	private TimeObservable Time;
	
	public ClockFrame(){
		Time = new TimeObservable();
		Tokyo = new ClockPanel(Time, "Tokyo", 0);
		Beijing = new ClockPanel(Time, "Beijing", -1);
		Paris = new ClockPanel(Time, "Paris", -8);
		London = new ClockPanel(Time, "London", -9);
		NewYork = new ClockPanel(Time, "New York", -14);
		LosAngels = new ClockPanel(Time, "Los Angels", -17);
		this.setLayout(new GridLayout(6, 1));
		this.add(Tokyo);
		this.add(Beijing);
		this.add(Paris);
		this.add(London);
		this.add(NewYork);
		this.add(LosAngels);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		
	}
	public static void main(String[] args) {
	      new ClockFrame();  
	  }
}
