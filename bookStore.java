import java.sql.*;
import java.util.*;

public class bookStore{
   public static void main (String[] args) throws SQLException{
      System.out.println("Welcome to the bookstore!");
      boolean running = true;  //A status variable used to keep track of whether the program is active or not
      
      try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore?useSSL=false", "myuser", "xxxx"); Statement stmt = conn.createStatement();)   //Creating a Connection and Statement object using try-with-resources, username and password currently set to defaults
      {              
         while (running == true){
            System.out.println("Please select an option");
            printMenu();                                                   //Display the menu to the user
      
            Scanner scanner = new Scanner(System.in);
            int menuChoice = scanner.nextInt();
      
            switch (menuChoice){                                  
               case 1:            
                  String insertQuery = enterBook();                        
                  
                  int countInserted = stmt.executeUpdate(insertQuery);     //INSERT query sent to our database
                  
                  if (countInserted > 0){                                  //Check if record was added successfully
                     System.out.println(countInserted + " record sucessfully added!\n");
                  }else{
                     System.out.println("Failed to insert record!\n");
                  }
               
                  running = true;
                  break;
               case 2:                
                  String updateQuery = updateBook();
                  
                  int countUpdated = stmt.executeUpdate(updateQuery);      //UPDATE query sent to our database
                  
                  if (countUpdated > 0){                                   //Used to check status of update
                     System.out.println(countUpdated + " record successfully updated!\n");
                  }else{
                     System.out.println("Failed to updated record!\n");
                  }
                  
                  running = true;                  
                  break;
               case 3:
                  String userDelete = deleteBook();
                  
                  int countDeleted = stmt.executeUpdate(userDelete);       //DELETE query sent to our database
                        
                  if (countDeleted > 0){                                   //Check status of delete query
                     System.out.println(countDeleted + " record sucessfully deleted!\n");
                  }else{
                     System.out.println("Failed to locate record to be deleted!\n");
                  }
                  
                  running = true;
                  break;
               case 4:
                  String userSearch = searchBooks();
                  
                  ResultSet rset = stmt.executeQuery(userSearch);         //SELECT query sent to our database
                  
                  if (!rset.isBeforeFirst()){                             //If first row of result set is empty, we assume no matching records were found
                     System.out.println("No records found matching your input\n");
                  }else{                                
                     while (rset.next()){
                        System.out.println("ID: " + rset.getInt("id") + ", " + "Title: " + rset.getString("title") + ", " + "Author: " + rset.getString("author") + ", " + "Quantity: " + rset.getInt("qty") + "\n");      //Present record to user if match is found
                     }
                  }                                    
                  running = true;
                  break;
               case 0:
                  System.out.println("Goodbye");
                  running = false;                                   //Status variable set to false to indicate termination of our program
                  break;
            }
         }
      }catch (SQLException ex){                                      //Catch SQLExceptions that may arise from duplicate primary keys or illegal characters
         ex.printStackTrace();
         throw new SQLException("Please enter a book ID that is not currently in use and remove any apostrophes from entries");
      }
      
   }   
      
   public static void printMenu(){                                   //Function used to display menu to user. Opted not to use enum as menu provides no further information beyond the function to be called in our switch case
      System.out.println("1. Enter book\n2. Update book\n3. Delete book\n4. Search books\n0. Exit");
   }
   
   public static String enterBook(){                                 //This function acccepts inputs from the user and constructs an acceptable INSERT query to add a new record to the database
      Scanner numScan = new Scanner(System.in);
      Scanner wordScan = new Scanner(System.in);
      
      System.out.println("Book ID:");
      int bookID = numScan.nextInt();
            
      System.out.println("Title of book: (Excluding apostrophes and quotation marks)");
      String bookTitle = wordScan.nextLine();
            
      System.out.println("Author of book: (Excluding apostrophes) ");
      String bookAuthor = wordScan.nextLine();
            
      System.out.println("Quantity of book: ");
      int quantity = numScan.nextInt();
      
      String insertQuery = "insert into books values " + "("+bookID+", '"+bookTitle+"', '" + bookAuthor + "', " + quantity + ");";
      
      
      return insertQuery;
   }
   
   public static String searchBooks(){                                  //This function accepts inputs from the user and constructs a SELECT query to search the database
      Scanner sc = new Scanner(System.in);
      System.out.println("Search by id, title or author?");
      String userAttribute = sc.nextLine();
                  
      System.out.println("Please enter the book id, title, or author you are looking for:");
      String userValue = sc.nextLine();
      
      String searchQuery = "select * from books where " + userAttribute + "=" + "'" + userValue + "';";
      //System.out.println(searchQuery);
      return searchQuery;
   }
   
   public static String deleteBook(){                                   //This function accepts user input and constructs a DELETE statement to remove a record from the database
      Scanner sc = new Scanner(System.in);
      System.out.println("Find book by id or title?");
      String attribute = sc.nextLine();
                  
      System.out.println("Please enter the book id or title to be deleted:");
      String value = sc.nextLine();
      
      String deleteQuery = "delete from books where " + attribute + "=" + "'" + value + "';";
      
      return deleteQuery;
   }
   
   public static String updateBook(){                                   //This function accepts user input and constructs an UPDATE statement to update a record in our database
      Scanner sc = new Scanner(System.in);
   
      System.out.println("Please select an attribute to locate the desired book: (id/title)");
      String identifyingAttribute = sc.nextLine();
                  
      System.out.println("What is the id/title of the record you would like to change?");
      String identifyingValue = sc.nextLine();
                  
      System.out.println("Which attribute would you like to modify? (id/title/author/qty)");
      String attribute = sc.nextLine();                  
                                    
      System.out.println("What would you like to change the value of " + attribute + " to?");
      String newValue = sc.nextLine(); 
   
      String updateQuery = "update books set " + attribute + "=" + "'" + newValue + "'" + " where " + identifyingAttribute + "=" + "'" + identifyingValue + "';";
      
      return updateQuery;
   }
   
}
