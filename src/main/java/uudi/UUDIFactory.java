package uudi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Factory class for producing or consuming UDDI messages in
 * various ways.
 * @author msharman
 */
public class UUDIFactory {
	
	/**
	 * In order to decouple the ObjectMapper based constructor from the 
	 * domain object, we extend the UUDIResult class to add a
	 * special @JsonCreator method. 
	 * 
	 * We then construct this class and upconvert back to the original
	 * UUDIResult. This saves from having to implement a custom
	 * Deserializer.
	 *
	 */
	public static class UUDIResultConstructor extends UUDIResult {

		/**
		 * Since the standard polymorphism mechanism can't handle the columns and
		 * resultType structure, we need to collect the information needed to
		 * delegate to the correct types here.
		 * 
		 * @param resultType
		 * @param version
		 * @param columns
		 * @param rowsets
		 * @throws JsonMappingException 
		 */
		@JsonCreator
		public UUDIResultConstructor(
				@JsonProperty("resultType") String resultType, 
				@JsonProperty("version") String version, 
				@JsonProperty("columns") List<String> columns,
				@JsonProperty("rowsets") List<UUDIRowSet> rowsets ) throws JsonMappingException {
			
			/* Firstly check the version and fail if we don't implement it.
			 * FIXME: Can we be more forgiving here ?
			 */
			if ( ! version.equals("0.1") ) {
				throw new JsonMappingException("Incompatible version " + version );
			}
		
			Class<?> cls = null;
			if ( resultType.equals("orders") ) {
				cls = UUDIOrder.class;
			} else if ( resultType.equals("history") ) {
				cls = UUDIHistory.class;
			} else {
				throw new JsonMappingException("Unknown resultType: " + resultType );
			}
			
			/* Loop through each result mapping to the correct result type */
			ObjectMapper mapper = new ObjectMapper(); // FIXME: should cache ??
			for( UUDIRowSet rs : rowsets ) {
				List<Object> typedRows = new ArrayList<Object>();
				for(Object obj : rs.getRows() ) {
					@SuppressWarnings("unchecked")
					List<Object> untypedRow = (List<Object>) obj;
					
					/* Create a map of keys to values */
					Map<String,Object> row = new HashMap<String,Object>();
					Iterator<String> col = columns.iterator();
					Iterator<Object> val = untypedRow.iterator();
					while( col.hasNext() ) {
						row.put( col.next(), val.next() );
					}
					
					/* Use the Jackson ObjectMapper to deserialise for us */
					typedRows.add(  mapper.convertValue( row, cls ) );
				}
				rs.setRows(  typedRows );
			}
			
			/* Store resulting typed version of RowSet list */
			this.setRowsets(rowsets);
		}

	}
	
	/**
	 * Parses the input stream as a JSON UDDI message.
	 * For the specifications of the format see here 
	 * {@link http://dev.eve-central.com/unifieduploader/start}.
	 * @param is The InputStream to consume.
	 * @return A parsed UUDIResult with correct types.
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static UUDIResult parseUUDIResultFromJson( InputStream is, ObjectMapper mapper )
			throws JsonParseException, JsonMappingException, IOException {
		mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true );
		mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true );
		UUDIResult res = mapper.readValue( is, UUDIFactory.UUDIResultConstructor.class );
		return res;
	}
	
	public static UUDIResult parseUUDIResultFromJson( byte[] bytes, ObjectMapper mapper )
			throws JsonParseException, JsonMappingException, IOException {
		mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true );
		mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true );
		UUDIResult res = mapper.readValue( bytes, UUDIFactory.UUDIResultConstructor.class );
		return res;
	}

}
