package org.apache.skywalking.oap.server.storage.plugin.cassandra.base;

import com.datastax.oss.driver.api.core.CqlSession;
import org.apache.skywalking.oap.server.core.storage.IBatchDAO;
import org.apache.skywalking.oap.server.library.client.request.InsertRequest;
import org.apache.skywalking.oap.server.library.client.request.PrepareRequest;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.client.CassandraClient;

import java.util.List;

public class BatchProcessDAO implements IBatchDAO {
    private CassandraClient cassandraClient;

    public BatchProcessDAO(CassandraClient cassandraClient) {
        this.cassandraClient = cassandraClient;
    }

    @Override
    public void asynchronous(InsertRequest insertRequest) {

    }

    @Override
    public void synchronous(List<PrepareRequest> prepareRequests) {

    }
}
