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

package org.apache.skywalking.oap.server.storage.plugin.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.oap.server.core.CoreModule;
import org.apache.skywalking.oap.server.core.storage.StorageException;
import org.apache.skywalking.oap.server.core.storage.StorageModule;
import org.apache.skywalking.oap.server.core.storage.IBatchDAO;
import org.apache.skywalking.oap.server.core.storage.StorageDAO;
import org.apache.skywalking.oap.server.core.storage.IHistoryDeleteDAO;
import org.apache.skywalking.oap.server.core.storage.cache.INetworkAddressAliasDAO;
import org.apache.skywalking.oap.server.core.storage.management.UITemplateManagementDAO;
import org.apache.skywalking.oap.server.core.storage.model.ModelCreator;
import org.apache.skywalking.oap.server.core.storage.profile.IProfileTaskLogQueryDAO;
import org.apache.skywalking.oap.server.core.storage.profile.IProfileTaskQueryDAO;
import org.apache.skywalking.oap.server.core.storage.profile.IProfileThreadSnapshotQueryDAO;
import org.apache.skywalking.oap.server.core.storage.query.IAggregationQueryDAO;
import org.apache.skywalking.oap.server.core.storage.query.ITopologyQueryDAO;
import org.apache.skywalking.oap.server.core.storage.query.IMetricsQueryDAO;
import org.apache.skywalking.oap.server.core.storage.query.ITraceQueryDAO;
import org.apache.skywalking.oap.server.core.storage.query.IBrowserLogQueryDAO;
import org.apache.skywalking.oap.server.core.storage.query.IMetadataQueryDAO;
import org.apache.skywalking.oap.server.core.storage.query.IAlarmQueryDAO;
import org.apache.skywalking.oap.server.core.storage.query.ITopNRecordsQueryDAO;
import org.apache.skywalking.oap.server.core.storage.query.ILogQueryDAO;
import org.apache.skywalking.oap.server.library.module.ModuleConfig;
import org.apache.skywalking.oap.server.library.module.ModuleProvider;
import org.apache.skywalking.oap.server.library.module.ModuleDefine;
import org.apache.skywalking.oap.server.library.module.ServiceNotProvidedException;
import org.apache.skywalking.oap.server.library.module.ModuleStartException;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.BatchProcessDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.StorageProcessDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.HistoryDeleteDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.NetworkAddressAliasDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.MetricsQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.TopologyQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.TraceQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.BrowserLogQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.MetadataQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.AggregationQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.AlarmQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.TopNRecordsQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.LogQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.ProfileTaskQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.ProfileTaskLogQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.ProfileThreadSnapshotQueryDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.KeyspaceReplicationStrategy;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.UITemplateManagementProcessorDAO;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.base.CassandraTableInstaller;
import org.apache.skywalking.oap.server.storage.plugin.cassandra.client.CassandraClient;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

/**
 * storage Provider For Cassandra
 */
@Slf4j
public class StorageModuleCassandraProvider extends ModuleProvider {

    private StorageModuleCassandraConfig config;
    private CassandraClient cassandraClient;

    @Override
    public String name() {
        return "cassandra";
    }

    @Override
    public Class<? extends ModuleDefine> module() {
        return StorageModule.class;
    }

    @Override
    public ModuleConfig createConfigBeanIfAbsent() {
        return config;
    }

    @Override
    public void prepare() throws ServiceNotProvidedException, ModuleStartException {
        List<InetSocketAddress> contactPoints = config.getClusters().stream().map(data -> {
            String[] hostPort = data.split(":");
            return new InetSocketAddress(hostPort[0], Integer.parseInt(hostPort[1]));
        }).collect(Collectors.toList());

        cassandraClient = new CassandraClient(CqlSession.builder()
                .withKeyspace(config.getKeyspace())
                .addContactPoints(contactPoints)
                .withLocalDatacenter(config.getDataCenter()).build());

        this.registerServiceImplementation(IBatchDAO.class, new BatchProcessDAO(cassandraClient));
        this.registerServiceImplementation(StorageDAO.class, new StorageProcessDAO(cassandraClient));
        this.registerServiceImplementation(IHistoryDeleteDAO.class, new HistoryDeleteDAO(cassandraClient));
        this.registerServiceImplementation(INetworkAddressAliasDAO.class, new NetworkAddressAliasDAO(cassandraClient));
        this.registerServiceImplementation(ITopologyQueryDAO.class, new TopologyQueryDAO(cassandraClient));
        this.registerServiceImplementation(IMetricsQueryDAO.class, new MetricsQueryDAO(cassandraClient));
        this.registerServiceImplementation(ITraceQueryDAO.class, new TraceQueryDAO(cassandraClient));
        this.registerServiceImplementation(IBrowserLogQueryDAO.class, new BrowserLogQueryDAO(cassandraClient));
        this.registerServiceImplementation(IMetadataQueryDAO.class, new MetadataQueryDAO(cassandraClient, 100));
        this.registerServiceImplementation(IAggregationQueryDAO.class, new AggregationQueryDAO(cassandraClient));
        this.registerServiceImplementation(IAlarmQueryDAO.class, new AlarmQueryDAO(cassandraClient));
        this.registerServiceImplementation(ITopNRecordsQueryDAO.class, new TopNRecordsQueryDAO(cassandraClient));
        this.registerServiceImplementation(ILogQueryDAO.class, new LogQueryDAO(cassandraClient));
        this.registerServiceImplementation(IProfileTaskQueryDAO.class, new ProfileTaskQueryDAO(cassandraClient));
        this.registerServiceImplementation(IProfileTaskLogQueryDAO.class, new ProfileTaskLogQueryDAO(cassandraClient));
        this.registerServiceImplementation(IProfileThreadSnapshotQueryDAO.class, new ProfileThreadSnapshotQueryDAO(cassandraClient));
        this.registerServiceImplementation(UITemplateManagementDAO.class, new UITemplateManagementProcessorDAO(cassandraClient));
    }

    @Override
    public void start() throws ServiceNotProvidedException, ModuleStartException {

        try {
            cassandraClient.connect();
            cassandraClient.createKeyspaceIfNecessary(config.getKeyspace(), config.getReplicationFactor(), KeyspaceReplicationStrategy.SIMPLE);
            CassandraTableInstaller installer = new CassandraTableInstaller(cassandraClient, getManager(), config);
            getManager().find(CoreModule.NAME).provider().getService(ModelCreator.class).addModelListener(installer);
        } catch (StorageException e) {
            throw new ModuleStartException(e.getMessage(), e);
        }
    }

    @Override
    public void notifyAfterCompleted() throws ServiceNotProvidedException, ModuleStartException {

    }

    @Override
    public String[] requiredModules() {
        return new String[]{CoreModule.NAME};
    }
}
