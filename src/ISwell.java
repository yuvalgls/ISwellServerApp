import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
		frmIswellcoilCp.setBounds(100, 100, 552, 555);
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
		txtDataLog.setBounds(10, 100, 200, 350);
		frmIswellcoilCp.getContentPane().add(txtDataLog);
		
		final JTextArea txtEmailLog = new JTextArea();
		txtEmailLog.setBounds(300, 100, 200, 350);
		frmIswellcoilCp.getContentPane().add(txtEmailLog);
		
		JLabel lblNewLabel_2 = new JLabel("Data Log : ");
		lblNewLabel_2.setBounds(10, 75, 100, 20);
		frmIswellcoilCp.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Email Log :");
		lblNewLabel_3.setBounds(300, 75, 100, 20);
		frmIswellcoilCp.getContentPane().add(lblNewLabel_3);
		
		JButton btnNewButton = new JButton("Go...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				

				//SubClass.StartHere(txtTimesRun);
				int i=0;
				while(i == 0){
					//System.out.println(datehandler.getDate());
					txtLastRun.setText(datehandler.getDate());
					System.out.println(datehandler.getDate()) ;
		    		String cd = datehandler.getDate();
		    		txtStarted.setText(cd);
		    		//check if you should download a new file
		    		boolean NewFile;
					try {
						NewFile = filehandler.ShouldDownLoadNewFile(cd);
						if(NewFile == true ){
			    			String Path = "C:\\ISwell\\DownloadFolder";
			    			URL url = null;
			    			try {
			    				url = new URL("http://www.ims.gov.il/ims/PublicXML/isr_sea.xml");
			    			} catch (MalformedURLException e) {
			    				e.printStackTrace();
			    			}
			    			//download the file and store the data on the mysql server
			    			filehandler.DownLoadFile(cd + ".xml", Path , url);
			    			System.out.println("Xml was downloaded to : " + Path + "\\" + cd + ".xml");
			    			txtDataLog.append("Data update on : " + cd);
			    			//dbhandler.ReadFromDataBase();
			    		}
						dbhandler.ReadEmailListFromDataBase(txtEmailLog);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					for(int a=3600 ; a>0 ; a--){
						frmIswellcoilCp.update(frmIswellcoilCp.getGraphics());
						txtEmailLog.update(txtEmailLog.getGraphics());
						txtNextRun.setText(String.valueOf(a));
						txtNextRun.update(txtNextRun.getGraphics());
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
