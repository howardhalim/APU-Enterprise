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
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        
        int inp = -1;
        try{
            inp = parseInt(input.nextLine());    
        } catch (Exception e) {}
        

        if(inp == 1){
            register();
        }
        else if(inp == 2){
            login();
        }
        else{
            System.out.println("Wrong Choice");
            menu();
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
            //TimeUnit.SECONDS.sleep(3);
            
            boolean check = x.verifyLogin(user_id,validation);
            if(check){
                account_id = user_id;
                System.out.println("Loging in...");
                //TimeUnit.SECONDS.sleep(1);
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
    public void editExec() throws Exception{
        System.out.println("-------------------------------");
        System.out.println("---------EDIT-ACCOUNT----------");
        
        System.out.print("Username: ");
        String username = input.nextLine();
        int target_account_id = -1;
        String unparsed_target_account_id = x.getAccountId(username);
        try {
            target_account_id = parseInt(unparsed_target_account_id);
            System.out.print("Password: ");
            String password = input.nextLine();
            System.out.print("First Name: ");
            String first_name = input.nextLine();
            System.out.print("Last Name: ");
            String last_name = input.nextLine();
            System.out.print("IC/PassportNumber: ");
            String ic_passportnum = input.nextLine();

            String result = x.editProfile(target_account_id, password, first_name, last_name, ic_passportnum);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(unparsed_target_account_id);
        }
        admin_menu();
    }
    
    public void editProfile() throws Exception{
        System.out.println("-------------------------------");
        System.out.println("---------EDIT-PROFILE----------");
        
        System.out.print("Password: ");
        String password = input.nextLine();
        System.out.print("First Name: ");
        String first_name = input.nextLine();
        System.out.print("Last Name: ");
        String last_name = input.nextLine();
        System.out.print("IC/PassportNumber: ");
        String ic_passportnum = input.nextLine();
        
        String result = x.editProfile(account_id, password, first_name, last_name, ic_passportnum);
        System.out.println(result);
        account_menu();
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
        System.out.println("0. EXIT");
        
        System.out.println("-------------------------------");
        System.out.print("Choice: ");
        
        int ch = -1;
        try{
            ch = parseInt(input.nextLine());
            String dum = input.nextLine();
        } catch (Exception e) {}
        
        switch(ch){
            case 0:{
                System.out.println(" >>> Thank you for using our service! <<< ");
                break;
            }
          
            case 1:{
                editProfile();
                break;
            }
                
            case 2:{
                storeNewInven();
                account_menu();
                break;
            }
                
            case 3:{
                listInven();
                account_menu();
                break;
            }
                
            case 4:{
                generateReport();
                account_menu();
                break;
            }
                
                
            default: {
                System.out.println("Choice is Unavailable!\nPlease Try Again");
                account_menu();
                break;
            }
        }
        
    }
    
    public void storeNewInven() throws Exception{
        System.out.println("-------Inventory------");
        System.out.print("Item Name: ");
        String itemName = input.nextLine();
        System.out.print("Brand: ");
        String brand = input.nextLine();
        System.out.print("Category: ");
        String category = input.nextLine();
        System.out.print("Stock: ");
        int stock = input.nextInt();
        System.out.print("Price: ");
        int price = input.nextInt();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        System.out.println("Time: "+dtf.format(now)); 
        
        x.storeNew(itemName,brand,category,stock,price,date);
        System.out.println("Storing Item...");
        TimeUnit.SECONDS.sleep(2);
        System.out.println("----------------------");
        System.out.println("Item Stored!");
        System.out.println("----------------------\n");
        
    }
    
    public void admin_menu() throws Exception{
        System.out.println("-------------------------------");
        System.out.println("----------ADMIN-PAGE-----------");
        System.out.println("1. List All Executive");
        System.out.println("2. Delete Executive Account");
        System.out.println("3. Edit Executive Account");
        System.out.println("4. List Inventory");
        System.out.println("5. Generate Report");
        System.out.println("0. EXIT");
        System.out.println("-------------------------------");
        System.out.print("Choice: ");
        
        int ch = -1;
        try{
            ch = parseInt(input.nextLine());   
            String dum = input.nextLine();
        } catch (Exception e) {}
        
        switch(ch){
            case 0:{
                System.out.println(" >>> Thank you for using our service! <<< ");
                break;
            }
            
            case 1:{
            try {
                listAllExec();
                
            } catch (Exception ex) {
                Logger.getLogger(system.class.getName()).log(Level.SEVERE, null, ex);
            }
                admin_menu();
                break;
            }
                
            case 2:{
                deleteExec();
                admin_menu();
                break;
            }
                
            case 3:{
                editExec();
                admin_menu();
                break;
            }
                
            case 4:{
                listInven();
                admin_menu();
                break;
            }
            case 5:{
                generateReport();
                admin_menu();
                break;
            }    
            
                
            default: {
                System.out.println("Choice is Unavailable!\nPlease Try Again");
                admin_menu();
                break;
            }
        }
    }
    public void listAllExec() throws Exception{
        List<List<String>> data = x.listAllExec();
        if (data.isEmpty()){
            System.out.println("Data Empty");
        } else {
            for(int i = 0;i<data.size();i++){
                List<String> temp = data.get(i);
                System.out.println("-------------------------");
                System.out.println("Name: "+temp.get(0)+" "+temp.get(1));
                System.out.println("IC/Passport: "+temp.get(2));
            }
        }
        System.out.println("### Done ###\n");
    }
    
    public void deleteExec() throws Exception {
        System.out.println("Enter username to be deleted :");
        String usernamedel = input.nextLine();
        x.deleteExec(usernamedel);
        TimeUnit.SECONDS.sleep(2);
        System.out.println("Account Deleted !\n");
    }

    private void listInven() throws Exception {
        List<List<String>> data = x.listInven();
        if (data.isEmpty()){
            System.out.println("Data Empty");
        } else {
            for(int i = 0;i<data.size();i++){
                List<String> temp = data.get(i);
                System.out.println("-------------------------");
                System.out.println("Item Name: "+temp.get(0));
                System.out.println("Brand: "+temp.get(1));
                System.out.println("Category: "+temp.get(2));
                System.out.println("Stock: "+temp.get(3));
                System.out.println("Price: "+temp.get(4));
                System.out.println("Date Stored: "+temp.get(5));
                System.out.println("-------------------------");
            }
        }
        System.out.println("### Done ###\n");
    }

    private void generateReport() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
