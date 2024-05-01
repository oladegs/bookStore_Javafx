package com.aero.bookstore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private ObservableList<String> authorList = FXCollections.observableArrayList("Michael Jordan",
            "Sudha M.",
            "Sydney Sheldon",
            "Agatha Christi",
            "Alexandria J.",
            "James Carter",
            "Sant Kabir");

    @FXML private TextField tfBookTitle;
    @FXML private ComboBox cmbAuthors;
    @FXML private Label lblOutput;
    @FXML private ToggleGroup genreGroup;
    @FXML private RadioButton rdbFiction;
    @FXML private RadioButton rdbNonFiction;
    @FXML private RadioButton rdbChildren;
    @FXML private RadioButton rdbBiography;
    @FXML private TextField tfPrice;

    private Connection conn;
    private final String table_name = "books_tbl";

    private PreparedStatement insertStatement;
    @FXML
    protected void onSaveBookClick() {
        String outputMessage = "";
        Double price = 0.0;
        String title = "";
        String author = "";
        String genre = "";
        Double discountPercentage = 0.05;
        Double finalPrice = 0.0;

        //read input given by user for book title through TextField
        if (tfBookTitle.getText().isEmpty()){
            outputMessage += "Book title cannot be empty";
        }else{
            outputMessage += "Title: " + tfBookTitle.getText().toString();
            title = tfBookTitle.getText().toString();
        }

        if (cmbAuthors.getValue() != null && !cmbAuthors.getValue().toString().isEmpty()){
            outputMessage += "\nAuthor : " + cmbAuthors.getValue().toString();
            author = cmbAuthors.getValue().toString();
        }else{
            outputMessage += "\nAuthor : NA";
        }

        //get the selected genre
        RadioButton selectedRadioButton = (RadioButton) this.genreGroup.getSelectedToggle();
        outputMessage += "\nGenre : " + selectedRadioButton.getText().toString();
        genre = selectedRadioButton.getText().toString();

        if (!this.tfPrice.getText().toString().isEmpty()){
            price = Double.parseDouble(tfPrice.getText().toString());
        }

        if (price >= 20){
            discountPercentage = 0.10;
        }

        finalPrice = price - ( discountPercentage * price );

        lblOutput.setText("Book Receipt : \n" +
                "Title : " + title + "\n" +
                "Author : " + author + "\n" +
                "Genre : " + genre + "\n" +
                "Price : $" + price + "\n" +
                "Discount : " + (discountPercentage * 100) + "%\n" +
                "Final Price : $" + finalPrice);


//        Book addedBook = new Book (title, author, genre, price, discountPercentage);

        System.out.println("outputMessage : " + outputMessage);
//        lblOutput.setText(outputMessage);

        insertToDB(title,author,genre,price);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        //this method is called when initializing the Controller

        //initialize combo box with author list
        cmbAuthors.setItems(authorList);

        //create an object of ToggleGroup
        this.genreGroup = new ToggleGroup();

        //put all RadioButtons in one ToggleGroup
        this.rdbFiction.setToggleGroup(this.genreGroup);
        this.rdbBiography.setToggleGroup(this.genreGroup);
        this.rdbChildren.setToggleGroup(this.genreGroup);
        this.rdbNonFiction.setToggleGroup(this.genreGroup);

        // CONNECT WITH DATABASE
        connectToDB();

    }
  void connectToDB() {
        String hostOnCampus = "jdbc:oracle:thin:@oracle1.centennialcollege.ca:1521:SQLD";
      String hostAtHome = "jdbc:oracle:thin:@199.212.26.208:1521:SQLD";
        String username = "COMP228_F23_jp_8";

        String password = "Farouk98";
      try {
          // 1. Register the driver
          Class.forName("oracle.jdbc.OracleDriver");

          //2. Create connection object
          // to access on Campus
          conn = DriverManager.getConnection(hostOnCampus,username,password);

          // to access on Campus
//          conn = DriverManager.getConnection(hostAtHome,username,password);

          System.out.println("Database connection established successfully");

          // Initialize and prepare the insert PreparedStatement
          insertStatement = conn.prepareStatement("INSERT INTO "+table_name + " VALUES(?,?,?,?)");
      } catch (Exception e) {
          System.out.println("Something went wrong " + e);
      }
  }

    private void insertToDB(String title, String author, String genre, double price) {
        try {
            if (!conn.isClosed()) {
                // Provide the values for insertStatement
                insertStatement.setString(1, title);
                insertStatement.setString(2, author);
                insertStatement.setString(3, genre);
                insertStatement.setDouble(4, price);

                // Execute insert operation
                int n = insertStatement.executeUpdate();

                if (n > 0) {
                    System.out.println("Inserted " + n + " record(s) successfully.");
                } else {
                    System.out.println("Insert failed. Check the query and data.");
                }
            } else {
                System.out.println("Database connection is closed. Can't insert.");
            }
        } catch (SQLException e) {
            System.out.println("Couldn't insert to the database: " + e);
        }
    }

}








