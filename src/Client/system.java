/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Interface.Interface;
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
        run();
    }
    
    public void run() throws Exception{
        register();
                
    }
    public void register()  throws Exception{
        System.out.println("Username: ");
        String username = input.next();
        System.out.println("Password: ");
        String password = input.next();
        
        x.registerAccount(username,password);
    }
    
}
