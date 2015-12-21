import javax.swing.* ;

import java.awt.* ;
import java.net.URL; 
import java.util.ArrayList;
import java.io.* ;

public class WordsPanel extends JPanel {
	String curStr = null ;
	public int width = 0 ;
	public int height = 0 ;
	private BufferedReader stageFile = null ;
	private String bufStr = null ;
	private int wordsType = -1 ;
	public final static int STAGE_TYPE = 0 ;
	public final static int STR_TYPE = 1 ;
	
	public WordsPanel(){
		super() ;
		width = SketchRPG.WINDOW_WIDTH ;
		height = 80 ;
	}
	public WordsPanel(String str){
		super() ;
		width = SketchRPG.WINDOW_WIDTH ;
		height = SketchRPG.WINDOW_HEIGHT ;
		curStr = str ;
		setPreferredSize(new Dimension(width , height)) ;
		repaint() ;
	}
	public void setSource(String str){
		this.bufStr = str ;
		wordsType = STR_TYPE ;
	}
	
	public void setSource(URL url){
		try {
			//System.out.println(url.toURI()) ;
			stageFile = new BufferedReader(new FileReader(new File(url.toURI()))) ;
			wordsType = STAGE_TYPE ;
			bufStr = stageFile.readLine();
			if(bufStr == null) 
				stageFile.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean hasNext(){
		if( bufStr != null ) return true ;
		else return false ;
	}
	public void next(){
		curStr = bufStr ;
		if(wordsType == STR_TYPE){
			bufStr = null ;
		}
		else{
			try{
				bufStr = stageFile.readLine();
				if(bufStr == null){
					stageFile.close();
				}
			}
			catch(Exception e){
				e.printStackTrace() ;
			}
		}
		repaint() ;
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		setBackground(Color.orange);
		Font font = new Font("Microsoft Yahei" , Font.PLAIN , 20) ;
		g.setFont(font);
		if(curStr != null)
			drawStringMultiLine(g,curStr) ;
	}
	public void drawStringMultiLine(Graphics g , String str){
		ArrayList<String> rstList = splitStr(str) ;
		int lineHeight = height / (rstList.size() + 1)  ;
		int baseHeight = 0 ;
		for(int i = 0 ; i < rstList.size(); i++){
			baseHeight += lineHeight ;
			g.drawString(rstList.get(i), 20, baseHeight);
		}
	}
	public ArrayList splitStr(String str){
		ArrayList<String> rstList = new ArrayList<String>() ;
		int startPos = 0 ;
		int maxLen = Math.min(15, str.length());
		while(startPos < str.length()){
			int subLen = maxLen ;
			int endPos = Math.min(startPos + subLen, str.length()) ;
			String subStr = str.substring(startPos , endPos) ;
			//detect if has the char '\n'
			int LFPos = subStr.indexOf('\n') ;
			if(LFPos != -1){
				subLen = LFPos ;
				endPos = Math.min(startPos + subLen, str.length()) ;
				subStr = str.substring(startPos , endPos ) ;
				subLen += 1 ; // skip '\n' for the next time
			}
			//System.out.println(subStr) ;
			rstList.add(subStr) ;
			startPos += subLen ;
		}
		return rstList ;
	}
	public void processObstacle(int obstacle){
		if(obstacle == Obstacle.BOUNDARY){
			setSource("地图边界") ;
		}
		else if(obstacle == Obstacle.EMPTY){
			setSource("没有发现什么") ;
		}
		else if(obstacle == Obstacle.HILL){
			setSource("一座小山") ;
		}
		else if(obstacle == Obstacle.RIVER){
			setSource("一条小河") ;
		}
		else if(obstacle == Obstacle.NPC){
			URL urlNPCStage = this.getClass().getClassLoader().getResource("res/stage/npc.dat") ;
			setSource(urlNPCStage) ;
		}
		else if(obstacle == Obstacle.TARGET){
			URL urlT = this.getClass().getClassLoader().getResource("res/stage/target.dat") ;
			setSource(urlT) ;
		}
		else{
			setSource("不明物体出现！") ;
		}
	}
}
