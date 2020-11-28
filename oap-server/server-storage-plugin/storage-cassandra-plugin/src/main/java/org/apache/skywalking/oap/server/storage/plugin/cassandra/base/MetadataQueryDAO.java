package org.apache.skywalking.oap.server.storage.plugin.cassandra.base;

import com.datastax.oss.driver.api.core.CqlSession;
import org.apache.skywalking.oap.server.core.query.type.Database;
import org.apache.skywalking.oap.server.core.query.type.Endpoint;
import org.apache.skywalking.oap.server.core.query.type.Service;
import org.apache.skywalking.oap.server.core.query.type.ServiceInstance;
import org.apache.skywalking.oap.server.core.storage.query.IMetadataQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.client.CassandraClient;

import java.io.IOException;
import java.util.List;


public class MetadataQueryDAO implements IMetadataQueryDAO {
    private CassandraClient cassandraClient;

    public MetadataQueryDAO(CassandraClient cassandraClient) {
        this.cassandraClient = cassandraClient;
    }

    @Override
    public List<Service> getAllServices(String group) throws IOException {
        return null;
    }

    @Override
    public List<Service> getAllBrowserServices() throws IOException {
        return null;
    }

    @Override
    public List<Database> getAllDatabases() throws IOException {
        return null;
    }

    @Override
    public List<Service> searchServices(String keyword) throws IOException {
        return null;
    }

    @Override
    public Service searchService(String serviceCode) throws IOException {
        return null;
    }

    @Override
    public List<Endpoint> searchEndpoint(String keyword, String serviceId, int limit) throws IOException {
        return null;
    }

    @Override
    public List<ServiceInstance> getServiceInstances(long startTimestamp, long endTimestamp, String serviceId) throws IOException {
        return null;
    }
}
