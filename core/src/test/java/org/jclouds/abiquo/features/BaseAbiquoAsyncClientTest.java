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

package org.jclouds.abiquo.features;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.testng.Assert.assertEquals;

import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.AbiquoContextFactory;
import org.jclouds.abiquo.config.AbiquoRestClientModule;
import org.jclouds.abiquo.http.filters.AbiquoAuthentication;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.RestClientTest;
import org.jclouds.rest.RestContextSpec;

import com.google.inject.Module;

/**
 * Tests annotation parsing of {@code AbiquoAsyncClient}
 * 
 * @author Ignasi Barrera
 */
public abstract class BaseAbiquoAsyncClientTest<T> extends RestClientTest<T>
{

    @Override
    protected void checkFilters(final HttpRequest request)
    {
        assertEquals(request.getFilters().size(), 1);
        assertEquals(request.getFilters().get(0).getClass(), AbiquoAuthentication.class);
    }

    @Override
    protected Module createModule()
    {
        return new AbiquoRestClientModule();
    }

    @Override
    public RestContextSpec<AbiquoClient, AbiquoAsyncClient> createContextSpec()
    {
        String identity =
            checkNotNull(System.getProperty("test.abiquo.identity"), "test.abiquo.identity");
        String credential =
            checkNotNull(System.getProperty("test.abiquo.credential"), "test.abiquo.credential");
        String endpoint =
            checkNotNull(System.getProperty("test.abiquo.endpoint"), "test.abiquo.endpoint");
        String apiVersion =
            checkNotNull(System.getProperty("test.abiquo.api-version"), "test.abiquo.api-version");
        String buildVersion =
            checkNotNull(System.getProperty("test.abiquo.build-version"),
                "test.abiquo.build-version");

        return new RestContextSpec<AbiquoClient, AbiquoAsyncClient>(AbiquoContextFactory.PROVIDER_NAME,
            endpoint,
            apiVersion,
            buildVersion,
            null,
            identity,
            credential,
            AbiquoClient.class,
            AbiquoAsyncClient.class);
    }
}
