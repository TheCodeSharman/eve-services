package uudi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Deserializes a UUDIResult by reparsing the the rows from the Map<String,Object> into specific
 * polymorphic types based on the resultType field in the order specified by the columns field.
 */
public class UUDIResultDeserializer extends StdDeserializer<UUDIResult> {


	protected UUDIResultDeserializer() {
		super(UUDIResult.class);
	}

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public UUDIResult deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		String version = "";
		String resultType = "";
		List<String> columns = null;
		UUDIResult result = new UUDIResult();
		
		// Use the object mapper to read in the UUDIResult json
		//
		// It's tempting to just call it from the root but this leads to infinite recursion.
		//
		// Instead we read each top level field manual but switch to ObjectMapper for
		// convenience to use the data binder as much as possible.
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		while (jp.nextToken() != JsonToken.END_OBJECT) {
			String fieldName = jp.getCurrentName();
			jp.nextToken();
			if ( fieldName.equals("version") ) {
				version = jp.getText();
			} else if (fieldName.equals("resultType") ) {
				resultType = jp.getText();
			} else if (fieldName.equals("columns")) {
				columns = mapper.readValue(jp, new TypeReference<List<String>>() {} );
			} else if (fieldName.equals("generator")) {
				result.setGenerator( mapper.readValue( jp, UUDIGenerator.class ) );
			} else if (fieldName.equals("uploadKeys")) {
				result.setUploadKeys( (List<UUDIUploadKey>)mapper.readValue( jp,  new TypeReference<List<UUDIUploadKey>>() {} ) );
			} else if (fieldName.equals("currentTime")) {
				result.setCurrentTime( mapper.readValue( jp, Date.class ) );
			} else if (fieldName.equals("rowsets")) {
				result.setRowsets((List<UUDIRowSet>) mapper.readValue( jp,  new TypeReference<List<UUDIRowSet>>() {} ) );
			} else { 
				throw new JsonMappingException("Unrecognized field '"+fieldName+"'");
			}
		}
		jp.close();
	
	
		// Check the version attribute
		if ( ! version.equals("0.1") ) {
			throw new JsonMappingException("Incompatible version " + version);
		}
		
		// Determine the content class of this message
		Class<?> cls = null;

		if ( resultType.equals("orders") ) {
			cls = UUDIOrder.class;
		} else if ( resultType.equals("history") ) {
			cls = UUDIHistory.class;
		} else {
			throw new JsonMappingException("Unknown resultType: " + resultType );
		}
		
		// Loop through each result mapping to the correct result type.
		for( UUDIRowSet rs : result.getRowsets() ) {
			List<Object> typedRows = new ArrayList<Object>();
			for(Object obj : rs.getRows() ) {
				List<Object> untypedRow = (List<Object>) obj;
				
				// Create a map of keys to values 
				Map<String,Object> row = new HashMap<String,Object>();
				Iterator<String> col = columns.iterator();
				Iterator<Object> val = untypedRow.iterator();
				while( col.hasNext() ) {
					row.put( col.next(), val.next() );
				}
				
				// Use the Jackson ObjectMapper to deserialise for us 
				typedRows.add( mapper.convertValue( row, cls ) );
			}
			rs.setRows( typedRows );
		}

		return result;
	}

}