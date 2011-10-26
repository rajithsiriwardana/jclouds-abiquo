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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.jclouds.abiquo.AbiquoContext;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.rest.ParentLinkName;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.enumerator.MachineState;
import com.abiquo.server.core.infrastructure.DatastoresDto;
import com.abiquo.server.core.infrastructure.MachineDto;
import com.abiquo.server.core.infrastructure.RackDto;

/**
 * Adds high level functionality to {@link MachineDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see http://community.abiquo.com/display/ABI18/Machine+Resource
 */
public class Machine extends DomainWrapper<MachineDto>
{
    MachineDto target;

    /** The rack where the machine belongs. */
    // Package protected to allow navigation from children
    Rack rack;

    /** List of available virtual switches provided by discover operation **/
    private List<String> virtualSwitches;

    /**
     * Constructor to be used only by the builder.
     */
    protected Machine(final AbiquoContext context, final MachineDto target)
    {
        super(context, target);
        extractSwitches();
    }

    public void delete()
    {
        context.getApi().getInfrastructureClient().deleteMachine(target);
        target = null;
    }

    public void save()
    {
        target = context.getApi().getInfrastructureClient().createMachine(rack.unwrap(), target);
    }

    public void update()
    {
        target = context.getApi().getInfrastructureClient().updateMachine(target);
    }

    // Parent access

    public Rack getRack()
    {
        Integer rackId = target.getIdFromLink(ParentLinkName.RACK);
        RackDto dto =
            context.getApi().getInfrastructureClient().getRack(rack.datacenter.unwrap(), rackId);
        rack = wrap(context, Rack.class, dto);

        return rack;
    }

    public void setRack(final Rack rack)
    {
        this.rack = rack;
    }

    public String getDescription()
    {
        return target.getDescription();
    }

    // Builder

    public static Builder builder(final AbiquoContext context, final Rack rack)
    {
        return new Builder(context, rack);
    }

    public static class Builder
    {
        private AbiquoContext context;

        private Integer id;

        private String name, description;

        private Integer virtualRamInMb;

        private Integer virtualRamUsedInMb = 1;

        private Integer virtualCpuCores;

        private Integer virtualCpusUsed = 1;

        private Integer virtualCpusPerCore = 1;

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
            checkNotNull(rack, ValidationErrors.NULL_PARENT + Rack.class);
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

        public Builder ipmiPort(final Integer ipmiPort)
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
            this.user = ip;
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

        public Builder id(final Integer id)
        {
            this.id = id;
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

        public Builder port(final Integer port)
        {
            this.port = port;
            return this;
        }

        public Builder datastores(final Iterable<Datastore> datastores)
        {
            this.datastores = datastores;
            return this;
        }

        public Builder virtualRamInMb(final Integer virtualRamInMb)
        {
            this.virtualRamInMb = virtualRamInMb;
            return this;
        }

        public Builder virtualRamUsedInMb(final Integer virtualRamUsedInMb)
        {
            this.virtualRamUsedInMb = virtualRamUsedInMb;
            return this;
        }

        public Builder virtualCpuCores(final Integer virtualCpuCores)
        {
            this.virtualCpuCores = virtualCpuCores;
            return this;
        }

        public Builder virtualCpusUsed(final Integer virtualCpusUsed)
        {
            this.virtualCpusUsed = virtualCpusUsed;
            return this;
        }

        public Builder virtualCpusPerCore(final Integer virtualCpusPerCore)
        {
            this.virtualCpusPerCore = virtualCpusPerCore;
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
            checkNotNull(rack, ValidationErrors.NULL_PARENT + Datacenter.class);
            this.rack = rack;
            return this;
        }

        public Machine build()
        {
            MachineDto dto = new MachineDto();
            dto.setId(id);
            dto.setName(name);
            dto.setDescription(description);
            dto.setVirtualRamInMb(virtualRamInMb);
            dto.setVirtualRamUsedInMb(virtualRamUsedInMb);
            dto.setVirtualCpuCores(virtualCpuCores);
            dto.setVirtualCpusUsed(virtualCpusUsed);
            dto.setVirtualCpusPerCore(virtualCpusPerCore);
            dto.setVirtualSwitch(virtualSwitch);
            dto.setPort(port);
            dto.setIp(ip);
            dto.setIpService(ipService);
            dto.setType(type);
            dto.setUser(user);
            dto.setPassword(password);
            dto.setIpmiIp(ipmiIp);
            dto.setIpmiPassword(ipmiPassword);
            dto.setIpmiPort(ipmiPort);
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
            return Machine.builder(in.context, in.rack).id(in.getId()).name(in.getName())
                .description(in.getDescription()).virtualCpuCores(in.getVirtualCpuCores())
                .virtualCpusPerCore(in.getVirtualCpusPerCore()).virtualCpusUsed(
                    in.getVirtualCpusUsed()).virtualRamInMb(in.getVirtualRamInMb())
                .virtualRamUsedInMb(in.getVirtualRamUsedInMb())
                .virtualSwitch(in.getVirtualSwitch()).port(in.getPort()).ip(in.getIp()).ipService(
                    in.getIpService()).hypervisorType(in.getType()).user(in.getUser()).password(
                    in.getPassword()).ipmiIp(in.getIpmiIp()).ipmiPassword(in.getIpmiPassword())
                .ipmiPort(in.getIpmiPort()).ipmiUser(in.getIpmiUser()).state(in.getState())
                .datastores(in.getDatastores());

        }
    }

