package com.thanatos;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.ibatis.session.SqlSession;
import com.thanatos.model.Order;
import com.thanatos.utility.MyBatisUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController extends BaseController implements Initializable{
    public TableView<Order> tableView = new TableView<Order>();
    public TableColumn<?, ?> symbolCol = new TableColumn<Object, Object>();
    public TableColumn<?, ?> qtyCol = new TableColumn<Object, Object>();
    ObservableList<Order> data;
    FilteredList<Order> filteredData;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
      data = FXCollections.observableArrayList(
    		  new Order("XYZ",99)

            );
      symbolCol.setCellValueFactory(new PropertyValueFactory("symbol"));
      qtyCol.setCellValueFactory(new PropertyValueFactory("qty"));
      tableView.setItems(data);
      //filteredData=new FilteredList<Order>(data,p->true);
	}
	public void testShow(){
		SqlSession session=MyBatisUtil.getSqlSessionFactory().openSession();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("currentName","Yoshie");
		map.put("newName","TEST");
	    int success=session.update("test",map);
	    session.commit();
	    session.close();
	}
	public void createNewOrder(){
		try{
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
