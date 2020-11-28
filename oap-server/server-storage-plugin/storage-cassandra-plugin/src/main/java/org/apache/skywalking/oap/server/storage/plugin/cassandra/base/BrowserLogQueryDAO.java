package org.apache.skywalking.oap.server.storage.plugin.cassandra.base;

import com.datastax.oss.driver.api.core.CqlSession;
import org.apache.skywalking.oap.server.core.browser.source.BrowserErrorCategory;
import org.apache.skywalking.oap.server.core.query.type.BrowserErrorLogs;
import org.apache.skywalking.oap.server.core.storage.query.IBrowserLogQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.client.CassandraClient;

import java.io.IOException;

public class BrowserLogQueryDAO implements IBrowserLogQueryDAO {
    private CassandraClient cassandraClient;

    public BrowserLogQueryDAO(CassandraClient cassandraClient) {
        this.cassandraClient = cassandraClient;
    }

    @Override
    public BrowserErrorLogs queryBrowserErrorLogs(String serviceId, String serviceVersionId, String pagePathId, String pagePath, BrowserErrorCategory category, long startSecondTB, long endSecondTB, int limit, int from) throws IOException {
        return null;
    }
}
