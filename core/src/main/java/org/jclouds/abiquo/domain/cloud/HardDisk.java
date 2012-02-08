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

import static com.google.common.base.Preconditions.checkNotNull;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.rest.ParentLinkName;

import com.abiquo.server.core.cloud.VirtualDatacenterDto;
import com.abiquo.server.core.infrastructure.storage.DiskManagementDto;

/**
 * Adds high level functionality to {@link DiskManagementDto}.
 * 
 * @author Ignasi Barrera
 * @see <a href="http://community.abiquo.com/display/ABI20/Hard+Disks+Resource">
 *      http://community.abiquo.com/display/ABI20/Hard+Disks+Resource</a>
 */
public class HardDisk extends DomainWrapper<DiskManagementDto>
{
    /** The virtual datacenter where the hard disk belongs. */
    // Package protected to allow navigation from children
    VirtualDatacenter virtualDatacenter;

    /**
     * Constructor to be used only by the builder.
     */
    protected HardDisk(final AbiquoContext context, final DiskManagementDto target)
    {
        super(context, target);
    }

    // Domain operations

    public void save()
    {
        target =
            context.getApi().getCloudClient().createHardDisk(virtualDatacenter.unwrap(), target);
    }

    public void delete()
    {
        context.getApi().getCloudClient().deleteHardDisk(target);
        target = null;
    }

    // Parent access

    /**
     * @see <a
     *      href="http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-RetrieveaVirtualDatacenter">
     *      http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-RetrieveaVirtualDatacenter</a>
     */
    public VirtualDatacenter getVirtualDatacenter()
    {
        Integer virtualDatacenterId = target.getIdFromLink(ParentLinkName.VIRTUAL_DATACENTER);
        VirtualDatacenterDto dto =
            context.getApi().getCloudClient().getVirtualDatacenter(virtualDatacenterId);
        virtualDatacenter = wrap(context, VirtualDatacenter.class, dto);
        return virtualDatacenter;
    }

    // Builder

    public static Builder builder(final AbiquoContext context,
        final VirtualDatacenter virtualDatacenter)
    {
        return new Builder(context, virtualDatacenter);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private Long sizeInMb;

        private VirtualDatacenter virtualDatacenter;

        public Builder(final AbiquoContext context, final VirtualDatacenter virtualDatacenter)
        {
            super();
            checkNotNull(virtualDatacenter, ValidationErrors.NULL_RESOURCE
                + VirtualDatacenter.class);
            this.context = context;
            this.virtualDatacenter = virtualDatacenter;
        }

        public Builder sizeInMb(final long sizeInMb)
        {
            this.sizeInMb = sizeInMb;
            return this;
        }

        public HardDisk build()
        {
            DiskManagementDto dto = new DiskManagementDto();
            dto.setSizeInMb(sizeInMb);

            HardDisk hardDisk = new HardDisk(context, dto);
            hardDisk.virtualDatacenter = virtualDatacenter;

            return hardDisk;
        }
    }

    // Delegate methods. Since a hard disk cannot be edited, setters are not visible

    public Long getSizeInMb()
    {
        return target.getSizeInMb();
    }

    public Integer getSequence()
    {
        return target.getSequence();
    }

    @Override
    public String toString()
    {
        return "HardDisk [sizeInMb()=" + getSizeInMb() + ", sequence()=" + getSequence() + "]";
    }

}