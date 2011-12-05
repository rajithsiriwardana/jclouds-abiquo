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

package org.jclouds.abiquo.features;

import static org.jclouds.abiquo.domain.DomainUtils.withHeader;

import java.io.IOException;
import java.lang.reflect.Method;

import org.jclouds.abiquo.domain.CloudResources;
import org.jclouds.abiquo.domain.cloud.options.VirtualApplianceOptions;
import org.jclouds.abiquo.domain.cloud.options.VirtualDatacenterOptions;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.http.functions.ReleasePayloadAndReturn;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.Test;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.cloud.VirtualApplianceDto;
import com.abiquo.server.core.cloud.VirtualDatacenterDto;
import com.abiquo.server.core.cloud.VirtualMachineDto;
import com.google.inject.TypeLiteral;

/**
 * Tests annotation parsing of {@code CloudAsyncClient}
 * 
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Test(groups = "unit")
public class CloudAsyncClientTest extends BaseAbiquoAsyncClientTest<CloudAsyncClient>
{
    /*********************** Virtual Datacenter ***********************/

    public void testListVirtualDatacentersParams() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            CloudAsyncClient.class.getMethod("listVirtualDatacenters",
                VirtualDatacenterOptions.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, VirtualDatacenterOptions.builder().datacenterId(1)
                .enterpriseId(1).build());

        assertRequestLineEquals(request,
            "GET http://localhost/api/cloud/virtualdatacenters?datacenter=1&enterprise=1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testListVirtualDatacentersNoParams() throws SecurityException,
        NoSuchMethodException, IOException
    {
        Method method =
            CloudAsyncClient.class.getMethod("listVirtualDatacenters",
                VirtualDatacenterOptions.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, VirtualDatacenterOptions.builder().build());

        assertRequestLineEquals(request,
            "GET http://localhost/api/cloud/virtualdatacenters HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testCreateVirtualDatacenter() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            CloudAsyncClient.class.getMethod("createVirtualDatacenter", VirtualDatacenterDto.class,
                Integer.class, Integer.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualDatacenterPost(), 1, 1);

        assertRequestLineEquals(request,
            "POST http://localhost/api/cloud/virtualdatacenters?enterprise=1&datacenter=1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(CloudResources.virtualDatacenterPostPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetVirtualDatacenter() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method = CloudAsyncClient.class.getMethod("getVirtualDatacenter", Integer.class);
        GeneratedHttpRequest<CloudAsyncClient> request = processor.createRequest(method, 1);

        assertRequestLineEquals(request,
            "GET http://localhost/api/cloud/virtualdatacenters/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testUpdateVirtualDatacenter() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            CloudAsyncClient.class.getMethod("updateVirtualDatacenter", VirtualDatacenterDto.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualDatacenterPut());

        assertRequestLineEquals(request,
            "PUT http://localhost/api/cloud/virtualdatacenters/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(CloudResources.virtualDatacenterPutPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteVirtualDatacenter() throws SecurityException, NoSuchMethodException
    {
        Method method =
            CloudAsyncClient.class.getMethod("deleteVirtualDatacenter", VirtualDatacenterDto.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualDatacenterPut());

        assertRequestLineEquals(request,
            "DELETE http://localhost/api/cloud/virtualdatacenters/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*********************** Virtual Appliance ***********************/

    public void testListVirtualAppliances() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            CloudAsyncClient.class.getMethod("listVirtualAppliances", VirtualDatacenterDto.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualDatacenterPut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/cloud/virtualdatacenters/1/virtualappliances HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetVirtualAppliance() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            CloudAsyncClient.class.getMethod("getVirtualAppliance", VirtualDatacenterDto.class,
                Integer.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualDatacenterPut(), 1);

        assertRequestLineEquals(request,
            "GET http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testCreateVirtualAppliance() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            CloudAsyncClient.class.getMethod("createVirtualAppliance", VirtualDatacenterDto.class,
                VirtualApplianceDto.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualDatacenterPut(), CloudResources
                .virtualAppliancePost());

        assertRequestLineEquals(request,
            "POST http://localhost/api/cloud/virtualdatacenters/1/virtualappliances HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(CloudResources.virtualAppliancePostPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testUpdateVirtualAppliance() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            CloudAsyncClient.class.getMethod("updateVirtualAppliance", VirtualApplianceDto.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualAppliancePut());

        assertRequestLineEquals(request,
            "PUT http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(CloudResources.virtualAppliancePutPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteVirtualAppliance() throws SecurityException, NoSuchMethodException
    {
        Method method =
            CloudAsyncClient.class.getMethod("deleteVirtualAppliance", VirtualApplianceDto.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualAppliancePut());

        assertRequestLineEquals(request,
            "DELETE http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteVirtualApplianceWithOptions() throws SecurityException,
        NoSuchMethodException
    {
        Method method =
            CloudAsyncClient.class.getMethod("deleteVirtualAppliance", VirtualApplianceDto.class,
                VirtualApplianceOptions.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualAppliancePut(),
                VirtualApplianceOptions.builder().force(true).build());

        assertRequestLineEquals(request,
            "DELETE http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1?force=true HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeployVirtualAppliance() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method = CloudAsyncClient.class.getMethod("actionVirtualAppliance", RESTLink.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor
                .createRequest(
                    method,
                    new RESTLink("deploy",
                        "http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/action/deploy"));

        assertRequestLineEquals(request,
            "POST http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/action/deploy HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testUndeployVirtualAppliance() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method = CloudAsyncClient.class.getMethod("actionVirtualAppliance", RESTLink.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor
                .createRequest(
                    method,
                    new RESTLink("undeploy",
                        "http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/action/undeploy"));

        assertRequestLineEquals(request,
            "POST http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/action/undeploy HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    /*********************** Virtual Machine ***********************/

    public void testListVirtualMachines() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            CloudAsyncClient.class.getMethod("listVirtualMachines", VirtualApplianceDto.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualAppliancePut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetVirtualMachine() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            CloudAsyncClient.class.getMethod("getVirtualMachine", VirtualApplianceDto.class,
                Integer.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualAppliancePut(), 1);

        assertRequestLineEquals(
            request,
            "GET http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testCreateVirtualMachine() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            CloudAsyncClient.class.getMethod("createVirtualMachine", VirtualApplianceDto.class,
                VirtualMachineDto.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualAppliancePut(), CloudResources
                .virtualMachinePost());

        assertRequestLineEquals(request,
            "POST http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(CloudResources.virtualMachinePostPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testUpdateVirtualMachine() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            CloudAsyncClient.class.getMethod("updateVirtualMachine", VirtualMachineDto.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualMachinePut());

        assertRequestLineEquals(
            request,
            "PUT http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(CloudResources.virtualMachinePutPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseXMLWithJAXB.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteVirtualMachine() throws SecurityException, NoSuchMethodException
    {
        Method method =
            CloudAsyncClient.class.getMethod("deleteVirtualMachine", VirtualMachineDto.class);
        GeneratedHttpRequest<CloudAsyncClient> request =
            processor.createRequest(method, CloudResources.virtualMachinePut());

        assertRequestLineEquals(
            request,
            "DELETE http://localhost/api/cloud/virtualdatacenters/1/virtualappliances/1/virtualmachines/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    @Override
    protected TypeLiteral<RestAnnotationProcessor<CloudAsyncClient>> createTypeLiteral()
    {
        return new TypeLiteral<RestAnnotationProcessor<CloudAsyncClient>>()
        {
        };
    }
}
