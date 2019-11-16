
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.math.BigInteger;
import javax.swing.JComponent;



/************************************************************************
 *									*
 *		Kleiner Secp256k1 Calculatur für Testzwecke		*
 *		Hier werden alle Berechnungen durchgeführt		*
 *									*
 ************************************************************************/



public class Calculator
{

	
	
private static int color = 0;				// Farbe der doppelten Elemente
private static Color[] txt_color = new Color[19];	// Zugeordnete Hintergrundfarbe jeden Textfeldes
public static boolean run_findEquals = true;		// Zum Beenden des Thread´s "FindEquals"




// Hier wird die richtige Rechenoperation anhand der ausgweählten Eingabefelder bestimmt
// Die Informations-Meldungen werden entspechend auf der GUI platziert
// Rückgabe ist Integer was wie folgt identifiziert wird:
// 0 = ungültige Operation
// 1 = scalare Addition 	( 2 Zahlen werden normal addiert)
// 2 = scalare Subtraktion 	( 2 Zahlen werden normal Subrahier)
// 3 = scalare Multiplikation 	( 2 Zahlen werden normal Multipliziert)
// 4 = scalare Division		( 2 Zahlen werden normal Dividiert)
// 5 = Addition 		auf der elliptischen Kurve ( Vektor + Vektor)
// 6 = Subtraktion  		auf der elliptischen Kurve ( Vektor - Vektor)
// 7 = Multiplikation  		auf der elliptischen Kurve ( Skalar * Vektor)
// 8 = Divison  		auf der elliptischen Kurve ( Vektor / Skalar)
public static int parseOP()
{
	int a = GUI.comboBox_1.getSelectedIndex();
	int b = GUI.comboBox_2.getSelectedIndex();
	int op = GUI.comboBox_op.getSelectedIndex();
	GUI.lbl_op.setForeground(new Color(100, 149, 237));
	GUI.txt_opBeschreibung.setForeground(new Color(100, 149, 237));
	GUI.txt[4].setVisible(true);
	GUI.btn_calc.setEnabled(true);
	GUI.txt_opBeschreibung.setText("");
	String a_txt = (String) GUI.comboBox_1.getSelectedItem();
	String b_txt = (String) GUI.comboBox_2.getSelectedItem();
	String op_txt="";
	if(op==0) op_txt = "addition";
	if(op==1) op_txt = "subtraction";
	if(op==2) op_txt = "multiplication";
	if(op==3) op_txt = "division";
	if(op==4) op_txt = "signature";
	if(op==5) op_txt = "verify";
	String txt_0 = " The "+op_txt+" of a "+a_txt+" with a "+b_txt+"\n is not possible!\n\n Adjust the input fields for the "+op_txt+" correctly";
	String txt_1 = " scalar  "+op_txt+"\n\n "+op_txt+" of a number with a number modulo n\n f(x) = (A "+GUI.comboBox_op.getSelectedItem()+" B)mod(n)";
	String txt_2 = " elliptic curve "+op_txt+"\n\n "+op_txt+" of a "+a_txt+" with a "+b_txt+"\n on the elliptic curve Secp256k1\n f(x) = (A "+GUI.comboBox_op.getSelectedItem()+" B)";
	String txt_3 = " ECDSA Signatur\n\n h = hash of the document to be signed\n k = random number\n B = the private key with which you want to sign \n G = generator point \n\n Signature r,s\n\n f(r) = k \u2022 G \n f(s) = (h + r \u2022 B)/k";
	String txt_4 = " ECDSA verification\n\n h  = hash of the document to be verified\n r,s = the signature\n B  = the public key with which you want to verified \n G  = generator point \n\n h/s \u2022 G + r/s \u2022 B = r(x)";
	if(a==1 && b==1 && op==0) { GUI.lbl_op.setText("scalar  addition"); 		GUI.txt[5].setVisible(false); GUI.txt_opBeschreibung.setText(txt_1); return 1;}
	if(a==1 && b==1 && op==1) { GUI.lbl_op.setText("scalar  subtraction"); 		GUI.txt[5].setVisible(false); GUI.txt_opBeschreibung.setText(txt_1); return 2;}
	if(a==1 && b==1 && op==2) { GUI.lbl_op.setText("scalar  multiplication");	GUI.txt[5].setVisible(false); GUI.txt_opBeschreibung.setText(txt_1); return 3;}
	if(a==1 && b==1 && op==3) { GUI.lbl_op.setText("scalar  division");		GUI.txt[5].setVisible(false); GUI.txt_opBeschreibung.setText(txt_1); return 4;}
	if(a==0 && b==0 && op==0) { GUI.lbl_op.setText("ECC addition"); 		GUI.txt[5].setVisible(true);  GUI.txt_opBeschreibung.setText(txt_2); return 5;}
	if(a==0 && b==0 && op==1) { GUI.lbl_op.setText("ECC subtraktion"); 		GUI.txt[5].setVisible(true); GUI.txt_opBeschreibung.setText(txt_2);  return 6;}
	if(a==1 && b==0 && op==2) { GUI.lbl_op.setText("ECC multiplication");		GUI.txt[5].setVisible(true); GUI.txt_opBeschreibung.setText(txt_2);  return 7;}
	if(a==0 && b==1 && op==2) { GUI.lbl_op.setText("ECC multiplikation");		GUI.txt[5].setVisible(true); GUI.txt_opBeschreibung.setText(txt_2);  return 7;}
	if(a==0 && b==1 && op==3) { GUI.lbl_op.setText("ECC division");			GUI.txt[5].setVisible(true); GUI.txt_opBeschreibung.setText(txt_2);  return 8;}
	if(a==0 && b==1 && op==4) { GUI.lbl_op.setText("signature");			GUI.txt[5].setVisible(true); GUI.txt_opBeschreibung.setText(txt_3);  return 9;}
	if(a==0 && b==0 && op==5) { GUI.lbl_op.setText("verify");			GUI.txt[5].setVisible(false); GUI.txt_opBeschreibung.setText(txt_4); return 10;}
	GUI.lbl_op.setText("No possible operation!"); 
	{
		GUI.lbl_op.setForeground(Color.RED); GUI.btn_calc.setEnabled(false); 
		GUI.txt_opBeschreibung.setText(txt_0);
		GUI.txt_opBeschreibung.setForeground(Color.red);
		return 0;
	}	
}
	

	

// subrahiert a-b  
private static BigInteger sub(BigInteger a, BigInteger b)
{
	return  a.add(Secp256k1.ORDNUNG.subtract(b)).mod(Secp256k1.ORDNUNG);
}	

	
	
	
// dividiert a / b	
public static BigInteger div(BigInteger a, BigInteger b)
{
	return a.multiply(b.modInverse(Secp256k1.ORDNUNG)).mod(Secp256k1.ORDNUNG);	
}
	
	


// Hauptmethode die bei Betätigung des "=" Symbols, die Berechnung ausführt.
public static void calc() throws Exception
{
	int op = parseOP();	
	BigInteger[] po1 = new BigInteger[2];
	BigInteger[] po2 = new BigInteger[2];	
	switch(op) 
	{
		case 1: GUI.txt[4].setText(	new BigInteger(GUI.txt[0].getText(),16).add	(new BigInteger(GUI.txt[2].getText(),16)).mod(Secp256k1.ORDNUNG).toString(16)); break;
		case 2: GUI.txt[4].setText(sub(	new BigInteger(GUI.txt[0].getText(),16), 	 new BigInteger(GUI.txt[2].getText(),16)).toString(16)); break;
		case 3: GUI.txt[4].setText(	new BigInteger(GUI.txt[0].getText(),16).multiply(new BigInteger(GUI.txt[2].getText(),16)).mod(Secp256k1.ORDNUNG).toString(16)); break;
		case 4: GUI.txt[4].setText(div(	new BigInteger(GUI.txt[0].getText(),16), 	 new BigInteger(GUI.txt[2].getText(),16)).toString(16)); break;
	
		case 5:  	// Addition elliptic curve
		{
			po1[0] = new BigInteger(GUI.txt[0].getText(),16);
			po1[1] = new BigInteger(GUI.txt[1].getText(),16);
			po2[0] = new BigInteger(GUI.txt[2].getText(),16);
			po2[1] = new BigInteger(GUI.txt[3].getText(),16);
			BigInteger[] erg = Secp256k1.addition(po1, po2);
			GUI.txt[4].setText(erg[0].toString(16));
			GUI.txt[5].setText(erg[1].toString(16));
			break;
		}
		case 6: 	// Suptraktion elliptic curve
		{
			po1[0] = new BigInteger(GUI.txt[0].getText(),16);
			po1[1] = new BigInteger(GUI.txt[1].getText(),16);
			po2[0] = new BigInteger(GUI.txt[2].getText(),16);
			po2[1] = new BigInteger(GUI.txt[3].getText(),16);
			BigInteger[] erg = Secp256k1.subtraktion(po1, po2);
			GUI.txt[4].setText(erg[0].toString(16));
			GUI.txt[5].setText(erg[1].toString(16));
			break;
		}
		case 7: 	// Multiplikation elliptic curve
		{
			BigInteger fac;			
			if(GUI.comboBox_1.getSelectedIndex()==1)
			{
				fac = new BigInteger(GUI.txt[0].getText(),16);
				po1[0] = new BigInteger(GUI.txt[2].getText(),16);
				po1[1] = new BigInteger(GUI.txt[3].getText(),16);
			}
			else
			{
				fac = new BigInteger(GUI.txt[2].getText(),16);
				po1[0] = new BigInteger(GUI.txt[0].getText(),16);
				po1[1] = new BigInteger(GUI.txt[1].getText(),16);
			}
			BigInteger[] erg = Secp256k1.multiply_Point(po1, fac);
			GUI.txt[4].setText(erg[0].toString(16));
			GUI.txt[5].setText(erg[1].toString(16));	
			break;
			}
		case 8: 	// Division elliptic curve
		{
			BigInteger div = new BigInteger(GUI.txt[2].getText(),16);
			po1[0] = new BigInteger(GUI.txt[0].getText(),16);
			po1[1] = new BigInteger(GUI.txt[1].getText(),16);
			BigInteger[] erg = Secp256k1.div(po1, div);
			GUI.txt[4].setText(erg[0].toString(16));
			GUI.txt[5].setText(erg[1].toString(16));	
			break;
		}	
		case 9: 	// Signature
		{
			BigInteger h = new BigInteger(GUI.txt[0].getText(),16);
			BigInteger k = new BigInteger(GUI.txt[1].getText(),16);
			BigInteger prv = new BigInteger(GUI.txt[2].getText(),16);			
			Secp256k1 sec = new Secp256k1();
			BigInteger[] erg = sec.sig(h.toByteArray(), prv.toByteArray(), k.toByteArray());
			GUI.txt[4].setText(erg[0].toString(16));
			GUI.txt[5].setText(erg[1].toString(16));	
			break;
		}
		case 10:	// Verify
		{
			BigInteger h = new BigInteger(GUI.txt[0].getText(),16);
			po1[0] = new BigInteger(GUI.txt[1].getText(),16);
			po1[1] = new BigInteger(GUI.txt[18].getText(),16);	
			po2[0] = new BigInteger(GUI.txt[2].getText(),16);
			po2[1] = new BigInteger(GUI.txt[3].getText(),16);
			Secp256k1 sec = new Secp256k1();
			Boolean erg = sec.verify(h.toByteArray(), po1, po2);
			GUI.txt[4].setText(erg.toString());		
			break;
		}
		default:	
	}	
	GUI.runProgressBar=false;
}
	
	
	
	
// Speichert den übergebenen Vektor im nächsten freien Merker
// Ist kein Merker mehr frei, wird nichts gespeichert
public static void saveToNextMerker(String a, String b)
{		
	for(int i=6;i<18;i=i+2)
	{
		if(isEmty(i)==true && isEmty(i+1)==true)
		{
			GUI.txt[i].setText(a);
			GUI.txt[i+1].setText(b);	
			break;
		}
	}
}




// Speichert den Merker zurück in den Eingabebereich. 
public static void saveFromMerkerTo(ActionEvent e)
{
	int nr =  (int) ((JComponent) e.getSource()).getClientProperty("int");  // nr = die Button Nummer die nur auf diesem Umständlichem Weg übergeben werden kann.
	if(nr%2==0) 			// die Merker auf der linken Seite
	{
		if(GUI.comboBox_op.getSelectedIndex()!=5)				
		{
			GUI.txt[0].setText(GUI.txt[nr+6].getText());
			GUI.txt[1].setText(GUI.txt[nr+7].getText());		
			if(isEmty(nr+7)) GUI.comboBox_1.setSelectedIndex(1);
			else 			 GUI.comboBox_1.setSelectedIndex(0);
		}
		else			// Bei Auswahl von Verify
		{
			GUI.txt[1].setText(GUI.txt[nr+6].getText());
			GUI.txt[18].setText(GUI.txt[nr+7].getText());
		}
	}
	else 				// die Merker auf der rechten Seite
	{
		GUI.txt[2].setText(GUI.txt[nr+5].getText());
		GUI.txt[3].setText(GUI.txt[nr+6].getText());
		if(isEmty(nr+6)) GUI.comboBox_2.setSelectedIndex(1);
		else 	GUI.comboBox_2.setSelectedIndex(0);
	}
}




// Prüft ob das Feld leer ist	
private static boolean isEmty(int m)
{
	if(GUI.txt[m].getText().equals("")) return true;
	else return false;
}
	
	

	
// Startet einen eigenen Thread der zyklich alle eingabe Felder mit einander vergleicht
// und doppelte Elemente farblich markiert
// Die Hintergrundfarbe wird zwichengespeichert und erst am Ende auf das Textfeld geschrieben, damit es nicht zum Flackern kommt.
public static void findEquals()
{
	Thread t = new Thread(new Runnable()
	{
		public void run()
		{
			while(run_findEquals)
			{				
				try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}					
				color =0;
				for(int i=0;i<19;i++) txt_color[i] = Color.white;
				txt_color[4] = new Color(245, 245, 245);
				txt_color[5] = new Color(245, 245, 245);
				String[] text = new String[19];					
				for(int i=0;i<19;i++) {text[i] = GUI.txt[i].getText();}
				for(int i=0;i<19;i++)
				{
					for(int j=0;j<19;j++)
					{
						if(text[i].equals(text[j]) && i!=j && text[i].equals("")==false)
						{
							txt_color[i] = nextColor();
							txt_color[j] = nextColor();								
						}
					}						
					color++;
				}
				for(int i=0;i<19;i++) {GUI.txt[i].setBackground(txt_color[i]);}					
				if(GUI.txt[0].getText().equals("color")) printAllColor();   
			}				
			System.out.println("Thread findEquals beendet");
		}
	});
	t.start();		
}
	
	
	
	
// Vergibt die nächste Farbe für die Hintergrundfarbe der gleichen Elemente
private static Color nextColor()
{
	Color[] c = new Color[19];
	c[3]  = Color.decode("#fbeeee");
	c[6]  = Color.decode("#F8ECE0");
	c[9]  = Color.decode("#F7F8E0");
	c[12] = Color.decode("#ECF8E0");
	c[15] = Color.decode("#E0F8E0");
	c[1]  = Color.decode("#E0F2F7");
	c[4]  = Color.decode("#E0E6F8");
	c[7]  = Color.decode("#E6E0F8");
	c[10] = Color.decode("#ffd6cc");
	c[13] = Color.decode("#ebe0cc");
	c[16] = Color.decode("#F8E0E6");
	c[2]  = Color.decode("#cce0ff");
	c[5]  = Color.decode("#F5A9A9");
	c[8]  = Color.decode("#F5D0A9");
	c[11] = Color.decode("#F2F5A9");
	c[14] = Color.decode("#D0F5A9");
	c[17] = Color.decode("#A9F5A9");
	c[0]  = Color.decode("#A9F5D0");
	c[18] = Color.decode("#ffcccc"); 
	return c[color];
}



	
//Testmethode die bei der Eingabe von "color" im ersten Feld alle Farben anzeigt.	
private static void printAllColor()
{
	color = 0;
	for(int i=0;i<19;i++) 
	{
		GUI.txt[i].setBackground(nextColor());
		color++;
	}		
}
	
	





