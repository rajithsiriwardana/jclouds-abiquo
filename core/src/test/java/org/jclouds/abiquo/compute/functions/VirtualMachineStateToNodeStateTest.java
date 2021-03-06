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

package org.jclouds.abiquo.compute.functions;

import static org.testng.Assert.assertEquals;

import org.jclouds.compute.domain.NodeState;
import org.testng.annotations.Test;

import com.abiquo.server.core.cloud.VirtualMachineState;

/**
 * Unit tests for the {@link VirtualMachineStateToNodeState} function.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class VirtualMachineStateToNodeStateTest
{
    public void testVirtualMachineStateToNodeState()
    {
        VirtualMachineStateToNodeState function = new VirtualMachineStateToNodeState();

        assertEquals(function.apply(VirtualMachineState.ALLOCATED), NodeState.PENDING);
        assertEquals(function.apply(VirtualMachineState.LOCKED), NodeState.PENDING);
        assertEquals(function.apply(VirtualMachineState.CONFIGURED), NodeState.PENDING);
        assertEquals(function.apply(VirtualMachineState.ON), NodeState.RUNNING);
        assertEquals(function.apply(VirtualMachineState.OFF), NodeState.SUSPENDED);
        assertEquals(function.apply(VirtualMachineState.PAUSED), NodeState.SUSPENDED);
        assertEquals(function.apply(VirtualMachineState.NOT_ALLOCATED), NodeState.TERMINATED);
        assertEquals(function.apply(VirtualMachineState.UNKNOWN), NodeState.UNRECOGNIZED);
    }
}
