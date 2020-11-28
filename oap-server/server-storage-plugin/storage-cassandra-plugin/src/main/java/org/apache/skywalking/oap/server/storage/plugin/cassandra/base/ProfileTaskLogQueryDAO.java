package org.apache.skywalking.oap.server.storage.plugin.cassandra.base;

import com.datastax.oss.driver.api.core.CqlSession;
import org.apache.skywalking.oap.server.core.query.type.ProfileTaskLog;
import org.apache.skywalking.oap.server.core.storage.profile.IProfileTaskLogQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.client.CassandraClient;

import java.io.IOException;
import java.util.List;

/**
 * @author caoyixiong
 * @Date: 2020/11/28
 * @Copyright (c) 2015, lianjia.com All Rights Reserved
 */
public class ProfileTaskLogQueryDAO implements IProfileTaskLogQueryDAO {
    private CassandraClient cassandraClient;

    public ProfileTaskLogQueryDAO(CassandraClient cassandraClient) {
        this.cassandraClient = cassandraClient;
    }

    @Override
    public List<ProfileTaskLog> getTaskLogList() throws IOException {
        return null;
    }
}
