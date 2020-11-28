package org.apache.skywalking.oap.server.storage.plugin.cassandra.base;

import com.datastax.oss.driver.api.core.CqlSession;
import org.apache.skywalking.oap.server.core.analysis.manual.segment.SegmentRecord;
import org.apache.skywalking.oap.server.core.profile.ProfileThreadSnapshotRecord;
import org.apache.skywalking.oap.server.core.query.type.BasicTrace;
import org.apache.skywalking.oap.server.core.storage.profile.IProfileThreadSnapshotQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.client.CassandraClient;

import java.io.IOException;
import java.util.List;

/**
 * @author caoyixiong
 * @Date: 2020/11/28
 * @Copyright (c) 2015, lianjia.com All Rights Reserved
 */
public class ProfileThreadSnapshotQueryDAO implements IProfileThreadSnapshotQueryDAO {
    private CassandraClient cassandraClient;

    public ProfileThreadSnapshotQueryDAO(CassandraClient cassandraClient) {
        this.cassandraClient = cassandraClient;
    }

    @Override
    public List<BasicTrace> queryProfiledSegments(String taskId) throws IOException {
        return null;
    }

    @Override
    public int queryMinSequence(String segmentId, long start, long end) throws IOException {
        return 0;
    }

    @Override
    public int queryMaxSequence(String segmentId, long start, long end) throws IOException {
        return 0;
    }

    @Override
    public List<ProfileThreadSnapshotRecord> queryRecords(String segmentId, int minSequence, int maxSequence) throws IOException {
        return null;
    }

    @Override
    public SegmentRecord getProfiledSegment(String segmentId) throws IOException {
        return null;
    }
}
