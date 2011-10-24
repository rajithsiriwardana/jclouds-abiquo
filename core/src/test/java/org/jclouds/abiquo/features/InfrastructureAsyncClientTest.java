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

import org.jclouds.abiquo.domain.Infrastructure;
import org.jclouds.abiquo.domain.infrastructure.options.MachineOptions;
import org.jclouds.abiquo.functions.infrastructure.ParseDatacenter;
import org.jclouds.abiquo.functions.infrastructure.ParseDatacenters;
import org.jclouds.abiquo.functions.infrastructure.ParseMachine;
import org.jclouds.abiquo.functions.infrastructure.ParseRack;
import org.jclouds.abiquo.functions.infrastructure.ParseRacks;
import org.jclouds.abiquo.functions.infrastructure.ParseRemoteService;
import org.jclouds.abiquo.functions.infrastructure.ParseRemoteServices;
import org.jclouds.http.functions.ReleasePayloadAndReturn;
import org.jclouds.rest.functions.ReturnNullOnNotFoundOr404;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import org.testng.annotations.Test;

import com.abiquo.model.enumerator.HypervisorType;
import com.abiquo.model.enumerator.RemoteServiceType;
import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.abiquo.server.core.infrastructure.RackDto;
import com.abiquo.server.core.infrastructure.RemoteServiceDto;
import com.google.inject.TypeLiteral;

