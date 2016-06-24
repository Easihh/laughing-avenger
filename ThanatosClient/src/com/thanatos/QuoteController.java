package com.thanatos;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.thanatos.model.Quote;
import com.thanatos.shared.RmiQuote;
import com.thanatos.shared.RmiQuoteIntf;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class QuoteController implements Initializable{
	
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
    private static ObservableList<Quote> quotes;
    @FXML
    private AnchorPane	accountInfo;
    private Registry myReg;
    private RmiQuoteIntf rmi;
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ControllerManager.setQuoteController(this);
		List<RmiQuote> rmiQuotes;
		List<Quote> quoteList=new ArrayList<Quote>();
		quotes=FXCollections.observableArrayList();
		try{
			myReg=LocateRegistry.getRegistry("127.0.0.1",5055);
			rmi=(RmiQuoteIntf)myReg.lookup("quote");
			List<String> myQuotes=new ArrayList<>();
			myQuotes.add("GOOG");
			rmiQuotes=rmi.getQuotesInfo(myQuotes);
			quoteList=Quote.rmiOrderToOrder(rmiQuotes);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
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
		try{
			quotes.clear();
			List<String> myQuotes=new ArrayList<>();
			myQuotes.add("GOOG");
			List<RmiQuote> rmiQuotes=rmi.getQuotesInfo(myQuotes);
			if(rmiQuotes.size()==0)
				System.out.println("Failed to get Quotes Infos");
			List<Quote> quoteList=Quote.rmiOrderToOrder(rmiQuotes);
			if(quoteList.size()==0)
				System.out.println("Monitor will be Empty");
			quotes.addAll(quoteList);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
