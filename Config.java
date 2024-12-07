
import java.io.FileInputStream;
import java.util.Properties;



/***************************************************************************************************************************
*	Config-Typ: Text basierte Config Datei: ServerConfig.txt																*
*	https://docs.oracle.com/javase/7/docs/api/java/util/Properties.html														*
*	Parameter werden mit key = wert abgespeichert.																			*
*	Kommentare: #Kommentar																									*
*	Leerzeichen zwischen = Zeichen können verwendet werden oder auch nicht. (Sie werden ignoriert)							*
*	Leerzeichen nach dem Wert werden als String mit eingelesen und sind daher Teil des Wertet!								*
*	Leerzeichen vor den Schlüsselwert sind auch erlaubt, nur keine anderen Zeichen in der selben Zeile davor.				*
*	Falls der Wert also später ein Integer sein soll, würde es bei einem Leerzeichen nach dem Wert zum Error kommen!		*
*	Anführungszeichen für Werte sind nicht erlaubt bzw. würden als Teil des Wertes mit eingelesen werden.					*
*	Alle Werte werden prinzipiell als String erkannt und müssen bei Bedarf konvertiert werden. 								*
*	Zeilenumbrüche sind innerhalb eines Wertes nicht erlaubt und würden den Wert an dieser Stelle abschneiden.				*
*	Der gleiche Schlüsselwert kann mehrfach verwendet werden, jeweils der letzte wird aktiv eingelesen.						*
*	Beliebiger Text mit Zeilenumbrüchen etc. wird ignoriert und überlesen.													*
****************************************************************************************************************************/





/****************************************************************************************************************************
*	Datei: color.cfg anlegen um im Hauptverzeichnis speichern
*	1. Color.cfg Beispieldatei mit "Dark-Color:
*
	darkAnimation = true
	background     = #303030
	btn_foreground = #aaaaaa
	txt_background = #101010
	txt_foreground = #ffffff
	btn_background = #505050
	
 	# Zum Color Codieren: https://imgonline.tools/convert-color
*****************************************************************************************************************************/









public class Config 
{
		public static String 	color0;		// Haupt-Hintergrund                                       
		public static String 	color1;		// Schriftfarbe Feldbeschreibung und Button-Schriftfarbe   
		public static String 	color2;		// Textfelder Hintergrund                                  
		public static String 	color3;		// Textfelder Schriftfarbe                                 
		public static String 	color4;		// Rahmenfarbe-Textfelder, und Button-Hintergrund          
		public static boolean 	darkAnimation = false;


		
		
	
/**	Lädt die Config aus der ServerConfig.txt in die Variablen
	Muss immer beim Start ausgeführt werden! 
	Übergeben wird der Dateiname der Config, empfohlen wird ServerConfig.txt **/
	public static void loadConfig(String fileName)
	{
		try 
		{
			Properties p = new Properties();
			p.load(new FileInputStream(fileName));
			darkAnimation	= Boolean.valueOf(	p.getProperty("darkAnimation","false"));		// Wenn true, wird Animation dunkel gezeichnet
			color0		= 					p.getProperty("background"		,"#f5f5f5");	// Haupt-Hintergrund                                    
			color1		= 					p.getProperty("btn_foreground"	,"#606060"); 	// Schriftfarbe Feldbeschreibung und Button-Schriftfarbe
			color2		= 					p.getProperty("txt_background"	,"#ffffff");  	// Textfelder Hintergrund                               
			color3		= 					p.getProperty("txt_foreground"	,"#000000");  	// Textfelder Schriftfarbe                              
			color4		= 					p.getProperty("btn_background"	,"#ccddee");  	// Rahmenfarbe-Textfelder, und Button-Hintergrund 
	
		} 
		catch (Exception e) 				// Wenn Config datei nicht existiert dann werden auch die default Werte genommen
		{			  
			darkAnimation = false;
			color0	= "#f5f5f5";
			color1	= "#606060";
			color2	= "#ffffff";
			color3	= "#000000";
			color4	= "#ccddee";    
		}
	}
}