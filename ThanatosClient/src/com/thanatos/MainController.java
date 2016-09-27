package com.thanatos;

import java.net.URL;
import java.util.ResourceBundle;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.thanatos.model.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable{
	
	@FXML
    public TableView<Order> pendingOrdersTableView;
    @FXML
    private AnchorPane	accountInfo;
    @FXML
    private AnchorPane mainPane;
    private RemoteOrderProducer producer;
	private RefreshQueueConsumer refresh;
	private Connection connection;
	private static MainController mainController=null;
	@FXML
	private OpenOrderController openPanelController;
	@FXML
	private PendingController pendingPanelController;
	@FXML
	private QuoteController quotePanelController;
	@FXML
	private AccountInfoController accountPanelController;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mainController=this;
	}
	
	public void createNewOrder(){
		try{
	        FXMLLoader loader = new FXMLLoader();
	        Stage current=(Stage)mainPane.getScene().getWindow();
	        loader.setLocation(getClass().getResource("/NewOrderView.fxml"));
	        AnchorPane page = (AnchorPane)loader.load();
	        NewOrderController controller=(NewOrderController)loader.getController();
	        controller.setOrderProducer(producer);
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
		
	public void setupMq(){
		ConnectionFactory factory=new ConnectionFactory();
		factory.setRequestedHeartbeat(30);
		factory.setHost("localhost");
		try {
				connection = factory.newConnection();
				refresh=new RefreshQueueConsumer(connection);
				producer=new RemoteOrderProducer(connection);
			} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static MainController getInstance(){
		return mainController;
	}
	
	public OpenOrderController getOpenPanelController() {
		return openPanelController;
	}
	
	public PendingController getPendingPanelController() {
		return pendingPanelController;
	}

	public QuoteController getQuotePanelController() {
		return quotePanelController;
	}

	public AccountInfoController getAccountPanelController() {
		return accountPanelController;
	}
	
}
