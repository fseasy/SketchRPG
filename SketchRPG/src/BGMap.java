import javax.swing.* ;

import java.awt.* ;
import java.net.URL;

public class BGMap extends JPanel {
	protected Person p = null ;
	
	private Image imgBgmap = null ;
	private Image imgHill = null ;
	private Image imgRiver = null ;
	private Image imgGrass = null ;
	private Image imgMaster = null ;
	
	public final static int RIVER_WIDTH = 200 ;
	public final static int RIVER_HEIGHT = 50 ;
	public final static int HILL_WIDTH = 45 ;
	public final static int HILL_HEIGHT = 45 ;
	public final static int GRASS_WIDTH = 45 ;
	public final static int GRASS_HEIGHT = 45 ;
	public final static int MASTER_HEIGHT = 45 ;
	public final static int MASTER_WIDTH = 45 ;
	public final static int BGMAP_WIDTH = 500 ;
	public final static int BGMAP_HEIGHT = 500 ;
	
	protected int windowOffsetX = 0 ;
	protected int windowOffsetY = 0 ;
	
	protected Rectangle riverRange = null ;
	protected Rectangle [] hillsRange = null ;
	protected Rectangle grassRange = null ;
	protected Rectangle masterRange = null ;
	//recode the obstacle
	public int obstacleType = Obstacle.EMPTY ;
	
