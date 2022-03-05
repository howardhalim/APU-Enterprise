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
import java.util.Scanner;

/**
 *
 * @author howard
 */
public class system {
    private Interface x ;
    Scanner input = new Scanner(System.in);
    public system () throws NotBoundException, MalformedURLException, RemoteException, Exception{
        x = (Interface) Naming.lookup("rmi://localhost:1098/APUServer");
        system_start();
    }

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

        String msg = x.registerAccount(username,password);
        System.out.println(msg);

        menu();
    }
    public void login() throws Exception{

        System.out.println("\n!!LOGIN PAGE !!");
        System.out.print("Username: ");
        String username = input.nextLine();

        System.out.print("Password: ");
        String password = input.nextLine();

        int user_id = x.login(username, password);
        if(user_id >=0){
            System.out.print("IC/Passport: ");
            String validation = input.nextLine();
            boolean check = x.verifyLogin(user_id,validation);
            if(check){
                System.out.println("LOGIN SUCCESSFUL");
                //go_next
            }
            else{
                System.out.println("Validation Fail! Please Retry");
                menu();
            }

        }
        else{
            System.out.println("LOGIN FAIL IDIH, USERNAME/PASSWORD IS WRONG");
            menu();
        }
    }
}