// --------------------------------------------------------- Signature & Verify ----------------------------------------------




// der Eingabebereich der GUI wird für die Berechnung der Sigantur angepasst 
public static void setOP_Sig()
{
	GUI.comboBox_op.setFont(new Font("Tahoma", Font.PLAIN, 18));
	GUI.comboBox_1.setSelectedIndex(0);
	GUI.comboBox_2.setSelectedIndex(1);
	GUI.lbl_X.setText("h");
	GUI.lbl_Y.setText("k");
	GUI.lbl_Z.setVisible(false);
	GUI.lbl_privKey.setVisible(true);
	GUI.lbl_privKey.setText("Private Key");
	GUI.lbl_privKey.setForeground(Color.RED);
	GUI.btn_G1.setVisible(false);
	GUI.btn_rand1.setVisible(true);
	GUI.btn_m1.setBounds(300, 135, 50, 20);
	GUI.btn_y1a.setVisible(false);
	GUI.btn_y1b.setVisible(false);
	GUI.lbl_r.setVisible(true);
	GUI.lbl_s.setVisible(true);
	GUI.txt[18].setVisible(false);
	GUI.txt[18].setText("");
	GUI.txt[1].setBounds(20, 110, 330, 20);
	GUI.lbl_Y.setBounds(10, 113, 23, 14);
	GUI.btn_m4.setVisible(false);
	GUI.lbl_D.setVisible(false);
	for(int i=0; i<12 ;i++) if(i%2==0) {GUI.btn_m[i].setText("<--A"); GUI.btn_m[i].setToolTipText("Save this memory field back to input A");}
}



