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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
            //change to appropriate directory
            String url = "jdbc:sqlite:D:\\DCOMS\\APU-Enterprise\\APUDatabase.db";
            con = DriverManager.getConnection(url);
            
        }
        catch (SQLException s)
        {
            System.out.println(s.getMessage());
        }
        return con;
    }
    public int login(String username, String password){
        if(username.equals("admin") && password.equals("admin")){
            return 0;
        }
        String sql = "SELECT * FROM account";
        
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                String user = rs.getString("username");
                String pass = rs.getString("password");
                int id = rs.getInt("id");
                System.out.println(user + password);
                if(user.equals(username) && pass.equals(password)){
                    System.out.println("Login Successfull");
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return -1;
    }
    
    public boolean verifyLogin(int user_id){
        return true;
    }
    
    public String registerAccount(String username, String password){
        String sql = "SELECT * FROM account";
        
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                String user = rs.getString("username");
                String pass = rs.getString("password");
                
                
                if(user.equals(username)){
                    return "Username has existed, Please Choose Another Username\n";
                   
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
        sql = "INSERT INTO account(username,password) VALUES(?,?)";
          
          try (Connection conn = this.connect();
               PreparedStatement ps = conn.prepareStatement(sql)) {
              
               ps.setString(1,username);
               ps.setString(2,password);
               ps.executeUpdate();
              return "Registration Successfull\n";
          } catch (SQLException e) {
              System.out.println(e.getMessage());
              return "ERROR";
        }
    }
    
    
}
