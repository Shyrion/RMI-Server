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

	ArrayList<IClient> users;
	String name;
	
	protected Chatroom() throws RemoteException {
		super();
		users = new ArrayList<IClient>();
	}
	
	protected Chatroom(String name) throws RemoteException {
		super();
		this.name = name;
		users = new ArrayList<IClient>();
	} 
	
	public static void main(String[] args){
		try {
			Registry reg = LocateRegistry.getRegistry();
			Distante obj = new Chatroom("Chatroom 1");
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

		try{
			users.add(client);
			client.setName(login);
			notifyAll(client.getName() +  " has join the channel");
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	private void notifyAll(String message) throws RemoteException {
		for(IClient i : users){
			i.notify(message);
			System.out.println(i.getName() + ", ");
		}
	}

	public boolean logout(IClient client) throws RemoteException {
		try {
			client.notify("Thanks for coming !");
			users.remove(client);
			notifyAll(client.getName() +  " has left the channel");
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
}
