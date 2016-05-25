import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.*;
import java.text.DecimalFormat;
import javafx.application.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Callback;

public class BestCommunicationsStore extends Application{

	Connection conn;
	Statement st;
	PreparedStatement prest;
	CallableStatement cs;
	ResultSet rs;
	
	//stages and scenes
	public static Stage window;
	public static Scene login, store, inventory, customer, sales;
	
	public static Text title;
	
	//buttons, textfields, labels for login scene
	public static Button btnLogin,btnCancel,btnBack,btnLogout;
	public static TextField txtUsr;
	public static PasswordField pwdf;
	public static Label usrlabel,pwdlabel,msglabel, msglabel1, msglabel2, msglabel3;
	
	//buttons, textfields, labels for store scene
	public static Button btnCust, btnInvt, btnSales;
	
	//buttons, textfields, labels, table for customer scene
	public static TextField txtCustID, txtCustFName, txtCustLName, txtCustAddress, txtCustPhone, txtCustCity;
	public static Button btnAddCust, btnUpdateCust, btnSearchCust, btnCustLoad, btnDeleteCust;
	public static TableView tableCust;
    public static ObservableList dataCust = FXCollections.observableArrayList();
	
    //buttons, textfields, labels for inventory scene
  	public static TextField txtInvtID, txtInvtName, txtInvtBrand, txtInvtCategory, txtInvtPrice, txtInvtQty;
  	public static Button btnAddInvt, btnUpdateInvt, btnSearchInvt, btnInvtLoad, btnDeleteInvt;
    public static TableView tableInvt;
    public static ObservableList dataInvt = FXCollections.observableArrayList();
    
    
	//buttons, textfields, labels for sales scene
    public static Label totalLabel;
	public static TextField txtTotal;
	public static Button btnSalesLoad, btnSalesPrint;
	public TableView tableSales;
	public static ObservableList dataSales = FXCollections.observableArrayList();
    
	//Layout
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static VBox vboxLogin, vboxStore, vboxCust, vboxInvt, vboxSales, vboxSales1;
	public static BorderPane borderLogin, borderStore, borderCust, borderInvt, borderSales;
	public static GridPane gridPaneLogin, gridPaneStore, gridPaneCust, gridPaneCustTable, gridPaneInvt, gridPaneSales, gridPaneSales1;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		LoginGUI();
		StoreGUI();
		CustGUI();
		InvtGUI();
		SalesGUI();
		SceneGUI();
		
