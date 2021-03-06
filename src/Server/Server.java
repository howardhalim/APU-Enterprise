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
            String url = "jdbc:sqlite:E:\\Lecture\\0_Assignment\\Year 3\\DCOMS\\APU-Enterprise\\APUDatabase.db";
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
                    String password = rs.getString("password");

                    data.add(first_name);
                    data.add(last_name);
                    data.add(ic);
                    data.add(password);
                     return data;
                }
               
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    
    public List<List<String>> listAllExec() throws Exception{
        String sql = "SELECT * FROM account";
        List<List<String>> data = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                
                int x = rs.getInt("id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String ic = rs.getString("ic_passportnum");
                
                List<String> temp = new ArrayList<>();
                temp.add(first_name);
                temp.add(last_name);
                temp.add(ic);
                data.add(temp);
            }
              
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }
    
    public void storeNew(String itemName, String brand, String category, int stock, int price, String date) throws Exception{
        String sql = "INSERT INTO inventory(item_name, brand, category, stock, price, date_stored) VALUES(?,?,?,?,?,?)";
         try (Connection conn = this.connect();
               PreparedStatement ps = conn.prepareStatement(sql)) {
              
               ps.setString(1,itemName);
               ps.setString(2,brand);
               ps.setString(3,category);
               ps.setInt(4,stock);
               ps.setInt(5,price);
               ps.setString(6, date);
               ps.executeUpdate();
               
          } catch (SQLException e) {
              System.out.println(e.getMessage());
        }   
    }
    
    public void deleteExec(String del){
        String sql = "DELETE FROM account WHERE username = ?";
         try (Connection conn = this.connect();
                PreparedStatement ps = conn.prepareStatement(sql)){
               ps.setString(1, del);
               ps.executeUpdate();
               
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public int checkUser(String username) throws Exception{
        String sql = "SELECT * FROM account";
        List<String> data = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                String user = rs.getString("username");
                if(username.equals(user)){
                    int acc_id = rs.getInt("id");
                    return acc_id;
                }
               
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
    
    public List<List<String>> listInven() throws Exception{
        String sql = "SELECT * FROM inventory ORDER BY item_name ASC;";
        List<List<String>> data = new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {

                int x = rs.getInt("id");
                String item_name = rs.getString("item_name");
                String brand = rs.getString("brand");
                String category = rs.getString("category");
                String stock = rs.getString("stock");
                String price = rs.getString("price");
                String date_stored = rs.getString("date_stored");

                List<String> temp = new ArrayList<>();
                temp.add(item_name);
                temp.add(brand);
                temp.add(category);
                temp.add(stock);
                temp.add(price);
                temp.add(date_stored);
                data.add(temp);
            }

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }
    
    public String editProfile(int account_id, String password, String first_name, String last_name, String ic_passportnum) throws SQLException{
        Connection conn = null;
        Statement st = null; 
        ResultSet sqlres = null;
        PreparedStatement ps = null;
        
        try {  
            String sql = "SELECT * FROM account WHERE ic_passportnum = \""+ic_passportnum+"\" ";
            conn = this.connect();
            st  = conn.createStatement();
            sqlres = st.executeQuery(sql);
            if (sqlres.next()) {
                int exisiting_ic_account_id = sqlres.getInt("id");
                if(account_id != exisiting_ic_account_id){
                    return "IC or Passport Unavailable";
                }
            }
            
        } catch (SQLException e) { System.out.println(e.getMessage()); return "error";
        } finally {
            if (conn != null) {conn.close();}
            if (st != null) {st.close();}
            if (sqlres != null) {sqlres.close();}
        }
        
        String sql3 = "UPDATE account SET password=\""+password+"\", "+
                "first_name= \""+first_name+"\", " +
                "last_name= \""+last_name+"\", " +
                "ic_passportnum= \""+ic_passportnum+"\" " +
                " WHERE id= \""+account_id+"\" ";
        try {
            conn = this.connect();
            ps = conn.prepareStatement(sql3);
            ps.executeUpdate();
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
        return "User profile updated";
    }

    public String getAccountId(String username) throws SQLException{
        Connection conn = null;
        Statement st = null; 
        ResultSet sqlres = null;
        PreparedStatement ps = null;
        int account_id = -1;
        
        try {  
            String sql = "SELECT * FROM account WHERE username = \""+username+"\" ";
            conn = this.connect();
            st  = conn.createStatement();
            sqlres = st.executeQuery(sql);
            if (!sqlres.next()) { return "User Not found";}
            else { account_id = sqlres.getInt("id"); }
            
        } catch (SQLException e) { System.out.println(e.getMessage()); return "error";
        } finally {
            if (conn != null) {conn.close();}
            if (st != null) {st.close();}
            if (sqlres != null) {sqlres.close();}
        }
        
        return Integer.toString(account_id);
    }
    
    public List<List<String>> generateReport(String x, String check){
        String from = "";
        String sql = "";
        if(check.equals("brand")){
            from = "brand";
             sql = "SELECT * FROM inventory WHERE "+ from + " = ?";
        }
        else if(check.equals("category")){
            from = "category";
             sql = "SELECT * FROM inventory WHERE "+ from + " = ?";
        }
        else if(check.equals("date")){
            from = "date_stored";
            x = '%' + x + '%';
             sql = "SELECT * FROM inventory WHERE "+ from + " LIKE ?";
        }
        else if(check.equals("item")){
            from = "item_name";
             sql = "SELECT * FROM inventory WHERE "+ from + " = ?";
        }
        
        List<List<String>> data = new ArrayList<>();
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            
             pstmt.setString(1,x);
             ResultSet rs    = pstmt.executeQuery();
            
            
            
            // loop through the result set
            while (rs.next()) {
                
                int y = rs.getInt("id");
                String item_name = rs.getString("item_name");
                String brand = rs.getString("brand");
                String category = rs.getString("category");
                String stock = rs.getString("stock");
                String price = rs.getString("price");
                String date_stored = rs.getString("date_stored");
                
                List<String> temp = new ArrayList<>();
                temp.add(item_name);
                temp.add(brand);
                temp.add(category);
                temp.add(stock);
                temp.add(price);
                temp.add(date_stored);
                data.add(temp);
            }

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(data);
        return data;
    }
}
    


