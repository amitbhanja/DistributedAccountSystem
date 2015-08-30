package com.lamport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;

public class OutgoingQueueHandler implements Runnable{

//	private static final long millis = 1000*20;
	public DatagramSocket socket;
	public LinkedBlockingQueue<MessageTransfer> Queue;
	public Lamport l;
	public OutgoingQueueHandler(DatagramSocket Socket,Lamport l){
		this.socket = Socket;
		this.l = l;
		Queue = new LinkedBlockingQueue<MessageTransfer>();
	}
	public void Enqueuemessage(MessageTransfer msg){
		Queue.add(msg);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			/*
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			MessageTransfer m = null ;
			if(!Queue.isEmpty())
			{
				try {
					m = Queue.take();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				ObjectOutputStream out = null;
				try {
					out = new ObjectOutputStream(buffer);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					out.writeObject(m);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					out.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					buffer.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				DatagramPacket d = null;
				try {
					d = new DatagramPacket(buffer.toByteArray(),buffer.size(),InetAddress.getLocalHost(),5000 + m.dest_id*2 + 1);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					this.socket.send(d);
					if(m.code == 0)
					{
						Gui.textArea.append("Transfer:k"+(m.src_id+1)+"->k"+(m.dest_id+1)+"("+m.data+")"+"\n");
						System.out.println("Transfer:k"+(m.src_id+1)+"->k"+(m.dest_id+1)); //Display on the screen.
					}
					else
					{
						Gui.textArea.append("Mark:k"+(m.src_id+1)+"->k"+(m.dest_id+1)+"\n");
						System.out.println("Mark:k"+(m.src_id+1)+"->k"+(m.dest_id+1));// Display on the screen
					}
					if(m.code == 0)
						this.l.money = this.l.money - m.data; //10 is changed to m.data
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
