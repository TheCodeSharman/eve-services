package uudi;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"price","volRemaining","range","orderID","volEntered","minVolume","bid","issueDate","duration","stationID","solarSystemID"})
public class UUDIOrder {

	@JsonProperty("orderID")
	private BigInteger orderId;
	
	private BigDecimal price;
	
	private int volRemaining;
	
	private int range;
	
	private int volEntered;
	
	private int minVolume;
	
	private boolean bid;
	
	private Date issueDate;
	
	private int duration;
	
	@JsonProperty("stationID")
	private int stationId;
	
	@JsonProperty("solarSystemID")
	private Integer solarSystemId; // Optional 
	
	public BigInteger getOrderId() {
		return orderId;
	}
	
	public void setOrderId(BigInteger orderId) {
		this.orderId = orderId;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public int getVolRemaining() {
		return volRemaining;
	}
	
	public void setVolRemaining(int volRemaining) {
		this.volRemaining = volRemaining;
	}
	
	public int getRange() {
		return range;
	}
	
	public void setRange(int range) {
		this.range = range;
	}
	
	public int getVolEntered() {
		return volEntered;
	}
	
	public void setVolEntered(int volEntered) {
		this.volEntered = volEntered;
	}
	
	public int getMinVolume() {
		return minVolume;
	}
	
	public void setMinVolume(int minVolume) {
		this.minVolume = minVolume;
	}
	
	public boolean isBid() {
		return bid;
	}
	
	public void setBid(boolean bid) {
		this.bid = bid;
	}
	
	public Date getIssueDate() {
		return issueDate;
	}
	
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public int getStationId() {
		return stationId;
	}
	
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	
	public Integer getSolarSystemId() {
		return solarSystemId;
	}
	
	public void setSolarSystemId(Integer solarSystemId) {
		this.solarSystemId = solarSystemId;
	}
}
