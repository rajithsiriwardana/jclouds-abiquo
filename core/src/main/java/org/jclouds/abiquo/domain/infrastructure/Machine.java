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

import java.util.List;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.rest.ParentLinkName;
import org.jclouds.abiquo.rest.internal.ExtendedUtils;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.ParseXMLWithJAXB;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.enumerator.MachineState;
import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.infrastructure.DatastoresDto;
import com.abiquo.server.core.infrastructure.MachineDto;
import com.abiquo.server.core.infrastructure.MachineStateDto;
import com.abiquo.server.core.infrastructure.RackDto;
import com.google.inject.TypeLiteral;

/**
 * Adds high level functionality to {@link MachineDto}. It defines domain methods for unmanaged
 * physical machines.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see API: <a href="http://community.abiquo.com/display/ABI20/MachineResource">
 *      http://community.abiquo.com/display/ABI20/MachineResource</a>
 */
public class Machine extends AbstractPhysicalMachine
{
    /** The rack where the machine belongs. */
    protected Rack rack;

    /** List of available virtual switches provided by discover operation **/
    protected List<String> virtualSwitches;

    /**
     * Constructor to be used only by the builder.
     */
    protected Machine(final AbiquoContext context, final MachineDto target)
    {
        super(context, target);
        extractVirtualSwitches();
    }

    /**
     * Create a new physical machine in Abiquo. The best way to create a machine if first calling
     * {@link <Datacenter>#<discoverSingleMachine>}. This will return a new {@link Machine}. The
     * following steps are: enabling a datastore, selecting a virtual switch and choosing a rack.
     * Refer link for more information.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/DatacenterResource#DatacenterResource-Retrieveremotemachineinformation"
     *      > http://community.abiquo.com/display/ABI20/DatacenterResource#DatacenterResource-
     *      Retrieveremotemachineinformation</a>
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/MachineResource#MachineResource-Createamachine"
     *      > http://community.abiquo.com/display/ABI20/MachineResource#MachineResource-
     *      Createamachine</a>
     */
    public void save()
    {
        target = context.getApi().getInfrastructureClient().createMachine(rack.unwrap(), target);
    }

    @Override
    public MachineState check()
    {
        MachineStateDto dto =
            context.getApi().getInfrastructureClient().checkMachineState(target, true);
        MachineState state = dto.getState();
        target.setState(state);
        return state;
    }

    // Parent access
    /**
     * Retrieve the unmanaged rack where the machine is.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/RackResource#RackResource-RetrieveaRack" >
     *      http://community.abiquo.com/display/ABI20/RackResource#RackResource-RetrieveaRack</a>
     */
    public Rack getRack()
    {
        RESTLink link =
            checkNotNull(target.searchLink(ParentLinkName.RACK),
                ValidationErrors.MISSING_REQUIRED_LINK + " " + ParentLinkName.RACK);

        ExtendedUtils utils = (ExtendedUtils) context.getUtils();
        HttpResponse response = utils.getAbiquoHttpClient().get(link);

        ParseXMLWithJAXB<RackDto> parser =
            new ParseXMLWithJAXB<RackDto>(utils.getXml(), TypeLiteral.get(RackDto.class));

        return wrap(context, Rack.class, parser.apply(response));
    }

    // Builder

