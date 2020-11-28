package org.apache.skywalking.oap.server.storage.plugin.cassandra.base;

import com.datastax.oss.driver.api.core.CqlSession;
import org.apache.skywalking.oap.server.core.query.type.LogState;
import org.apache.skywalking.oap.server.core.query.type.Logs;
import org.apache.skywalking.oap.server.core.query.type.Pagination;
import org.apache.skywalking.oap.server.core.storage.query.ILogQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.client.CassandraClient;

import java.io.IOException;

public class LogQueryDAO implements ILogQueryDAO {
    private CassandraClient cassandraClient;

    public LogQueryDAO(CassandraClient cassandraClient) {
        this.cassandraClient = cassandraClient;
    }

    @Override
    public Logs queryLogs(String metricName, int serviceId, int serviceInstanceId, String endpointId, String traceId, LogState state, String stateCode, Pagination paging, int from, int limit, long startTB, long endTB) throws IOException {
        return null;
    }
}
