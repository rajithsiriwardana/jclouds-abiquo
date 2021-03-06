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

package org.jclouds.abiquo.domain.cloud.options;

import org.jclouds.abiquo.domain.options.QueryOptions;

/**
 * Available options to query virtual machine.
 * 
 * @author Alessia Prete
 */
public class VirtualMachineOptions extends QueryOptions
{
    public static Builder builder()
    {
        return new Builder();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        VirtualMachineOptions options = new VirtualMachineOptions();
        options.map.putAll(map);
        return options;
    }

    @Override
    public String toString()
    {
        return this.map.toString();
    }

    public static class Builder
    {
        private Boolean force;

        public Builder force(final Boolean force)
        {
            this.force = force;
            return this;
        }

        public VirtualMachineOptions build()
        {
            VirtualMachineOptions options = new VirtualMachineOptions();

            if (force != null)
            {
                options.map.put("force", String.valueOf(force));
            }

            return options;
        }
    }
}
