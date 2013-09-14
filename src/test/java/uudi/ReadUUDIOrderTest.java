package uudi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

import uudi.UUDIOrder;
import uudi.UUDIResult;
import uudi.UUDIRowSet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ReadUUDIOrderTest {
	public static InputStream UUDI_ORDERS;

	public static String[] UUDI_ORDERS_EXPECTED_COLUMNS 
		= { "price","volRemaining","range","orderID","volEntered","minVolume",
        	"bid","issueDate","duration","stationID","solarSystemID" };
	
	public static UUDIResult parsedOrders;
	
	public static DateFormat dateFmt;
	
	@BeforeClass
	public static void setUp() throws IOException {
		UUDI_ORDERS = (InputStream)ReadUUDIOrderTest.class.getResource("orders.json").getContent();
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true );
		mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true );
		mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true );
		parsedOrders =  mapper.readValue( UUDI_ORDERS, UUDIResult.class );
		dateFmt = DateFormat.getDateTimeInstance( DateFormat.SHORT, DateFormat.LONG, Locale.UK );
	}
	
	@Test
	public void testParseJson() throws JsonParseException, JsonMappingException, 
				UnsupportedEncodingException, IOException {
		assertTrue( parsedOrders != null );
	}

	@Test
	public void testResultValues() throws ParseException  {
		assertEquals( dateFmt.parse("03/09/2013 13:58:52 GMT"), parsedOrders.getCurrentTime() );
	}
	
	@Test
	public void testResultRowsets() throws ParseException  {
		List<UUDIRowSet> rss = parsedOrders.getRowsets();
		UUDIRowSet rs = rss.get( 0 );
		assertEquals( 25605, rs.getTypeId() );
		assertEquals( 10000002, rs.getRegionId() );
		assertEquals( dateFmt.parse("03/09/2013 03:40:28 GMT"), rs.getGeneratedAt() );
	}
	
	@Test
	public void testResultrows() throws ParseException  {
		List<UUDIRowSet> rss = parsedOrders.getRowsets();
		UUDIRowSet rs = rss.get( 0 );
		UUDIOrder order = (UUDIOrder) rs.getRows().get(0);
		assertEquals( new BigDecimal("180000.0"), order.getPrice() );
		assertEquals( 201, order.getVolRemaining() );
		assertEquals( 32767, order.getRange() );
		assertEquals( new BigInteger("3154707984"), order.getOrderId() );
		assertEquals( 201, order.getVolEntered() );
		assertEquals( 1, order.getMinVolume() );
		assertEquals( false, order.isBid() );
		assertEquals( dateFmt.parse("31/08/2013 16:30:57 GMT"), order.getIssueDate() );
		assertEquals( 90, order.getDuration() );
		assertEquals( 60004228, order.getStationId() );
		assertEquals( new Integer(30000129), order.getSolarSystemId() );
	}
}
