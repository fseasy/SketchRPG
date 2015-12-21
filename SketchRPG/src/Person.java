import javax.swing.* ;

import java.awt.* ;
import java.net.URL;

public class Person {
	
	public static final int PERSON_WIDTH = 45 ;
	public static final int PERSON_HEIGHT = 45 ;
	
	// the postion at the map !!
	protected int xPos = 0 ;
	protected int yPos = 0 ;
	
	private Image imgU0 = null ;
	private Image imgU1 = null ;
	private Image imgD0 = null ;
	private Image imgD1 = null ;
	private Image imgL0 = null ;
	private Image imgL1 = null ;
	private Image imgR0 = null ;
	private Image imgR1 = null ;
	
	public Image imgCur = null ;
	private int steps = 0 ;
	public int stepLen = 10 ;

	public final static int L = 0 ;
	public final static int R = 1 ;
	public final static int U = 2 ;
	public final static int D = 3 ;
	public Person(){
		super() ;
		ClassLoader curClassLoader = this.getClass().getClassLoader();
		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		
		URL urlU0 = curClassLoader.getResource("res/img/U0.png") ;
		imgU0 = defaultToolkit.getImage(urlU0) ;
		
		URL urlU1 = curClassLoader.getResource("res/img/U1.png") ;
		imgU1 = defaultToolkit.getImage(urlU1) ;
		
		URL urlD0 = curClassLoader.getResource("res/img/D0.png") ;
		imgD0 = defaultToolkit.getImage(urlD0) ;
		
		URL urlD1 = curClassLoader.getResource("res/img/D1.png") ;
		imgD1 = defaultToolkit.getImage(urlD1) ;
		
		URL urlR0 = curClassLoader.getResource("res/img/R0.png") ;
		imgR0 = defaultToolkit.getImage(urlR0) ;
		
		URL urlR1 = curClassLoader.getResource("res/img/R1.png") ;
		imgR1 = defaultToolkit.getImage(urlR1) ;
		
		URL urlL0 = curClassLoader.getResource("res/img/L0.png") ;
		imgL0 = defaultToolkit.getImage(urlL0) ;
		
		URL urlL1 = curClassLoader.getResource("res/img/L1.png") ;
		imgL1 = defaultToolkit.getImage(urlL1) ;

		setIcon(imgL0) ;
	}
	public void nextStep(int d ){
		steps = (++ steps ) % 2 ; 
		if(d == D){
			if(steps % 2 == 0) setIcon(imgD0) ;
			else setIcon(imgD1) ;
		}
		else if(d == U){
			if(steps % 2 == 0) setIcon(imgU0) ;
			else setIcon(imgU1) ;
		}
		else if(d == R){
			if(steps%2 == 0) setIcon(imgR0) ;
			else setIcon(imgR1) ;
		}
		else{
			if(steps%2 == 0) setIcon(imgL0) ;
			else setIcon(imgL1) ;
		}
	}
	public void setIcon(Image cur){
		imgCur = cur ;
	}
	public void setPosition(int x , int y){
		xPos = x ;
		yPos = y ;
	}
	public int getX(){
		return xPos ;
	}
	public int getY(){
		return yPos ;
	}
}
