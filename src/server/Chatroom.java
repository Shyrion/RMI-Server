package server;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

import client.IClient;


public class Chatroom extends UnicastRemoteObject implements Distante{

	ArrayList<String> users;
	
	protected Chatroom() throws RemoteException {
		super();
		users = new ArrayList<String>();
	} 
	
	public static void main(String[] args){
		try {
			Registry reg = LocateRegistry.getRegistry();
			Distante obj = new Chatroom();
			reg.rebind("cr1", obj);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String echo() throws RemoteException {
		System.out.println("Echo depuis le serveur !");
		return "Salut !";
	}

	@Override
	public boolean login(String login, String password, IClient client) throws RemoteException {
		System.out.println(login);
		client.notify("Welcome to the chatroom " + this);
		return true;
	}
	
	public boolean logout(String login) throws RemoteException {
		for(int i=0; i<users.size(); i++){
			if (users.get(i).equals(login)) users.remove(i);
			System.out.println("Users " + login + " logout !");
			return true;
		}
		return false;
	}
	
}
