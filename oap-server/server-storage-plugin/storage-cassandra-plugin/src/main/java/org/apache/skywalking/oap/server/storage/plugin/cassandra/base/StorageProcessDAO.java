package org.apache.skywalking.oap.server.storage.plugin.cassandra.base;

import com.datastax.oss.driver.api.core.CqlSession;
import org.apache.skywalking.oap.server.core.analysis.config.NoneStream;
import org.apache.skywalking.oap.server.core.analysis.management.ManagementData;
import org.apache.skywalking.oap.server.core.analysis.metrics.Metrics;
import org.apache.skywalking.oap.server.core.analysis.record.Record;
import org.apache.skywalking.oap.server.core.storage.*;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.client.CassandraClient;

/**
 * @author caoyixiong
 * @Date: 2020/11/28
 * @Copyright (c) 2015, lianjia.com All Rights Reserved
 */
public class StorageProcessDAO implements StorageDAO {

    private CassandraClient cassandraClient;

    public StorageProcessDAO(CassandraClient cassandraClient) {
        this.cassandraClient = cassandraClient;
    }

    @Override
    public IMetricsDAO newMetricsDao(StorageBuilder<Metrics> storageBuilder) {
        return null;
    }

    @Override
    public IRecordDAO newRecordDao(StorageBuilder<Record> storageBuilder) {
        return null;
    }

    @Override
    public INoneStreamDAO newNoneStreamDao(StorageBuilder<NoneStream> storageBuilder) {
        return null;
    }

    @Override
    public IManagementDAO newManagementDao(StorageBuilder<ManagementData> storageBuilder) {
        return null;
    }
}
