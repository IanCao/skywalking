/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.oap.server.storage.plugin.cassandra.client;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatementBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import org.apache.skywalking.oap.server.library.client.Client;
import org.apache.skywalking.oap.server.library.util.CollectionUtils;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.KeyspaceReplicationStrategy;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;

public class CassandraClient implements Client {

    private CqlSession cqlSession;

    public CqlSession getCqlSession() {
        return cqlSession;
    }

    public CassandraClient(CqlSession cqlSession) {
        this.cqlSession = cqlSession;
    }

    @Override
    public void connect() {
    }

    public void createKeyspaceIfNecessary(String keyspace, int replicationFactor, KeyspaceReplicationStrategy strategy) {
        CreateKeyspace createKeyspace = null;
        if (strategy == KeyspaceReplicationStrategy.NETWORK_TOPOLOGGY) {
            createKeyspace = createKeyspace(keyspace).ifNotExists().withNetworkTopologyStrategy(null);
        } else {
            createKeyspace = createKeyspace(keyspace).ifNotExists().withSimpleStrategy(replicationFactor);
        }
        cqlSession.execute(createKeyspace.build());
    }

    @Override
    public void shutdown() throws IOException {
        cqlSession.close();
    }

    public List<String> getAllTables(String keyspace) {
        SimpleStatementBuilder builder = new SimpleStatementBuilder(String.format(" SELECT table_name FROM system_schema.tables WHERE keyspace_name ='%s'", keyspace));
        ResultSet resultSet = cqlSession.execute(builder.build());
        List<Row> rows = resultSet.all();
        if (CollectionUtils.isEmpty(rows)) {
            return Collections.EMPTY_LIST;
        }
        return rows.stream().map(row -> row.getString("table_name")).collect(Collectors.toList());
    }

    public boolean exist(String keyspace, String tableName) {
        SimpleStatementBuilder builder = new SimpleStatementBuilder(String.format(" SELECT count(1) as result FROM system_schema.tables WHERE keyspace_name ='%s' and table_name = '%s'", keyspace, tableName.toLowerCase()));
        ResultSet resultSet = cqlSession.execute(builder.build());
        List<Row> rows = resultSet.all();
        if (CollectionUtils.isEmpty(rows)) {
            return false;
        }
        return rows.get(1).getLong("result") > 0;
    }
}