// der Eingabebereich der GUI wird für die Berechnung der Verifikation angepasst 
public static void setOP_ver()
{
	GUI.comboBox_op.setFont(new Font("Tahoma", Font.PLAIN, 18));
	GUI.comboBox_1.setSelectedIndex(0);
	GUI.comboBox_2.setSelectedIndex(0);
	GUI.lbl_X.setText("h");
	GUI.lbl_Y.setText("r");
	GUI.lbl_Z.setText("s");
	GUI.lbl_Z.setVisible(true);
	GUI.lbl_privKey.setVisible(true);
	GUI.lbl_privKey.setText("Public Key");
	GUI.lbl_privKey.setForeground(Color.blue);
	GUI.btn_G1.setVisible(false);
	GUI.btn_rand1.setVisible(false);
	GUI.btn_m1.setBounds(300, 102, 49, 15);
	GUI.btn_y1a.setVisible(false);
	GUI.btn_y1b.setVisible(false);
	GUI.lbl_r.setVisible(false);
	GUI.lbl_s.setVisible(false);
	GUI.txt[18].setVisible(true);
	GUI.txt[1].setBounds(20, 130, 330, 20);
	GUI.lbl_Y.setBounds(10, 133, 23, 14);
	GUI.txt[1].setText("");
	GUI.txt[2].setText("");
	GUI.txt[3].setText("");
	GUI.btn_m4.setVisible(true);
	GUI.lbl_D.setVisible(true);
	for(int i=0; i<12 ;i++) if(i%2==0) {GUI.btn_m[i].setText("<--D"); GUI.btn_m[i].setToolTipText("Save this memory field back to input D");}
}



