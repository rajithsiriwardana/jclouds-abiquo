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
import static com.google.common.collect.Iterables.filter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jclouds.abiquo.AbiquoAsyncClient;
import org.jclouds.abiquo.AbiquoClient;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.domain.cloud.options.VirtualMachineOptions;
import org.jclouds.abiquo.domain.enterprise.Enterprise;
import org.jclouds.abiquo.domain.network.Ip;
import org.jclouds.abiquo.domain.network.Network;
import org.jclouds.abiquo.domain.network.Nic;
import org.jclouds.abiquo.domain.task.AsyncTask;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.rest.ParentLinkName;
import org.jclouds.abiquo.rest.internal.ExtendedUtils;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.rest.RestContext;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.model.transport.AcceptedRequestDto;
import com.abiquo.server.core.appslibrary.VirtualMachineTemplateDto;
import com.abiquo.server.core.cloud.VirtualApplianceDto;
import com.abiquo.server.core.cloud.VirtualDatacenterDto;
import com.abiquo.server.core.cloud.VirtualMachineDto;
import com.abiquo.server.core.cloud.VirtualMachineState;
import com.abiquo.server.core.cloud.VirtualMachineStateDto;
import com.abiquo.server.core.cloud.VirtualMachineTaskDto;
import com.abiquo.server.core.enterprise.EnterpriseDto;
import com.abiquo.server.core.infrastructure.network.IpPoolManagementDto;
import com.abiquo.server.core.infrastructure.network.NicsDto;
import com.abiquo.server.core.infrastructure.storage.DiskManagementDto;
import com.abiquo.server.core.infrastructure.storage.DisksManagementDto;
import com.abiquo.server.core.infrastructure.storage.VolumeManagementDto;
import com.abiquo.server.core.infrastructure.storage.VolumesManagementDto;
import com.abiquo.server.core.task.TasksDto;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;
import com.google.inject.TypeLiteral;

/**
 * Adds high level functionality to {@link VirtualMachineDto}.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 * @see API: <a href="http://community.abiquo.com/display/ABI20/VirtualMachineResource">
 *      http://community.abiquo.com/display/ABI20/VirtualMachineResource</a>
 */
public class VirtualMachine extends DomainWrapper<VirtualMachineDto>
{
    /** The virtual appliance where the virtual machine belongs. */
    private VirtualAppliance virtualAppliance;

    /** The virtual machine template of the virtual machine. */
    private VirtualMachineTemplate template;

    /**
     * Constructor to be used only by the builder.
     */
    protected VirtualMachine(final RestContext<AbiquoClient, AbiquoAsyncClient> context, final VirtualMachineDto target)
    {
        super(context, target);
    }

    // Domain operations

    /**
     * Delete the virtual machine.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/VirtualMachineResource#VirtualMachineResource-Deleteavirtualmachine"
     *      >
     *      http://community.abiquo.com/display/ABI20/VirtualMachineResource#VirtualMachineResource
     *      -Deleteavirtualmachine</a>
     */
    public void delete()
    {
        context.getApi().getCloudClient().deleteVirtualMachine(target);
        target = null;
    }

    /**
     * Create a new virtual machine in Abiquo.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/VirtualMachineResource#VirtualMachineResource-Createavirtualmachine"
     *      > http://community.abiquo.com/display/ABI20/VirtualMachineResource#
     *      VirtualMachineResource-Createavirtualmachine</a>
     */
    public void save()
    {
        checkNotNull(template, ValidationErrors.NULL_RESOURCE + VirtualMachineTemplate.class);
        checkNotNull(template.getId(), ValidationErrors.MISSING_REQUIRED_FIELD + " id in "
            + VirtualMachineTemplate.class);

        this.updateLink(target, ParentLinkName.VIRTUAL_MACHINE_TEMPLATE, template.unwrap(), "edit");

        target =
            context.getApi().getCloudClient()
                .createVirtualMachine(virtualAppliance.unwrap(), target);
    }

