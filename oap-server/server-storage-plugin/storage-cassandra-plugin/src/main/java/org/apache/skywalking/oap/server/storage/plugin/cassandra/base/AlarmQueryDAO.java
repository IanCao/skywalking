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

package org.apache.skywalking.oap.server.storage.plugin.cassandra.base;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.google.common.base.Strings;
import org.apache.skywalking.oap.server.core.alarm.AlarmRecord;
import org.apache.skywalking.oap.server.core.query.enumeration.Scope;
import org.apache.skywalking.oap.server.core.query.type.AlarmMessage;
import org.apache.skywalking.oap.server.core.query.type.Alarms;
import org.apache.skywalking.oap.server.core.storage.query.IAlarmQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.client.CassandraClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlarmQueryDAO implements IAlarmQueryDAO {
    private CassandraClient cassandraClient;

    public AlarmQueryDAO(CassandraClient cassandraClient) {
        this.cassandraClient = cassandraClient;
    }

    @Override
    public Alarms getAlarm(Integer scopeId, String keyword, int limit, int from, long startTB, long endTB) throws IOException {
        StringBuilder sql = new StringBuilder();
        List<Object> parameters = new ArrayList<>(10);
        sql.append("from ").append(AlarmRecord.INDEX_NAME).append(" where ");
        sql.append(" 1=1 ");
        if (Objects.nonNull(scopeId)) {
            sql.append(" and ").append(AlarmRecord.SCOPE).append(" = ?");
            parameters.add(scopeId.intValue());
        }
        if (startTB != 0 && endTB != 0) {
            sql.append(" and ").append(AlarmRecord.TIME_BUCKET).append(" >= ?");
            parameters.add(startTB);
            sql.append(" and ").append(AlarmRecord.TIME_BUCKET).append(" <= ?");
            parameters.add(endTB);
        }

        if (!Strings.isNullOrEmpty(keyword)) {
            sql.append(" and ").append(AlarmRecord.ALARM_MESSAGE).append(" like concat('%',?,'%') ");
            parameters.add(keyword);
        }
        sql.append(" order by ").append(AlarmRecord.START_TIME).append(" desc ");

        Alarms alarms = new Alarms();
        ResultSet resultSet = cassandraClient.getCqlSession().execute("select count(1) total from (select 1 " + sql.toString() + " )", parameters.toArray(new Object[0]));
        for (Row row : resultSet) {
            alarms.setTotal(row.getInt("total"));
        }

        this.buildLimit(sql, from, limit);

        ResultSet rows = cassandraClient.getCqlSession().execute("select * " + sql.toString(), parameters.toArray(new Object[0]));
        for (Row row : rows) {
            AlarmMessage message = new AlarmMessage();
            message.setId(row.getString(AlarmRecord.ID0));
            message.setMessage(row.getString(AlarmRecord.ALARM_MESSAGE));
            message.setStartTime(row.getLong(AlarmRecord.START_TIME));
            message.setScope(Scope.Finder.valueOf(row.getInt(AlarmRecord.SCOPE)));
            message.setScopeId(row.getInt(AlarmRecord.SCOPE));

            alarms.getMsgs().add(message);
        }
        return alarms;
    }

    protected void buildLimit(StringBuilder sql, int from, int limit) {
        sql.append(" LIMIT ").append(limit);
        sql.append(" OFFSET ").append(from);
    }
}
