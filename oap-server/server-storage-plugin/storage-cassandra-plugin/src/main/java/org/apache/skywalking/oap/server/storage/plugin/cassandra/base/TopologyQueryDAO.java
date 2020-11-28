package org.apache.skywalking.oap.server.storage.plugin.cassandra.base;

import com.datastax.oss.driver.api.core.CqlSession;
import org.apache.skywalking.oap.server.core.query.type.Call;
import org.apache.skywalking.oap.server.core.storage.query.ITopologyQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.client.CassandraClient;

import java.io.IOException;
import java.util.List;

/**
 * @author caoyixiong
 * @Date: 2020/11/28
 * @Copyright (c) 2015, lianjia.com All Rights Reserved
 */
public class TopologyQueryDAO implements ITopologyQueryDAO {
    private CassandraClient cassandraClient;

    public TopologyQueryDAO(CassandraClient cassandraClient) {
        this.cassandraClient = cassandraClient;
    }

    @Override
    public List<Call.CallDetail> loadServiceRelationsDetectedAtServerSide(long startTB, long endTB, List<String> serviceIds) throws IOException {
        return null;
    }

    @Override
    public List<Call.CallDetail> loadServiceRelationDetectedAtClientSide(long startTB, long endTB, List<String> serviceIds) throws IOException {
        return null;
    }

    @Override
    public List<Call.CallDetail> loadServiceRelationsDetectedAtServerSide(long startTB, long endTB) throws IOException {
        return null;
    }

    @Override
    public List<Call.CallDetail> loadServiceRelationDetectedAtClientSide(long startTB, long endTB) throws IOException {
        return null;
    }

    @Override
    public List<Call.CallDetail> loadInstanceRelationDetectedAtServerSide(String clientServiceId, String serverServiceId, long startTB, long endTB) throws IOException {
        return null;
    }

    @Override
    public List<Call.CallDetail> loadInstanceRelationDetectedAtClientSide(String clientServiceId, String serverServiceId, long startTB, long endTB) throws IOException {
        return null;
    }

    @Override
    public List<Call.CallDetail> loadEndpointRelation(long startTB, long endTB, String destEndpointId) throws IOException {
        return null;
    }
}
