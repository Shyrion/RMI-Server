package client;

import java.rmi.Remote;

public interface IClient extends Remote{

	void notify(String string);

}
