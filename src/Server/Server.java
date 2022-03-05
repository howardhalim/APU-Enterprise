/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Interface.Interface;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author howard
 */
public class Server implements Interface {
    private static Server APUServer = null;
    private String serverName = null;
    
     public Server (String name){
         this.setServerName(name);
    }
     public String getServerName(){
         return serverName;
     }
    public void setServerName(String serverName){
        this.serverName= serverName;
    }
    
    public static void main (String args[]) throws RemoteException, AlreadyBoundException{
         
             APUServer = new Server ("APUServer");
             Registry reg = LocateRegistry.createRegistry(1098);
             Remote obj = UnicastRemoteObject.exportObject(APUServer,1098);
             reg.bind(APUServer.getServerName(),obj);
             System.out.println("APU Server Started");
     }
    public static Connection connect(){

       Connection con = null;
        try
        {
            String url = "jdbc:sqlite:\\C:\\Users\\howard\\OneDrive - Asia Pacific University\\SEM5\\DCOMS\\APU_LibrarySystem\\src\\APUDatabase.db";
            con = DriverManager.getConnection(url);
            
        }
        catch (SQLException s)
        {
            System.out.println(s.getMessage());
        }
        return con;
    }
    public boolean login(String username, String password){
        if("aa".equals(username) && "aa".equals(password)){
            return true;
        }
        return false;
    }
    
    public void registerAccount(String username, String password){
        String sql = "INSERT INTO account(username,password) VALUES(?,?)";
          
          try (Connection conn = this.connect();
               PreparedStatement ps = conn.prepareStatement(sql)) {
              
               ps.setString(1,username);
               ps.setString(2,password);
               ps.executeUpdate();
              System.out.println("REGISTERED");
          } catch (SQLException e) {
              System.out.println(e.getMessage());
        }
    }
    
    
}
