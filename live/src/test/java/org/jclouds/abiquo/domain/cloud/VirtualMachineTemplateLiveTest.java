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

package org.jclouds.abiquo.domain.cloud;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.jclouds.abiquo.domain.config.Category;
import org.jclouds.abiquo.domain.config.Icon;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.environment.CloudTestEnvironment;
import org.jclouds.abiquo.features.BaseAbiquoClientLiveTest;
import org.testng.annotations.Test;

/**
 * Live integration tests for the {@link VirtualMachineTemplate} domain class.
 * 
 * @author Francesc Montserrat
 */
@Test(groups = "live")
public class VirtualMachineTemplateLiveTest extends BaseAbiquoClientLiveTest<CloudTestEnvironment>
{

    public void testGetParent()
    {
        Datacenter datacenter = env.virtualMachine.getTemplate().getDatacenter();
        assertNotNull(datacenter);
        assertEquals(datacenter.getId(), env.datacenter.getId());
    }

    public void testGetIcon()
    {
        Icon icon = env.virtualMachine.getTemplate().getIcon();
        assertNotNull(icon);
    }

    public void testGetCategory()
    {
        Category category = env.virtualMachine.getTemplate().getCategory();
        assertNotNull(category);
    }
}