/**
 * Tests annotation parsing of {@code InfrastructureAsyncClient}
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class InfrastructureAsyncClientTest extends
    BaseAbiquoAsyncClientTest<InfrastructureAsyncClient>
{

    // Datacenter

    public void testListDatacenters() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = InfrastructureAsyncClient.class.getMethod("listDatacenters");
        GeneratedHttpRequest<InfrastructureAsyncClient> request = processor.createRequest(method);

        assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseDatacenters.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testCreateDatacenter() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("createDatacenter", DatacenterDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, Infrastructure.datacenterPost());

        assertRequestLineEquals(request, "POST http://localhost/api/admin/datacenters HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(Infrastructure.datacenterPostPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseDatacenter.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetDatacenter() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = InfrastructureAsyncClient.class.getMethod("getDatacenter", Integer.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, 1);

        assertRequestLineEquals(request, "GET http://localhost/api/admin/datacenters/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseDatacenter.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testUpdateDatacenter() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("updateDatacenter", DatacenterDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, Infrastructure.datacenterPut());

        assertRequestLineEquals(request, "PUT http://localhost/api/admin/datacenters/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(Infrastructure.datacenterPutPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseDatacenter.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteDatacenter() throws SecurityException, NoSuchMethodException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("deleteDatacenter", DatacenterDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, Infrastructure.datacenterPut());

        assertRequestLineEquals(request, "DELETE http://localhost/api/admin/datacenters/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    // Rack

    public void testListRacks() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = InfrastructureAsyncClient.class.getMethod("listRacks", DatacenterDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, Infrastructure.datacenterPut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/racks HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/notmanagedrackdto+xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseRacks.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testCreateRack() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("createRack", DatacenterDto.class,
                RackDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, Infrastructure.datacenterPut(), Infrastructure
                .rackPost());

        assertRequestLineEquals(request,
            "POST http://localhost/api/admin/datacenters/1/racks HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(Infrastructure.rackPostPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseRack.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetRack() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class
                .getMethod("getRack", DatacenterDto.class, Integer.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, Infrastructure.datacenterPut(), 1);

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/racks/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseRack.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testUpdateRack() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method = InfrastructureAsyncClient.class.getMethod("updateRack", RackDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, Infrastructure.rackPut());

        assertRequestLineEquals(request,
            "PUT http://localhost/api/admin/datacenters/1/racks/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(Infrastructure.rackPutPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseRack.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteRack() throws SecurityException, NoSuchMethodException
    {
        Method method = InfrastructureAsyncClient.class.getMethod("deleteRack", RackDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, Infrastructure.rackPut());

        assertRequestLineEquals(request,
            "DELETE http://localhost/api/admin/datacenters/1/racks/1 HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    // Remote service

    public void testListRemoteService() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("listRemoteServices", DatacenterDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, Infrastructure.datacenterPut());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/remoteservices HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseRemoteServices.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testCreateRemoteService() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("createRemoteService", DatacenterDto.class,
                RemoteServiceDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, Infrastructure.datacenterPut(), Infrastructure
                .remoteServicePost());

        assertRequestLineEquals(request,
            "POST http://localhost/api/admin/datacenters/1/remoteservices HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(Infrastructure.remoteServicePostPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseRemoteService.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testGetRemoteService() throws SecurityException, NoSuchMethodException, IOException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("getRemoteService", DatacenterDto.class,
                RemoteServiceType.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, Infrastructure.datacenterPut(),
                RemoteServiceType.STORAGE_SYSTEM_MONITOR);

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/remoteservices/storagesystemmonitor HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseRemoteService.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testUpdateRemoteService() throws SecurityException, NoSuchMethodException,
        IOException
    {
        Method method =
            InfrastructureAsyncClient.class
                .getMethod("updateRemoteService", RemoteServiceDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, Infrastructure.remoteServicePut());

        assertRequestLineEquals(request,
            "PUT http://localhost/api/admin/datacenters/1/remoteservices/nodecollector HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, withHeader(Infrastructure.remoteServicePutPayload()),
            "application/xml", false);

        assertResponseParserClassEquals(method, request, ParseRemoteService.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDeleteRemoteService() throws SecurityException, NoSuchMethodException
    {
        Method method =
            InfrastructureAsyncClient.class
                .getMethod("deleteRemoteService", RemoteServiceDto.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, Infrastructure.remoteServicePut());

        assertRequestLineEquals(request,
            "DELETE http://localhost/api/admin/datacenters/1/remoteservices/nodecollector HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ReleasePayloadAndReturn.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, null);

        checkFilters(request);
    }

    public void testDiscoverSingleMachineAllParams() throws SecurityException,
        NoSuchMethodException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("discoverSingleMachine", DatacenterDto.class,
                String.class, HypervisorType.class, String.class, String.class,
                MachineOptions.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor
                .createRequest(method, Infrastructure.datacenterPut(), "80.80.80.80",
                    HypervisorType.KVM, "user", "password", MachineOptions.builder().port(8889)
                        .build());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/action/discoversingle HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseMachine.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    public void testDiscoverSingleMachineDefaultValues() throws SecurityException,
        NoSuchMethodException
    {
        Method method =
            InfrastructureAsyncClient.class.getMethod("discoverSingleMachine", DatacenterDto.class,
                String.class, HypervisorType.class, String.class, String.class,
                MachineOptions.class);
        GeneratedHttpRequest<InfrastructureAsyncClient> request =
            processor.createRequest(method, Infrastructure.datacenterPut(), "80.80.80.80",
                HypervisorType.KVM, "user", "password", MachineOptions.builder().build());

        assertRequestLineEquals(request,
            "GET http://localhost/api/admin/datacenters/1/action/discoversingle HTTP/1.1");
        assertNonPayloadHeadersEqual(request, "Accept: application/xml\n");
        assertPayloadEquals(request, null, null, false);

        assertResponseParserClassEquals(method, request, ParseMachine.class);
        assertSaxResponseParserClassEquals(method, null);
        assertExceptionParserClassEquals(method, ReturnNullOnNotFoundOr404.class);

        checkFilters(request);
    }

    @Override
    protected TypeLiteral<RestAnnotationProcessor<InfrastructureAsyncClient>> createTypeLiteral()
    {
        return new TypeLiteral<RestAnnotationProcessor<InfrastructureAsyncClient>>()
        {
        };
    }
}
