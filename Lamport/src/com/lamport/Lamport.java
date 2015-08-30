package com.lamport;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;
//import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

public class Lamport implements Runnable{
	public static OutgoingQueueHandler queuehandler;
	public boolean SendMessage;
	public int state;
	public final static int running = 0;
	public final static int record = 1;
	private int processid;
	public int money;
	public boolean StateRecorded;
	public static int me;
	 public int[][]  LastMessage = new int[2][3];
	  public int[][]  Recorded    = new int[2][3];
	  public int[][]  Marker      = new int[2][3];
	public LinkedBlockingQueue<MessageTransfer> InQueue;
	private int Receive = 0;
	private int Send = 1;
	public Lamport(){}
	public Lamport(int processid,int money){
		this.processid = processid;
		me = this.processid;
		//this.port = port;
		this.money = money;
		InQueue = new LinkedBlockingQueue<MessageTransfer>();
		initialize();
	}
	public void initialize() {
		// TODO Auto-generated method stub
		int i;
		for(i = 0;i < 3;i++)
		{
			this.LastMessage[Receive][i] = 0;
			this.LastMessage[Send][i] = 0;
			this.Recorded[Receive][i] = 0;
			this.Recorded[Send][i] = 0;
			this.Marker[Receive][i] = -1;
			this.Marker[Send][i] = -1;
		}
		this.StateRecorded = false;
		this.state = running;
		this.SendMessage = true;
	}
	/*
	 * Handling of messages when there is normal transfer of money.
	 */
	public void running(MessageTransfer msg){
		//System.out.println("Running method is called in process "+this.processid);
		if(msg.code == 1)
		{
			if(this.StateRecorded){
				
				this.Recorded[Receive][msg.src_id] = this.LastMessage[Receive][msg.src_id];
				this.Marker[Receive][msg.src_id] = 1;
			}
			else{
				this.SendMessage = false;// Set to false for not sending normal money transfers untill the marker messages are sent.
				this.Recorded[Receive][this.processid] = this.money;
				System.out.println("Current Money "+this.money);
				Display(this.money);
				//System.out.println(this.money);// Display this with Konto (this.processid + 1)
				if(!msg.SnapShot) //Distinguish between the marker messages initialization or the subsequent ones.
				{
				  
				  this.Recorded[Receive][msg.src_id] = 0;
				
				  this.Marker[Receive][msg.src_id] = 1;
				}
				SendMarker();
				this.StateRecorded = true;
				this.state = record;
				//return this.money;
			}
		}
		else
		{
			System.out.println(" Receiving Money at "+this.processid+"money received "+msg.data);
			this.money = this.money + msg.data;
			this.LastMessage[Receive][msg.src_id] = msg.data;
			//return 0;
		}
		//return 0;
	}
	/*
	 * Display the current state on the Gui
	 */
	public void Display(int currentmoney){
		//System.out.println("Display method called in process "+this.processid);
		switch(this.processid){
		case 0: Gui.konto1CurrBalance.setText(currentmoney+Gui.euro);
			    break;
		case 1: Gui.konto2CurrBalance.setText(currentmoney+Gui.euro);
				break;
		case 2: Gui.konto3CurrBalance.setText(currentmoney+Gui.euro);
				break;
		}
		
		Integer bal = Gui.totalBalance();
		Gui.totalCurrBalance.setText(bal+Gui.euro);
	}
	/*
	 * Record the states of the processes and the channels
	 */
	public void record(MessageTransfer msg){
		if(msg.SnapShot) return;
		if(msg.code == 1)
		{
			if(this.StateRecorded)
			{
				this.Recorded[Receive][msg.src_id] = LastMessage[Receive][msg.src_id];
				this.Marker[Receive][msg.src_id] = 1;
				if(finishedSnapshot())
				{
					System.out.println("Initializing the process "+this.processid);
					initialize();
				}
			}
		}
		else
		{
			System.out.println(" Receiving Money at "+this.processid+"money received "+msg.data);
			this.LastMessage[Receive][msg.src_id] = msg.data;
			this.money = this.money + msg.data;
		}
	}
	/*
	 * Check if the snapshot has finished or not
	 */
	public boolean finishedSnapshot(){
		//if(!StateRecorded) return false;
		for(int i = 0;i < 3;i++)
			if(i != this.processid)
				if((this.Marker[Receive][i] < 0) || (this.Marker[Send][i] < 0))
					return false;
		return true;
		
	}
	/*
	 * Receive all the messages to this process
	 */
	public void Receive(MessageTransfer msg){
		System.out.println("Received by"+this.processid);
		//System.out.println("Received packet from "+msg.src_id);
		//System.out.println("Message Code : "+msg.code);
		//if(msg.src_id == this.processid) return;
		if((this.StateRecorded) && (msg.SnapShot)) return;
		//System.out.println("State of the machine :"+this.state);
		switch(this.state){
		case running: running(msg);
				      break;
		case record: record(msg);
	                 break;
		}
	}
	/*
	 * Send the marker messages to other processes.
	 */
	private void SendMarker()
	{
		//System.out.println("Value of me: "+me);
		for(int i = 0;i < 3;i++)
		{
			if(i != this.processid)
			{
				//System.out.println("Current Iteration : "+i);
				MessageTransfer msg = new MessageTransfer(false,1,0,this.processid,i);
				queuehandler.Enqueuemessage(msg);
				this.Marker[Send][i] = 1;
			}
		}
		this.SendMessage = true;
	}
	/*
	public static void main(String[] args) throws SocketException {
		// TODO Auto-generated method stub
      Lamport acc1 = new Lamport(0,5000,200);
      DatagramSocket Socket = new DatagramSocket(acc1.port);
      
	}
*/
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			DatagramSocket SocketSend = new DatagramSocket(9000 + this.processid * 2);
			int m = 0;
			OutgoingQueueHandler q = new OutgoingQueueHandler(SocketSend,this);
			queuehandler = q;
			Thread t = new Thread(q);
			t.start();
			DatagramSocket SocketReceive = new DatagramSocket(5000 + this.processid * 2 + 1);
			IncomingQueueHandler I = new IncomingQueueHandler(SocketReceive,this);
			Thread t1 = new Thread(I);
			t1.start();
			while(true){
			
				try {
					int randomDelay = 500 + new Random().nextInt(2000); // delay is kept between 500 ms to 2000 ms
					Thread.sleep(randomDelay);
					//System.out.println("Random Delay:"+randomDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				int randomMoney = 1 + new Random().nextInt(100); // random money between 1 and 100
				
				System.out.println("randomMoney="+randomMoney);
				if(this.SendMessage)
				{
				if(this.money - randomMoney > 0)
				{
					Random n = new Random();
					m = n.nextInt(3);
					if(m != this.processid)
					{
						MessageTransfer msg = new MessageTransfer(false,0,randomMoney,this.processid,m);
						q.Enqueuemessage(msg);
					}
				}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
