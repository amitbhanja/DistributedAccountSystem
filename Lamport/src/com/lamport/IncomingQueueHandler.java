package com.lamport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class IncomingQueueHandler implements Runnable {

	public DatagramSocket socket;
	public Lamport l;
	public IncomingQueueHandler(DatagramSocket Socket,Lamport l){
		this.l = l;
		this.socket = Socket;
	}
	@Override
	public void run() {
		while(true){
			byte[] buffer = new byte[1000];
			DatagramPacket packet;
			packet = new DatagramPacket(buffer,buffer.length);
			try {
				this.socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
		      ObjectInputStream oos = null;
			try {
				oos = new ObjectInputStream(baos);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			MessageTransfer m = null;
		      try {
				 m = (MessageTransfer)oos.readObject();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    this.l.Receive(m);
		}
	}

}
