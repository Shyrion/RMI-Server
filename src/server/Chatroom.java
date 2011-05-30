package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import client.ItfClient;
import client.SPClient;

@SuppressWarnings("serial")
public class Chatroom extends UnicastRemoteObject implements IChatroom {
	private ArrayList<SPClient> users;
	private String name;
	private final String password = "t";

	public Chatroom() throws RemoteException {
		super();
		users = new ArrayList<SPClient>();
	}

	public Chatroom(String name) throws RemoteException {
		super();
		this.name = name;
		users = new ArrayList<SPClient>();
	}

	public SPClient getClient(String name) throws RemoteException {
		for (SPClient c : users) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	@Override
	public boolean login(SPClient client, String pwd) throws RemoteException {
		try {
			if (pwd.equals(password)) {
				if (users.size() > 0) {
					for (SPClient c : users) {
						if (client.getName().equals(c.getName())) {
							return false;
						}
					}
					for (SPClient c : users) {
						client.notifyConnect(c);
					}
				}
				users.add(client);
				notifyAllConnect(client);
			} else {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean logout(SPClient client) throws RemoteException {
		try {
			boolean found = false;
			for (SPClient c : users) {
				if (c.getName().equals(client.getName())) {
					users.remove(c);
					found = true;
				}
			}
			if (found) {
				notifyAllDisconnect(client.getName());
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean broadCastMessage(String sender, String message) throws RemoteException {
		try {
			notifyAllMessage(sender, message);
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

	private void notifyAllConnect(SPClient client) throws RemoteException {
		for (SPClient c : users) {
			try {
				c.notifyConnect(client);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	private void notifyAllDisconnect(String message) throws RemoteException {
		for (ItfClient i : users) {
			try {
				i.notifyDisconnect(message);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}

	private void notifyAllMessage(String sender, String message) throws RemoteException {
		for (ItfClient i : users) {
			try {
				i.notifyMessage(sender, message);
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
}