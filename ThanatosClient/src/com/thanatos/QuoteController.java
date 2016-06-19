package com.thanatos;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.thanatos.Dao.OrderDao;
import com.thanatos.Dao.QuoteDao;
import com.thanatos.model.Order;
import com.thanatos.model.Quote;
import com.thanatos.shared.RmiQuote;
import com.thanatos.shared.RmiQuoteIntf;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class QuoteController implements Initializable{
	
	private OrderDao orderDao;
	private QuoteDao quoteDao;
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
    
    private ObservableList<Order> data;
    private ObservableList<Quote> quotes;
    private FilteredList<Order> filteredData;
    @FXML
    private AnchorPane	accountInfo;
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		quoteDao=(QuoteDao)Main.ctx.getBean("quoteDao");
		List<RmiQuote> rmiQuotes;
		try{
			Registry myReg=LocateRegistry.getRegistry("127.0.0.1",5055);
			RmiQuoteIntf rmi=(RmiQuoteIntf)myReg.lookup("quote");
			List<String> myQuotes=new ArrayList<>();
			myQuotes.add("GOOG");
			rmiQuotes=rmi.getQuotesInfo(myQuotes);
			System.out.println("aRGG");
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	  List<Quote> quoteList=quoteDao.getWatchedQuotes();
	  data = FXCollections.observableArrayList(
			  new Order("XYZ",99),new Order("XYZ",99),new Order("XYZ",99),new Order("XYZ",99),new Order("XYZ",99),
			  new Order("XYZ",99),new Order("XYZ",99)
	        );
	  quotes=FXCollections.observableArrayList();
	  quotes.addAll(quoteList);
	  quoteTableSymbolCol.setCellValueFactory(new PropertyValueFactory("symbol"));
	  quoteTableVolumeCol.setCellValueFactory(new PropertyValueFactory("volume"));
	  quoteTableLastPxCol.setCellValueFactory(new PropertyValueFactory("lastPx"));
	  quoteTableBidCol.setCellValueFactory(new PropertyValueFactory("bid"));
	  quoteTableAskCol.setCellValueFactory(new PropertyValueFactory("ask"));
	  quoteTableDayLowCol.setCellValueFactory(new PropertyValueFactory("dayLow"));
	  quoteTableDayHighCol.setCellValueFactory(new PropertyValueFactory("dayHigh"));
	  quoteTableChangeCol.setCellValueFactory(new PropertyValueFactory("change"));
	  quoteTableView.setItems(quotes);
	  quoteTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	public void refreshMonitor() {
		System.out.println("Refresh Quote Table");
	}
}
