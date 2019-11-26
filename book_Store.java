import java.sql.*;
import java.util.*;

public class book_Store{
   public static void main (String[] args){
      System.out.println("Welcome to Taro's bookstore!");
      boolean running = true;
      
      try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore?useSSL=false", "tarosukdeo", "T@r0r0ck$"); Statement stmt = conn.createStatement();)
      {              
         while (running == true){
            System.out.println("Please select an option");
            printMenu();
      
            Scanner scanner = new Scanner(System.in);
            Scanner sc = new Scanner(System.in);
            int menuChoice = scanner.nextInt();
      
            switch (menuChoice){
               case 1:
                  System.out.println("Book ID:");
                  int bookID = scanner.nextInt();
            
                  System.out.println("Title of book: (Excluding apostrophes) ");
                  String bookTitle = sc.nextLine();
            
                  System.out.println("Author of book: (Excluding apostrophes) ");
                  String bookAuthor = sc.nextLine();
            
                  System.out.println("Quantity of book: ");
                  int quantity = scanner.nextInt();
            
                  String insertQuery = enterBook (bookID, bookTitle, bookAuthor, quantity);
                  //System.out.println(insertQuery);
                  int countInserted = stmt.executeUpdate(insertQuery);
                  
                  if (countInserted > 0){
                     System.out.println(countInserted + " record sucessfully added!\n");
                  }else{
                     System.out.println("Failed to insert record!\n");
                  }
               
                  running = true;
                  break;
               case 2:
                  System.out.println("Please select an attribute to locate the desired book: (id/title)");
                  String currentAttribute = sc.nextLine();
                  
                  System.out.println("What is the id/title of the record you would like to change?");
                  String identifier = sc.nextLine();
                  
                  System.out.println("Which attribute would you like to modify? (id/title/author/qty)");
                  String updateAttribute = sc.nextLine();                  
                                    
                  System.out.println("What would you like to change the value of " + updateAttribute + " to?");
                  String updateValue = sc.nextLine();                 
                  
                  String updateQuery = updateBook (updateAttribute, updateValue, currentAttribute, identifier);
                  
                  int countUpdated = stmt.executeUpdate(updateQuery);
                  
                  if (countUpdated > 0){
                     System.out.println(countUpdated + " record successfully updated!\n");
                  }else{
                     System.out.println("Failed to updated record!\n");
                  }
                  
                  running = true;                  
                  break;
               case 3:
                  System.out.println("Find book by id or title?");
                  String attribute = sc.nextLine();
                  
                  System.out.println("Please enter the book id or title to be deleted:");
                  String value = sc.nextLine();
                  
                  String userDelete = deleteBook(attribute, value);
                  
                  int countDeleted = stmt.executeUpdate(userDelete);
                  
                  if (countDeleted > 0){
                     System.out.println(countDeleted + " record sucessfully deleted!\n");
                  }else{
                     System.out.println("Failed to delete record!\n");
                  }
                  
                  running = true;
                  break;
               case 4:
                  System.out.println("Search by id, title or author?");
                  String userAttribute = sc.nextLine();
                  
                  System.out.println("Please enter the book id, title, or author you are looking for:");
                  String userValue = sc.nextLine();
                  
                  String userSearch = searchBooks(userAttribute, userValue);
                  
                  ResultSet rset = stmt.executeQuery(userSearch);
                  
                  if (!rset.isBeforeFirst()){
                     System.out.println("No records found matching your input");
                  }else{                                
                     while (rset.next()){
                        System.out.println("ID: " + rset.getInt("id") + ", " + "Title: " + rset.getString("title") + ", " + "Author: " + rset.getString("author") + ", " + "Quantity: " + rset.getInt("qty") + "\n");
                     }
                  }                  
                  
                  running = true;
                  break;
               case 0:
                  System.out.println("Goodbye");
                  running = false;
                  break;
            }
         }
      }catch (SQLException ex){
         ex.printStackTrace();
         System.out.println("Please ensure that you did not enter a duplicate Book ID and there are no apostrophes in your book title/author's name");
      }
      
   }   
      
   public static void printMenu(){
      System.out.println("1. Enter book\n2. Update book\n3. Delete book\n4. Search books\n0. Exit");
   }
   
   public static String enterBook (int id, String title, String author, int qty){
      String insertQuery = "insert into books values " + "("+id+", '"+title+"', '" + author + "', " + qty + ");";
      
      return insertQuery;
   }
   
   public static String searchBooks (String attribute, String value){
      String searchQuery = "select * from books where " + attribute + "=" + "'" + value + "';";
      //System.out.println(searchQuery);
      return searchQuery;
   }
   
   public static String deleteBook (String attribute, String value){
      String deleteQuery = "delete from books where " + attribute + "=" + "'" + value + "';";
      
      return deleteQuery;
   }
   
   public static String updateBook (String attribute, String newValue, String identifyingAttribute, String identifyingValue){
      String updateQuery = "update books set " + attribute + "=" + "'" + newValue + "'" + " where " + identifyingAttribute + "=" + "'" + identifyingValue + "';";
      System.out.println(updateQuery);
      
      return updateQuery;
   }
}
