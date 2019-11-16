import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;


/************************************************************************
 *									*
 *		Kleiner Secp256k1 Calculatur für Testzwecke		*
 *									*
 ************************************************************************/



public class GUI extends JFrame
{
	public static final String	progName	= "Secp256k1 Calculator";
	public static final String	version 	= "V1.0.12";
	public static final String	autor 		= "Mr. Maxwell";
	public static final String	eMail		= "Maxwell-KSP@gmx.de";
	public static final String	myBitcoinAddr 	= "12zeCvN7zbAi3JDQhC8tU3DBm35kDEUNiB";	
	public static JLabel lbl_X;
	public static JLabel lbl_Y;
	public static JLabel lbl_Z;
	public static JLabel lbl_op;
	public static JLabel lbl_privKey;
	public static JLabel lbl_r;
	public static JLabel lbl_s;
	public static JTextField txt[] = new JTextField[19]; // Alle Textfelder für Hex Zahlen
	public static JButton btn_y1a;
	public static JButton btn_y1b;
	public static JButton btn_y2a;
	public static JButton btn_y2b;
	public static JButton btn_G1;
	public static JButton btn_G2;
	public static JButton btn_rand1;
	public static JButton btn_rand2;
	public static JButton btn_calc;
	public static JButton btn_m1;
	public static JButton btn_m4;
	public static JButton[] btn_m = new JButton[12]; 	// Die 12 unteren Merker Buttons
	public static JComboBox comboBox_1;	
	public static JComboBox comboBox_2;
	public static JComboBox comboBox_op;
	public static JProgressBar progressBar;
	public static Boolean runProgressBar;			// Zum Beenden des Threades ProgressBar auf false setzten
	public static JLabel lbl_D;
	public static JTextArea txt_info;
	public static JTextArea txt_opBeschreibung;

	
	
public static void main(String[] args)
{
	EventQueue.invokeLater(new Runnable()
	{
		public void run()
		{
			try
			{
				GUI frame = new GUI();
				frame.setVisible(true);
				Calculator.findEquals();
			} 
			catch (Exception e) {e.printStackTrace();}
			Runtime.getRuntime().addShutdownHook( new Thread() // Zu beendende Threads
			{
				  @Override public void run() 
				  {
					  Calculator.run_findEquals = false;
					  try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
				  }
			});	
		}
	});
}


	
	
	
	
public GUI()
{
	JPanel 	contentPane 	= new JPanel();
	JScrollPane scrollPane 	= new JScrollPane();
	JPanel 	panel 		= new JPanel();
	panel.setBackground(new Color(245, 245, 245));
	scrollPane.setViewportView(panel);
	contentPane.setLayout(new BorderLayout(0, 0));
	setContentPane(contentPane);
	panel.setPreferredSize(new Dimension(1160,670)); 
	panel.setLayout(null);
	contentPane.add(scrollPane);
	
	getContentPane().setFont(new Font("Courier New", Font.PLAIN, 13));
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(5, 100, 1180, 720);
	setTitle(progName+"               "+version);
	
	
	
	txt_info = new JTextArea(); 						// Info Feld rechts
	txt_info.setEditable(false);
	txt_info.setBounds(787, 198, 372, 79);
	txt_info.setFont(new Font("Arial", Font.PLAIN, 16));
	txt_info.setForeground(new Color(100, 149, 237));
	txt_info.setBackground(new Color(245, 245, 245));
	txt_info.setText("Elliptic curve calculator with the curve: Secp256k1\nAll entered in hexa decimal.");
	panel.add(txt_info);
	
	JTextField lbl_donate = new JTextField();				// Spenden Feld
	lbl_donate.setBorder(null);
	lbl_donate.setEditable(false);
	lbl_donate.setBounds(787, 639, 372, 29);
	lbl_donate.setFont(new Font("Arial", Font.PLAIN, 13));
	lbl_donate.setForeground(new Color(100, 149, 237));
	lbl_donate.setBackground(new Color(245, 245, 245));
	lbl_donate.setText("please donate "+myBitcoinAddr);
	panel.add(lbl_donate);
	
	txt_opBeschreibung = new JTextArea();					// Das untere info Feld rechts mit der Beschreibung des Programmes
	txt_opBeschreibung.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
	txt_opBeschreibung.setForeground(new Color(100, 149, 237));
	txt_opBeschreibung.setFont(new Font("Arial", Font.PLAIN, 13));
	txt_opBeschreibung.setEditable(false);
	txt_opBeschreibung.setBackground( new Color(245, 245, 245));
	txt_opBeschreibung.setBounds(787, 288, 358, 192);
	panel.add(txt_opBeschreibung);
	
	JLabel lbl_A_B = new JLabel("A                                                               B                                                              C");
	lbl_A_B.setHorizontalAlignment(SwingConstants.CENTER);
	lbl_A_B.setFont(new Font("Arial", Font.PLAIN, 20));
	lbl_A_B.setForeground(new Color(100, 149, 237));
	lbl_A_B.setBounds(140, 55, 822, 20);
	panel.add(lbl_A_B);
	
	lbl_D = new JLabel("D");
	lbl_D.setForeground(new Color(100, 149, 237));
	lbl_D.setFont(new Font("Arial", Font.PLAIN, 20));
	lbl_D.setBounds(155, 109, 30, 20);
	lbl_D.setVisible(false);
	panel.add(lbl_D);
	
	lbl_X = new JLabel("X");
	lbl_X.setBounds(10, 83, 23, 14);
	panel.add(lbl_X);
	
	lbl_Y = new JLabel("Y");
	lbl_Y.setBounds(10, 113, 23, 14);
	panel.add(lbl_Y);
	
	lbl_privKey = new JLabel();
	lbl_privKey.setForeground(Color.RED);
	lbl_privKey.setVisible(false);
	lbl_privKey.setBounds(577, 61, 132, 14);
	panel.add(lbl_privKey);

	lbl_op = new JLabel("Addition");
	lbl_op.setForeground(SystemColor.textHighlight);
	lbl_op.setFont(new Font("Arial", Font.PLAIN, 12));
	lbl_op.setHorizontalAlignment(SwingConstants.RIGHT);
	lbl_op.setBounds(273, 41, 132, 18);
	panel.add(lbl_op);
	
	lbl_r = new JLabel("r");									
	lbl_s = new JLabel("s");
	lbl_Z = new JLabel("s");
	lbl_r.setBounds(1147, 80, 23, 14);
	lbl_s.setBounds(1147, 111, 23, 14);
	lbl_Z.setBounds(10, 161, 23, 14);
	lbl_r.setVisible(false);
	lbl_s.setVisible(false);
	lbl_Z.setVisible(false);
	panel.add(lbl_r);
	panel.add(lbl_s);
	panel.add(lbl_Z);
	
	
	
	for(int i=0;i<19;i++)                 				// Alle Textfelder werden gesetzt
	{
		txt[i] = new JTextField();
		txt[i].setFont(new Font("Courier New", Font.PLAIN, 9));
		panel.add(txt[i]);
	}
	txt[0].setBounds(20, 80, 330, 20);
	txt[1].setBounds(20, 110, 330, 20);
	txt[2].setBounds(415, 80, 330, 20);
	txt[3].setBounds(415, 110, 330, 20);
	txt[4].setBounds(815, 80, 330, 20);
	txt[5].setBounds(815, 110, 330, 20);
	txt[4].setEditable(false);
	txt[5].setEditable(false);
	txt[6].setBounds(415, 220, 330, 20);  
	txt[7].setBounds(415, 250, 330, 20);  
	txt[8].setBounds(415, 300, 330, 20);  
	txt[9].setBounds(415, 330, 330, 20);  
	txt[10].setBounds(415, 380, 330, 20); 
	txt[11].setBounds(415, 410, 330, 20); 
	txt[12].setBounds(415, 460, 330, 20); 
	txt[13].setBounds(415, 490, 330, 20);
	txt[14].setBounds(415, 540, 330, 20);
	txt[15].setBounds(415, 570, 330, 20);
	txt[16].setBounds(415, 620, 330, 20); 
	txt[17].setBounds(415, 650, 330, 20); 
	txt[18].setBounds(20, 160, 330, 20);
	txt[18].setVisible(false);

	
	
	btn_calc = new JButton("=");
	btn_calc.setToolTipText("Calculate now!");
	btn_calc.setBounds(755, 80, 50, 50);
	btn_calc.setFont(new Font("Arial", Font.BOLD, 28));
	btn_calc.addActionListener(new ActionListener()         // Startet die Rechnung und die ProcessBar jeweils in einem eigenem Thread
	{							// (Damit die GUI nicht hängenbleibt in der Berechnungszeit)
		public void actionPerformed(ActionEvent e) 
		{
			Thread t1 = new Thread(new Runnable() 
			{
				public void run() 
				{
					try{Calculator.calc();} 
					catch (Exception e) 
					{
						txt[4].setText("Math. Error");
						txt[5].setText("Math. Error");
						runProgressBar=false;	
						e.printStackTrace();
					}
				}	
			});
			
			Thread t2 = new Thread(new Runnable() 
			{
				public void run() {GUI.runProgressBar();}				
			});
			t2.start();	
			t1.start();	
		}	
	});
	panel.add(btn_calc);

	
	
	btn_y2a = new JButton("Y");
	btn_y2a.setToolTipText("Calculates the 1. y  value of  x.    There are always 2 y-values!");
	btn_y2a.setBounds(415, 135, 50, 20);
	btn_y2a.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			txt[3].setText(Secp256k1.y_von_x(new BigInteger(txt[2].getText(),16))[0].toString(16));
		}
	});
	
	btn_y1a = new JButton("Y");
	btn_y1a.setToolTipText("Calculates the 1. y  value of  x.    There are always 2 y-values!");
	btn_y1a.setBounds(20, 135, 50, 20);
	btn_y1a.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			txt[1].setText(Secp256k1.y_von_x(new BigInteger(txt[0].getText(),16))[0].toString(16));
		}
	});
	btn_y1a.setFont(new Font("Tahoma", Font.BOLD, 12));
	panel.add(btn_y1a);
	
	btn_y1b = new JButton("Y");
	btn_y1b.setToolTipText("Calculates the 2. y  value of  x.    There are always 2 y-values!");
	btn_y1b.setBounds(80, 135, 50, 20);
	btn_y1b.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			txt[1].setText(Secp256k1.y_von_x(new BigInteger(txt[0].getText(),16))[1].toString(16));
		}
	});
	btn_y1b.setFont(new Font("Tahoma", Font.BOLD, 12));
	panel.add(btn_y1b);
	
	btn_y2a.setFont(new Font("Tahoma", Font.BOLD, 12));
	panel.add(btn_y2a);
	
	btn_y2b = new JButton("Y");
	btn_y2b.setToolTipText("Calculates the 2. y  value of  x.    There are always 2 y-values!");
	btn_y2b.setBounds(475, 135, 50, 20);
	btn_y2b.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			txt[3].setText(Secp256k1.y_von_x(new BigInteger(txt[2].getText(),16))[1].toString(16));
		}
	});
	btn_y2b.setFont(new Font("Tahoma", Font.BOLD, 12));
	panel.add(btn_y2b);
	
	btn_G1 = new JButton("G");
	btn_G1.setToolTipText("generator point of Secp256k1");
	btn_G1.setBounds(20, 55, 50, 20);
	btn_G1.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			txt[0].setText(Secp256k1.GENERATOR.toString(16));
			txt[1].setText(Secp256k1.GENERATORY.toString(16));
		}
	});
	btn_G1.setFont(new Font("Tahoma", Font.BOLD, 12));
	panel.add(btn_G1);
	
	btn_rand1 = new JButton("rand");
	btn_rand1.setToolTipText("generates a random point or a random number");
	btn_rand1.setBounds(80, 55, 50, 20);
	btn_rand1.setMargin(new Insets(0,0,0,0));
	btn_rand1.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) 
		{						
				Secp256k1 secp = new Secp256k1();
				BigInteger[] a = secp.multiply_G(new BigInteger(256, new Random()));
				txt[0].setText(a[0].toString(16));
				txt[1].setText(a[1].toString(16));
		}
	});
	btn_rand1.setFont(new Font("Tahoma", Font.BOLD, 12));
	panel.add(btn_rand1);
	
	btn_G2 = new JButton("G");
	btn_G2.setToolTipText("generator point of Secp256k1");
	btn_G2.setBounds(415, 55, 50, 20);
	btn_G2.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent arg0) 	
		{
			txt[2].setText(Secp256k1.GENERATOR.toString(16));
			txt[3].setText(Secp256k1.GENERATORY.toString(16));
		}
	});
	btn_G2.setFont(new Font("Tahoma", Font.BOLD, 12));
	panel.add(btn_G2);
	
	btn_rand2 = new JButton("rand");
	btn_rand2.setToolTipText("generates a random point or a random number");
	btn_rand2.setBounds(475, 55, 50, 20);
	btn_rand2.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent arg0) 
		{
				Secp256k1 secp = new Secp256k1();
				BigInteger[] a = secp.multiply_G(new BigInteger(256, new Random()));
				txt[2].setText(a[0].toString(16));
				txt[3].setText(a[1].toString(16));
		}
	});
	btn_rand2.setMargin(new Insets(0, 0, 0, 0));
	btn_rand2.setFont(new Font("Tahoma", Font.BOLD, 12));
	panel.add(btn_rand2);
	
	comboBox_2 = new JComboBox();
	comboBox_2.setToolTipText("Choose whether you want to enter a vector or a scalar.");
	comboBox_2.setBounds(415, 20, 200, 20);
	comboBox_2.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(comboBox_2.getSelectedIndex()==0)
			{
				txt[3].setVisible(true);
				btn_G2.setVisible(true);
				btn_y2a.setVisible(true);
				btn_y2b.setVisible(true);
			}
			else
			{
				txt[3].setVisible(false);
				btn_G2.setVisible(false);
				btn_y2a.setVisible(false);
				btn_y2b.setVisible(false);
				txt[3].setText("");
			}
			Calculator.parseOP();		
		}
	});
	
	comboBox_1 = new JComboBox();
	comboBox_1.setToolTipText("Choose whether you want to enter a vector or a scalar.");
	comboBox_1.setBounds(20, 20, 200, 20);
	comboBox_1.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(comboBox_1.getSelectedIndex()==0)
			{
				lbl_X.setVisible(true);
				lbl_Y.setVisible(true);
				txt[1].setVisible(true);
				btn_G1.setVisible(true);
				btn_y1a.setVisible(true);
				btn_y1b.setVisible(true);
			}
			else
			{
				lbl_X.setVisible(false);
				lbl_Y.setVisible(false);
				txt[1].setVisible(false);
				btn_G1.setVisible(false);
				btn_y1a.setVisible(false);
				btn_y1b.setVisible(false);
				txt[1].setText("");
			}
			Calculator.parseOP();		
		}
	});
	
	btn_m1 = new JButton("A-->M");                    		// Merker Buttons
	btn_m1.setToolTipText("save this field in the next free memory");
	btn_m1.setBounds(300, 135, 50, 20);
	btn_m1.setMargin(new Insets(0, 0, 0, 0));
	btn_m1.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) 
		{			
			if(txt[1].isVisible() && comboBox_op.getSelectedIndex()!=5) Calculator.saveToNextMerker(txt[0].getText(),txt[1].getText());
			else Calculator.saveToNextMerker(txt[0].getText(),"");
		}
	});
	btn_m1.setFont(new Font("Tahoma", Font.BOLD, 12));
	panel.add(btn_m1);
	
	JButton btn_m2 = new JButton("B-->M");
	btn_m2.setToolTipText("save this field in the next free memory");
	btn_m2.setBounds(694, 134, 50, 20);
	btn_m2.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			if(txt[3].isVisible()) Calculator.saveToNextMerker(txt[2].getText(),txt[3].getText());
			else Calculator.saveToNextMerker(txt[2].getText(),"");
		}
	});
	btn_m2.setMargin(new Insets(0, 0, 0, 0));
	btn_m2.setFont(new Font("Tahoma", Font.BOLD, 12));
	panel.add(btn_m2);
	
	JButton btn_m3 = new JButton("C-->M");
	btn_m3.setToolTipText("save this field in the next free memory");
	btn_m3.setBounds(1095, 134, 50, 20);
	btn_m3.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(txt[5].isVisible()) Calculator.saveToNextMerker(txt[4].getText(),txt[5].getText());
			else Calculator.saveToNextMerker(txt[4].getText(),"");
		}
	});
	btn_m3.setMargin(new Insets(0, 0, 0, 0));
	btn_m3.setFont(new Font("Tahoma", Font.BOLD, 12));
	panel.add(btn_m3);
	

	btn_m4 = new JButton("D-->M");
	btn_m4.setToolTipText("save this field in the next free memory");
	btn_m4.setBounds(300, 182, 49, 15);
	btn_m4.setVisible(false);
	btn_m4.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
		Calculator.saveToNextMerker(txt[1].getText(),txt[18].getText());
			
		}
	});
	btn_m4.setMargin(new Insets(0, 0, 0, 0));
	btn_m4.setFont(new Font("Tahoma", Font.BOLD, 12));
	panel.add(btn_m4);

	
	
	
	
	comboBox_1.setBackground(Color.LIGHT_GRAY);							// Combo Boxen
	comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"Vector", "Scalar"}));
	panel.add(comboBox_1);
	
	comboBox_2.setBackground(Color.LIGHT_GRAY);
	comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"Vector", "Scalar"}));
	panel.add(comboBox_2);
	
	comboBox_op = new JComboBox();
	comboBox_op.setToolTipText("Selection of the ECC operation");
	comboBox_op.setBounds(358, 80, 50, 50);
	comboBox_op.setFont(new Font("Tahoma", Font.PLAIN, 30));
	comboBox_op.setBackground(Color.LIGHT_GRAY);
	comboBox_op.setModel(new DefaultComboBoxModel(new String[] {"+", "-", "\u2022", ":", "sig", "ver"}));
	comboBox_op.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(comboBox_op.getSelectedIndex() <  4) { Calculator.setOP_calc();}
			if(comboBox_op.getSelectedIndex() == 4) { Calculator.setOP_Sig(); }
			if(comboBox_op.getSelectedIndex() == 5) { Calculator.setOP_ver(); }
			Calculator.parseOP();		}
	});
	panel.add(comboBox_op);
	
	
	
	JLabel[] lbl_m = new JLabel[6];						// Alle Beschreibungs Label für die Merker
	for(int i=0; i<6;i++)
	{
		lbl_m[i] = new JLabel("memory field "+i+"  description                                                                                         M"+i);
		lbl_m[i].setFont(new Font("Arial", Font.PLAIN, 11));
		lbl_m[i].setBounds(20, 201+i*80, 482, 20);
		panel.add(lbl_m[i]);
	}
	
	
	
	
	JTextArea[] txt_beschreibung_m = new JTextArea[6];			// Alle Merker Beschreibungsfelder
	for(int i=0; i<6;i++)
	{	
		txt_beschreibung_m[i] = new JTextArea();
		txt_beschreibung_m[i].setToolTipText("editable field for the description of the memory");
		txt_beschreibung_m[i].setBounds(20, 220+i*80, 390, 50);
		panel.add(txt_beschreibung_m[i]);
	}
	
	
	
																							

	for(int i=0; i<12;i++)						// Die 12 unteren  "Merker-->"  Buttons werden erstellt
	{
		if(i%2==0) 
		{
			btn_m[i] = new JButton("<--A");
			btn_m[i].setBounds(440, (203+i*40), 50, 14);
			btn_m[i].setToolTipText("Save this memory field back to input A");
		}
		else 	
		{
			btn_m[i] = new JButton("<--B");
			btn_m[i].setToolTipText("Save this memory field back to input B");
			btn_m[i].setBounds(495, (203+(i-1)*40), 50, 14);	
		}		
		btn_m[i].setMargin(new Insets(0, 0, 0, 0));
		btn_m[i].setFont(new Font("Tahoma", Font.BOLD, 12));
		panel.add(btn_m[i]);
		btn_m[i].addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {  Calculator.saveFromMerkerTo(e);}});
		btn_m[i].putClientProperty("int", i); 			// Wird benötigt um die Button Nummer dem ActionEvent mit zu teilen.
	}
	
	
	
	
	JButton[] btn_del = new JButton[9];                				// Alle Delite Buttons werden erstellt
	for(int i=0; i<9;i++)
	{
		btn_del[i] = new JButton("x");
		btn_del[i].setMargin(new Insets(0, 0, 0, 0));
		btn_del[i].setFont(new Font("Tahoma", Font.BOLD, 13));
		btn_del[i].setToolTipText("deletes this field");
		if(i>2)btn_del[i].setBounds(724, 203+(i-3)*80, 20, 14);
		panel.add(btn_del[i]);
	}
	btn_del[0].setBounds(329, 62, 20, 14);
	btn_del[1].setBounds(724, 62, 20, 14);
	btn_del[2].setBounds(1124, 62, 20, 14);
	btn_del[0].addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {txt[0].setText("");txt[1].setText("");txt[18].setText("");}});
	btn_del[1].addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {txt[2].setText("");txt[3].setText("");}});
	btn_del[2].addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {txt[4].setText("");txt[5].setText("");}});
	btn_del[3].addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {txt[6].setText("");txt[7].setText("");}});
	btn_del[4].addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {txt[8].setText("");txt[9].setText("");}});
	btn_del[5].addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {txt[10].setText("");txt[11].setText("");}});
	btn_del[6].addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {txt[12].setText("");txt[13].setText("");}});
	btn_del[7].addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {txt[14].setText("");txt[15].setText("");}});
	btn_del[8].addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {txt[16].setText("");txt[17].setText("");}});
	
	
	
	JButton btn_delAll = new JButton("DEL");				// Löscht den Kompletten Rechner, alle Felder!
	btn_delAll.setToolTipText("deletes all fields of the calculator");
	btn_delAll.setBounds(1095, 11, 50, 20);
	btn_delAll.addActionListener(new ActionListener() 
	{
		public void actionPerformed(ActionEvent arg0) 
		{
			for(int i=0; i<19;i++) txt[i].setText("");
			for(int i=0; i<6;i++) txt_beschreibung_m[i].setText("");
		}
	});
	btn_delAll.setForeground(Color.RED);
	btn_delAll.setMargin(new Insets(0, 0, 0, 0));
	btn_delAll.setFont(new Font("Tahoma", Font.BOLD, 12));
	panel.add(btn_delAll);
	
	
	
	progressBar = new JProgressBar();
	progressBar.setBounds(814, 79, 331, 51);
	progressBar.setBorderPainted(false);
	progressBar.setBorder(null);
	progressBar.setIndeterminate(true);
	progressBar.setVisible(false);
	progressBar.setEnabled(false);
	progressBar.setOrientation(SwingConstants.VERTICAL);
	progressBar.setBackground(new Color(245, 245, 245));
	panel.add(progressBar);	
}

//------------------------------------------ ENDE GUI --------------------------------------------------------------//




//Startet die ProgressBar für die Visualisierung der Berechnungszeit
public static void runProgressBar()
{
	runProgressBar = true;		
	progressBar.setVisible(true);
	for(int i=0; i<300 && runProgressBar; i++)
	{
		try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}	
		progressBar.setValue(i);
	}
	progressBar.setVisible(false);
}
}
