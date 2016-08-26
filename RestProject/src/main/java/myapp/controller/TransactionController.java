package myapp.controller;

import myapp.transaction.Transaction;
import myapp.transaction.TransactionStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Timofey Boldyrev on 24.08.2016.
 */
@RestController
@RequestMapping(value = "/transactionservice")
@ComponentScan(basePackages = "myapp.transaction")
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    TransactionStorage transactionStorage;

    @RequestMapping(value = "/transaction/", method = RequestMethod.GET)
    public List<Transaction> getAllTransactions() {
        log.debug("GET request. Get all transactions. ");
        List<Transaction> listToOut = transactionStorage.getAll();
        log.debug("OutputList: " + listToOut);
        return listToOut;
    }

    @RequestMapping(value = "/transaction/{id}", method = RequestMethod.GET)
    public Transaction getTransaction(@PathVariable("id") long id) {
        log.debug("GET request. Get transactions by id=" + id);
        Transaction transactionToOut = transactionStorage.getById(id);
        log.debug("Output transaction: " + transactionToOut);
        return transactionToOut;
    }

    @RequestMapping(value = "/types/{type}", method = RequestMethod.GET)
    public List<Long> getTransactionsByType(@PathVariable("type") String type) {
        log.debug("GET request. Get transactions id by type=" + type);
        List<Long> listToOut = transactionStorage.getIdListByType(type);
        log.debug("OutputList: " + listToOut);
        return listToOut;
    }

    @RequestMapping(value = "/transaction/{id}", method = RequestMethod.PUT)
    public void putTransaction(@PathVariable("id") long id, @RequestBody Transaction addingTransaction) {
        log.debug("PUT request. Put transaction id=" + id + ", transaction=" + addingTransaction);
        transactionStorage.add(id, addingTransaction);
    }

    @RequestMapping(value = "/transaction/{id}", method = RequestMethod.DELETE)
    public void deleteTransaction(@PathVariable("id") long id) {
        log.debug("DELETE request. Delete transaction by id=" + id);
        log.debug("Deleted transaction: " + transactionStorage.delete(id));
    }

    @RequestMapping(value = "/transaction/", method = RequestMethod.DELETE)
    public void deleteAllTransaction() {
        log.debug("DELETE request. Delete all transactions");
        log.debug("Deleted transactions: " + transactionStorage.getAll());
        transactionStorage.clear();
    }



}
