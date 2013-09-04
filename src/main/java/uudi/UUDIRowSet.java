package uudi;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class UUDIRowSet {
	
	private Date generatedAt;
	
	@JsonProperty( "regionID" )
	private int regionId;
	
	@JsonProperty( "typeID" )
	private int typeId;

	private List<?> rows;

	/**
	 * The data/time in ISO 8601 format (example 2011-10-22T15:46:00+00:00) when the rowset was
	 * created. This allows tracking the 'freshness' of the contained data which is important 
	 * since data from multiple sources can be generated and received at different times.
	 * @return
	 */
	public Date getGeneratedAt() {
		return generatedAt;
	}

	public void setGeneratedAt(Date generatedAt) {
		this.generatedAt = generatedAt;
	}

	/**
	 * Id of the region. Can be null if client lacks data.
	 * @return
	 */
	public int getRegionId() {
		return regionId;
	}

	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	/**
	 * The typeID of the upload, for history and orders.
	 * @return
	 */
	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * Contains the actual data rows. See Order Rows {@link http://dev.eve-central.com/unifieduploader/start#order_rows}
	 * and History Rows {@link http://dev.eve-central.com/unifieduploader/start#history_rows}  descriptions below.
	 * @return
	 */
	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
}
