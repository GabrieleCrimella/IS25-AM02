package it.polimi.ingsw.is25am02.network;

/*
Interface that defines the methods used by the server to notify clients of state changes.
*/
public interface VirtualView {
    void reportError(String details) throws Exception;
    //todo all the showUpdate!!!
}
