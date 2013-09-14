package uudi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ReadUUDIHistoryTest {
	public static InputStream UUDI_HISTORY;
	
	public static UUDIResult parsedOrders;
	
	public static DateFormat dateFmt;
	
	public static ObjectMapper mapper;
	
	@BeforeClass
	public static void setUp() throws IOException {
		UUDI_HISTORY = (InputStream)ReadUUDIHistoryTest.class.getResource("history.json").getContent();
		mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true );
		mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true );
		mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true );
		parsedOrders =  mapper.readValue( UUDI_HISTORY, UUDIResult.class );
		dateFmt = DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.LONG, Locale.UK );
	}
	
	@Test
	public void testParseJson() throws JsonParseException, JsonMappingException, 
				UnsupportedEncodingException, IOException {
		assertTrue( parsedOrders != null );
	}

	@Test
	public void testResultValues() throws ParseException  {
		assertEquals( dateFmt.parse("04/09/2013 15:18:12 GMT"), parsedOrders.getCurrentTime() );
	}
	
	@Test
	public void testResultRowsets() throws ParseException  {
		List<UUDIRowSet> rss = parsedOrders.getRowsets();
		UUDIRowSet rs = rss.get( 0 );
		assertEquals( 23069, rs.getTypeId() );
		assertEquals( 10000030, rs.getRegionId() );
		assertEquals( dateFmt.parse("28/08/2013 15:46:14 GMT"), rs.getGeneratedAt() );
	}
	
	@Test
	public void testResultrows() throws ParseException  {
		List<UUDIRowSet> rss = parsedOrders.getRowsets();
		UUDIRowSet rs = rss.get( 0 );
		UUDIHistory history = (UUDIHistory) rs.getRows().get(0);
		assertEquals( dateFmt.parse("01/07/2012 00:00:00 GMT"), history.getDate() );
		assertEquals( 7, history.getOrders() );
		assertEquals( 12, history.getQuantity() );
		assertEquals( new BigDecimal("45000000.0"), history.getLow() );
		assertEquals( new BigDecimal("45799999.9900000021"), history.getHigh() );
		assertEquals( new BigDecimal("45133333.3316000029"), history.getAverage() );
	}
}