    /**
     * Update virtual machine information in the server with the data from this virtual machine.
     * This is an asynchronous call. This method returns a
     * {@link org.jclouds.abiquo.domain.task.AsyncTask} object that keeps track of the task
     * completion. Please refer to the documentation for details.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/VirtualMachineResource#VirtualMachineResource-Modifyavirtualmachine"
     *      > http://community.abiquo.com/display/ABI20/VirtualMachineResource#
     *      VirtualMachineResource-Modifyavirtualmachine</a>
     * @see github: <a href=
     *      "https://github.com/abiquo/jclouds-abiquo/wiki/Asynchronous-monitor-example" >
     *      https://github.com/abiquo/jclouds-abiquo/wiki/Asynchronous-monitor-example</a>
     * @return The task reference or <code>null</code> if the operation completed synchronously.
     */
    public AsyncTask update()
    {
        AcceptedRequestDto<String> taskRef =
            context.getApi().getCloudClient().updateVirtualMachine(target);
        return taskRef == null ? null : getTask(taskRef);
    }

    /**
     * Update virtual machine information in the server with the data from this virtual machine.
     * This is an asynchronous call. This method returns a
     * {@link org.jclouds.abiquo.domain.task.AsyncTask} object that keeps track of the task
     * completion. Please refer to the documentation for details.
     * 
     * @param force Force update.
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/VirtualMachineResource#VirtualMachineResource-Modifyavirtualmachine"
     *      > http://community.abiquo.com/display/ABI20/VirtualMachineResource#
     *      VirtualMachineResource-Modifyavirtualmachine</a>
     * @see github: <a href=
     *      "https://github.com/abiquo/jclouds-abiquo/wiki/Asynchronous-monitor-example" >
     *      https://github.com/abiquo/jclouds-abiquo/wiki/Asynchronous-monitor-example</a>
     * @return The task reference or <code>null</code> if the operation completed synchronously.
     */
    public AsyncTask update(final boolean force)
    {
        AcceptedRequestDto<String> taskRef =
            context.getApi().getCloudClient()
                .updateVirtualMachine(target, VirtualMachineOptions.builder().force(force).build());
        return taskRef == null ? null : getTask(taskRef);
    }

    /**
     * Change the state of the virtual machine. This is an asynchronous call. This method returns a
     * {@link org.jclouds.abiquo.domain.task.AsyncTask} object that keeps track of the task
     * completion. Please refer to the documentation for details.
     * 
     * @param state The new state of the virtual machine.
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/VirtualMachineResource#VirtualMachineResource-Changethestateofavirtualmachine"
     *      > http://community.abiquo.com/display/ABI20/VirtualMachineResource#
     *      VirtualMachineResource-Changethestateofavirtualmachine</a>
     * @see github: <a href=
     *      "https://github.com/abiquo/jclouds-abiquo/wiki/Asynchronous-monitor-example" >
     *      https://github.com/abiquo/jclouds-abiquo/wiki/Asynchronous-monitor-example</a>
     * @return The task reference or <code>null</code> if the operation completed synchronously.
     */
    public AsyncTask changeState(final VirtualMachineState state)
    {
        VirtualMachineStateDto dto = new VirtualMachineStateDto();
        dto.setState(state);

        AcceptedRequestDto<String> taskRef =
            context.getApi().getCloudClient().changeVirtualMachineState(target, dto);

        return getTask(taskRef);
    }

    /**
     * Retrieve the state of the virtual machine.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/VirtualMachineResource#VirtualMachineResource-Retrievethestateofthevirtualmachine"
     *      > http://community.abiquo.com/display/ABI20/VirtualMachineResource#
     *      VirtualMachineResource-Retrievethestateofthevirtualmachine</a>
     * @return Current state of the virtual machine.
     */
    public VirtualMachineState getState()
    {
        VirtualMachineStateDto stateDto =
            context.getApi().getCloudClient().getVirtualMachineState(target);
        VirtualMachineState state = stateDto.getState();
        target.setState(state);
        target.setIdState(state.id());
        return state;
    }

    // Parent access

    /**
     * Retrieve the virtual appliance where this virtual machine is.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/VirtualApplianceResource#VirtualApplianceResource-Retrieveavirtualappliance"
     *      > http://community.abiquo.com/display/ABI20/VirtualApplianceResource#
     *      VirtualApplianceResource-Retrieveavirtualappliance</a>
     * @return The virtual appliance where this virtual machine is.
     */
    public VirtualAppliance getVirtualAppliance()
    {
        RESTLink link =
            checkNotNull(target.searchLink(ParentLinkName.VIRTUAL_APPLIANCE),
                ValidationErrors.MISSING_REQUIRED_LINK + " " + ParentLinkName.VIRTUAL_APPLIANCE);

        ExtendedUtils utils = (ExtendedUtils) context.getUtils();
        HttpResponse response = utils.getAbiquoHttpClient().get(link);

        ParseXMLWithJAXB<VirtualApplianceDto> parser =
            new ParseXMLWithJAXB<VirtualApplianceDto>(utils.getXml(),
                TypeLiteral.get(VirtualApplianceDto.class));

        return wrap(context, VirtualAppliance.class, parser.apply(response));
    }

