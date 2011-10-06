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

package org.jclouds.abiquo.functions.infrastructure;

import static org.jclouds.abiquo.domain.DomainUtils.withHeader;
import static org.testng.Assert.assertEquals;

import org.jclouds.abiquo.domain.Infrastructure;
import org.jclouds.abiquo.functions.ParseXMLTest;
import org.jclouds.abiquo.xml.internal.JAXBParser;
import org.testng.annotations.Test;

import com.abiquo.server.core.infrastructure.DatacenterDto;
import com.google.inject.TypeLiteral;

/**
 * Test Datacenters parsing.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class ParseDatacenterTest extends ParseXMLTest<DatacenterDto>
{

    @Override
    protected ParseDatacenter getParser()
    {
        return new ParseDatacenter(new JAXBParser(), TypeLiteral.get(DatacenterDto.class));
    }

    @Override
    protected String getPayload()
    {
        return withHeader(Infrastructure.datacenterPostPayload());
    }

    @Override
    protected void verifyObject(final DatacenterDto object)
    {
        verifyDatacenter(object);
    }

    static void verifyDatacenter(final DatacenterDto datacenter)
    {
        assertEquals(datacenter.getName(), "DC");
        assertEquals(datacenter.getLocation(), "Honolulu");
    }

}