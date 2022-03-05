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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            String url = "jdbc:sqlite:C:/Users/Asus/Desktop/DCOMS GIT/APUDatabase.db";
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
                //System.out.println(user + password);
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

    public boolean verifyLogin(int user_id, String validation){
        String sql = "SELECT * FROM account";
        boolean found = false;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                int id = rs.getInt("id");
                String valid = rs.getString("ic_passportnum");

                if(id == user_id && validation.equals(valid)){
                    found = true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return found;
    }
    
    
    public int registerAccount(String username, String password){
        String sql = "SELECT * FROM account";
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                String user = rs.getString("username");
                String pass = rs.getString("password");

                if(user.equals(username)){
                    return 1;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return 0;
        
    }
    public void dataInput(String firstName, String lastName, String IC, String username, String password){
        String sql = "INSERT INTO account(first_name, last_name, ic_passportnum,username,password) VALUES(?,?,?,?,?)";
          
          try (Connection conn = this.connect();
               PreparedStatement ps = conn.prepareStatement(sql)) {
              
               ps.setString(1,firstName);
               ps.setString(2,lastName);
               ps.setString(3,IC);
               ps.setString(4,username);
               ps.setString(5,password);
               ps.executeUpdate();
             
          } catch (SQLException e) {
              System.out.println(e.getMessage());
              
        }
          
        
    }
    
    public List<String> retreiveAccount(int id){
        String sql = "SELECT * FROM account";
        List<String> data = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                int x = rs.getInt("id");
                if(x==id){
                     String first_name = rs.getString("first_name");
                     String last_name = rs.getString("last_name");
                     String ic = rs.getString("ic_passportnum");
                     data.add(first_name);
                     data.add(last_name);
                     data.add(ic);
                     return data;
                }
               
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void editAccount(String username, String newusername, String password) throws SQLException{
        Connection conn = null;
        Statement st = null; 
        ResultSet sqlres = null;
        PreparedStatement ps = null;
        
        String sql = "SELECT * FROM account WHERE username = \""+username+"\" ";
        try {  
            conn = this.connect();
            st  = conn.createStatement();
            sqlres = st.executeQuery(sql);
            int id = sqlres.getInt("id");
            if (!sqlres.next()) { System.out.println("User Not found"); return;}
            
        } catch (SQLException e) { System.out.println("2"+e.getMessage()); return;
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (st != null) {
                st.close();
            }
            if (sqlres != null) {
                sqlres.close();
            }
        }

        String sql2 = "SELECT * FROM account WHERE username = \""+ newusername+"\"";
        //if username changed
        if(!username.equals(newusername)){
            try {
                conn = this.connect();
                st  = conn.createStatement();
                sqlres = st.executeQuery(sql2);
                if (sqlres.next()) { System.out.println("Error! Username not available"); return;}
            } catch (SQLException e) { System.out.println("1"+e.getMessage()); return;
            } finally {
                if (conn != null) {
                    conn.close();
                }
                if (st != null) {
                    st.close();
                }
                if (sqlres != null) {
                    sqlres.close();
                }
            }
        }
        
        String sql3 = "UPDATE account SET username=\""+newusername+"\", password=\""+password+"\" WHERE username=\""+username+"\"";
        try {
            conn = this.connect();
            ps = conn.prepareStatement(sql3);
            ps.executeUpdate();
            System.out.println("SUKSES");
            return;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
    }
}