    /**
     * Retrieve the virtual datacenter where this virtual machine is.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/VirtualDatacenterResource#VirtualDatacenterResource-Retireveavirtualdatacenter"
     *      > http://community.abiquo.com/display/ABI20/VirtualDatacenterResource#
     *      VirtualDatacenterResource-Retireveavirtualdatacenter</a>
     * @return The virtual datacenter where this virtual machine is.
     */
    public VirtualDatacenter getVirtualDatacenter()
    {
        Integer virtualDatacenterId = target.getIdFromLink(ParentLinkName.VIRTUAL_DATACENTER);
        VirtualDatacenterDto dto =
            context.getApi().getCloudClient().getVirtualDatacenter(virtualDatacenterId);
        return wrap(context, VirtualDatacenter.class, dto);
    }

    /**
     * Retrieve the enterprise of this virtual machine.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/EnterpriseResource#EnterpriseResource-RetrieveanEnterprise"
     *      > http://community.abiquo.com/display/ABI20/EnterpriseResource#EnterpriseResource-
     *      RetrieveanEnterprise</a>
     * @return Enterprise of this virtual machine.
     */
    public Enterprise getEnterprise()
    {
        Integer enterpriseId = target.getIdFromLink(ParentLinkName.ENTERPRISE);
        EnterpriseDto dto = context.getApi().getEnterpriseClient().getEnterprise(enterpriseId);
        return wrap(context, Enterprise.class, dto);
    }

    /**
     * Retrieve the template of this virtual machine.
     * 
     * @return Template of this virtual machine.
     */
    public VirtualMachineTemplate getTemplate()
    {
        VirtualMachineTemplateDto dto =
            context.getApi().getCloudClient().getVirtualMachineTemplate(target);
        return wrap(context, VirtualMachineTemplate.class, dto);
    }

    // Children access

    public List<HardDisk> listAttachedHardDisks()
    {
        DisksManagementDto hardDisks =
            context.getApi().getCloudClient().listAttachedHardDisks(target);
        return wrap(context, HardDisk.class, hardDisks.getCollection());
    }

    public List<HardDisk> listAttachedHardDisks(final Predicate<HardDisk> filter)
    {
        return Lists.newLinkedList(filter(listAttachedHardDisks(), filter));
    }

    public HardDisk findAttachedHardDisk(final Predicate<HardDisk> filter)
    {
        return Iterables.getFirst(filter(listAttachedHardDisks(), filter), null);
    }

    public List<Volume> listAttachedVolumes()
    {
        VolumesManagementDto volumes =
            context.getApi().getCloudClient().listAttachedVolumes(target);
        return wrap(context, Volume.class, volumes.getCollection());
    }

    public List<Volume> listAttachedVolumes(final Predicate<Volume> filter)
    {
        return Lists.newLinkedList(filter(listAttachedVolumes(), filter));
    }

    public Volume findAttachedVolume(final Predicate<Volume> filter)
    {
        return Iterables.getFirst(filter(listAttachedVolumes(), filter), null);
    }

    public List<AsyncTask> listTasks()
    {
        TasksDto result = context.getApi().getTaskClient().listTasks(target);
        List<AsyncTask> tasks = wrap(context, AsyncTask.class, result.getCollection());

        // Return the most recent task first
        Collections.sort(tasks, new Ordering<AsyncTask>()
        {
            @Override
            public int compare(final AsyncTask left, final AsyncTask right)
            {
                return Longs.compare(left.getTimestamp(), right.getTimestamp());
            }
        }.reverse());

        return tasks;
    }

    public List<AsyncTask> listTasks(final Predicate<AsyncTask> filter)
    {
        return Lists.newLinkedList(filter(listTasks(), filter));
    }

