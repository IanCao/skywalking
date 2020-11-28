package org.apache.skywalking.oap.server.storage.plugin.cassandra.base;

import com.datastax.oss.driver.api.core.CqlSession;
import org.apache.skywalking.oap.server.core.analysis.manual.segment.SegmentRecord;
import org.apache.skywalking.oap.server.core.analysis.manual.segment.SpanTag;
import org.apache.skywalking.oap.server.core.query.type.QueryOrder;
import org.apache.skywalking.oap.server.core.query.type.Span;
import org.apache.skywalking.oap.server.core.query.type.TraceBrief;
import org.apache.skywalking.oap.server.core.query.type.TraceState;
import org.apache.skywalking.oap.server.core.storage.query.ITraceQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.client.CassandraClient;

import java.io.IOException;
import java.util.List;

/**
 * @author caoyixiong
 * @Date: 2020/11/28
 * @Copyright (c) 2015, lianjia.com All Rights Reserved
 */
public class TraceQueryDAO implements ITraceQueryDAO {
    private CassandraClient cassandraClient;

    public TraceQueryDAO(CassandraClient cassandraClient) {
        this.cassandraClient = cassandraClient;
    }

    @Override
    public TraceBrief queryBasicTraces(long startSecondTB, long endSecondTB, long minDuration, long maxDuration, String endpointName, String serviceId, String serviceInstanceId, String endpointId, String traceId, int limit, int from, TraceState traceState, QueryOrder queryOrder, List<SpanTag> tags) throws IOException {
        return null;
    }

    @Override
    public List<SegmentRecord> queryByTraceId(String traceId) throws IOException {
        return null;
    }

    @Override
    public List<Span> doFlexibleTraceQuery(String traceId) throws IOException {
        return null;
    }
}
