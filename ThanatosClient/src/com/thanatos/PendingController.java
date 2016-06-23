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

public class PendingController implements Initializable{
	
	@FXML
	public TableView<Order> pendingOrdersTableView;
    @FXML
    public TableColumn<?, ?> symbolCol;
    @FXML
    public TableColumn<?, ?> qtyCol;
    private ObservableList<Order> pOrders;
    @FXML
    private AnchorPane	accountInfo;
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	  pOrders=FXCollections.observableArrayList();
	  //pOrders.add(new Order("AMZ",9999));
	  symbolCol.setCellValueFactory(new PropertyValueFactory("symbol"));
	  qtyCol.setCellValueFactory(new PropertyValueFactory("qty"));
	  //symbolCol.prefWidthProperty().bind(pendingOrdersTableView.widthProperty().multiply(0.25));
	  //qtyCol.prefWidthProperty().bind(pendingOrdersTableView.widthProperty().multiply(0.25));
	  //pendingOrdersTableView.setItems(pOrders);
	  pendingOrdersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	public void refreshMonitor() {
		System.out.println("Refresh Pending Order Table");
	}
}