// der Eingabebereich der GUI wird für die Berechnung zurück auf Calculator gesetzt 
public static void setOP_calc()
{
	GUI.comboBox_op.setFont(new Font("Tahoma", Font.PLAIN, 30));
	GUI.lbl_X.setText("X");
	GUI.lbl_Y.setText("Y");
	GUI.lbl_privKey.setVisible(false);
	GUI.btn_G1.setVisible(true);
	GUI.btn_rand1.setVisible(true);
	GUI.btn_m1.setBounds(300, 135, 50, 20);
	GUI.btn_y1a.setVisible(true);
	GUI.btn_y1b.setVisible(true);
	GUI.lbl_r.setVisible(false);
	GUI.lbl_s.setVisible(false);
	GUI.txt[18].setVisible(false);
	GUI.txt[18].setText("");
	GUI.lbl_Z.setVisible(false);
	GUI.txt[1].setBounds(20, 110, 330, 20);
	GUI.lbl_Y.setBounds(10, 113, 23, 14);
	GUI.btn_m4.setVisible(false);
	GUI.lbl_D.setVisible(false);
	for(int i=0; i<12 ;i++) if(i%2==0) {GUI.btn_m[i].setText("<--A"); GUI.btn_m[i].setToolTipText("Save this memory field back to input A");}
}



	
}
