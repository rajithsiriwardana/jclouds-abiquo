/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jclouds.abiquo.config;

import java.util.Map;

import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.features.AdminAsyncClient;
import org.jclouds.abiquo.features.AdminClient;
import org.jclouds.abiquo.features.CloudAsyncClient;
import org.jclouds.abiquo.features.CloudClient;
import org.jclouds.abiquo.features.ConfigAsyncClient;
import org.jclouds.abiquo.features.ConfigClient;
import org.jclouds.abiquo.features.EnterpriseAsyncClient;
import org.jclouds.abiquo.features.EnterpriseClient;
import org.jclouds.abiquo.features.InfrastructureAsyncClient;
import org.jclouds.abiquo.features.InfrastructureClient;
import org.jclouds.abiquo.features.TaskAsyncClient;
import org.jclouds.abiquo.features.TaskClient;
import org.jclouds.abiquo.features.VirtualMachineTemplateAsyncClient;
import org.jclouds.abiquo.features.VirtualMachineTemplateClient;
import org.jclouds.abiquo.handlers.AbiquoErrorHandler;
import org.jclouds.abiquo.internal.AbiquoContextImpl;
import org.jclouds.abiquo.rest.internal.AbiquoHttpAsyncClient;
import org.jclouds.abiquo.rest.internal.AbiquoHttpClient;
import org.jclouds.abiquo.rest.internal.ExtendedUtils;
import org.jclouds.http.HttpErrorHandler;
import org.jclouds.http.RequiresHttp;
import org.jclouds.http.annotation.ClientError;
import org.jclouds.http.annotation.Redirection;
import org.jclouds.http.annotation.ServerError;
import org.jclouds.rest.ConfiguresRestClient;
import org.jclouds.rest.RestContext;
import org.jclouds.rest.Utils;
import org.jclouds.rest.config.BinderUtils;
import org.jclouds.rest.config.RestClientModule;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;

/**
 * Configures the Abiquo connection.
 * 
 * @author Ignasi Barrera
 */
@RequiresHttp
@ConfiguresRestClient
public class AbiquoRestClientModule extends RestClientModule<AbiquoClient, AbiquoAsyncClient>
{
    public static final Map<Class< ? >, Class< ? >> DELEGATE_MAP = ImmutableMap
        .<Class< ? >, Class< ? >> builder() //
        .put(InfrastructureClient.class, InfrastructureAsyncClient.class) //
        .put(EnterpriseClient.class, EnterpriseAsyncClient.class) //
        .put(AdminClient.class, AdminAsyncClient.class) //
        .put(ConfigClient.class, ConfigAsyncClient.class) //
        .put(CloudClient.class, CloudAsyncClient.class) //
        .put(VirtualMachineTemplateClient.class, VirtualMachineTemplateAsyncClient.class) //
        .put(TaskClient.class, TaskAsyncClient.class) //
        .build();

    public AbiquoRestClientModule()
    {
        super(AbiquoClient.class, AbiquoAsyncClient.class, DELEGATE_MAP);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void configure()
    {
        // Override behavior without calling super() method to allow binding the AbiquoContextImpl
        // to the RestContext. Otherwise the ComputeServiceContextBuilder will inject the wrong type
        // in the providerSpecificContext property
        bind(new TypeLiteral<RestContext>()
        {
        }).to(AbiquoContextImpl.class).in(Scopes.SINGLETON);
        bind(new TypeLiteral<RestContext<AbiquoClient, AbiquoAsyncClient>>()
        {
        }).to(AbiquoContextImpl.class).in(Scopes.SINGLETON);
        bindAsyncClient();
        bindClient();
        bindErrorHandlers();
        bindRetryHandlers();

        bindAbiquoGenericHttpClient();
        bind(Utils.class).to(ExtendedUtils.class);
    }

    protected void bindAbiquoGenericHttpClient()
    {
        BinderUtils.bindAsyncClient(binder(), AbiquoHttpAsyncClient.class);
        BinderUtils.bindClient(binder(), AbiquoHttpClient.class, AbiquoHttpAsyncClient.class,
            ImmutableMap.<Class< ? >, Class< ? >> of(AbiquoHttpClient.class,
                AbiquoHttpAsyncClient.class));
    }

    @Override
    protected void bindErrorHandlers()
    {
        bind(HttpErrorHandler.class).annotatedWith(Redirection.class).to(AbiquoErrorHandler.class);
        bind(HttpErrorHandler.class).annotatedWith(ClientError.class).to(AbiquoErrorHandler.class);
        bind(HttpErrorHandler.class).annotatedWith(ServerError.class).to(AbiquoErrorHandler.class);
    }

}
