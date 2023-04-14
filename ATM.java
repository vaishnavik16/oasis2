
import java.util.Scanner;
import java.sql.SQLException;

public class ATM {
     
	Scanner sc=new Scanner(System.in);
    DataBase dB;
    
    public ATM() throws SQLException
    {
    	dB= new DataBase();
    }
    
    public void login_Customer() throws SQLException
    {
    	System.out.println("*****Welcome to OIS Bank ATM*****\n");
    	System.out.println("Enter Customer ID: ");
    	int Cust_ID=sc.nextInt();
    	System.out.println("Enter Customer PassWord: ");
    	String password=sc.next();
    	boolean flag= dB.login(Cust_ID, password);
    	if(flag) {
    		System.out.println("Your Data Fetched Successfully");
    		display_Menu();
    		
    	}else {
    		System.out.println("Sorry userID or Password is wrong, try again");
    		login_Customer(); 
    	}
    	
    }//end of login
    
    public void display_Menu() throws SQLException {
    	System.out.println("------------------------------------------");
    	System.out.println("|"+" 1.Balance Inquery");
    	System.out.println("|"+" 2.Withdraw Money");
    	System.out.println("|"+" 3.Deposit Money");
    	System.out.println("|"+" 4.Transiction history");
        System.out.println("|"+" 5.Transfer");
    	System.out.println("|"+" 6.Exit");
    	System.out.println("------------------------------------------");
    	System.out.println("Select which operation you want to perform");
    	int choice=sc.nextInt();
    	switch(choice)
    	{
    	case 1: check_Balance();
    	        break;
    	case 2: withdraw_Money();
    	         break;
    	case 3:  deposit_Money();
    	          break;
    	case 4: transaction();
    	          break;
    	case 5: transfer();
    	          break;
    	case 6: log_out();
    	 default: System.out.println("please select correct operation");
    	           break;
    	}
    }
    
    public void check_Balance() throws SQLException
    {
    	int balance=dB.getBalance(dB.CustomerID);
    	System.out.println("Balance amount is : "+balance);
    	display_Menu();
    }//end of checkBalance
    
    
    public void withdraw_Money() throws SQLException {
    
    	System.out.println("Enter ammount to be withdraw: ");
    	int amount=sc.nextInt();
    	boolean flag=dB.withdrawMoney(dB.CustomerID,amount);
    	if(flag) {
    		System.out.println("Collect your money "+amount);
    		
    	}
    	else {
    		System.out.println("Your Account not have enough Money");
    		
    	}
    	display_Menu();
    }//end of withdraw
    
    
    public void deposit_Money() throws SQLException
    {
    	System.out.println("Enter ammount to be deposited: ");
    	int amount=sc.nextInt();
    	boolean flag=dB.depositMoney(dB.CustomerID,amount);
    	if(flag) {
    		System.out.println("Money deposited is "+amount);
    		}
    	display_Menu();
    }//end of deposit
    
    public void transaction() throws SQLException
    {
    	dB.getStatement(dB.CustomerID);
    	display_Menu();
    }//end of transaction
    
    public void transfer() throws SQLException
      {
    	System.out.println("Give details of the accounts to which you want to transfer money");
    	System.out.println("Enter Account Number: ");
    	int acc_No=sc.nextInt();
    	int transferee_ID=dB.checkAccount(acc_No);
    	if(transferee_ID>0)
    	 {
    		System.out.println("Enter Amount To Be transfer: ");
        	int amount=sc.nextInt();
        	dB.transfer(transferee_ID,amount);
          } 
    	else {
    		System.out.println("user account is not valid");
    	   }
    	
    	display_Menu();
    	
      }//end of transfer
    
    public void log_out() {
    	
    	System.out.println("*****Thank you for using OIS ATM*****");
    	System.exit(0);
    }
    
   	public static void main(String[] args)throws SQLException {
	
   		ATM atm =new ATM();
   		atm.login_Customer();
	}
}