	public BGMap(Person p){
		super(null) ;
		
		this.p = p ;
		windowOffsetY = SketchRPG.WINDOW_HEIGHT - BGMAP_HEIGHT ;
		//river
		riverRange = new Rectangle(-5 , 250 , RIVER_WIDTH , RIVER_HEIGHT) ;
		// hills position
		hillsRange = new Rectangle[5] ;
		hillsRange[0] = new Rectangle(75,320 , HILL_WIDTH , HILL_HEIGHT) ;
		hillsRange[1] = new Rectangle(280,200 , HILL_WIDTH , HILL_HEIGHT) ;
		hillsRange[2] = new Rectangle(320,190 , HILL_WIDTH , HILL_HEIGHT) ;
		hillsRange[3] = new Rectangle(175,0 , HILL_WIDTH , HILL_HEIGHT) ; // target hill
		hillsRange[4] = new Rectangle(275,0 , HILL_WIDTH , HILL_HEIGHT) ; // target hill
		//grass
		grassRange = new Rectangle(250,250,225,225) ;
		//master
		masterRange = new Rectangle(350 , 260 , MASTER_WIDTH , MASTER_HEIGHT) ;
		//images
		ClassLoader curClassLoader = this.getClass().getClassLoader();
		Toolkit defaultToolkit = Toolkit.getDefaultToolkit() ;
		URL urlMap = curClassLoader.getResource("res/img/map.png") ;
		//System.out.println(urlMap);
		imgBgmap = defaultToolkit.getImage(urlMap) ;
		
		URL urlRiver = curClassLoader.getResource("res/img/river.png") ;
		imgRiver = defaultToolkit.getImage(urlRiver) ;
		
		URL urlHill = curClassLoader.getResource("res/img/hill.png") ;
		imgHill = defaultToolkit.getImage(urlHill) ;
		
		URL urlGrass = curClassLoader.getResource("res/img/grass.png") ;
		imgGrass = defaultToolkit.getImage(urlGrass) ;
		
		URL urlMaster = curClassLoader.getResource("res/img/master.png") ;
		imgMaster = defaultToolkit.getImage(urlMaster) ;
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g) ;
		g.drawImage(imgBgmap, 0 , 0 , this) ;
		//river
		g.drawImage(imgRiver, riverRange.x , riverRange.y , this) ;
		//hill
		for(int i = 0 ; i < hillsRange.length ; i++){
			g.drawImage(imgHill , hillsRange[i].x , hillsRange[i].y , this) ;
		}
		//grass
		for(int x = 0 ; x < grassRange.width ; x += GRASS_WIDTH){
			for(int y = 0 ; y < grassRange.height ; y += GRASS_HEIGHT){
				g.drawImage(imgGrass ,  x + grassRange.x,  y + grassRange.y , this) ;
			}
		}
		//master
		g.drawImage(imgMaster , masterRange.x , masterRange.y , this) ;
		//people
		g.drawImage(p.imgCur , p.getX(), p.getY(),this) ;
	}
	// to detech if has collision , and set the obstacle type
	public boolean isNotCollision(int tmpX , int tmpY){
		
		//to test the bounds of map
		if(tmpX <= 0 || tmpX >= BGMAP_WIDTH - Person.PERSON_WIDTH){
			obstacleType = Obstacle.BOUNDARY ;
			return false ;
		}
		if(tmpY <= 0 || tmpY >= BGMAP_HEIGHT - Person.PERSON_HEIGHT){
			obstacleType = Obstacle.BOUNDARY ;
			return false ;
		}
		//to test the objects of hills and river
		Rectangle newPos = new Rectangle(tmpX , tmpY , Person.PERSON_WIDTH - 20, Person.PERSON_HEIGHT - 20) ;
		if(newPos.intersects(riverRange)){
			obstacleType = Obstacle.RIVER ;
			return false ;
		}
		if(newPos.intersects(masterRange)){
			obstacleType = Obstacle.NPC ;
			return false ;
		}
		for(int i = 0 ; i < hillsRange.length ; i++){
			if(newPos.intersects(hillsRange[i])){
				if(isTargetHill(hillsRange[i])){
					obstacleType = Obstacle.TARGET ;
				}
				else{
					obstacleType = Obstacle.HILL ;
				}
				return false ;
			}
		}
		obstacleType = Obstacle.EMPTY ;
		return true ;
	}
	public int getObstacleType(){
		return obstacleType ;
	}
	public boolean isTargetHill(Rectangle hill){
		if(hill == hillsRange[3] || hill == hillsRange[4]){
			return true ;
		}
		else{
			return false ;
		}
	}
	public boolean move(int direct){
		int tmpX = p.getX() ;
		int tmpY = p.getY() ;
		if(direct == Person.L){
			tmpX -= p.stepLen ;
		}
		else if(direct == Person.R){
			tmpX += p.stepLen ;
		}
		else if(direct == Person.U){
			tmpY -= p.stepLen ;
		}
		else if(direct == Person.D){
			tmpY += p.stepLen ;
		}
		boolean isNotCol = isNotCollision(tmpX , tmpY) ;
		if(isNotCol){
			p.setPosition(tmpX, tmpY);
			p.nextStep(direct) ;
			return true ;
		}
		else{
			return false ;
		}
	}
	public int getWindowOffsetX(){
		return windowOffsetX ;
	}
	public int getWindowOffsetY(){
		return windowOffsetY ;
	}
	public boolean changeWindowOffset(int direct){
		int pWindowX = p.getX() + windowOffsetX ;
		int pWindowY = p.getY() + windowOffsetY ;
		if(direct == Person.R){
			if(windowOffsetX + BGMap.BGMAP_WIDTH <= SketchRPG.WINDOW_WIDTH){
				windowOffsetX = SketchRPG.WINDOW_WIDTH - BGMap.BGMAP_WIDTH ;
				return false ;
			}
			if(pWindowX > 325){
				windowOffsetX -= p.stepLen ;
				return true ;
			}
		}
		else if(direct == Person.L){
			if(windowOffsetX >= 0){
				windowOffsetX = 0 ;
				return false ;
			}
			if(pWindowX < 125){
				windowOffsetX += p.stepLen ;
				return true ;
			}
		}
		else if(direct == Person.U){
			if(windowOffsetY >= 0){
				windowOffsetY = 0 ;
				return false ;
			}
			if(pWindowY < 100){
				windowOffsetY += p.stepLen ;
				return true ;
			}
		}
		else if(direct == Person.D){
			if(windowOffsetY + BGMap.BGMAP_HEIGHT <= SketchRPG.WINDOW_HEIGHT){
				windowOffsetY = SketchRPG.WINDOW_HEIGHT - BGMap.BGMAP_HEIGHT  ;
				return false ;
			}
			if(pWindowY >= 200){
				windowOffsetY -= p.stepLen ;
				return true ;
			}
		}
		//otherwise , don't change
		return false ;
	}
}
