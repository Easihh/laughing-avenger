package com.thanatos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.thanatos.Dao.OrderDao;
import com.thanatos.Dao.QuoteDao;
import com.thanatos.model.Order;
import com.thanatos.model.Quote;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable{
	
	private OrderDao orderDao;
	private QuoteDao quoteDao;
	@FXML
    public TableView<Order> pendingOrdersTableView;
	@FXML
	public TableView<Order> openTradeTableView;
    @FXML
    public TableColumn<?, ?> symbolCol;
    @FXML
    public TableColumn<?, ?> qtyCol;
    private ObservableList<Order> data;
    private FilteredList<Order> filteredData;
    @FXML
    private AnchorPane	accountInfo;
    @FXML
    private AnchorPane mainPane;
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	  data = FXCollections.observableArrayList(
			  new Order("XYZ",99),new Order("XYZ",99),new Order("XYZ",99),new Order("XYZ",99),new Order("XYZ",99),
			  new Order("XYZ",99),new Order("XYZ",99)
	        );
	  //orderDao=(OrderDao)Main.ctx.getBean("orderDao");
	  symbolCol.setCellValueFactory(new PropertyValueFactory("symbol"));
	  qtyCol.setCellValueFactory(new PropertyValueFactory("qty"));
	  symbolCol.prefWidthProperty().bind(pendingOrdersTableView.widthProperty().multiply(0.25));
	  qtyCol.prefWidthProperty().bind(pendingOrdersTableView.widthProperty().multiply(0.25));
	  pendingOrdersTableView.setItems(data);
	  pendingOrdersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}
	public void createNewOrder(){
		try{
	        FXMLLoader loader = new FXMLLoader();
	        Stage current=(Stage)mainPane.getScene().getWindow();
	        loader.setLocation(getClass().getResource("/NewOrderView.fxml"));
	        AnchorPane page = (AnchorPane)loader.load();
	        Stage orderDialog=new Stage();	        
	        orderDialog.setTitle("Create Order");
			Scene scene = new Scene(page);
			orderDialog.setScene(scene);
			orderDialog.initModality(Modality.WINDOW_MODAL);
			orderDialog.initOwner(current);
			orderDialog.show();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void loadData(){
		URL u;
		InputStream is=null;
		BufferedReader dis;
		String s="";
		try{
			u=new URL("http://download.finance.yahoo.com/d/quotes.csv?s=GOOG&f=sl1d1t1c1ohgv&e=.csv");
			is=u.openStream();
			dis=new BufferedReader(new InputStreamReader(is));
			while((s=dis.readLine())!=null)
				System.out.println(s);
		}
		catch(MalformedURLException me){
			System.out.println(me.getMessage());
		}
		catch(IOException io){
			System.out.println(io.getMessage());
		}
		finally{
			try{is.close();}catch(IOException io){io.getMessage();}
		}
	}
}
