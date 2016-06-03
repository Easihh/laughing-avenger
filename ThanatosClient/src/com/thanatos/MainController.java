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
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainController implements Initializable{
	
	private OrderDao orderDao;
	private QuoteDao quoteDao;
	@FXML
    public TableView<Order> pendingOrdersTableView;
	@FXML
	public TableView<Order> openTradeTableView;
	@FXML
	public TableView<Quote> quoteTableView;
    @FXML
    public TableColumn<?, ?> quoteTableSymbolCol;
    @FXML
    public TableColumn<?, ?> quoteTableVolumeCol;
    @FXML
    public TableColumn<?, ?> quoteTableLastPxCol;
    @FXML
    public TableColumn<?, ?> quoteTableBidCol;
    @FXML
    public TableColumn<?, ?> quoteTableAskCol;
    @FXML
    public TableColumn<?, ?> quoteTableDayLowCol;
    @FXML
    public TableColumn<?, ?> quoteTableDayHighCol;
    @FXML
    public TableColumn<?, ?> quoteTableChangeCol;
    @FXML
    public TableColumn<?, ?> symbolCol;
    @FXML
    public TableColumn<?, ?> qtyCol;
    private ObservableList<Order> data;
    private ObservableList<Quote> currentQuote;
    private FilteredList<Order> filteredData;
    @FXML
    private AnchorPane	accountInfo;
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	  quoteDao=(QuoteDao)Main.ctx.getBean("quoteDao");
	  List<Quote> quoteList=quoteDao.getWatchedQuotes();
	  data = FXCollections.observableArrayList(
			  new Order("XYZ",99),new Order("XYZ",99),new Order("XYZ",99),new Order("XYZ",99),new Order("XYZ",99),
			  new Order("XYZ",99),new Order("XYZ",99)
	        );
	  currentQuote=FXCollections.observableArrayList();
	  currentQuote.addAll(quoteList);
	  quoteTableSymbolCol.setCellValueFactory(new PropertyValueFactory("symbol"));
	  quoteTableVolumeCol.setCellValueFactory(new PropertyValueFactory("volume"));
	  quoteTableLastPxCol.setCellValueFactory(new PropertyValueFactory("lastPx"));
	  quoteTableBidCol.setCellValueFactory(new PropertyValueFactory("bid"));
	  quoteTableAskCol.setCellValueFactory(new PropertyValueFactory("ask"));
	  quoteTableDayLowCol.setCellValueFactory(new PropertyValueFactory("dayLow"));
	  quoteTableDayHighCol.setCellValueFactory(new PropertyValueFactory("dayHigh"));
	  quoteTableChangeCol.setCellValueFactory(new PropertyValueFactory("change"));
	  quoteTableView.setItems(currentQuote);
	  //quoteTableView.setPlaceholder(new Label(""));
	  pendingOrdersTableView.setPlaceholder(new Label(""));
	  symbolCol.setCellValueFactory(new PropertyValueFactory("symbol"));
	  qtyCol.setCellValueFactory(new PropertyValueFactory("qty"));
	  symbolCol.prefWidthProperty().bind(pendingOrdersTableView.widthProperty().multiply(0.25));
	  qtyCol.prefWidthProperty().bind(pendingOrdersTableView.widthProperty().multiply(0.25));
	  pendingOrdersTableView.setItems(data);
	  quoteTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	  openTradeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
