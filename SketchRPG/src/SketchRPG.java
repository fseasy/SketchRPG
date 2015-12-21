import javax.swing.* ;

import java.awt.* ;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SketchRPG extends JFrame implements KeyListener{
	public final static int WINDOW_WIDTH = 450 ;
	public final static int WINDOW_HEIGHT = 300  ;
	private JPanel mainPanel = null ;
	protected BGMap bgmap = null ;
	protected Person person = null ;
	protected WordsPanel wordsPanel = null ;
	
	//map position
	private int offsetX = 0 ;
	private int offsetY = 0 ;
	//person position at the map
	private int personX = 0 ;
	private int personY = 0 ;
	
	public final static int GAME_STATE_MAP = 0 ;
	public final static int GAME_STATE_TALK = 1 ;
	public final static int GAME_STATE_OVER = 2 ;
	
	private int gameState = GAME_STATE_MAP ; 
	public SketchRPG(){
		super("SketchRPG") ;
		mainPanel = new JPanel() ;
		mainPanel.setLayout(null);
		mainPanel.setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
		
		setContentPane(mainPanel) ;
		setBounds(400,300,WINDOW_WIDTH , WINDOW_HEIGHT) ;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;
		pack() ;
		setVisible(true) ;
		loading() ;
		loadMap() ;
	}
	public void loading(){
		wordsPanel = new WordsPanel("SketchRPG\n描述了一个少年寻找更广阔世界入口的故事。\n"
									+ "虽然游戏情节简单，但真实世界中却又是何其的困难。\n"
									+ "不断努力，才能看到更加开阔的世界吧\n"
									+ "by 11103101 徐伟\n") ;
		setContentPane(wordsPanel) ;
		try{
			Thread.sleep(3000) ;
		}
		catch(Exception e){
			e.printStackTrace() ;
		}
		wordsPanel.setSource("使用方向键来行走，使用‘w’键来触发情景\n有问题麻烦老师联系\n18004515446") ;
		wordsPanel.next() ;
		setContentPane(wordsPanel) ;
		try{
			Thread.sleep(3000) ;
		}
		catch(Exception e){
			e.printStackTrace() ;
		}
		setContentPane(mainPanel) ;
		wordsPanel = null ;
	}
	public void loadMap(){
		person = new Person() ;
		bgmap = new BGMap(person) ;
		wordsPanel = new WordsPanel() ;
		//set init position
		personY = 420 ;
		personX = 80 ;
		person.setPosition(personX , personY );
		mainPanel.removeAll();
		mainPanel.add(bgmap) ;
		offsetY = bgmap.getWindowOffsetY() ;
		bgmap.setBounds(offsetX , offsetY , BGMap.BGMAP_WIDTH , BGMap.BGMAP_HEIGHT);
		repaint() ;
		addKeyListener(this) ;
	}
	public static void main(String[] argv){
		SketchRPG xx = new SketchRPG() ;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int keyCode = e.getKeyCode();
		char keyChar = e.getKeyChar() ;
		if(gameState == GAME_STATE_MAP){
			int direct ;
			switch(keyCode){
				case KeyEvent.VK_UP :
					direct = Person.U ;
					break ;
				case KeyEvent.VK_DOWN :
					direct = Person.D ;
					break ;
				case KeyEvent.VK_LEFT :
					direct = Person.L ;
					break ;
				case KeyEvent.VK_RIGHT :
					direct = Person.R ;
					break ;
				default :
					direct = -1 ;
			}
			if(direct != -1){
				bgmap.move(direct) ;
				//decide if moving the bgmap
				if(bgmap.changeWindowOffset(direct)){
					bgmap.setLocation(bgmap.getWindowOffsetX() , bgmap.getWindowOffsetY()) ;
				}
			}
			else if(keyChar == 'W' || keyChar == 'w'){
				if(wordsPanel == null) return ;
				wordsPanel.processObstacle(bgmap.getObstacleType());
				wordsPanel.next();
				mainPanel.add(wordsPanel) ;
				wordsPanel.setBounds(0,0,wordsPanel.width,wordsPanel.height);
				mainPanel.setComponentZOrder(wordsPanel,0) ; // make it visible 
				gameState = GAME_STATE_TALK ;
				repaint() ;
			}
		}
		else if(gameState == GAME_STATE_TALK){
			if(wordsPanel.hasNext()){
				wordsPanel.next();
			}
			else{
				if(bgmap.getObstacleType() == Obstacle.TARGET){
					gameState = GAME_STATE_OVER ;
					mainPanel.removeAll() ;
					wordsPanel = null ;
					wordsPanel = new WordsPanel("OVER\nby 11103101 徐伟") ;
					mainPanel.add(wordsPanel) ;
					wordsPanel.setBounds(0,0,WINDOW_WIDTH,WINDOW_HEIGHT);
					repaint() ;
				}
				else{
					gameState = GAME_STATE_MAP ;
					mainPanel.remove(wordsPanel) ;
				}
			}
		}
		repaint() ;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
