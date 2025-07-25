package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.core.utils.GameSessionManager;
import it.polimi.ingsw.network.rmi.server.GameServerImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This class is responsible for starting the RMI server.
 * It creates or retrieves the RMI registry on port 1099 and binds the GameServer implementation to it.
 */
public class RMIServerApp {
    private GameSessionManager sessionManager;

    /**
     * Constructs a new RMIServerApp with the specified GameSessionManager.
     *
     * @param sessionManager the manager for game sessions
     */
    public RMIServerApp(GameSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Starts the RMI server.
     *
     * @throws RemoteException if the server fails to start
     */
    public void startServer() throws RemoteException {
        Registry registry;
        try {
            //System.setProperty("java.rmi.server.hostname", "192.168.1.15");
            registry = LocateRegistry.createRegistry(1099); // Crea o recupera il registro sulla porta 1099
            // System.out.println("RMI registry created at port 1099");
        } catch (RemoteException e) {
            registry = LocateRegistry.getRegistry(1099); // Ottiene il riferimento al registro esistente
            System.err.println("RMI registry already exists at port 1099");
        }
        try {
            GameServerImpl serverImpl = new GameServerImpl(sessionManager);
            registry.rebind("GameServer", serverImpl);
            System.out.println("Game server RMI is running on port 1099...");
        } catch (RemoteException e) {
            System.err.println("Failed to export RMI object: " + e.getMessage());
        }
    }
}

