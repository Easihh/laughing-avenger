package com.thanatos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import com.thanatos.Dao.OrderDao;
import com.thanatos.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable{
	
	private OrderDao orderDao;
    public TableView<Order> tableView = new TableView<Order>();
    public TableColumn<?, ?> symbolCol = new TableColumn<Object, Object>();
    public TableColumn<?, ?> qtyCol = new TableColumn<Object, Object>();
    private ObservableList<Order> data;
    private FilteredList<Order> filteredData;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
      data = FXCollections.observableArrayList(
    		  new Order("XYZ",99)

            );
      symbolCol.setCellValueFactory(new PropertyValueFactory("symbol"));
      qtyCol.setCellValueFactory(new PropertyValueFactory("qty"));
      symbolCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
      qtyCol.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
      tableView.setItems(data);
      //tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
      //filteredData=new FilteredList<Order>(data,p->true);
	}
	public void createNewOrder(){
		try{
			orderDao=(OrderDao)Main.ctx.getBean("orderDao");
			orderDao.insert(new Order("AMZ",999));
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/com/thanatos/NewOrderview.fxml"));
	        BorderPane page = (BorderPane)loader.load();
	        Stage orderDialog=new Stage();
	        orderDialog.setTitle("STUFF");
			Scene scene = new Scene(page);
			orderDialog.setScene(scene);
			orderDialog.initModality(Modality.WINDOW_MODAL);
			orderDialog.initOwner(Main.primaryStage);
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