    public static Builder builder(final AbiquoContext context, final Rack rack)
    {
        return new Builder(context, rack);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private String name, description;

        private Integer virtualRamInMb;

        private Integer virtualRamUsedInMb = DEFAULT_VRAM_USED;

        private Integer virtualCpuCores;

        private Integer virtualCpusUsed = DEFAULT_VCPU_USED;

        private String virtualSwitch;

        private Integer port;

        private String ip;

        private MachineState state = MachineState.STOPPED;

        private String ipService;

        private HypervisorType type;

        private String user;

        private String password;

        private Iterable<Datastore> datastores;

        private String ipmiIp;

        private Integer ipmiPort;

        private String ipmiUser;

        private String ipmiPassword;

        private Rack rack;

        public Builder(final AbiquoContext context, final Rack rack)
        {
            super();
            checkNotNull(rack, ValidationErrors.NULL_RESOURCE + Rack.class);
            this.rack = rack;
            this.context = context;
        }

        public Builder state(final MachineState state)
        {
            this.state = state;
            return this;
        }

        public Builder ipmiPassword(final String ipmiPassword)
        {
            this.ipmiPassword = ipmiPassword;
            return this;
        }

        public Builder ipmiUser(final String ipmiUser)
        {
            this.ipmiUser = ipmiUser;
            return this;
        }

        public Builder ipmiPort(final int ipmiPort)
        {
            this.ipmiPort = ipmiPort;
            return this;
        }

        public Builder ipmiIp(final String ipmiIp)
        {
            this.ipmiIp = ipmiIp;
            return this;
        }

        public Builder user(final String user)
        {
            this.user = user;
            return this;
        }

        public Builder ip(final String ip)
        {
            this.ip = ip;
            if (ipService == null)
            {
                ipService = ip;
            }
            return this;
        }

        public Builder ipService(final String ipService)
        {
            this.ipService = ipService;
            return this;
        }

        public Builder password(final String password)
        {
            this.password = password;
            return this;
        }

        public Builder virtualSwitch(final String virtualSwitch)
        {
            this.virtualSwitch = virtualSwitch;
            return this;
        }

        public Builder name(final String name)
        {
            this.name = name;
            return this;
        }

        public Builder description(final String description)
        {
            this.description = description;
            return this;
        }

        public Builder port(final int port)
        {
            this.port = port;
            return this;
        }

        public Builder datastores(final Iterable<Datastore> datastores)
        {
            this.datastores = datastores;
            return this;
        }

        public Builder virtualRamInMb(final int virtualRamInMb)
        {
            this.virtualRamInMb = virtualRamInMb;
            return this;
        }

        public Builder virtualRamUsedInMb(final int virtualRamUsedInMb)
        {
            this.virtualRamUsedInMb = virtualRamUsedInMb;
            return this;
        }

        public Builder virtualCpuCores(final int virtualCpuCores)
        {
            this.virtualCpuCores = virtualCpuCores;
            return this;
        }

        public Builder virtualCpusUsed(final int virtualCpusUsed)
        {
            this.virtualCpusUsed = virtualCpusUsed;
            return this;
        }

        public Builder hypervisorType(final HypervisorType hypervisorType)
        {
            this.type = hypervisorType;

            // Sets default hypervisor port
            if (this.port == null)
            {
                this.port = hypervisorType.defaultPort;
            }

            return this;
        }

        public Builder rack(final Rack rack)
        {
            checkNotNull(rack, ValidationErrors.NULL_RESOURCE + Datacenter.class);
            this.rack = rack;
            return this;
        }

        public Machine build()
        {
            MachineDto dto = new MachineDto();
            dto.setName(name);
            dto.setDescription(description);
            dto.setVirtualRamInMb(virtualRamInMb);
            dto.setVirtualRamUsedInMb(virtualRamUsedInMb);
            dto.setVirtualCpuCores(virtualCpuCores);
            dto.setVirtualCpusUsed(virtualCpusUsed);
            dto.setVirtualSwitch(virtualSwitch);
            if (port != null)
            {
                dto.setPort(port);
            }
            dto.setIp(ip);
            dto.setIpService(ipService);
            dto.setType(type);
            dto.setUser(user);
            dto.setPassword(password);
            dto.setIpmiIP(ipmiIp);
            dto.setIpmiPassword(ipmiPassword);
            if (ipmiPort != null)
            {
                dto.setIpmiPort(ipmiPort);
            }
            dto.setIpmiUser(ipmiUser);
            dto.setState(state);

            DatastoresDto datastoresDto = new DatastoresDto();
            datastoresDto.getCollection().addAll(unwrap(datastores));
            dto.setDatastores(datastoresDto);

            Machine machine = new Machine(context, dto);
            machine.rack = rack;

            return machine;
        }

        public static Builder fromMachine(final Machine in)
        {
            Builder builder =
                Machine.builder(in.context, in.rack).name(in.getName())
                    .description(in.getDescription()).virtualCpuCores(in.getVirtualCpuCores())
                    .virtualCpusUsed(in.getVirtualCpusUsed())
                    .virtualRamInMb(in.getVirtualRamInMb())
                    .virtualRamUsedInMb(in.getVirtualRamUsedInMb())
                    .virtualSwitch(in.getVirtualSwitch()).port(in.getPort()).ip(in.getIp())
                    .ipService(in.getIpService()).hypervisorType(in.getType()).user(in.getUser())
                    .password(in.getPassword()).ipmiIp(in.getIpmiIp())
                    .ipmiPassword(in.getIpmiPassword()).ipmiUser(in.getIpmiUser())
                    .state(in.getState()).datastores(in.getDatastores());

            // Parameters that can be null
            if (in.getIpmiPort() != null)
            {
                builder.ipmiPort(in.getIpmiPort());
            }

            return builder;
        }
    }

    // Delegate methods

    public void setRack(final Rack rack)
    {
        this.rack = rack;
    }
}
