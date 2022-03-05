/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Asus
 */
public interface Interface extends Remote {
     public boolean login (String username, String password) throws Exception;
     public void registerAccount(String username, String password) throws Exception;
}