    public AsyncTask findTask(final Predicate<AsyncTask> filter)
    {
        return Iterables.getFirst(filter(listTasks(), filter), null);
    }

    public List<Nic> listAttachedNics()
    {
        NicsDto nics = context.getApi().getCloudClient().listAttachedNics(target);

        return wrap(context, Nic.class, nics.getCollection());
    }

    public List<Nic> listAttachedNics(final Predicate<Nic> filter)
    {
        return Lists.newLinkedList(filter(listAttachedNics(), filter));
    }

    public Nic findAttachedNic(final Predicate<Nic> filter)
    {
        return Iterables.getFirst(filter(listAttachedNics(), filter), null);
    }

    // Actions

    public AsyncTask deploy()
    {
        return deploy(false);
    }

    public AsyncTask deploy(final boolean forceEnterpriseSoftLimits)
    {
        VirtualMachineTaskDto force = new VirtualMachineTaskDto();
        force.setForceEnterpriseSoftLimits(forceEnterpriseSoftLimits);

        AcceptedRequestDto<String> response =
            context.getApi().getCloudClient().deployVirtualMachine(unwrap(), force);

        return getTask(response);
    }

    public AsyncTask undeploy()
    {
        return undeploy(false);
    }

    public AsyncTask undeploy(final boolean forceUndeploy)
    {
        VirtualMachineTaskDto force = new VirtualMachineTaskDto();
        force.setForceUndeploy(forceUndeploy);

        AcceptedRequestDto<String> response =
            context.getApi().getCloudClient().undeployVirtualMachine(unwrap(), force);

        return getTask(response);
    }

    /**
     * Reboot a virtual machine. This is an asynchronous call. This method returns a
     * {@link org.jclouds.abiquo.domain.task.AsyncTask} object that keeps track of the task
     * completion. Please refer to the documentation for details.
     * 
     * @see API: <a href=
     *      "http://community.abiquo.com/display/ABI20/VirtualMachineResource#VirtualMachineResource-Resetavirtualmachine"
     *      > http://community.abiquo.com/display/ABI20/Rack+Resource#/VirtualMachineResource#
     *      VirtualMachineResource-Resetavirtualmachine</a>
     * @see github: <a href=
     *      "https://github.com/abiquo/jclouds-abiquo/wiki/Asynchronous-monitor-example" >
     *      https://github.com/abiquo/jclouds-abiquo/wiki/Asynchronous-monitor-example</a>
     * @return The task reference or <code>null</code> if the operation completed synchronously.
     */
    public AsyncTask reboot()
    {
        AcceptedRequestDto<String> response =
            context.getApi().getCloudClient().rebootVirtualMachine(unwrap());

        return getTask(response);
    }

    public AsyncTask attachHardDisks(final HardDisk... hardDisks)
    {
        List<HardDisk> expected = listAttachedHardDisks();
        expected.addAll(Arrays.asList(hardDisks));

        HardDisk[] disks = new HardDisk[expected.size()];
        return replaceHardDisks(expected.toArray(disks));
    }

    public AsyncTask detachAllHardDisks()
    {
        AcceptedRequestDto<String> taskRef =
            context.getApi().getCloudClient().detachAllHardDisks(target);
        return taskRef == null ? null : getTask(taskRef);
    }

    public AsyncTask detachHardDisks(final HardDisk... hardDisks)
    {
        List<HardDisk> expected = listAttachedHardDisks();
        Iterables.removeIf(expected, hardDiskIdIn(hardDisks));

        HardDisk[] disks = new HardDisk[expected.size()];
        return replaceHardDisks(expected.toArray(disks));
    }

    public AsyncTask replaceHardDisks(final HardDisk... hardDisks)
    {
        AcceptedRequestDto<String> taskRef =
            context.getApi().getCloudClient().replaceHardDisks(target, toHardDiskDto(hardDisks));
        return taskRef == null ? null : getTask(taskRef);
    }

    public AsyncTask attachVolumes(final Volume... volumes)
    {
        List<Volume> expected = listAttachedVolumes();
        expected.addAll(Arrays.asList(volumes));

        Volume[] vols = new Volume[expected.size()];
        return replaceVolumes(expected.toArray(vols));
    }

    public AsyncTask detachAllVolumes()
    {
        AcceptedRequestDto<String> taskRef =
            context.getApi().getCloudClient().detachAllVolumes(target);
        return taskRef == null ? null : getTask(taskRef);
    }

