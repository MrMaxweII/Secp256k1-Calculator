
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;



/***************************************************************************************************************
 *														*
 *		Info: Dieses Dokument enthält zwei Klassen!							*
 *														*
 *		Diese AnimationECC Klasse erstellt ein animiertes Bild der Kurve Secp256k1.			*
 *		Zu erst muss mit dem Konstruktor ein AnimationECC Object erzeugt werden!			*
 *		Dem Konstruktor muss das Panel übergeben werden, auf welches gezeichnet wird.			*
 *														*
 *		Die SetBounds-Methode skaliert die Position und die Größe des Animationsfensters		*
 *		und speichert alle Punktkoordinaten der Kurve in einem großem Array der Klasse PunktJLabel 	*
 *		Sollte nur einmal bei Programmstart ausgeführt werden, nicht in Schleife! 			*
 *														*
 *		Die start() Methode startet in einem eigenem Thread die Animation.				*
 *		Die close() Methode beendet die Animation und den Thread.					*
 *		Achtung: close() Muss am ende ausgeführt werden, sonnst läuft der Thread weiter! 		*
 *		close() sollte daher immer in den addShutdownHook() Bereich des Programmes eingefügt werden!	*
 *														*
 ****************************************************************************************************************/



public class AnimationECC 
{
	JPanel panelHaupt;				// Ist das Panel welches vom Konstruktor übergeben wurde und auf Das gezeichnet wird.
	static boolean run = false;			// damit wird der Thread beendet (muss static sein!!!)
	static boolean antialiasing;			// Schaltet Antialiasing ein oder aus.
	int sleepTime = 20;				// Die ZyklusZeit der Animation woraus sich die FPS ergibt.
	int maxRounds = 100;				// Maximale Wiederholungen der Animation bevor der Thread beendet wird.
	int[] bounds = new int[4];			// Die Position und die Größe der Animation
	static int diameter = 2;			// Durchmesser des Punktest
	int pointCount = 8000;				// Anzahl der Animierten Punkte auf der Kurve (wird zur Laufzeit reduziert)
	PointJLabel[] point = new PointJLabel[pointCount];// Das Array der animierten Punkte
	
	
	
	    
	
/** Dem Konstruktor wird das Panel übergeben auf das die Grafig gezeichnet werden soll. **/
public AnimationECC(JPanel panelHaupt)
{
	this.panelHaupt = panelHaupt;	
	for(int i=0;i<pointCount; i++)
	{	
		point[i] = new PointJLabel();
	}
}
	


/**	Skalliert die Position und die Größe des Animationsfensters
	und speichert alle Punktkoordinaten der Kurve in einem großem Array der Klasse PunktJLabel 
	Sollte nur einmal bei Programmstart ausgeführt werden, nicht in Schleife! **/
public void setBounds(int x, int y, double b, double h)
{
	bounds[0]= x;
	bounds[1]= y;
	bounds[2]= (int)b;
	bounds[3]= (int)h;	
	int i=0;
	for(double dx=-1.91293; dx<20 && i<pointCount; dx=dx+0.00225)
	{
		point[i].setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
		if(i==4000) dx=-1.91293;
		double y1 = -(Math.sqrt(dx*dx*dx +7.0))*2.0;	
		if(i<4000) point[3999-i].setBoundsPoint((int)((dx+2.5)*20.0 * b/220.0) , (int)(((y1*10.0)+378.0) *h/800.0) , diameter); 
		else	   point[i].setBoundsPoint((int)((dx+2.5)*20.0 * b/220.0) , (int)(((-y1*10.0)+378.0)*h/800.0) , diameter);
		i++;
	}
	point = PointsDilutiong(point);
	for(int j=0;j<pointCount; j++)	// Fügt schon mal alle Punkte den Panel zu
	{
		panelHaupt.add(point[j]);
		point[j].setForeground(new Color(255,255,255));
	}
}	



/**	Startet die Animation und den Thread.
 	Achtung: Der Thread muss mit der Methode "close()" immer beendet werden!*/
public void start()
{
	Thread t1 = new Thread(new Runnable() 
	{
		public void run() 
		{
			run=true;		
			for(int rounds=0; rounds<maxRounds;rounds++)	// Wiederholung der gesamten Animation bis maxRounds
			{
				for(int i=0;i<pointCount && run; i++)
				{					
					for(int k=0;k<108 && run;k++)
					{
						if(i-k<0) 	
						{
							point[(i-k)+pointCount].setForeground(new Color(255 , (k+20)*2 ,k*2));	
							point[(i-k)+pointCount].setDiameter((pointCount-k)/25);
						}
						else 		
						{
							point[i-k].setForeground(new Color(255 , (k+20)*2 , k*2));	
							point[(i-k)].setDiameter((pointCount-k)/25);
						}
					}				
					try {Thread.sleep(sleepTime);} catch (InterruptedException e) {e.printStackTrace();}
				}
			}
			run = false;
			System.out.println("Thread AnimationECC beendet.");
		}				
	});	
	if(run==false) t1.start();		
}
	
	

// Löscht zu nah aneinander liegende Punkte in der Kurve und verteilt damit die Punkte gleichmäßiger.
// Das zurückgegebene Array ist dann natürlich kürzer.
private PointJLabel[] PointsDilutiong(PointJLabel[] point)
{
	ArrayList<PointJLabel> list = new ArrayList<PointJLabel>();
	list.add(point[0]);
	for(int i=1;i<pointCount; i++)
	{
		double x1 = (double)list.get(list.size() - 1).x;
		double y1 = (double)list.get(list.size() - 1).y;
		double x2 = (double)point[i].x;
		double y2 = (double)point[i].y;
		double dx = Math.sqrt(Math.pow(x1-x2, 2.0) + Math.pow(y1-y2, 2.0));  // Abstand zwischen zwei Punkten
		if(dx>2)list.add(point[i]);
	}
	pointCount = list.size();
	point = new PointJLabel[list.size()];
	for(int i=0;i<list.size(); i++)
	{
		point[i] = list.get(i);
	}
	return point;
}




/**	Setzt den Durchmesser des Punktest 
	Muss vor setBounds gesetzt werden! **/
public void setDiameter(int d)
{
	diameter = d;
}


/** Antialiasing an oder aus **/
public void setAntialiasing(boolean a)
{
	antialiasing = a;
}
	

/**	Setzt die Sleep Time des Threades in ms.
	Damit wird die Geschwindigkeit der Animation verändert.
	Default ist 20ms **/
public void setSleepTime(int ms)
{
	this.sleepTime = ms;
}


/** 	Setzt die maximalen Wiederholungen der Animation bevor der Thread beendet wird.
	Wirkt als zusätzliche Sicherheit zum beenden des Threads.
	Default ist 100 Wiederholungen **/
public void setMaxRound(int count)
{
	this.maxRounds = count;
}


/** Zeichnet die Kurve für Testzwecke **/
public void printCurv()
{
	for(int i=0;i<pointCount; i++)
	{
		point[i].setForeground(new Color(100,100,100));
	}
}


/**	Beendet die Animation und den Thread.
	Muss am ende ausgefÜhrt werden sonnst läuft der Thread weiter! */
public void close()
{
	run = false;
	for(int i=0;i<pointCount; i++)
	{
		point[i].setForeground(new Color(255,255,230));
	}
}
}
//----------------------------------- Ende Class AnimationECC -----------------------------------//





