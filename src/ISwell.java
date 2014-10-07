import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


public class ISwell {

	private JFrame frmIswellcoilCp;
	private JTextField txtStarted;
	private JTextField txtLastRun;
	private JTextField txtNextRun;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ISwell window = new ISwell();
					window.frmIswellcoilCp.setVisible(true);
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
	private void initialize() {
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
		
		txtStarted = new JTextField();
		txtStarted.setBounds(10, 50, 100, 20);
		frmIswellcoilCp.getContentPane().add(txtStarted);
		txtStarted.setColumns(10);
		
		txtLastRun = new JTextField();
		txtLastRun.setBounds(150, 50, 100, 20);
		frmIswellcoilCp.getContentPane().add(txtLastRun);
		txtLastRun.setColumns(10);
		
		txtNextRun = new JTextField();
		txtNextRun.setBounds(300, 50, 100, 20);
		frmIswellcoilCp.getContentPane().add(txtNextRun);
		txtNextRun.setColumns(10);
		
		final JTextArea txtDataLog = new JTextArea();
		txtDataLog.setBounds(10, 100, 200, 810);
		frmIswellcoilCp.getContentPane().add(txtDataLog);
		
		final JTextArea txtEmailLog = new JTextArea();
		txtEmailLog.setBounds(250, 100, 300, 810);
		frmIswellcoilCp.getContentPane().add(txtEmailLog);
		
		JLabel lblNewLabel_2 = new JLabel("Data Log : ");
		lblNewLabel_2.setBounds(10, 75, 100, 20);
		frmIswellcoilCp.getContentPane().add(lblNewLabel_2);
		
		final JTextArea txtSMSLog = new JTextArea();
		txtSMSLog.setBounds(600, 100, 300, 810);
		frmIswellcoilCp.getContentPane().add(txtSMSLog);
		
		JLabel lblNewLabel_4 = new JLabel("SMS Log : ");
		lblNewLabel_4.setBounds(600, 75, 100, 20);
		frmIswellcoilCp.getContentPane().add(lblNewLabel_4);
		
		
		JLabel lblNewLabel_3 = new JLabel("Email Log :");
		lblNewLabel_3.setBounds(250, 75, 100, 20);
		frmIswellcoilCp.getContentPane().add(lblNewLabel_3);
		
		JButton btnNewButton = new JButton("Go...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				txtStarted.setText(datehandler.getDate());
				//SubClass.StartHere(txtTimesRun);
				int i=0;
				while(i == 0){
					//System.out.println(datehandler.getDate());
					txtLastRun.setText(datehandler.getDate());
					//System.out.println(datehandler.getDate()) ;
		    		String cd = datehandler.getDate();
		    		//txtStarted.setText(cd);
		    		//check if you should download a new file
		    		boolean analyze;
		    		String XMLFile;
					try {
						//NewFile = filehandler.ShouldDownLoadNewFile(cd);
		    			String Path = "C:\\ISwell\\DownloadFolder";
		    			URL url = null;
		    			try {
		    				url = new URL("http://www.ims.gov.il/ims/PublicXML/isr_sea.xml");
		    			} catch (MalformedURLException e) {
		    				e.printStackTrace();
		    			}
		    			XMLFile = filehandler.DownLoadFile(cd + ".xml", Path , url);
		    			analyze = SubClass.shouldanalyzexml(XMLFile);
		    			if(analyze == true){
		    				xmlhandler.XMLParser(XMLFile);
		    				txtDataLog.insert("Data update on : " + cd + "\n", 0);
		    			}else{
		    				File file = new File(XMLFile);
		    				file.delete();
		    			}
						dbhandler.ReadEmailListFromDataBase(txtEmailLog , txtSMSLog);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					// 3600 for an hour
					// 1800 for an half an hour
					// 900 for 15 min
					// 450 for 7.5 min
					// 300 for 5 min
					for(int a=300 ; a>0 ; a--){
//						if(txtEmailLog.getLineCount()>50){
//							txtEmailLog.repla
//						}
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
	}
}
