import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class Gui extends JFrame {

	private JPanel contentPane;
	private JTextField txtToday;
	private JTextField txtDataUpdate;
	private JTextField txtTimesRun;
	protected Timer timer;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	    
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				System.out.println("Starting");
				
				try {
					Gui frame = new Gui();
					frame.setExtendedState(frame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
	}

	/**
	 * Create the frame.
	 */
	public Gui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 754, 724);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblWwwiswellcoil = new JLabel("www.ISwell.co.il");
		lblWwwiswellcoil.setFont(new Font("David", Font.BOLD, 30));
		lblWwwiswellcoil.setBounds(247, 11, 237, 63);
		contentPane.add(lblWwwiswellcoil);
		
		JLabel lblTodayIs = new JLabel("Last run was on : ");
		lblTodayIs.setBounds(10, 80, 150, 15);
		contentPane.add(lblTodayIs);
		
		JLabel lblDatabaseLastUpdated = new JLabel("DataBase last updated in : ");
		lblDatabaseLastUpdated.setBounds(10, 110, 150, 15);
		contentPane.add(lblDatabaseLastUpdated);
		
		txtToday = new JTextField();
		txtToday.setHorizontalAlignment(SwingConstants.CENTER);
		txtToday.setEditable(false);
		txtToday.setBounds(160, 80, 90, 20);
		contentPane.add(txtToday);
		txtToday.setColumns(10);
		//textFieldToday.setText(SubClass.getDate());
		txtToday.setText(datehandler.getDate());
		
		
		txtDataUpdate = new JTextField();
		txtDataUpdate.setHorizontalAlignment(SwingConstants.CENTER);
		txtDataUpdate.setEditable(false);
		txtDataUpdate.setBounds(160, 110, 90, 20);
		contentPane.add(txtDataUpdate);
		txtDataUpdate.setColumns(10);
		
		JButton btnGO = new JButton("GO!!!");
		btnGO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SubClass.StartHere(txtTimesRun);
		    }
		});
		
		
		
		btnGO.setBounds(280, 650, 150, 25);
		contentPane.add(btnGO);
		
		JTextArea textAreaLogData = new JTextArea();
		textAreaLogData.setBounds(10, 300, 350, 300);
		contentPane.add(textAreaLogData);
		
		JLabel lblNewLabel = new JLabel("DataLog");
		lblNewLabel.setBounds(10, 280, 80, 20);
		contentPane.add(lblNewLabel);
		
		JTextArea textAreaLog = new JTextArea();
		textAreaLog.setBounds(370, 300, 350, 300);
		contentPane.add(textAreaLog);
		
		JLabel lblNewLabel_1 = new JLabel("EmailLog");
		lblNewLabel_1.setBounds(370, 280, 80, 20);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Times run : ");
		lblNewLabel_2.setBounds(300, 80, 80, 20);
		contentPane.add(lblNewLabel_2);
		
		
		txtTimesRun = new JTextField();
		txtTimesRun.setHorizontalAlignment(SwingConstants.CENTER);
		txtTimesRun.setText("0");
		txtTimesRun.setEditable(false);
		txtTimesRun.setBounds(380, 80, 90, 20);
		contentPane.add(txtTimesRun);
		txtTimesRun.setColumns(10);
		
	}
	
}
	
