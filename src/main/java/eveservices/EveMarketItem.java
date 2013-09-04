package eveservices;


import java.util.List;

import uudi.UUDIHistory;
import uudi.UUDIOrder;

public class EveMarketItem {
	private int typeId;
	private List<UUDIOrder> buyOrders;
	private List<UUDIOrder> sellOrder;
	private List<UUDIHistory> transactionHistory;
	
	public int getTypeId() {
		return typeId;
	}
	
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
	public List<UUDIOrder> getBuyOrders() {
		return buyOrders;
	}
	
	public void setBuyOrders(List<UUDIOrder> buyOrders) {
		this.buyOrders = buyOrders;
	}
	
	public List<UUDIOrder> getSellOrder() {
		return sellOrder;
	}
	
	public void setSellOrder(List<UUDIOrder> sellOrder) {
		this.sellOrder = sellOrder;
	}
	
	public List<UUDIHistory> getTransactionHistory() {
		return transactionHistory;
	}
	
	public void setTransactionHistory(List<UUDIHistory> transactionHistory) {
		this.transactionHistory = transactionHistory;
	}

}
