package org.apache.skywalking.oap.server.storage.plugin.cassandra.base;

import com.datastax.oss.driver.api.core.CqlSession;
import org.apache.skywalking.oap.server.core.storage.IHistoryDeleteDAO;
import org.apache.skywalking.oap.server.core.storage.model.Model;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.client.CassandraClient;

import java.io.IOException;

public class HistoryDeleteDAO implements IHistoryDeleteDAO {

    private CassandraClient cassandraClient;

    public HistoryDeleteDAO(CassandraClient cassandraClient) {
        this.cassandraClient = cassandraClient;
    }

    @Override
    public void deleteHistory(Model model, String timeBucketColumnName, int ttl) throws IOException {

    }
}
