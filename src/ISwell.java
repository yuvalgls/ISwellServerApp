import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;


public class ISwell {

	 static JFrame frmIswellcoilCp = new JFrame();
	 static JTextField txtStarted = new JTextField();
	 static JTextField txtLastRun = new JTextField();
	 static JTextField txtNextRun = new JTextField();
	 static JTextArea txtEmailLog = new JTextArea();
	 static JTextArea txtSMSLog = new JTextArea();
	 static JTextArea txtDataLog = new JTextArea();
	 static JButton btnNewButton = new JButton();
//JTextArea
	/**
	 * Launch the application.
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					ISwell window = new ISwell();
					ISwell.frmIswellcoilCp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ISwell() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private static void initialize() {
		frmIswellcoilCp = new JFrame();
		frmIswellcoilCp.setTitle("ISwell.co.il CP");
		frmIswellcoilCp.setBounds(100, 100, 950, 950);
		frmIswellcoilCp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmIswellcoilCp.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Last run on : ");
		lblNewLabel.setBounds(150, 20, 100, 20);
		frmIswellcoilCp.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Next run in : ");
		lblNewLabel_1.setBounds(300, 20, 100, 20);
		frmIswellcoilCp.getContentPane().add(lblNewLabel_1);
		
		JLabel lblStartedOn = new JLabel("Started on : ");
		lblStartedOn.setBounds(10, 20, 100, 20);
		frmIswellcoilCp.getContentPane().add(lblStartedOn);
		
		final JTextField txtStarted = new JTextField();
		txtStarted.setText("testing");
		txtStarted.setEditable(false);
		txtStarted.setBounds(10, 50, 100, 20);
		frmIswellcoilCp.getContentPane().add(txtStarted);
		txtStarted.setColumns(10);
		
		txtLastRun = new JTextField();
		txtLastRun.setEditable(false);
		txtLastRun.setBounds(150, 50, 100, 20);
		frmIswellcoilCp.getContentPane().add(txtLastRun);
		txtLastRun.setColumns(10);
		
		txtNextRun = new JTextField();
		txtNextRun.setEditable(false);
		txtNextRun.setBounds(300, 50, 100, 20);
		frmIswellcoilCp.getContentPane().add(txtNextRun);
		txtNextRun.setColumns(10);
		
		final JTextArea txtDataLog = new JTextArea();
		txtDataLog.setEditable(false);
		txtDataLog.setBounds(10, 100, 300, 810);
		frmIswellcoilCp.getContentPane().add(txtDataLog);
		
		final JTextArea txtEmailLog = new JTextArea();
		txtEmailLog.setEditable(false);
		txtEmailLog.setBounds(320, 100, 300, 810);
		frmIswellcoilCp.getContentPane().add(txtEmailLog);
		
		JLabel lblNewLabel_2 = new JLabel("Data Log : ");
		lblNewLabel_2.setBounds(10, 75, 100, 20);
		frmIswellcoilCp.getContentPane().add(lblNewLabel_2);
		
		final JTextArea txtSMSLog = new JTextArea();
		txtSMSLog.setEditable(false);
		txtSMSLog.setBounds(630, 100, 300, 810);
		frmIswellcoilCp.getContentPane().add(txtSMSLog);
		
		JLabel lblNewLabel_4 = new JLabel("SMS Log : ");
		lblNewLabel_4.setBounds(630, 75, 100, 20);
		frmIswellcoilCp.getContentPane().add(lblNewLabel_4);
		
		
		JLabel lblNewLabel_3 = new JLabel("Email Log :");
		lblNewLabel_3.setBounds(320, 75, 100, 20);
		frmIswellcoilCp.getContentPane().add(lblNewLabel_3);
		
		JButton btnNewButton = new JButton("Go...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				txtStarted.setText(datehandler.getDate());
				//SubClass.StartHere(txtTimesRun);
				int i=0;
				while(i == 0){
					txtLastRun.setText(datehandler.getDate());
		    		String cd = datehandler.getDate();
		    		//check if you should download a new file
		    		boolean analyze;
		    		String XMLFile;
					try {
						String Path = null;
		    			URL url = new URL("http://www.ims.gov.il/ims/PublicXML/isr_sea.xml");
		    				//sea updates
	    				if(SubClass.IsProgrammer()){
							Path = "C:\\ISwell\\DownloadFolder\\isramar\\isr_sea\\";
						}else{
							Path = "/root/ISwell/DownloadFolder/isramar/isr_sea/";
						}
	    				// insted of downloading rightahead we beed to start firefox with the path
		    			XMLFile = filehandler.DownLoadFile(cd + ".xml", Path , url);
		    			analyze = SubClass.shouldanalyzexml(XMLFile);
		    			if(analyze == true){
		    				xmlhandler.SeaXMLParser(XMLFile);
		    				txtDataLog.insert("Data update on : " + cd + "\n", 0);
//		    				Socialhandler.PostToFacebook();
		    			}else{
		    				File file = new File(XMLFile);
		    				file.delete();
		    			}
		    			//Weather update for the rss feed
		    			//Weather Forecast for Israel
		    			url = new URL("http://www.ims.gov.il/ims/PublicXML/isr_country.xml");
		    			if(SubClass.IsProgrammer()){
							Path = "C:\\ISwell\\DownloadFolder\\isramar\\isr_weather\\";
						}else{
							Path = "/root/ISwell/DownloadFolder/isramar/isr_weather/";
						}
//		    			XMLFile = filehandler.DownLoadFile(cd + ".xml", Path , url);
		    			filehandler.download(String.valueOf(url), Path + cd + ".xml");
//		    			analyze = SubClass.shouldanalyzeweatherxml(XMLFile);
		    			analyze = false;
		    			if(analyze == true){
		    				xmlhandler.weatherXMLParser(XMLFile);
		    				txtDataLog.insert("Data update on : " + cd + "\n", 0);
//		    				Socialhandler.PostToFacebook();
		    			}else{
//		    				File file = new File(XMLFile);
//		    				file.delete();
		    			}
		    			//start analize clients
						dbhandler.ReadEmailListFromDataBase(txtEmailLog , txtSMSLog);
						SubClass.chackforwarnings(txtEmailLog, txtDataLog);
//						long range forcast
						if(SubClass.IsProgrammer()){
							SubClass.checkLongRangForcast(txtDataLog, txtEmailLog);
						}else{
//							System.out.println(cd.substring(8) + ">= 700 AND " + cd.substring(8) + "<= 705");
							if(Integer.valueOf(cd.substring(8)) >= 700 && Integer.valueOf(cd.substring(8)) <= 705){
			    				SubClass.checkLongRangForcast(txtDataLog, txtEmailLog);
		    				}
						}
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					// 3600 for an hour
					// 1800 for an half an hour
					// 900 for 15 min
					// 450 for 7.5 min
					// 300 for 5 min
					for(int a=300 ; a>0 ; a--){
						frmIswellcoilCp.update(frmIswellcoilCp.getGraphics());
						if(a%60>=10){
							txtEmailLog.update(txtEmailLog.getGraphics());
							txtNextRun.setText(String.valueOf(a/60 + ":" + a%60));
							txtNextRun.update(txtNextRun.getGraphics());
						}else{
							txtEmailLog.update(txtEmailLog.getGraphics());
							txtNextRun.setText(String.valueOf(a/60 + ":0" + a%60));
							txtNextRun.update(txtNextRun.getGraphics());
						}
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e2) {
							e2.printStackTrace();
						}
					}
					
				}
				}
			});
		btnNewButton.setBounds(420, 45, 100, 30);
		frmIswellcoilCp.getContentPane().add(btnNewButton);
		
		JLabel label = new JLabel("long range and warnings");
		label.setBounds(550, 50, 300, 20);
		frmIswellcoilCp.getContentPane().add(label);	
		
		//set ISwell Visible and press "go" on startup
		ISwell.frmIswellcoilCp.setVisible(true);
		btnNewButton.doClick();
	}
}
