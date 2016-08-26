package myapp.transaction;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Timofey Boldyrev on 23.08.2016.
 */
@Service
class TransactionStorageImpl implements TransactionStorage {

    private Map<Long, Transaction> transactions;

    public TransactionStorageImpl() {
        transactions = new ConcurrentHashMap<Long, Transaction>();
    }
    public List<Transaction> getAll() {
        return new ArrayList<Transaction>(transactions.values());
    }

    public Transaction getById(long id) {
        return transactions.get(id);
    }

    public List<Long> getIdListByType(String type) {
        List<Long> outList = new ArrayList<Long>();
        for (Map.Entry<Long, Transaction> transactionEntry : transactions.entrySet()) {
            if (transactionEntry.getValue().getType().equals(type)) {
                outList.add(transactionEntry.getKey());
            }
        }
        return outList;
    }

    public void add(long id, Transaction newTransaction) {
        transactions.put(id, newTransaction);
    }

    public Transaction delete(long id) {
        return transactions.remove(id);
    }

    public void clear() {
        transactions.clear();
    }

}