    // Delegate methods

    public Integer getId()
    {
        return target.getId();
    }

    public String getIp()
    {
        return target.getIp();
    }

    public String getIpmiIp()
    {
        return target.getIpmiIp();
    }

    public String getIpmiPassword()
    {
        return target.getIpmiPassword();
    }

    public Integer getIpmiPort()
    {
        return target.getIpmiPort();
    }

    public String getIpmiUser()
    {
        return target.getIpmiUser();
    }

    public String getIpService()
    {
        return target.getIpService();
    }

    public String getName()
    {
        return target.getName();
    }

    public String getPassword()
    {
        return target.getPassword();
    }

    public Integer getPort()
    {
        return target.getPort();
    }

    public MachineState getState()
    {
        return target.getState();
    }

    public HypervisorType getType()
    {
        return target.getType();
    }

    public String getUser()
    {
        return target.getUser();
    }

    public Integer getVirtualCpuCores()
    {
        return target.getVirtualCpuCores();
    }

    public Integer getVirtualCpusPerCore()
    {
        return target.getVirtualCpusPerCore();
    }

    public Integer getVirtualCpusUsed()
    {
        return target.getVirtualCpusUsed();
    }

    public Integer getVirtualRamInMb()
    {
        return target.getVirtualRamInMb();
    }

    public Integer getVirtualRamUsedInMb()
    {
        return target.getVirtualRamUsedInMb();
    }

    public String getVirtualSwitch()
    {
        return target.getVirtualSwitch();
    }

    public void setDatastores(final DatastoresDto datastores)
    {
        target.setDatastores(datastores);
    }

    public void setDescription(final String description)
    {
        target.setDescription(description);
    }

    public void setIp(final String ip)
    {
        target.setIp(ip);
    }

    public void setIpmiIp(final String ipmiIp)
    {
        target.setIpmiIp(ipmiIp);
    }

    public void setIpmiPassword(final String ipmiPassword)
    {
        target.setIpmiPassword(ipmiPassword);
    }

    public void setIpmiPort(final Integer ipmiPort)
    {
        target.setIpmiPort(ipmiPort);
    }

    public void setIpmiUser(final String ipmiUser)
    {
        target.setIpmiUser(ipmiUser);
    }

    public void setIpService(final String ipService)
    {
        target.setIpService(ipService);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setPassword(final String password)
    {
        target.setPassword(password);
    }

    public void setPort(final Integer port)
    {
        target.setPort(port);
    }

    public void setState(final MachineState state)
    {
        target.setState(state);
    }

    public void setType(final HypervisorType type)
    {
        target.setType(type);
    }

    public void setUser(final String user)
    {
        target.setUser(user);
    }

    public void setVirtualCpuCores(final Integer virtualCpuCores)
    {
        target.setVirtualCpuCores(virtualCpuCores);
    }

    public void setVirtualCpusPerCore(final Integer virtualCpusPerCore)
    {
        target.setVirtualCpusPerCore(virtualCpusPerCore);
    }

    public void setVirtualCpusUsed(final Integer virtualCpusUsed)
    {
        target.setVirtualCpusUsed(virtualCpusUsed);
    }

    public void setVirtualRamInMb(final Integer virtualRamInMb)
    {
        target.setVirtualRamInMb(virtualRamInMb);
    }

    public void setVirtualRamUsedInMb(final Integer virtualRamUsedInMb)
    {
        target.setVirtualRamUsedInMb(virtualRamUsedInMb);
    }

    public void setVirtualSwitch(final String virtualSwitch)
    {
        target.setVirtualSwitch(virtualSwitch);
    }

    // Aux operations

    public List<Datastore> getDatastores()
    {
        return wrap(context, Datastore.class, target.getDatastores().getCollection());
    }

    /**
     * Converts the tokenized String provided by the API throw the operation into a list of Strings
     * and stores it at the atribute switches.
     */
    public void extractSwitches()
    {
        StringTokenizer st = new StringTokenizer(getVirtualSwitch(), "/");

        this.virtualSwitches = new ArrayList<String>();

        while (st.hasMoreTokens())
        {
            this.virtualSwitches.add(st.nextToken());
        }

        if (virtualSwitches.size() > 0)
        {
            this.setVirtualSwitch(virtualSwitches.get(0));
        }
    }

    public List<String> getVirtualSwitches()
    {
        return virtualSwitches;
    }

    public void setVirtualSwitches(final List<String> virtualSwitches)
    {
        this.virtualSwitches = virtualSwitches;
    }
}