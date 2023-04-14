import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class DataBase {

   Statement stmt=null;
   ResultSet rs=null;
   Connection con=null;
   int CustomerID=0;
   int Balance_Money=0;
   
	//constructor
	public DataBase()
	{
	   try {
		      Class.forName("oracle.jdbc.driver.OracleDriver");
		      con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","abhi","1234");
		      System.out.println("DataBase Connected!!!");
	   }catch(Exception e) {
		   System.out.println(e.getMessage());
	   }
	}
	
	public boolean login(int Cust_ID,String password) throws SQLException
	{
		 
			stmt=con.createStatement();
			rs=stmt.executeQuery("select * from customer where CustomerID='"+Cust_ID+"'and CustomerPassword='"+password+"'");
			if(rs.next())
			{
				CustomerID=rs.getInt("CustomerID");
				return true;
			}else {
				return false;
			}
	}
	
	public int getBalance(int id) throws SQLException {
		
		rs=stmt.executeQuery("select BalanceAmount from Account where CustomerID="+id+"");
		if(rs.next()) {
			Balance_Money=rs.getInt("BalanceAmount");
			return Balance_Money;
		}else {
			return 0;
		}
	}
	
	
	public boolean withdrawMoney(int id,int amount) throws SQLException {
		if(amount>Balance_Money) {
			return false;
		}else {
			stmt.executeUpdate("update Account set BalanceAmount="+(Balance_Money-amount)+"where customerid="+id);
			Balance_Money=Balance_Money-amount;
			String q="insert into transaction(cust_ID,Deposited,Withdraw,Balance) values(?,?,?,?)";
	         
			PreparedStatement pstm=con.prepareStatement(q);
			pstm.setInt(1,CustomerID);
			pstm.setInt(2,0);
			pstm.setInt(3,amount);
			pstm.setInt(4,Balance_Money);
			pstm.executeUpdate();
		
			return true;
		}
	}
	
	
	public boolean depositMoney(int id,int amount) throws SQLException {
		
		int a=stmt.executeUpdate("update Account set BalanceAmount="+(Balance_Money+amount)+"where customerid="+id);
		if(a==1) {
			Balance_Money=Balance_Money+amount;
			String q="insert into transaction(cust_ID,Deposited,Withdraw,Balance) values(?,?,?,?)";
	         
			PreparedStatement pstm=con.prepareStatement(q);
			pstm.setInt(1,CustomerID);
			pstm.setInt(2,amount);
			pstm.setInt(3,0);
			pstm.setInt(4,Balance_Money);
			pstm.executeUpdate();
			return true;
		}else {
			return false;	
		}
	  
	
		}
	
	 public void getStatement(int id) throws SQLException
	    {
		 
		 rs=stmt.executeQuery("select * from transaction where Cust_ID='"+id+"'");
		 System.out.println("---------------------------------------------------");
		 System.out.println("| "+"ID"+"\t"+"Deposited"+"\t"+"Withdrawn"+"\t"+"Balance");
		 while(rs.next())
		 {
			 System.out.println("| "+rs.getInt(1)+"\t"+rs.getInt(2)+"\t\t"+rs.getInt(3)+ "\t\t"+rs.getInt(4));
		 }
		 System.out.println("---------------------------------------------------");
	 }
	 
	 public int checkAccount(int acc_No) throws SQLException
	 {
		 rs=stmt.executeQuery("select * from customer where acc_no="+acc_No);
		 if(rs.next())
		 {
			 int transferee_ID=rs.getInt("CustomerID");
				return transferee_ID;
		 }
		 else { 
			 return 0;
		 }
	 }
	 
	 public void transfer(int transferee_ID,int amount) throws SQLException
	 {
		boolean flag=withdrawMoney(CustomerID,amount);
		
		if(flag)
		{
			rs=stmt.executeQuery("select * from Account where CustomerID="+transferee_ID);
			if(rs.next())
			{
				int Balance=rs.getInt("BalanceAmount");
				int a=stmt.executeUpdate("update Account set BalanceAmount="+(Balance+amount)+"where CustomerID="+transferee_ID);
			   
			   if(a==1) {
				   System.out.println("Money transfered to account");
			   }
			   else {
				   System.out.println("Money is not transfered to account");
			   }
			}
			
	     }
		else {
			System.out.println("money is not withdrawn");
		}
	  return;
	 }
	
}
