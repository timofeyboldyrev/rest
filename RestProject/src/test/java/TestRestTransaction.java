import com.jayway.restassured.http.ContentType;
import myapp.transaction.Transaction;
import org.junit.*;

import java.util.*;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

/**
 * Created by Timofey Boldyrev on 24.08.2016.
 */
public class TestRestTransaction {

    private static List<Transaction> transactionList;

    @BeforeClass
    public static void init() {
        transactionList = new ArrayList<Transaction>();
        transactionList.add(new Transaction(8.2, "car"));
        transactionList.add(new Transaction(15.5, "car"));
        transactionList.add(new Transaction(3.1, "shop"));
    }

    @Before
    public void deleteAllBeforeTest() {
        deleteAll();
    }

    @After
    public void deleteAllAfterTest() {
        deleteAll();
    }

    @Test
    public void testGetById() {
        getExpectingEmpty(0);
        getExpectingEmpty(1);
        getExpectingEmpty(2);
    }

    @Test
    public void testPut() {
        put(0);
        put(2);
        getExpectingObject(0);
        getExpectingEmpty(1);
        getExpectingObject(2);
    }

    @Test
    public void testDelete() {
        put(0);
        getExpectingObject(0);
        delete(0);
        getExpectingEmpty(0);
    }

    @Test
    public void testGetByType() {
        put(0);
        put(1);
        put(2);
        getByTypeExpectingObjects("car", 0, 1);
        getByTypeExpectingObjects("shop", 2);
        delete(0);
        delete(2);
        getByTypeExpectingObjects("car", 1);
        getByTypeExpectingEmpty("shop");
    }

    @Test
    public void testGetAll() {
        put(0);
        put(1);
        put(2);
        getAllExpectingObjects(transactionList.get(0), transactionList.get(1), transactionList.get(2));
        delete(0);
        getAllExpectingObjects(transactionList.get(1), transactionList.get(2));
        deleteAll();
        getAllExpectingEmpty();
    }

    private static void deleteAll() {
        when().
                delete("/transactionservice/transaction/").
        then().
                statusCode(200);
    }

    private void delete(int id) {
        when().
                delete("/transactionservice/transaction/" + id).
        then().
                statusCode(200);
    }

    private void put(int id) {
        Transaction transaction = transactionList.get(id);
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("amount", transaction.getAmount());
        json.put("type", transaction.getType());
        given().
                contentType(ContentType.JSON).
                body(json).
        when().
                put("/transactionservice/transaction/" + id).
        then().
                statusCode(200);
    }

    private void getExpectingObject(int id) {
        Transaction transaction = transactionList.get(id);
        when().
                get("/transactionservice/transaction/" + id).
        then().
                body("amount", is((float)transaction.getAmount())).
                body("type", equalTo(transaction.getType())).
                statusCode(200);
    }

    private void getExpectingEmpty(long id) {
        when().
                get("/transactionservice/transaction/" + id).
        then().
                body(isEmptyOrNullString()).
                statusCode(200);
    }

    private void getByTypeExpectingObjects(String type, int... expectingIdArray) {
        for (int id : expectingIdArray) {
            when().
                    get("/transactionservice/types/" + type).
            then().
                    body("", hasItem(id)).
                    statusCode(200);
        }
    }

    private void getByTypeExpectingEmpty(String type) {
        when().
                get("/transactionservice/types/" + type).
        then().
                body("", hasSize(0)).
                statusCode(200);
    }

    private void getAllExpectingObjects(Transaction... expectingTransactions) {
        for (Transaction transaction : expectingTransactions) {
            when().
                    get("/transactionservice/transaction/").
            then().
                    body("amount", hasItem((float)transaction.getAmount())).
                    body("type", hasItem(equalTo(transaction.getType()))).
                    statusCode(200);
        }
    }

    private void getAllExpectingEmpty() {
        when().
                get("/transactionservice/transaction/").
        then().
                body("", hasSize(0)).
                statusCode(200);
    }

}
