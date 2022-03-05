/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Interface.Interface;
import static java.lang.Integer.parseInt;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author howard
 */
public class system {
    private Interface x ;
    Scanner input = new Scanner(System.in);
    private int account_id;
    public system () throws NotBoundException, MalformedURLException, RemoteException, Exception{
        x = (Interface) Naming.lookup("rmi://localhost:1098/APUServer");
        system_start();
        //checkconnect();
    }
//    public void checkconnect(){
//        Connection con = null;
//        try
//        {
//            //change to appropriate directory
//            String url = "jdbc:sqlite:\\D:\\DCOMS\\APU-Enterprise\\APUDatabase.db";
//            con = DriverManager.getConnection(url);
//            
//        }
//        catch (SQLException s)
//        {
//            System.out.println(s.getMessage());
//        }
//    }
    public void system_start() throws Exception{
        
        menu();
   
    }
    public void menu() throws Exception{
        System.out.println("\n!!APU Enterprise!!");
        
        System.out.println("1. Register");
        System.out.println("2. Login\n");
        
        System.out.print("Choose: ");
        
        int inp = parseInt(input.nextLine());
        
        if(inp == 1){
            register();
        }
        else if(inp == 2){
            login();
        }
        else{
            System.out.println("Wrong Choice");
        }
    }
    public void register() throws Exception{
        System.out.println("\n!!ACCOUNT REGISTRATION!!");
        System.out.print("Username: ");
        String username = input.nextLine();
        
        System.out.print("Password: ");
        String password = input.nextLine();
        
        int msg = x.registerAccount(username,password);
        if(msg == 1){
            System.out.println("Username has existed, Please Choose Another Username\n");
        }
        else if (msg == 0){
            
            System.out.println("\nPlease Fill Up Details Below To Finish the Registration: \n");
            System.out.print("First Name: ");
            String firstName = input.nextLine();
            System.out.print("Last Name: ");
            String lastName = input.nextLine();
            System.out.print("IC/PassportNumber: ");
            String IC = input.nextLine();
            
            x.dataInput(firstName, lastName, IC, username, password);
            
            System.out.println("Registration Successful!");
        }
        
        
        menu();
    }
    public void login() throws Exception{
        
        System.out.println("\n!!LOGIN PAGE !!");
        System.out.print("Username: ");
        String username = input.nextLine();
        
        System.out.print("Password: ");
        String password = input.nextLine();
        
        int user_id = x.login(username, password);
        if(user_id >0){
            System.out.println("\nPlease Enter IC/Passport Number to verify");
            System.out.print("IC/Passport: ");
            String validation = input.nextLine();
            System.out.println("Verifying...");
            TimeUnit.SECONDS.sleep(3);
            
            boolean check = x.verifyLogin(user_id,validation);
            if(check){
                account_id = user_id;
                System.out.println("Loging in...");
                TimeUnit.SECONDS.sleep(1);
                account_menu();
                //go_next
            }
            else{
                System.out.println("Validation Fail! Please Retry");
                menu();
            }
        
        }
        else if(user_id == 0) {
           System.out.println("Admin loging in...");
           account_id = -1;
           TimeUnit.SECONDS.sleep(1);
           admin_menu();
        }
        else{
            System.out.println("LOGIN FAIL, USERNAME/PASSWORD IS WRONG");
            menu();
        } 
    }
    
    public void account_menu() throws Exception{
        List<String>data = x.retreiveAccount(account_id);
        if(data == null){
            return;
        }
        System.out.println("-------------------------------");
        System.out.println("---------ACCOUNT-PAGE----------");
        System.out.println("Name: "+data.get(0)+ " " + data.get(1));
        System.out.println("IC/Passport: "+ data.get(2));
        System.out.println("1. Edit Account Profile");
        System.out.println("2. Store New Inventory");
        System.out.println("3. List Inventory");
        System.out.println("4. Generate Report");
        
        System.out.println("-------------------------------");
        System.out.print("Choice: ");
        
        int ch = input.nextInt();
        
        switch(ch){
            case 1:{
                
                break;
            }
                
            case 2:{
                break;
            }
                
            case 3:{
                break;
            }
                
            case 4:{
                break;
            }
                
                
            default: {
                System.out.println("Choice is Unavailable!\nPlease Try Again");
                account_menu();
                break;
            }
        }
        
    }
    
    public void admin_menu(){
        System.out.println("-------------------------------");
        System.out.println("----------ADMIN-PAGE-----------");
        System.out.println("1. List All Executive");
        System.out.println("2. Delete Executive Account");
        System.out.println("3. Edit Executive Account");
        System.out.println("4. List Inventory");
        System.out.println("5. Generate Report");
        System.out.println("-------------------------------");
        System.out.print("Choice: ");
        
        int ch = input.nextInt();
        switch(ch){
            case 1:{
                
                break;
            }
                
            case 2:{
                break;
            }
                
            case 3:{
                break;
            }
                
            case 4:{
                break;
            }
            case 5:{
                break;
            }    
            
                
            default: {
                System.out.println("Choice is Unavailable!\nPlease Try Again");
                admin_menu();
                break;
            }
        }
    }
}
