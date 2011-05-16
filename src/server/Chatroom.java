package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import client.IClient;

public class Chatroom extends UnicastRemoteObject implements Distante {

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

	public static void main(String[] args) {
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
		try {
			client.setName(login);
			if (users.size() > 0) {
				for (IClient c : users) {
					client.notifyConnect(c.getName());
				}
			}
			users.add(client);
			notifyAllConnect(client.getName());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean logout(IClient client) throws RemoteException {
		try {
			if (users.contains(client)) {
				notifyAllDisconnect(client.getName());
				users.remove(client);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean sendPrivateMessage(IClient sender, IClient receiver, String message)
	          throws RemoteException {
		try {
			sender.notifyMessage(sender.getName(), message);
			receiver.notifyMessage(sender.getName(), message);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean broadCastMessage(IClient sender, String message) throws RemoteException {
		try {
			notifyAllMessage(sender.getName(), message);
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

	private void notifyAllConnect(String message) throws RemoteException {
		for (IClient i : users) {
			try {
				i.notifyConnect(message);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	private void notifyAllDisconnect(String message) throws RemoteException {
		for (IClient i : users) {
			try {
				i.notifyDisconnect(message);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	private void notifyAllMessage(String sender, String message) throws RemoteException {
		for (IClient i : users) {
			try {
				i.notifyMessage(sender, message);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
}