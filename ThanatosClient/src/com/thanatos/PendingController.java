package com.thanatos;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.ResourceBundle;

import com.thanatos.model.Order;
import com.thanatos.shared.RmiOrder;
import com.thanatos.shared.RmiOrderIntf;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML
    public TableColumn<?, ?> ordStatusCol;
    @FXML
    public TableColumn<?, ?>  ticketCol;
    private ObservableList<Order> pOrders;
    @FXML
    private AnchorPane	accountInfo;
    private RmiOrderIntf rmi;
    private final String targetIp="127.0.0.1";
    private final int targetPort=5055;
    private Registry myReg;
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pOrders=FXCollections.observableArrayList();
		try{
			myReg=LocateRegistry.getRegistry(targetIp,targetPort);
			rmi=(RmiOrderIntf)myReg.lookup("order");
			List<RmiOrder> rmiOrders=rmi.getPendingOrders();
			pOrders.addAll(Order.rmiOrdertoOrder(rmiOrders));
		    symbolCol.setCellValueFactory(new PropertyValueFactory("symbol"));
		    qtyCol.setCellValueFactory(new PropertyValueFactory("qty"));
		    ordStatusCol.setCellValueFactory(new PropertyValueFactory("orderStatus"));
		    ticketCol.setCellValueFactory(new PropertyValueFactory("ticketID"));
		    pendingOrdersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		    pendingOrdersTableView.setItems(pOrders);
		  }
		catch(Exception e ){
			System.out.println(e.getMessage());
		}
	}

	public void refreshMonitor() {
		System.out.println("Refresh Pending Order Table");
	}
}