    public AsyncTask detachVolumes(final Volume... volumes)
    {
        List<Volume> expected = listAttachedVolumes();
        Iterables.removeIf(expected, volumeIdIn(volumes));

        Volume[] vols = new Volume[expected.size()];
        return replaceVolumes(expected.toArray(vols));
    }

    public AsyncTask replaceVolumes(final Volume... volumes)
    {
        AcceptedRequestDto<String> taskRef =
            context.getApi().getCloudClient().replaceVolumes(target, toVolumeDto(volumes));
        return taskRef == null ? null : getTask(taskRef);
    }

    public AsyncTask replaceNics(final Ip... ips)
    {
        AcceptedRequestDto<String> taskRef =
            context.getApi().getCloudClient().replaceNics(target, toIpDto(ips));
        return taskRef == null ? null : getTask(taskRef);
    }

    public void setGatewayNetwork(final Network network)
    {
        context.getApi().getCloudClient().setGatewayNetwork(target, network.unwrap());
    }

    // Builder

    public static Builder builder(final RestContext<AbiquoClient, AbiquoAsyncClient> context,
        final VirtualAppliance virtualAppliance, final VirtualMachineTemplate template)
    {
        return new Builder(context, virtualAppliance, template);
    }

    public static class Builder
    {
        private final RestContext<AbiquoClient, AbiquoAsyncClient> context;

        private VirtualAppliance virtualAppliance;

        private final VirtualMachineTemplate template;

        private String name;

        private String description;

        private Integer ram;

        private Integer cpu;

        private Integer vncPort;

        private String vncAddress;

        private Integer idState;

        private Integer idType;

        private String password;

        private String uuid;

        public Builder(final RestContext<AbiquoClient, AbiquoAsyncClient> context, final VirtualAppliance virtualAppliance,
            final VirtualMachineTemplate template)
        {
            super();
            checkNotNull(virtualAppliance, ValidationErrors.NULL_RESOURCE + VirtualAppliance.class);
            this.virtualAppliance = virtualAppliance;
            this.template = template;
            this.context = context;
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

        public Builder ram(final int ram)
        {
            this.ram = ram;
            return this;
        }

        public Builder cpu(final int cpu)
        {
            this.cpu = cpu;
            return this;
        }

        public Builder password(final String password)
        {
            this.password = password;
            return this;
        }

        // This methods are used only to build a builder from an existing VirtualMachine but should
        // never be used by the user. This fields are set automatically by Abiquo

        private Builder vncPort(final int vdrpPort)
        {
            this.vncPort = vdrpPort;
            return this;
        }

        private Builder vncAddress(final String vdrpIP)
        {
            this.vncAddress = vdrpIP;
            return this;
        }

        private Builder idState(final int idState)
        {
            this.idState = idState;
            return this;
        }

        private Builder idType(final int idType)
        {
            this.idType = idType;
            return this;
        }

        public Builder virtualAppliance(final VirtualAppliance virtualAppliance)
        {
            checkNotNull(virtualAppliance, ValidationErrors.NULL_RESOURCE + VirtualAppliance.class);
            this.virtualAppliance = virtualAppliance;
            return this;
        }

        public VirtualMachine build()
        {
            VirtualMachineDto dto = new VirtualMachineDto();
            dto.setName(name);
            dto.setDescription(description);
            dto.setHdInBytes(template.getHdRequired());
            dto.setVdrpIP(vncAddress);

            if (cpu != null)
            {
                dto.setCpu(cpu);
            }

            if (ram != null)
            {
                dto.setRam(ram);
            }

            if (vncPort != null)
            {
                dto.setVdrpPort(vncPort);
            }

            if (idState != null)
            {
                dto.setIdState(idState);
            }

            if (idType != null)
            {
                dto.setIdType(idType);
            }

            dto.setPassword(password);
            dto.setUuid(uuid);

            VirtualMachine virtualMachine = new VirtualMachine(context, dto);
            virtualMachine.virtualAppliance = virtualAppliance;
            virtualMachine.template = template;

            return virtualMachine;
        }

        public static Builder fromVirtualMachine(final VirtualMachine in)
        {
            return VirtualMachine.builder(in.context, in.virtualAppliance, in.template)
                .name(in.getName()).description(in.getDescription()).ram(in.getRam())
                .cpu(in.getCpu()).vncAddress(in.getVncAddress()).vncPort(in.getVncPort())
                .idState(in.getIdState()).idType(in.getIdType()).password(in.getPassword());
        }
    }

