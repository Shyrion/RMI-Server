package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import client.SPClient;

public class LoginModule extends UnicastRemoteObject implements ILoginModule {
	private IChatroom chatroom;

	public LoginModule()  throws RemoteException {
		chatroom = new Chatroom("Chatroom 1");
	}

	public static void main(String[] args) {
		try {
			Registry reg = LocateRegistry.getRegistry();
			LoginModule loginModule = new LoginModule();
			reg.rebind("LoginModule", loginModule);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public IChatroom login(SPClient client, String pwd) throws RemoteException {
		if (chatroom.login(client, pwd)) {
			return chatroom;
		}
		return null;
	}

}
