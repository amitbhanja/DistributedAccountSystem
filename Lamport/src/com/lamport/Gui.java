package com.lamport;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class Gui extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static JFrame frame;
	public static JPanel markerPanel;
	
	public static JSeparator separator;
	public static JLabel heading;
	
	public static JLabel konto1; // for displaying text as "konto#" on GUI
	public static JLabel konto2;
	public static JLabel konto3;
	public static JLabel totalBalance;
	
	public static JLabel konto1CurrBalance; // for displaying current balance when markers are sent
	public static JLabel konto2CurrBalance; // and snapshots are taken. Also, use 
	public static JLabel konto3CurrBalance; // konto#CurrBalance.setText(whatEverBalance + euro);
	public static JLabel totalCurrBalance;
	
	public static JButton konto1SnapshotButton; // Snapshot buttons for respective kontos'
	public static JButton konto2SnapshotButton;
	public static JButton konto3SnapshotButton;
	
	public static JTextArea textArea; // Text Area to display account movement information
	public static JScrollPane scrollPane;
	
	public static String euro = " €";
	public static String newline = "\n";
	
	public static Lamport p1, p2, p3;
	public static Thread t1, t2, t3; // 3 threads for each Lamport instances
	
	//public static CyclicBarrier barrier;
	
	
	public static void addComponentsToPane(final Container pane) {
		
		System.out.println("STARTED: addComponentsToPane");
		
		markerPanel = new JPanel(null);	
		
		heading = new JLabel("SnapShot-Alogrithm by Chandy and Lamport");
		heading.setFont(new Font("", Font.BOLD, 18));
		heading.setBounds(55,30,390,80);
		markerPanel.add(heading);
		
		textArea = new JTextArea(5, 30);
		scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		scrollPane.setBounds(50, 110, 550, 400);
		// textArea.append(text + "\n");
		markerPanel.add(scrollPane);
		
		separator = new JSeparator(SwingConstants.VERTICAL);  
		separator.setBounds(640, 50, 1, 500);
		markerPanel.add(separator);
		
		/**
		 * 
		 * Konto1 information.
		 * Use konto1CurrBalance.setText(balance + euro); to dynamically display values
		 */
		
		konto1 = new JLabel("konto1");
		konto1.setFont(new Font("", Font.BOLD, 12));
		konto1.setBounds(680, 80, 200, 100);
		markerPanel.add(konto1);
		
		konto1CurrBalance = new JLabel("0.00" + euro);
		konto1CurrBalance.setText("3000" + euro);
		konto1CurrBalance.setBounds(750, 80, 200, 100);
		markerPanel.add(konto1CurrBalance);
		
		konto1SnapshotButton = new JButton("Snapshot");
		konto1SnapshotButton.setBounds(750, 140, 100, 25);
		markerPanel.add(konto1SnapshotButton);
		
		konto1SnapshotButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("konto1 Snapshot Button clicked");
				
				MessageTransfer msg = new MessageTransfer(true, 1, 0, 20, 20);
				p1.Receive(msg);
				
				//Integer bal = 0;
				//bal = totalBalance();
				
				// totalCurrBalance.setText(bal+euro);
				
				// konto1CurrBalance.setText(money + euro);
				// textArea.append("Transfer:k1-> (" + money + euro + ")" + newline);
												
			}
		});
		
		
		/**
		 * 
		 * Konto1 information.
		 * Use konto2CurrBalance.setText(balance + euro); to dynamically display values
		 */
		
		konto2 = new JLabel("konto2");
		konto2.setFont(new Font("", Font.BOLD, 12));
		konto2.setBounds(680, 160, 200, 100);
		markerPanel.add(konto2);
		
		konto2CurrBalance = new JLabel("0.00" + euro);
		konto2CurrBalance.setText("5000" + euro);
		konto2CurrBalance.setBounds(750, 160, 200, 100);
		markerPanel.add(konto2CurrBalance);
		
		konto2SnapshotButton = new JButton("Snapshot");
		konto2SnapshotButton.setBounds(750, 220, 100, 25);
		markerPanel.add(konto2SnapshotButton);
		
		konto2SnapshotButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("konto2 Snapshot Button clicked");
				
				MessageTransfer msg = new MessageTransfer(true, 1, 0, 20, 20);
				p2.Receive(msg);
				
				// Integer bal = 0;
				// bal = totalBalance();
				
				// totalCurrBalance.setText(bal+euro);
				
				// konto2CurrBalance.setText("500.00" + euro);
				// textArea.append("This is from konto2" + newline);
			}
		});
		
		
		/**
		 * 
		 * Konto3 information.
		 * Use konto3CurrBalance.setText(balance + euro); to dynamically display values
		 */
		
		konto3 = new JLabel("konto3");
		konto3.setFont(new Font("", Font.BOLD, 12));
		konto3.setBounds(680, 240, 200, 100);
		markerPanel.add(konto3);
		
		konto3CurrBalance = new JLabel("0.00" + euro);
		konto3CurrBalance.setText("7000" + euro);
		konto3CurrBalance.setBounds(750, 240, 200, 100);
		markerPanel.add(konto3CurrBalance);
		
		konto3SnapshotButton = new JButton("Snapshot");
		konto3SnapshotButton.setBounds(750, 300, 100, 25);
		markerPanel.add(konto3SnapshotButton);
		
		konto3SnapshotButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("konto3 Snapshot Button clicked");
				
				MessageTransfer msg = new MessageTransfer(true, 1, 0, 20, 20);
				p3.Receive(msg);
				
				// Integer bal = 0;
				// bal = totalBalance();
				
				// totalCurrBalance.setText(bal+euro);
				
				//konto3CurrBalance.setText("8000.00" + euro);
				//textArea.append("This is from konto3" + newline);
												
			}
		});
		
		
		totalBalance = new JLabel("Balance");
		totalBalance.setFont(new Font("", Font.BOLD, 12));
		totalBalance.setBounds(680, 320, 200, 100);
		markerPanel.add(totalBalance);
		
		totalCurrBalance = new JLabel("0.00" + euro);
		totalCurrBalance.setText("15000" + euro);
		totalCurrBalance.setBounds(750, 320, 200, 100);
		markerPanel.add(totalCurrBalance);
		
		pane.add(markerPanel);
		
		
	} // End of addComponentsToPane()
	
	public static Integer totalBalance() {
				
		String k1 = konto1CurrBalance.getText();
		String k2 = konto2CurrBalance.getText();
		String k3 = konto3CurrBalance.getText();
				
		String[] str = k1.split(" ");
		final Integer k1_Balance = Integer.parseInt(str[0]);
		str = k2.split(" ");
		final Integer k2_Balance = Integer.parseInt(str[0]);
		str = k3.split(" ");
		final Integer k3_Balance = Integer.parseInt(str[0]);	
		
		return k1_Balance+k2_Balance+k3_Balance;
	}
	
	private static void createAndShowGUI() {
		System.out.println("STARTED: createAndShowUI");
    	
    	frame = new JFrame("Lamport");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponentsToPane(frame.getContentPane());
        frame.pack();

        Insets insets = frame.getInsets();
        frame.setSize(new Dimension(insets.left + insets.right + 1800,
                insets.top + insets.bottom + 600));
        
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
        System.out.println("ENDED: createAndShowUI");
        
        p1 = new Lamport(0, 3000);
        p2 = new Lamport(1, 5000);
        p3 = new Lamport(2, 7000);
        
        t1 = new Thread(p1);
        t1.start();
        
        t2 = new Thread(p2);
        t2.start();
        
        t3 = new Thread(p3);
        t3.start();
        
        System.out.println("Completed creation of 3 Lamport instaces and threads");
        
	} //End of createAndShowGUI()
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("calling createAndShowGUI()");
		createAndShowGUI();

	}

}
