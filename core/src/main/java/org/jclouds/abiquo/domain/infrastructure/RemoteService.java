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

package org.jclouds.abiquo.domain.infrastructure;

import static com.google.common.base.Preconditions.checkNotNull;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.rest.ParentLinkName;

import com.abiquo.model.enumerator.RemoteServiceType;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.RemoteServiceDto;

/**
 * Adds high level functionality to {@link RemoteServiceDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see http://community.abiquo.com/display/ABI18/Remote+Service+Resource
 */
public class RemoteService extends DomainWrapper<RemoteServiceDto>
{
    /** The datacenter using the remote service. */
    private Datacenter datacenter;

    /**
     * Constructor to be used only by the builder.
     */
    protected RemoteService(final AbiquoContext context, final RemoteServiceDto target)
    {
        super(context, target);
    }

    @Override
    public void delete()
    {
        context.getApi().getInfrastructureClient().deleteRemoteService(target);
        target = null;
    }

    @Override
    public void save()
    {
        target =
            context.getApi().getInfrastructureClient()
                .createRemoteService(datacenter.unwrap(), target);
    }

    @Override
    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateRemoteService(target);
    }

    public boolean isAvailable()
    {
        return context.getApi().getInfrastructureClient().isAvailable(target);
    }

    // Parent access

    public Datacenter getDatacenter()
    {
        Integer datacenterId = target.getIdFromLink(ParentLinkName.DATACENTER);
        DatacenterDto dto = context.getApi().getInfrastructureClient().getDatacenter(datacenterId);
        datacenter = wrap(context, Datacenter.class, dto);

        return datacenter;
    }

    public static Builder builder(final AbiquoContext context, final Datacenter datacenter)
    {
        return new Builder(context, datacenter);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private Integer id;

        private Datacenter datacenter;

        private String ip;

        private Integer port;

        private RemoteServiceType type;

        private int status;

        // To be used only internally by the builder
        private String uri;

        public Builder(final AbiquoContext context, final Datacenter datacenter)
        {
            super();
            checkNotNull(datacenter, ValidationErrors.NULL_PARENT + Datacenter.class);
            this.datacenter = datacenter;
            this.context = context;
        }

        public Builder datacenter(final Datacenter datacenter)
        {
            checkNotNull(datacenter, ValidationErrors.NULL_PARENT + Datacenter.class);
            this.datacenter = datacenter;
            return this;
        }

        public Builder status(final int status)
        {
            this.status = status;
            return this;
        }

        public Builder type(final RemoteServiceType type)
        {
            this.type = type;
            return this;
        }

        public Builder ip(final String ip)
        {
            this.ip = ip;
            return this;
        }

        public Builder port(final int port)
        {
            this.port = port;
            return this;
        }

        private String generateUri(final String ip, final Integer port, final RemoteServiceType type)
        {
            return type.getDefaultProtocol() + ip + ":" + port + "/" + type.getServiceMapping();
        }

        public RemoteService build()
        {
            if (uri == null)
            {
                checkNotNull(ip, ValidationErrors.MISSING_REQUIRED_FIELD + "ip");
                checkNotNull(type, ValidationErrors.MISSING_REQUIRED_FIELD + "type");

                uri = generateUri(ip, port == null ? type.getDefaultPort() : port, type);
            }

            RemoteServiceDto dto = new RemoteServiceDto();
            dto.setId(id);
            dto.setType(type);
            dto.setUri(uri);
            dto.setStatus(status);
            RemoteService remoteservice = new RemoteService(context, dto);
            remoteservice.datacenter = datacenter;
            return remoteservice;
        }

        public static Builder fromRemoteService(final RemoteService in)
        {
            Builder builder =
                RemoteService.builder(in.context, in.getDatacenter()).status(in.getStatus())
                    .type(in.getType());
            builder.uri = in.getUri();
            return builder;
        }
    }

    // Delegate methods

    public Integer getId()
    {
        return target.getId();
    }

    public RemoteServiceType getType()
    {
        return target.getType();
    }

    public int getStatus()
    {
        return target.getStatus();
    }

    public String getUri()
    {
        return target.getUri();
    }

    public void setStatus(final int status)
    {
        target.setStatus(status);
    }

    public void setType(final RemoteServiceType type)
    {
        target.setType(type);
    }

    public void setUri(final String uri)
    {
        target.setUri(uri);
    }
}
