package com.lamport;

import java.io.Serializable;

public class MessageTransfer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	boolean SnapShot;
	int code;// Code is 0 for Normal message. 1 for Marker.
	int data;
	int src_id;
	int dest_id;
	public MessageTransfer(boolean Snapshot,int code,int data,int src_id,int dest_id){
		this.SnapShot = Snapshot;
		this.code = code;
		this.data = data;
		this.src_id = src_id;
		this.dest_id = dest_id;
	}
}
