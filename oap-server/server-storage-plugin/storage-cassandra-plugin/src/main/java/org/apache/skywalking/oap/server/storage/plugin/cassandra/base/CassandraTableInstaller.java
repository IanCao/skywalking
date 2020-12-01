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

import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.google.gson.JsonObject;
import org.apache.skywalking.oap.server.core.analysis.NodeType;
import org.apache.skywalking.oap.server.core.storage.StorageException;
import org.apache.skywalking.oap.server.core.storage.model.Model;
import org.apache.skywalking.oap.server.core.storage.model.ModelColumn;
import org.apache.skywalking.oap.server.core.storage.model.ModelInstaller;
import org.apache.skywalking.oap.server.core.storage.type.StorageDataComplexObject;
import org.apache.skywalking.oap.server.library.client.Client;
import org.apache.skywalking.oap.server.library.module.ModuleManager;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.StorageModuleCassandraConfig;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.client.CassandraClient;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class CassandraTableInstaller extends ModelInstaller {
    private StorageModuleCassandraConfig config;

    public CassandraTableInstaller(Client client, ModuleManager moduleManager, StorageModuleCassandraConfig config) {
        super(client, moduleManager);
        this.config = config;
    }

    @Override
    protected boolean isExists(Model model) throws StorageException {
        TableMetaInfo.addModel(model);
        CassandraClient cassandraClient = (CassandraClient) client;
        return cassandraClient.exist(config.getKeyspace(), model.getName());
    }

    @Override
    protected void createTable(Model model) throws StorageException {
        CassandraClient cassandraClient = (CassandraClient) client;
        CreateTable createTable = SchemaBuilder.createTable(model.getName())
                .ifNotExists()
                .withPartitionKey("id", DataTypes.TEXT);
        for (ModelColumn column : model.getColumns()) {
            createTable.withColumn(column.getColumnName().getStorageName(), transform(column.getType(), column.getGenericType()));
        }
        cassandraClient.getCqlSession().execute(createTable.build());
        createIndex(model);
    }

    private void createIndex(Model model) {
    }

    private DataType transform(Class<?> type, Type genericType) {
        if (Integer.class.equals(type) || int.class.equals(type) || NodeType.class.equals(type)) {
            return DataTypes.INT;
        } else if (Long.class.equals(type) || long.class.equals(type)) {
            return DataTypes.BIGINT;
        } else if (Double.class.equals(type) || double.class.equals(type)) {
            return DataTypes.DOUBLE;
        } else if (String.class.equals(type)) {
            return DataTypes.TEXT;
        } else if (StorageDataComplexObject.class.isAssignableFrom(type)) {
            return DataTypes.TEXT;
        } else if (byte[].class.equals(type)) {
            return DataTypes.BLOB;
        } else if (JsonObject.class.equals(type)) {
            return DataTypes.BLOB;
        } else if (List.class.isAssignableFrom(type)) {
            final Type elementType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            return transform((Class<?>) elementType, elementType);
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + type.getName());
        }
    }
}
