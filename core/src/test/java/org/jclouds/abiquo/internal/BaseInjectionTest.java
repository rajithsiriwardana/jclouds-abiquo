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

package org.jclouds.abiquo.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Properties;

import org.jclouds.compute.ComputeServiceContextFactory;
import org.jclouds.lifecycle.Closer;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Unit tests for the {@link BaseCloudService} class.
 * 
 * @author Ignasi Barrera
 */
@Test(groups = "unit")
public class BaseInjectionTest
{
    protected Injector injector;

    @BeforeClass
    public void setup()
    {
        String identity =
            checkNotNull(System.getProperty("test.abiquo.identity"), "test.abiquo.identity");
        String credential =
            checkNotNull(System.getProperty("test.abiquo.credential"), "test.abiquo.credential");

        injector =
            new ComputeServiceContextFactory()
                .createContext("abiquo", identity, credential,
                    ImmutableSet.<Module> of(new SLF4JLoggingModule()), buildProperties())
                .getUtils().getInjector();
    }

    protected Properties buildProperties()
    {
        Properties props = new Properties();
        props.setProperty("abiquo.endpoint", "http://localhost/api");
        return props;
    }

    @AfterClass
    public void tearDown() throws Exception
    {
        if (injector != null)
        {
            injector.getInstance(Closer.class).close();
        }
    }

}