    // Delegate methods

    public int getCpu()
    {
        return target.getCpu();
    }

    public String getDescription()
    {
        return target.getDescription();
    }

    // Read-only field. This value is computed from the size of the Template
    public long getHdInBytes()
    {
        return target.getHdInBytes();
    }

    public Integer getId()
    {
        return target.getId();
    }

    public int getIdState()
    {
        return target.getIdState();
    }

    public int getIdType()
    {
        return target.getIdType();
    }

    public String getName()
    {
        return target.getName();
    }

    public String getPassword()
    {
        return target.getPassword();
    }

    public int getRam()
    {
        return target.getRam();
    }

    public String getUuid()
    {
        return target.getUuid();
    }

    public String getVncAddress()
    {
        return target.getVdrpIP();
    }

    public int getVncPort()
    {
        return target.getVdrpPort();
    }

    public void setCpu(final int cpu)
    {
        target.setCpu(cpu);
    }

    public void setDescription(final String description)
    {
        target.setDescription(description);
    }

    public void setName(final String name)
    {
        target.setName(name);
    }

    public void setPassword(final String password)
    {
        target.setPassword(password);
    }

    public void setRam(final int ram)
    {
        target.setRam(ram);
    }

    private static VolumeManagementDto[] toVolumeDto(final Volume... volumes)
    {
        checkNotNull(volumes, "must provide at least one volume");

        VolumeManagementDto[] dtos = new VolumeManagementDto[volumes.length];
        for (int i = 0; i < volumes.length; i++)
        {
            dtos[i] = volumes[i].unwrap();
        }

        return dtos;
    }

    private static DiskManagementDto[] toHardDiskDto(final HardDisk... hardDisks)
    {
        checkNotNull(hardDisks, "must provide at least one hard disk");

        DiskManagementDto[] dtos = new DiskManagementDto[hardDisks.length];
        for (int i = 0; i < hardDisks.length; i++)
        {
            dtos[i] = hardDisks[i].unwrap();
        }

        return dtos;
    }

    private static IpPoolManagementDto[] toIpDto(final Ip... ips)
    {
        checkNotNull(ips, "must provide at least one ip");

        IpPoolManagementDto[] dtos = new IpPoolManagementDto[ips.length];
        for (int i = 0; i < ips.length; i++)
        {
            dtos[i] = ips[i].unwrap();
        }

        return dtos;
    }

    private static Predicate<Volume> volumeIdIn(final Volume... volumes)
    {
        return new Predicate<Volume>()
        {
            List<Integer> ids = volumeIds(Arrays.asList(volumes));

            @Override
            public boolean apply(final Volume input)
            {
                return ids.contains(input.getId());
            }
        };
    }

    private static Predicate<HardDisk> hardDiskIdIn(final HardDisk... hardDisks)
    {
        return new Predicate<HardDisk>()
        {
            List<Integer> ids = hardDisksIds(Arrays.asList(hardDisks));

            @Override
            public boolean apply(final HardDisk input)
            {
                return ids.contains(input.getId());
            }
        };
    }

    private static List<Integer> volumeIds(final List<Volume> volumes)
    {
        return Lists.transform(volumes, new Function<Volume, Integer>()
        {
            @Override
            public Integer apply(final Volume input)
            {
                return input.getId();
            }
        });
    }

    private static List<Integer> hardDisksIds(final List<HardDisk> HardDisk)
    {
        return Lists.transform(HardDisk, new Function<HardDisk, Integer>()
        {
            @Override
            public Integer apply(final HardDisk input)
            {
                return input.getId();
            }
        });
    }

    @Override
    public String toString()
    {
        return "VirtualMachine [id=" + getId() + ", state=" + target.getState().name() + ", cpu="
            + getCpu() + ", description=" + getDescription() + ", hdInBytes=" + getHdInBytes()
            + ", idType=" + getIdType() + ", name=" + getName() + ", password=" + getPassword()
            + ", ram=" + getRam() + ", uuid=" + getUuid() + ", vncAddress=" + getVncAddress()
            + ", vncPort=" + getVncPort() + "]";
    }

}
