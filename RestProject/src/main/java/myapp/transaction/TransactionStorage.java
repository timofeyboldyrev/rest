package myapp.transaction;

import java.util.List;

/**
 * Created by Timofey Boldyrev on 24.08.2016.
 */
public interface TransactionStorage {

    List<Transaction> getAll();
    Transaction getById(long id);
    List<Long> getIdListByType(String type);
    void add(long id, Transaction newTransaction);
    Transaction delete(long id);
    void clear();

}
