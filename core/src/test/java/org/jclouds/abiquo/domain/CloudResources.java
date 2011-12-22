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

package org.jclouds.abiquo.domain;

import static org.jclouds.abiquo.domain.DomainUtils.link;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.enumerator.VolumeState;
import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.cloud.VirtualApplianceDto;
import com.abiquo.server.core.cloud.VirtualDatacenterDto;
import com.abiquo.server.core.cloud.VirtualMachineDeployDto;
import com.abiquo.server.core.cloud.VirtualMachineDto;
import com.abiquo.server.core.infrastructure.storage.TierDto;
import com.abiquo.server.core.infrastructure.storage.VolumeManagementDto;

/**
 * Cloud domain utilities.
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
public class CloudResources
{
    public static VirtualDatacenterDto virtualDatacenterPost()
    {
        VirtualDatacenterDto virtualDatacenter = new VirtualDatacenterDto();
        virtualDatacenter.setName("VDC");
        virtualDatacenter.setHypervisorType(HypervisorType.KVM);
        virtualDatacenter.setVlan(NetworkResources.vlanPost());
        return virtualDatacenter;
    }

    public static VirtualApplianceDto virtualAppliancePost()
    {
        VirtualApplianceDto virtualAppliance = new VirtualApplianceDto();
        virtualAppliance.setName("VA");
        return virtualAppliance;
    }

    public static VirtualMachineDto virtualMachinePost()
    {
        VirtualMachineDto virtualMachine = new VirtualMachineDto();
        virtualMachine.setName("VM");
        return virtualMachine;
    }

    public static VirtualDatacenterDto virtualDatacenterPut()
    {
        VirtualDatacenterDto virtualDatacenter = virtualDatacenterPost();
        virtualDatacenter.setId(1);
        virtualDatacenter.addLink(new RESTLink("datacenter",
            "http://localhost/api/admin/datacenters/1"));
        virtualDatacenter.addLink(new RESTLink("enterprise",
            "http://localhost/api/admin/enterprises/1"));
        virtualDatacenter.addLink(new RESTLink("edit",
            "http://localhost/api/cloud/virtualdatacenters/1"));
        virtualDatacenter.addLink(new RESTLink("tiers",
            "http://localhost/api/cloud/virtualdatacenters/1/tiers"));
        virtualDatacenter.addLink(new RESTLink("virtualappliances",
            "http://localhost/api/cloud/virtualdatacenters/1/virtualappliances"));
        virtualDatacenter.addLink(new RESTLink("volumes",
            "http://localhost/api/cloud/virtualdatacenters/1/volumes"));
        virtualDatacenter.addLink(new RESTLink("privatenetworks",
            "http://localhost/api/cloud/virtualdatacenters/1/privatenetworks"));
        virtualDatacenter.addLink(new RESTLink("defaultnetwork",
            "http://localhost/api/cloud/virtualdatacenters/1/privatenetworks/1"));
        virtualDatacenter.addLink(new RESTLink("defaultvlan",
            "http://localhost/api/cloud/virtualdatacenters/1/action/defaultvlan"));
        return virtualDatacenter;

    }

    public static VirtualApplianceDto virtualAppliancePut()
    {
        VirtualApplianceDto virtualAppliance = virtualAppliancePost();
        virtualAppliance.setId(1);
        virtualAppliance.addLink(new RESTLink("virtualdatacenter",
            "http://localhost/api/cloud/virtualdatacenters/1"));
        virtualAppliance.addLink(new RESTLink("edit",
            "http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1"));
        virtualAppliance.addLink(new RESTLink("deploy",
            "http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/action/deploy"));
        virtualAppliance.addLink(new RESTLink("undeploy",
            "http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/action/undeploy"));
        virtualAppliance.addLink(new RESTLink("virtualmachine",
            "http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines"));
        return virtualAppliance;
    }

    public static VirtualMachineDto virtualMachinePut()
    {
        VirtualMachineDto virtualMachine = virtualMachinePost();
        virtualMachine.setId(1);
        virtualMachine.addLink(new RESTLink("virtualappliance",
            "http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1"));
        virtualMachine
            .addLink(new RESTLink("edit",
                "http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines/1"));
        virtualMachine
            .addLink(new RESTLink("state",
                "http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines/1/state"));
        virtualMachine
            .addLink(new RESTLink("volumes",
                "http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines/1/storage/volumes"));
        return virtualMachine;
    }

    public static VolumeManagementDto volumePost()
    {
        VolumeManagementDto volume = new VolumeManagementDto();
        volume.setName("Volume");
        volume.setSizeInMB(1024);
        volume.addLink(new RESTLink("tier",
            "http://localhost/api/cloud/virtualdatacenters/1/tiers/1"));
        return volume;
    }

    public static VolumeManagementDto volumePut()
    {
        VolumeManagementDto volume = volumePost();
        volume.setId(1);
        volume.setState(VolumeState.DETACHED.name());

        volume.getLinks().clear();

        RESTLink mappings =
            new RESTLink("action",
                "http://localhost/api/cloud/virtualdatacenters/1/volumes/1/action/initiatormappings");
        mappings.setTitle("initiator mappings");
        volume.addLink(mappings);
        volume.addLink(new RESTLink("edit",
            "http://localhost/api/cloud/virtualdatacenters/1/volumes/1"));
        volume.addLink(new RESTLink("tier",
            "http://localhost/api/cloud/virtualdatacenters/1/tiers/1"));
        volume.addLink(new RESTLink("virtualdatacenter",
            "http://localhost/api/cloud/virtualdatacenters/1"));
        return volume;
    }

    public static TierDto cloudTierPut()
    {
        TierDto tier = new TierDto();
        tier.setId(1);
        tier.setEnabled(true);
        tier.setName("Tier");
        tier
            .addLink(new RESTLink("edit", "http://localhost/api/cloud/virtualdatacenters/1/tiers/1"));
        return tier;
    }

    public static VirtualMachineDeployDto virtualMachineDeploy()
    {
        VirtualMachineDeployDto deploy = new VirtualMachineDeployDto();
        deploy.setForceEnterpriseSoftLimits(false);
        return deploy;
    }

    public static String virtualDatacenterPostPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<virtualDatacenter>");
        buffer.append("<cpuHard>0</cpuHard>");
        buffer.append("<cpuSoft>0</cpuSoft>");
        buffer.append("<hdHard>0</hdHard>");
        buffer.append("<hdSoft>0</hdSoft>");
        buffer.append("<publicIpsHard>0</publicIpsHard>");
        buffer.append("<publicIpsSoft>0</publicIpsSoft>");
        buffer.append("<ramHard>0</ramHard>");
        buffer.append("<ramSoft>0</ramSoft>");
        buffer.append("<storageHard>0</storageHard>");
        buffer.append("<storageSoft>0</storageSoft>");
        buffer.append("<vlansHard>0</vlansHard>");
        buffer.append("<vlansSoft>0</vlansSoft>");
        buffer.append("<hypervisorType>KVM</hypervisorType>");
        buffer.append("<name>VDC</name>");
        buffer.append(NetworkResources.vlanNetworkPostPayload());
        buffer.append("</virtualDatacenter>");
        return buffer.toString();
    }

    public static String virtualAppliancePostPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<virtualAppliance>");
        buffer.append("<error>0</error>");
        buffer.append("<highDisponibility>0</highDisponibility>");
        buffer.append("<name>VA</name>");
        buffer.append("<publicApp>0</publicApp>");
        buffer.append("</virtualAppliance>");
        return buffer.toString();
    }

    public static String virtualMachinePostPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<virtualMachine>");
        buffer.append("<cpu>0</cpu>");
        buffer.append("<hdInBytes>0</hdInBytes>");
        buffer.append("<highDisponibility>0</highDisponibility>");
        buffer.append("<idState>0</idState>");
        buffer.append("<idType>0</idType>");
        buffer.append("<name>VM</name>");
        buffer.append("<ram>0</ram>");
        buffer.append("<vdrpPort>0</vdrpPort>");
        buffer.append("</virtualMachine>");
        return buffer.toString();
    }

    public static String virtualDatacenterPutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<virtualDatacenter>");
        buffer.append(link("/admin/datacenters/1", "datacenter"));
        buffer.append(link("/admin/enterprises/1", "enterprise"));
        buffer.append(link("/cloud/virtualdatacenters/1", "edit"));
        buffer.append(link("/cloud/virtualdatacenters/1/tiers", "tiers"));
        buffer.append(link("/cloud/virtualdatacenters/1/virtualappliances", "virtualappliances"));
        buffer.append(link("/cloud/virtualdatacenters/1/volumes", "volumes"));
        buffer.append(link("/cloud/virtualdatacenters/1/privatenetworks", "privatenetworks"));
        buffer.append(link("/cloud/virtualdatacenters/1/privatenetworks/1", "defaultnetwork"));
        buffer.append(link("/cloud/virtualdatacenters/1/action/defaultvlan", "defaultvlan"));
        buffer.append("<cpuHard>0</cpuHard>");
        buffer.append("<cpuSoft>0</cpuSoft>");
        buffer.append("<hdHard>0</hdHard>");
        buffer.append("<hdSoft>0</hdSoft>");
        buffer.append("<publicIpsHard>0</publicIpsHard>");
        buffer.append("<publicIpsSoft>0</publicIpsSoft>");
        buffer.append("<ramHard>0</ramHard>");
        buffer.append("<ramSoft>0</ramSoft>");
        buffer.append("<storageHard>0</storageHard>");
        buffer.append("<storageSoft>0</storageSoft>");
        buffer.append("<vlansHard>0</vlansHard>");
        buffer.append("<vlansSoft>0</vlansSoft>");
        buffer.append("<hypervisorType>KVM</hypervisorType>");
        buffer.append("<id>1</id>");
        buffer.append("<name>VDC</name>");
        buffer.append(NetworkResources.vlanNetworkPostPayload());
        buffer.append("</virtualDatacenter>");
        return buffer.toString();
    }

    public static String virtualAppliancePutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<virtualAppliance>");
        buffer.append(link("/cloud/virtualdatacenters/1", "virtualdatacenter"));
        buffer.append(link("/cloud/virtualdatacenters/1/virtualappliances/1", "edit"));
        buffer.append(link("/cloud/virtualdatacenters/1/virtualappliances/1/action/deploy",
            "deploy"));
        buffer.append(link("/cloud/virtualdatacenters/1/virtualappliances/1/action/undeploy",
            "undeploy"));
        buffer.append(link("/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines",
            "virtualmachine"));
        buffer.append("<error>0</error>");
        buffer.append("<highDisponibility>0</highDisponibility>");
        buffer.append("<id>1</id>");
        buffer.append("<name>VA</name>");
        buffer.append("<publicApp>0</publicApp>");
        buffer.append("</virtualAppliance>");
        return buffer.toString();
    }

    public static String virtualMachinePutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<virtualMachine>");
        buffer.append(link("/cloud/virtualdatacenters/1/virtualappliances/1", "virtualappliance"));
        buffer.append(link("/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines/1",
            "edit"));
        buffer.append(link(
            "/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines/1/state", "state"));
        buffer.append(link(
            "/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines/1/storage/volumes",
            "volumes"));
        buffer.append("<cpu>0</cpu>");
        buffer.append("<hdInBytes>0</hdInBytes>");
        buffer.append("<highDisponibility>0</highDisponibility>");
        buffer.append("<id>1</id>");
        buffer.append("<idState>0</idState>");
        buffer.append("<idType>0</idType>");
        buffer.append("<name>VM</name>");
        buffer.append("<ram>0</ram>");
        buffer.append("<vdrpPort>0</vdrpPort>");
        buffer.append("</virtualMachine>");
        return buffer.toString();
    }

    public static String volumePostPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<volume>");
        buffer.append(link("/cloud/virtualdatacenters/1/tiers/1", "tier"));
        buffer.append("<name>Volume</name>");
        buffer.append("<sizeInMB>1024</sizeInMB>");
        buffer.append("</volume>");
        return buffer.toString();
    }

    public static String volumePutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<volume>");
        buffer.append(link("/cloud/virtualdatacenters/1/volumes/1/action/initiatormappings",
            "action", "initiator mappings"));
        buffer.append(link("/cloud/virtualdatacenters/1/volumes/1", "edit"));
        buffer.append(link("/cloud/virtualdatacenters/1/tiers/1", "tier"));
        buffer.append(link("/cloud/virtualdatacenters/1", "virtualdatacenter"));
        buffer.append("<id>1</id>");
        buffer.append("<name>Volume</name>");
        buffer.append("<state>DETACHED</state>");
        buffer.append("<sizeInMB>1024</sizeInMB>");
        buffer.append("</volume>");
        return buffer.toString();
    }

    public static String cloudTierPutPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<tier>");
        buffer.append(link("/cloud/virtualdatacenters/1/tiers/1", "edit"));
        buffer.append("<enabled>true</enabled>");
        buffer.append("<id>1</id>");
        buffer.append("<name>Tier</name>");
        buffer.append("</tier>");
        return buffer.toString();
    }

    public static String virtualMachineDeployPayload()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<virtualMachineDeploy>");
        buffer.append("<forceEnterpriseSoftLimits>false</forceEnterpriseSoftLimits>");
        buffer.append("</virtualMachineDeploy>");
        return buffer.toString();

    }
}
