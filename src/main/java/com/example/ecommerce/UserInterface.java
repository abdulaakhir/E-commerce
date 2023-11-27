package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class UserInterface {
    GridPane loginPage;
    HBox headerBar;
    HBox footerBar;

    Button signInButton;

    Label welcomeLabel;

    VBox body;
    Customer loggedInCustomer;

    ProductList productList=new ProductList();

    VBox productPage;

    Button placeOrderButton=new Button("Place Order");

    ObservableList<Product> itemsInCart= FXCollections.observableArrayList();
     public BorderPane createContent(){
        BorderPane root=new BorderPane();
        root.setPrefSize(800,600);
        //root.getChildren().add(loginPage);//method to add nodes as children to pane
        root.setTop(headerBar);
        //root.setCenter(loginPage);
         body=new VBox();
         body.setPadding(new Insets(10));
         body.setAlignment(Pos.CENTER);
         root.setCenter(body);
         productPage=productList.getAllProducts();
         body.getChildren().add(productPage);

         root.setBottom(footerBar);

         return root;
    }

    public UserInterface() {
        createLoginPage();
        createHeaderBar();
        createFooterBar();

    }

    private void createLoginPage(){
        Text userNameText=new Text("User Name");
        Text passwordText=new Text("Password");

        TextField userName=new TextField("angad@gmail.com");
        /////////////////////////////////////above line


        userName.setPromptText("Type your user name here");
        PasswordField password=new PasswordField();
        password.setText("abc123");
        //////////////////////////////////////////above line


        password.setPromptText("Type your password here");
        Label messageLabel=new Label("Hi");

        Button loginButton=new Button("Login");

        loginPage=new GridPane();
        loginPage.setStyle(" -fx-background-color:gray;");
        //is se hum background color ko change kar sukte hai
        loginPage.setAlignment(Pos.CENTER);
        loginPage.setHgap(10);
        loginPage.setVgap(10);
        loginPage.add(userNameText,0,0);
        loginPage.add(userName,1,0);
        loginPage.add(passwordText,0,1);
        loginPage.add(password,1,1);
        loginPage.add(messageLabel,0,2);
        loginPage.add(loginButton,1,2);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name=userName.getText();
                String pass=password.getText();
                Login login=new Login();
                loggedInCustomer=login.customerLogin(name,pass);
                if(loggedInCustomer!=null){
                    messageLabel.setText("Welcome " + loggedInCustomer.getName());
                    welcomeLabel.setText("Welcome-" + loggedInCustomer.getName());
                    headerBar.getChildren().add(welcomeLabel);
                    body.getChildren().clear();
                    body.getChildren().add(productPage);
                }
                else{
                    messageLabel.setText("Login Failed ! please provide correct credential");
                }
            }
        });
    }

    private void createHeaderBar(){
         Button homeButton=new Button();
         Image image=new Image("D:\\project\\Ecommerce\\src\\ecommerimage.jpg");
         ImageView imageView=new ImageView();
         imageView.setImage(image);
         imageView.setFitHeight(20);
         imageView.setFitWidth(80);
         homeButton.setGraphic(imageView);

         TextField searchBar=new TextField();
         searchBar.setPromptText("Search here");
         searchBar.setPrefWidth(180);

         Button searchButton=new Button("Search");

         signInButton=new Button("Sign In");
         welcomeLabel =new Label();

         Button cartButton=new Button("cart");

         Button orderButton=new Button("orders");

         headerBar=new HBox();
         headerBar.setStyle(" -fx-background-color:gray;");
         headerBar.setPadding(new Insets(10));
         headerBar.setSpacing(10);
         headerBar.setAlignment(Pos.CENTER);
         headerBar.getChildren().addAll(homeButton,searchBar,searchButton,signInButton,cartButton,orderButton);

         signInButton.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent actionEvent) {
                 body.getChildren().clear();//remove everything
                 body.getChildren().add(loginPage);//put login page
                 headerBar.getChildren().remove(signInButton);
             }
         });

         cartButton.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent actionEvent) {
                 body.getChildren().clear();
                 VBox prodPage=productList.getProductsInCart(itemsInCart);
                 prodPage.setAlignment(Pos.CENTER);
                 prodPage.getChildren().add(placeOrderButton);
                 body.getChildren().add(prodPage);
                 footerBar.setVisible(false);//all case need to handle for this
             }
         });

         placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent actionEvent) {
                 //need list of products and a customer
                 if(itemsInCart==null){
                     //please select a product first to place order
                     showDialog("please add some products in the cart to place order!");
                     return ;
                 }
                 if(loggedInCustomer==null){
                     showDialog("Please login first to place order");
                     return ;
                 }
                 int count = Order.placeMultipleOrder(loggedInCustomer,itemsInCart);
                 if(count!=0){
                     showDialog("Order for"+count+"products placed successfully!!");
                 }else{
                     showDialog("Order failed!!");
                 }
             }
         });

         homeButton.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent actionEvent) {
                 body.getChildren().clear();
                 body.getChildren().add(productPage);
                 footerBar.setVisible(true);
                 if(loggedInCustomer==null && headerBar.getChildren().indexOf(signInButton)==-1){
                         headerBar.getChildren().add(signInButton);
                 }
             }
         });

    }

    private void createFooterBar(){
        Button byNowButton=new Button("BuyNow");
        Button addToCartButton=new Button("Add to Cart");

        footerBar=new HBox();
//         headerBar.setStyle(" -fx-background-color:gray;");
        footerBar.setPadding(new Insets(10));
        footerBar.setSpacing(10);
        footerBar.setAlignment(Pos.CENTER);
        footerBar.getChildren().addAll(byNowButton,addToCartButton);

        byNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product=productList.getSelectedProduct();
                if(product==null){
                    //please select a product first to place order
                    showDialog("please select a product first to place order!");
                    return ;
                }
                if(loggedInCustomer==null){
                    showDialog("Please login first to place order");
                    return ;
                }
                int count = Order.placeMultipleOrder(loggedInCustomer,itemsInCart);
                if(count!=0){
                    showDialog("Order for successfully!!");
                }else{
                    showDialog("Order failed!!");
                }
            }
        });

        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product=productList.getSelectedProduct();
                if(product==null){
                    //please select a product first to place order
                    showDialog("please select a product first to add it to cart");
                    return ;
                }
                itemsInCart.add(product);
                showDialog("Selected Item has been added to the cart successfully");
            }
        });
    }

    private void showDialog(String message){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setTitle("Message");
        alert.showAndWait();
    }
}