		//Image icon = new Image(getClass().getResourceAsStream("https://example.com/javaicon.png"));
		window.setTitle("Best Communications Store");
		window.getIcons().add(new Image("abc.png"));
		window.setScene(login);
		window.show();
	}

	public void SceneGUI() {
		double width = (screenSize.getWidth()*0.989);
		double height = (screenSize.getHeight()*0.925);
		borderLogin = new BorderPane();
		borderLogin.setId("border");
		borderLogin.setPadding(new Insets(10,50,50,50));
		borderLogin.setCenter(vboxLogin);
		
		borderStore = new BorderPane();
		borderStore.setId("border");
		borderStore.setPadding(new Insets(10,50,50,50));
		borderStore.setCenter(vboxStore);
		
		borderCust = new BorderPane();
		borderCust.setId("border");
		borderCust.setPadding(new Insets(10,50,50,50));
		borderCust.setCenter(vboxCust);
		
		borderInvt = new BorderPane();
		borderInvt.setId("border");
		borderInvt.setPadding(new Insets(10,50,50,50));
		borderInvt.setCenter(vboxInvt);
		
		borderSales = new BorderPane();
		borderSales.setId("border");
		borderSales.setPadding(new Insets(10,50,50,50));
		borderSales.setCenter(vboxSales);
		/*
		login = new Scene(borderLogin, width, height);
		store = new Scene(borderStore, width, height);
		customer = new Scene(borderCust, width, height);
		inventory = new Scene(borderInvt, width, height);
		sales = new Scene(borderSales, width, height);
		*/
		login = new Scene(borderLogin);
		store = new Scene(borderStore);
		customer = new Scene(borderCust);
		inventory = new Scene(borderInvt);
		sales = new Scene(borderSales);
		
		login.getStylesheets().add(getClass().getClassLoader().getResource("login.css").toExternalForm());
		store.getStylesheets().add(getClass().getClassLoader().getResource("login.css").toExternalForm());
		customer.getStylesheets().add(getClass().getClassLoader().getResource("login.css").toExternalForm());
		inventory.getStylesheets().add(getClass().getClassLoader().getResource("login.css").toExternalForm());
		sales.getStylesheets().add(getClass().getClassLoader().getResource("login.css").toExternalForm());
	}

	public void LoginGUI() throws SQLException {
		gridPaneLogin = new GridPane();
		gridPaneLogin.setPadding(new Insets(40,20,20,20));
		gridPaneLogin.setHgap(5);
		gridPaneLogin.setVgap(5);
        //Reflection for text
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        gridPaneLogin.setEffect(r);
        
		title = new Text("Best Communications Store");
		title.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
		msglabel = new Label();
		usrlabel = new Label("Email: ");
		pwdlabel = new Label("Password: ");
		txtUsr = new TextField();
		txtUsr.setPromptText("Enter your email here!");
		pwdf = new PasswordField();
		pwdf.setPromptText("Enter your password here!");
		btnLogin = new Button("Login");
		btnCancel = new Button("Clear");
		vboxLogin = new VBox();
		vboxLogin.setAlignment(Pos.CENTER);
		gridPaneLogin.setAlignment(Pos.CENTER);
		
		gridPaneLogin.add(usrlabel, 0, 0);
		gridPaneLogin.add(txtUsr, 1, 0);
		gridPaneLogin.add(pwdlabel, 0, 1);
		gridPaneLogin.add(pwdf, 1, 1);
		gridPaneLogin.add(btnLogin, 2, 0);
		gridPaneLogin.add(btnCancel, 2, 1);
		gridPaneLogin.add(msglabel, 1, 2);
        
		gridPaneLogin.setId("gridPane");
        btnLogin.setId("btnLogin");
        btnCancel.setId("btnCancel");
        title.setId("text");
        
        vboxLogin.getChildren().addAll(title, gridPaneLogin);
        
        btnLogin.setOnAction(e -> {
        	
        	String usrname = txtUsr.getText();
        	String pass = pwdf.getText();
        	
        	try {
        		Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://ec2-52-20-54-9.compute-1.amazonaws.com/f_user16", "f_user16", "f_user16"); //This has to be replaced with a properly working database's ip address w/ username and password
				st = conn.createStatement();
				String sql = "Select * from user";
				rs = st.executeQuery(sql);
				while(rs.next()){
					String tmpemail = rs.getString("email");
					String tmppwd = rs.getString("password");
					if(usrname.equals(tmpemail)==true){
						if(pass.equals(tmppwd)==true){
							window.setScene(store);
						}else{
							msglabel.setText("Incorrect Password");
							msglabel.setTextFill(Color.ORANGE);
						}
					}else{
						msglabel.setText("Incorrect Email");
						msglabel.setTextFill(Color.ORANGE);
					}
				}
        	}catch (Exception ex) {
				System.err.println(ex);
			}
        });
        
        btnCancel.setOnAction(e -> {
        	txtUsr.clear();
        	pwdf.clear();
        });
	}
	
	public void StoreGUI() {
		gridPaneStore = new GridPane();
		gridPaneStore.setPadding(new Insets(40,20,20,20));
		gridPaneStore.setHgap(5);
		gridPaneStore.setVgap(5);
        //Reflection for text
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        gridPaneStore.setEffect(r);
        
        title = new Text("Best Communications Store");
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
		
		btnCust = new Button("Customer Table");
		btnInvt = new Button("Inventory Table");
		btnSales = new Button("Sales Table");
		btnLogout = new Button("Logout");
		vboxStore = new VBox();
		vboxStore.setAlignment(Pos.CENTER);
		gridPaneStore.setAlignment(Pos.CENTER);
		
		gridPaneStore.add(btnCust, 0, 0);
		gridPaneStore.add(btnInvt, 1, 0);
		gridPaneStore.add(btnSales, 2, 0);
		gridPaneStore.add(btnLogout, 3, 0);
		        
		gridPaneStore.setId("gridPane");
		btnCust.setId("btnCust");
		btnInvt.setId("btnInvt");
		btnSales.setId("btnSales");
		btnLogout.setId("btnLogout");
        title.setId("text");
        
        vboxStore.getChildren().addAll(title, gridPaneStore);
        
        btnCust.setOnAction(e -> {
        	window.setScene(customer);
        });
        
        btnInvt.setOnAction(e -> {
        	window.setScene(inventory);
        });
        
        btnSales.setOnAction(e -> {
        	window.setScene(sales);
        });
        
        btnLogout.setOnAction(e -> {
        	window.setScene(login);
        	txtUsr.clear();
        	pwdf.clear();
        	clearTF();
        });
	}

	@SuppressWarnings("unchecked")
	public void CustGUI() {
		gridPaneCust = new GridPane();
		gridPaneCust.setPadding(new Insets(40,20,20,20));
		gridPaneCust.setHgap(5);
		gridPaneCust.setVgap(5);
        //Reflection for text
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        gridPaneCust.setEffect(r);
        
        title = new Text("Best Communications Store");
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
		
        msglabel1 = new Label();
        txtCustID = new TextField();
        txtCustID.setPromptText("Customer ID");
        txtCustFName = new TextField();
        txtCustFName.setPromptText("First Name");
        txtCustLName = new TextField();
        txtCustLName.setPromptText("Last Name");
        txtCustAddress = new TextField();
        txtCustAddress.setPromptText("Address");
        txtCustPhone = new TextField();
        txtCustPhone.setPromptText("Phone");
        txtCustCity = new TextField();
        txtCustCity.setPromptText("City");
        
        btnCustLoad = new Button("Load Customers");
		btnSearchCust = new Button("Search Customer");
		btnAddCust = new Button("Add New Customer");
		btnUpdateCust = new Button("Update Customer");
		btnDeleteCust = new Button("Delete Customer");
		btnBack = new Button("Back");
		btnLogout = new Button("Logout");
		vboxCust = new VBox();
		vboxCust.setAlignment(Pos.CENTER);
		gridPaneCust.setAlignment(Pos.CENTER);
		
		TableColumn IdCol = new TableColumn("CustomerID");
		IdCol.setMinWidth(100);
		IdCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(0).toString());                        
            }                    
        });
		
		TableColumn firstNameCol = new TableColumn("First Name");
		firstNameCol.setMinWidth(200);
		firstNameCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(1).toString());                        
            }                    
        });

		TableColumn lastNameCol = new TableColumn("Last Name");
		lastNameCol.setMinWidth(200);
		lastNameCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(2).toString());                        
            }                    
        });
		
		TableColumn AddressCol = new TableColumn("Address");
		AddressCol.setMinWidth(200);
		AddressCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(3).toString());                        
            }                    
        });
 
		TableColumn PhoneCol = new TableColumn("Phone");
		PhoneCol.setMinWidth(200);
		PhoneCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(4).toString());                        
            }                    
        });
        
		TableColumn CityCol = new TableColumn("City");
		CityCol.setMinWidth(200);
		CityCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(5).toString());                        
            }                    
        });
         
        tableCust = new TableView();
        tableCust.setItems(dataCust);
        tableCust.getColumns().addAll(IdCol, firstNameCol, lastNameCol, AddressCol, PhoneCol, CityCol);
		
		gridPaneCust.add(txtCustID, 0, 1);
		gridPaneCust.add(txtCustFName, 1, 1);
		gridPaneCust.add(txtCustLName, 2, 1);
		gridPaneCust.add(txtCustAddress, 0, 2);
		gridPaneCust.add(txtCustPhone, 1, 2);
		gridPaneCust.add(txtCustCity, 2, 2);
		gridPaneCust.add(btnSearchCust, 0, 3);
		gridPaneCust.add(btnAddCust, 1, 3);
		gridPaneCust.add(btnUpdateCust, 2, 3);
		gridPaneCust.add(btnDeleteCust, 3, 3);
		gridPaneCust.add(btnCustLoad, 0, 4);
		gridPaneCust.add(btnBack, 1, 4);
		gridPaneCust.add(btnLogout, 2, 4);
		gridPaneCust.add(msglabel1, 3 ,4);
		
		gridPaneCust.setId("gridPane");
		btnCustLoad.setId("btnCust");
		btnSearchCust.setId("btnCust");
		btnAddCust.setId("btnInvt");
		btnUpdateCust.setId("btnSales");
		btnDeleteCust.setId("btnSales");
		btnBack.setId("btnBack");
		btnLogout.setId("btnLogout");
        title.setId("text");
        
        vboxCust.getChildren().addAll(title, tableCust, gridPaneCust);
        
        btnCustLoad.setOnAction(e -> {
        	dataCust.clear();
            tableCust.setItems(dataCust);
        	try {
        		Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://ec2-52-20-54-9.compute-1.amazonaws.com/f_user16", "f_user16", "f_user16");
				String CustID = txtCustID.getText();
				st = conn.createStatement();
				String sql = "Select * from customer";
				rs = st.executeQuery(sql);
				while(rs.next()){
	                ObservableList<String> row = FXCollections.observableArrayList();
	                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
	                    row.add(rs.getString(i));
	                }
	                System.out.println("Row [1] added "+row );
	                dataCust.add(row);
	            }
				msglabel1.setText("Load completed!");
				msglabel1.setTextFill(Color.ORANGE);
			} catch (Exception ex) {
				System.err.println(ex);
				msglabel1.setText("Error while Loading");
				msglabel1.setTextFill(Color.ORANGE);
			}

        	clearTF();
        });
                        
        btnSearchCust.setOnAction(e -> {
        	dataCust.clear();
            tableCust.setItems(dataCust);
        	try {
        		Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://ec2-52-20-54-9.compute-1.amazonaws.com/f_user16", "f_user16", "f_user16");
				String CustID = txtCustID.getText();
				st = conn.createStatement();
				String sql = "Select * from customer as C where C.customerid = "+CustID;
				rs = st.executeQuery(sql);
				while(rs.next()){
	                ObservableList<String> row = FXCollections.observableArrayList();
	                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
	                    row.add(rs.getString(i));
	                }
	                System.out.println("Row [1] added "+row );
	                dataCust.add(row);
	            }
				msglabel1.setText("Search completed!");
				msglabel1.setTextFill(Color.ORANGE);
			} catch (Exception ex) {
				System.err.println(ex);
				msglabel1.setText("Error while searching");
				msglabel1.setTextFill(Color.ORANGE);
			}

        	clearTF();
        });
        
        btnAddCust.setOnAction(e -> {
        	dataCust.clear();
            tableCust.setItems(dataCust);
        	try {
        		String fname = txtCustFName.getText();
        		String lname = txtCustLName.getText();
        		Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://ec2-52-20-54-9.compute-1.amazonaws.com/f_user16", "f_user16", "f_user16");
				
				prest = conn.prepareStatement("insert into customer (fname,lname,address,phone,city) value (?,?,?,?,?)");
				prest.setString(1, txtCustFName.getText());
				prest.setString(2, txtCustLName.getText());
				prest.setString(3, txtCustAddress.getText());
				prest.setString(4, txtCustPhone.getText());
				prest.setString(5, txtCustCity.getText());
				
				prest.executeUpdate();
				
				st = conn.createStatement();
				String sql = "Select * from customer";
				rs = st.executeQuery(sql);
				while(rs.next()){
	                ObservableList<String> row = FXCollections.observableArrayList();
	                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
	                    row.add(rs.getString(i));
	                }
	                System.out.println("Row [1] added "+row );
	                dataCust.add(row);

	            }				
				msglabel1.setText("Insert new record successfully!!!");
				msglabel1.setTextFill(Color.ORANGE);
			} catch (Exception ex) {
				msglabel1.setText("Insert new record unsuccessfully!!!");
				msglabel1.setTextFill(Color.ORANGE);
				System.err.println(ex);
			}
        	
        	clearTF();
        });
        
        btnUpdateCust.setOnAction(e -> {
        	dataCust.clear();
            tableCust.setItems(dataCust);
        	try {
        		Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://ec2-52-20-54-9.compute-1.amazonaws.com/f_user16", "f_user16", "f_user16");
				
				prest = conn.prepareStatement("update customer set fname=?,lname=?,address=?,phone=?,city=? where customerid=?");
				prest.setString(1, txtCustFName.getText());
				prest.setString(2, txtCustLName.getText());
				prest.setString(3, txtCustAddress.getText());
				prest.setString(4, txtCustPhone.getText());
				prest.setString(5, txtCustCity.getText());
				prest.setString(6, txtCustID.getText());
				
				prest.executeUpdate();
				st = conn.createStatement();
				String sql = "Select * from customer";
				rs = st.executeQuery(sql);
				while(rs.next()){
	                ObservableList<String> row = FXCollections.observableArrayList();
	                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
	                    row.add(rs.getString(i));
	                }
	                System.out.println("Row [1] added "+row );
	                dataCust.add(row);

	            }
				msglabel1.setText("Update record successfully!!!");
				msglabel1.setTextFill(Color.ORANGE);
			} catch (Exception ex) {
				msglabel1.setText("Update record unsuccessfully!!!");
				msglabel1.setTextFill(Color.ORANGE);
				System.err.println(ex);
			}
        	
        	clearTF();
        });
        
        btnDeleteCust.setOnAction(e -> {
        	dataCust.clear();
            tableCust.setItems(dataCust);
        	try {
        		Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://ec2-52-20-54-9.compute-1.amazonaws.com/f_user16", "f_user16", "f_user16");
				prest = conn.prepareStatement("delete from customer where customerid=?");
				prest.setString(1, txtCustID.getText());
				
				prest.executeUpdate();
				
				st = conn.createStatement();
				String sql = "Select * from customer";
				rs = st.executeQuery(sql);
				while(rs.next()){
	                ObservableList<String> row = FXCollections.observableArrayList();
	                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
	                    row.add(rs.getString(i));
	                }
	                System.out.println("Row [1] added "+row );
	                dataCust.add(row);
	            }
				msglabel1.setText("Customer deleted!");
				msglabel1.setTextFill(Color.ORANGE);
			} catch (Exception ex) {
				System.err.println(ex);
				msglabel1.setText("Error while deleting");
				msglabel1.setTextFill(Color.ORANGE);
			}

        	clearTF();
        });
        
        btnBack.setOnAction(e -> {
        	window.setScene(store);
        	clearTF();
        	dataCust.clear();
            tableCust.setItems(dataCust);
            dataInvt.clear();
            tableInvt.setItems(dataInvt);
            dataSales.clear();
            tableSales.setItems(dataSales);
        });
        
        btnLogout.setOnAction(e -> {
        	window.setScene(login);
        	clearTF();
        });
	}
	
	public void InvtGUI(){
		gridPaneInvt = new GridPane();
		gridPaneInvt.setPadding(new Insets(40,20,20,20));
		gridPaneInvt.setHgap(5);
		gridPaneInvt.setVgap(5);
        //Reflection for text
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        gridPaneInvt.setEffect(r);
        
        title = new Text("Best Communications Store");
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
		
        msglabel2 = new Label();
        txtInvtID = new TextField();
        txtInvtID.setPromptText("Product ID");
        txtInvtName = new TextField();
        txtInvtName.setPromptText("Product Name");
        txtInvtBrand = new TextField();
        txtInvtBrand.setPromptText("Product Brand");
        txtInvtCategory = new TextField();
        txtInvtCategory.setPromptText("Category");
        txtInvtPrice = new TextField();
        txtInvtPrice.setPromptText("Price");
        txtInvtQty = new TextField();
        txtInvtQty.setPromptText("Quantity");
        
        btnInvtLoad = new Button("Load Products");
		btnSearchInvt = new Button("Search Product");
		btnAddInvt = new Button("Add New Product");
		btnUpdateInvt = new Button("Update Product");
		btnDeleteInvt = new Button("Delete Product");
		btnBack = new Button("Back");
		btnLogout = new Button("Logout");
		vboxInvt = new VBox();
		vboxInvt.setAlignment(Pos.CENTER);
		gridPaneInvt.setAlignment(Pos.CENTER);
		
		TableColumn InvtIDCol = new TableColumn("ProductID");
		InvtIDCol.setMinWidth(10);
		InvtIDCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(0).toString());                        
            }                    
        });
        
        TableColumn InvtNameCol = new TableColumn("Product Name");
        InvtNameCol.setMinWidth(200);
        InvtNameCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(1).toString());                        
            }                    
        });
        
        TableColumn InvtBrandCol = new TableColumn("Brand");
        InvtBrandCol.setMinWidth(200);
        InvtBrandCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(2).toString());                        
            }                    
        });
        
        TableColumn InvtCategoryCol = new TableColumn("Category");
        InvtCategoryCol.setMinWidth(10);
        InvtCategoryCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(3).toString());                        
            }                    
        });
        
        TableColumn InvtPriceCol = new TableColumn("Price");
        InvtPriceCol.setMinWidth(40);
        InvtPriceCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(4).toString());                        
            }                    
        });
        
        TableColumn InvtQtyCol = new TableColumn("Quantity");
        InvtQtyCol.setMinWidth(10);
        InvtQtyCol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(5).toString());                        
            }                    
        });
        
        tableInvt = new TableView();
        tableInvt.setItems(dataInvt);
        tableInvt.getColumns().addAll(InvtIDCol, InvtNameCol, InvtBrandCol, InvtCategoryCol, InvtPriceCol, InvtQtyCol);
        
        gridPaneInvt.add(txtInvtID, 0, 1);
        gridPaneInvt.add(txtInvtName, 1, 1);
        gridPaneInvt.add(txtInvtBrand, 2, 1);
        gridPaneInvt.add(txtInvtCategory, 0, 2);
        gridPaneInvt.add(txtInvtPrice, 1, 2);
        gridPaneInvt.add(txtInvtQty, 2, 2);
        gridPaneInvt.add(btnSearchInvt, 0, 3);
        gridPaneInvt.add(btnAddInvt, 1, 3);
        gridPaneInvt.add(btnUpdateInvt, 2, 3);
        gridPaneInvt.add(btnDeleteInvt, 3, 3);
        gridPaneInvt.add(btnInvtLoad, 0, 4);
        gridPaneInvt.add(btnBack, 1, 4);
        gridPaneInvt.add(btnLogout, 2, 4);
        gridPaneInvt.add(msglabel2, 3, 4);
		
        gridPaneInvt.setId("gridPane");
        btnInvtLoad.setId("btnCust");
		btnSearchInvt.setId("btnCust");
		btnAddInvt.setId("btnInvt");
		btnUpdateInvt.setId("btnSales");
		btnDeleteInvt.setId("btnSales");
		btnBack.setId("btnBack");
		btnLogout.setId("btnLogout");
        title.setId("text");
        
        vboxInvt.getChildren().addAll(title, tableInvt, gridPaneInvt);
        
        btnInvtLoad.setOnAction(e -> {
        	dataInvt.clear();
            tableInvt.setItems(dataInvt);
        	try {
        		Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://ec2-52-20-54-9.compute-1.amazonaws.com/f_user16", "f_user16", "f_user16");
				String InvtID = txtInvtID.getText();
				String sql = "Select * from products";
				rs = st.executeQuery(sql);
				while(rs.next()){
	                ObservableList<String> row = FXCollections.observableArrayList();
	                row.add(rs.getString("productid"));
	                row.add(rs.getString("productname"));
	                row.add(rs.getString("brand"));
	                row.add(rs.getString("category"));
	                row.add("$"+rs.getString("price"));
	                row.add(rs.getString("qty"));
	                System.out.println("Row [1] added "+row );
	                dataInvt.add(row);

	            }
				msglabel2.setText("Load completed!");
				msglabel2.setTextFill(Color.ORANGE);
			} catch (Exception ex) {
				System.err.println(ex);
				msglabel2.setText("Error while Loading");
				msglabel2.setTextFill(Color.ORANGE);
			}

        	clearTF();
        });
        
        btnSearchInvt.setOnAction(e -> {
        	dataInvt.clear();
            tableInvt.setItems(dataInvt);
        	try {
        		Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://ec2-52-20-54-9.compute-1.amazonaws.com/f_user16", "f_user16", "f_user16");
				String InvtID = txtInvtID.getText();
				String sql = "Select * from products as P where P.productid = "+InvtID;
				rs = st.executeQuery(sql);
				while(rs.next()){
	                ObservableList<String> row = FXCollections.observableArrayList();
	                row.add(rs.getString("productid"));
	                row.add(rs.getString("productname"));
	                row.add(rs.getString("brand"));
	                row.add(rs.getString("category"));
	                row.add("$"+rs.getString("price"));
	                row.add(rs.getString("qty"));
	                System.out.println("Row [1] added "+row );
	                dataInvt.add(row);

	            }
				msglabel2.setText("Search completed!");
				msglabel2.setTextFill(Color.ORANGE);
			} catch (Exception ex) {
				System.err.println(ex);
				msglabel2.setText("Error while searching");
				msglabel2.setTextFill(Color.ORANGE);
			}

        	clearTF();
        });
        
        btnAddInvt.setOnAction(e -> {
        	dataInvt.clear();
            tableInvt.setItems(dataInvt);
        	try {
        		Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://ec2-52-20-54-9.compute-1.amazonaws.com/f_user16", "f_user16", "f_user16");
				
				prest = conn.prepareStatement("insert into products (productname,brand,category,price,qty) value (?,?,?,?,?)");
				prest.setString(1, txtInvtName.getText());
				prest.setString(2, txtInvtBrand.getText());
				prest.setString(3, txtInvtCategory.getText());
				prest.setString(4, txtInvtPrice.getText());
				prest.setInt(5, Integer.parseInt(txtInvtQty.getText()));
				
				prest.executeUpdate();
				String sql = "select * from products";
				rs = st.executeQuery(sql);
				while(rs.next()){
	                ObservableList<String> row = FXCollections.observableArrayList();
	                row.add(rs.getString("productid"));
	                row.add(rs.getString("productname"));
	                row.add(rs.getString("brand"));
	                row.add(rs.getString("category"));
	                row.add("$"+rs.getString("price"));
	                row.add(rs.getString("qty"));
	                System.out.println("Row [1] added "+row );
	                dataInvt.add(row);
	            }				
				msglabel2.setText("Insert new record successfully!!!");
				msglabel2.setTextFill(Color.ORANGE);
			} catch (Exception ex) {
				msglabel2.setText("Insert new record unsuccessfully!!!");
				msglabel2.setTextFill(Color.ORANGE);
				System.err.println(ex);
			}
        	
        	clearTF();
        });
        
        btnUpdateInvt.setOnAction(e -> {
        	dataInvt.clear();
            tableInvt.setItems(dataInvt);
        	try {
        		Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://ec2-52-20-54-9.compute-1.amazonaws.com/f_user16", "f_user16", "f_user16");
				
				prest = conn.prepareStatement("update products set productname=?,brand=?,category=?,price=?,qty=? where productid=?");
				prest.setString(1, txtInvtName.getText());
				prest.setString(2, txtInvtBrand.getText());
				prest.setString(3, txtInvtCategory.getText());
				prest.setString(4, txtInvtPrice.getText());
				prest.setInt(5, Integer.parseInt(txtInvtQty.getText()));
				prest.setString(6, txtInvtID.getText());
				
				prest.executeUpdate();
				String sql = "select * from products";
				rs = st.executeQuery(sql);
				while(rs.next()){
	                ObservableList<String> row = FXCollections.observableArrayList();
	                row.add(rs.getString("productid"));
	                row.add(rs.getString("productname"));
	                row.add(rs.getString("brand"));
	                row.add(rs.getString("category"));
	                row.add("$"+rs.getString("price"));
	                row.add(rs.getString("qty"));
	                System.out.println("Row [1] added "+row );
	                dataInvt.add(row);
	            }
				msglabel2.setText("Update record successfully!!!");
				msglabel2.setTextFill(Color.ORANGE);
			} catch (Exception ex) {
				msglabel2.setText("Update record unsuccessfully!!!");
				msglabel2.setTextFill(Color.ORANGE);
				System.err.println(ex);
			}
        	
        	clearTF();
        });
        
        btnDeleteInvt.setOnAction(e -> {
        	dataInvt.clear();
            tableInvt.setItems(dataInvt);
        	try {
        		Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://ec2-52-20-54-9.compute-1.amazonaws.com/f_user16", "f_user16", "f_user16");
				prest = conn.prepareStatement("delete from products where productid=?");
				prest.setString(1, txtInvtID.getText());
				
				prest.executeUpdate();
				
				st = conn.createStatement();
				String sql = "Select * from products";
				rs = st.executeQuery(sql);
				while(rs.next()){
	                ObservableList<String> row = FXCollections.observableArrayList();
	                row.add(rs.getString("productid"));
	                row.add(rs.getString("productname"));
	                row.add(rs.getString("brand"));
	                row.add(rs.getString("category"));
	                row.add("$"+rs.getString("price"));
	                row.add(rs.getString("qty"));
	                System.out.println("Row [1] added "+row );
	                dataInvt.add(row);
	            }
				msglabel2.setText("Product deleted!");
				msglabel2.setTextFill(Color.ORANGE);
			} catch (Exception ex) {
				System.err.println(ex);
				msglabel2.setText("Error while deleting");
				msglabel2.setTextFill(Color.ORANGE);
			}

        	clearTF();
        });
        
        btnBack.setOnAction(e -> {
        	window.setScene(store);
        	clearTF();
        	dataCust.clear();
            tableCust.setItems(dataCust);
            dataInvt.clear();
            tableInvt.setItems(dataInvt);
            dataSales.clear();
            tableSales.setItems(dataSales);
        });
        
        btnLogout.setOnAction(e -> {
        	window.setScene(login);
        	clearTF();
        });
	}
	
	public void SalesGUI(){
		gridPaneSales = new GridPane();
		gridPaneSales.setPadding(new Insets(40,20,20,20));
		gridPaneSales.setHgap(5);
		gridPaneSales.setVgap(5);
		gridPaneSales1 = new GridPane();
		gridPaneSales1.setPadding(new Insets(40,20,20,20));
		gridPaneSales1.setHgap(5);
		gridPaneSales1.setVgap(5);
        //Reflection for text
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        gridPaneSales.setEffect(r);
        gridPaneSales1.setEffect(r);
        
        title = new Text("Best Communications Store");
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
		
        msglabel3 = new Label();
        totalLabel = new Label("Total Sales");
        txtTotal = new TextField();
        txtTotal.setPromptText("Total Sales");
		btnSalesLoad = new Button("Load Sales");
		btnSalesPrint = new Button("Print Table");
		btnBack = new Button("Back");
		btnLogout = new Button("Logout");
		vboxSales = new VBox();
		vboxSales.setAlignment(Pos.CENTER);
		vboxSales1 = new VBox();
		vboxSales1.setAlignment(Pos.CENTER);
		gridPaneSales.setAlignment(Pos.CENTER);
		gridPaneSales1.setAlignment(Pos.CENTER);
		
		TableColumn Idcol = new TableColumn("SalesID");
		Idcol.setMinWidth(5);
        Idcol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(0).toString());                        
            }                    
        });
        
        TableColumn ODatecol = new TableColumn("Date Ordered");
        ODatecol.setMinWidth(85);
        ODatecol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(1).toString());                        
            }                    
        });
        
        TableColumn SDatecol = new TableColumn("Date Shipped");
        SDatecol.setMinWidth(50);
        SDatecol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(2).toString());                        
            }                    
        });
        
        TableColumn PIDcol = new TableColumn("ProductID");
        PIDcol.setMinWidth(10);
        PIDcol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(3).toString());                        
            }                    
        });
        
        TableColumn OQtycol = new TableColumn("OrderQty");
        OQtycol.setMinWidth(10);
        OQtycol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(4).toString());                        
            }                    
        });
        
        TableColumn Revcol = new TableColumn("Subtotal");
        Revcol.setMinWidth(40);
        Revcol.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                return new SimpleStringProperty(param.getValue().get(5).toString());                        
            }                    
        });
        
		tableSales = new TableView();
        tableSales.setItems(dataSales);
        tableSales.getColumns().addAll(Idcol,ODatecol,SDatecol,PIDcol,OQtycol,Revcol);
        
        gridPaneSales1.add(totalLabel, 0, 0);
        gridPaneSales1.add(txtTotal, 1, 0);
        gridPaneSales.add(btnSalesLoad, 0, 1);
        gridPaneSales.add(btnSalesPrint, 1, 1);
        gridPaneSales.add(btnBack, 2, 1);
        gridPaneSales.add(btnLogout, 3, 1);
        gridPaneSales.add(msglabel3, 1, 2);
        
        gridPaneSales.setId("gridPane");
        gridPaneSales1.setId("gridPane");
        btnSalesLoad.setId("btnCust");
        btnSalesPrint.setId("btnCust");
		btnBack.setId("btnBack");
		btnLogout.setId("btnLogout");
        title.setId("text");
        
        vboxSales1.getChildren().addAll(title, tableSales, gridPaneSales1);
        vboxSales.getChildren().addAll(vboxSales1, gridPaneSales);
        
        btnSalesLoad.setOnAction(e -> {
        	double total = 0.0;
        	DecimalFormat f = new DecimalFormat("##.00");
        	try {
        		Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://ec2-52-20-54-9.compute-1.amazonaws.com/f_user16", "f_user16", "f_user16");
				String sql = "select Orders.orderid as OrderID, Orders.dateordered as OrderDate, Orders.dateshipped as ShipDate, Orders.productid as ProductID, Orders.quantity as OrderQty, round(Orders.quantity * products.price,2) as Subtotal from Orders inner join products on Orders.productid = products.productid";
				rs = st.executeQuery(sql);
				while(rs.next()){
	                ObservableList<String> row = FXCollections.observableArrayList();
	                row.add(rs.getString("OrderID"));
	                row.add(rs.getString("OrderDate"));
	                row.add(rs.getString("ShipDate"));
	                row.add(rs.getString("ProductID"));
	                row.add(rs.getString("OrderQty"));
	                row.add("$"+rs.getString("Subtotal"));
	                total += Double.parseDouble(rs.getString("Subtotal"));
	                txtTotal.setText("$"+f.format(total));
	                System.out.println("Row [1] added "+row );
	                dataSales.add(row);
	            }
				msglabel3.setText("Load Completed!");
				msglabel3.setTextFill(Color.ORANGE);
        	}catch (Exception ex) {
        		System.err.println(ex);
				msglabel3.setText("Error while loading");
				msglabel3.setTextFill(Color.ORANGE);
        	}
        });
        
        btnSalesPrint.setOnAction(e -> {
        	try{
        		printSales();
                
        		msglabel3.setText("Print Completed!");
				msglabel3.setTextFill(Color.ORANGE);
        	}catch (Exception ex){
        		msglabel3.setText("Error while printing");
				msglabel3.setTextFill(Color.ORANGE);
        	}
        });
        
        btnBack.setOnAction(e -> {
        	window.setScene(store);
        	clearTF();
        	dataCust.clear();
            tableCust.setItems(dataCust);
            dataInvt.clear();
            tableInvt.setItems(dataInvt);
            dataSales.clear();
            tableSales.setItems(dataSales);
        });
        
        btnLogout.setOnAction(e -> {
        	window.setScene(login);
        	clearTF();
        });
	}
	
	public void clearTF(){
		txtUsr.clear();
    	pwdf.clear();
		txtCustID.clear();
    	txtCustFName.clear();
        txtCustLName.clear();
        txtCustAddress.clear();
        txtCustPhone.clear();
        txtCustCity.clear();
        txtInvtID.clear();;
        txtInvtName.clear();;
        txtInvtBrand.clear();;
        txtInvtCategory.clear();;
        txtInvtPrice.clear();;
        txtInvtQty.clear();
        txtTotal.clear();
	}
	
	public void printSales(){
		PrinterJob job = PrinterJob.createPrinterJob();
		//boolean success=job.printPage(vboxSales1);
		//boolean success = job.printPage();	
		/*
		if (job != null && job.showPrintDialog(vboxSales.getScene().getWindow())){
		    boolean success = job.printPage(vboxSales1);
		    if (success) {
		        job.endJob();
		    }
		}
		*/
		if (job != null && job.showPageSetupDialog(vboxSales.getScene().getWindow())){
		    boolean success = job.printPage(vboxSales1);
		    if (success) {
		        job.endJob();
		    }
		}
	}
}
