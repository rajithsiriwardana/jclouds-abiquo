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

package org.jclouds.abiquo;

import java.util.concurrent.TimeUnit;

import org.jclouds.abiquo.features.AdminClient;
import org.jclouds.abiquo.features.CloudClient;
import org.jclouds.abiquo.features.ConfigClient;
import org.jclouds.abiquo.features.EnterpriseClient;
import org.jclouds.abiquo.features.InfrastructureClient;
import org.jclouds.abiquo.features.TaskClient;
import org.jclouds.abiquo.features.VirtualMachineTemplateClient;
import org.jclouds.concurrent.Timeout;
import org.jclouds.rest.annotations.Delegate;

/**
 * Provides synchronous access to Abiquo.
 * 
 * @see API: <a href="http://community.abiquo.com/display/ABI20/API+Reference">
 *      http://community.abiquo.com/display/ABI20/API+Reference</a>
 * @see AbiquoAsyncClient
 * @author Ignasi Barrera
 */
@Timeout(duration = 30, timeUnit = TimeUnit.SECONDS)
public interface AbiquoClient
{
    /**
     * Provides synchronous access to Admin features.
     */
    @Delegate
    AdminClient getAdminClient();

    /**
     * Provides synchronous access to Infrastructure features.
     */
    @Delegate
    InfrastructureClient getInfrastructureClient();

    /**
     * Provides synchronous access to Cloud features.
     */
    @Delegate
    CloudClient getCloudClient();

    /**
     * Provides synchronous access to Apps library features.
     */
    @Delegate
    VirtualMachineTemplateClient getVirtualMachineTemplateClient();

    /**
     * Provides synchronous access to Enterprise features.
     */
    @Delegate
    EnterpriseClient getEnterpriseClient();

    /**
     * Provides synchronous access to configuration features.
     */
    @Delegate
    ConfigClient getConfigClient();

    /**
     * Provides synchronous access to task asynchronous features.
     */
    @Delegate
    TaskClient getTaskClient();
}