/****************************************************************************************************************
 *														*
 *	Diese PointJLabel Klasse ist eine Subklasse von JLabel.							*
 *	Sie wird genau wie JLabel verwendet, enthält jedoch die unteren zusätzlichen Atribute und Methoden.	*
 *	PointJLabel wird verwendet um einen einzelnen Punkt auf der Kurve zu speichern und zu zeichnen.		*
 *	Dazu wird ein Array aus PointJLabel Objekten erzeugt welches dann das Bild darstellt.			*
 *														*
 ****************************************************************************************************************/



class PointJLabel extends JLabel
{
	int x = 0;
	int y = 0;
	int d = 12; 	

	/** Zeichnet den Punkt auf das JLabel */
	protected void paintComponent(Graphics g) 
	{		
		super.paintComponent(g);	
		if(AnimationECC.antialiasing)                    // Antialiasing
		{
			Graphics2D graphics2D = (Graphics2D) g;	
		    	graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		    	RenderingHints.VALUE_ANTIALIAS_ON); 
		    	graphics2D.fillOval(x,y, d, d);
		}
		else g.fillOval(x,y, d, d);
	}
	
	public void setBoundsPoint(int x, int y, int d)
	{
		this.x = x;
		this.y = y;
		this.d = d;		
	}
	
	public void setDiameter(int d)
	{		
		if(d>AnimationECC.diameter) this.d = d;
		else this.d = AnimationECC.diameter;		
	}
}
