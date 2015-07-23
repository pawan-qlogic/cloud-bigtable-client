/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.bigtable.naming;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.bigtable.admin.table.v1.CreateTableRequest;
import com.google.bigtable.admin.table.v1.ListTablesRequest;
import com.google.cloud.bigtable.naming.BigtableClusterName;

@RunWith(JUnit4.class)
public class TestBigtableClusterName {

  public static BigtableClusterName bigtableClusterName = new BigtableClusterName(
      "some-project", "some-zone", "some-cluster");
  private String clusterId = "projects/some-project/zones/some-zone/clusters/some-cluster";

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testFormat() {
    Assert.assertEquals(clusterId, bigtableClusterName.getClusterName());
  }

  @Test
  public void testListTablesRequest() {
    ListTablesRequest.Builder builder = ListTablesRequest.newBuilder();
    builder.setName(bigtableClusterName.getClusterName());
    Assert.assertEquals(clusterId, builder.getName());
  }

  @Test
  public void testCreateTablesRequest() {
    CreateTableRequest.Builder builder = CreateTableRequest.newBuilder();
    builder.setName(bigtableClusterName.getClusterName());
    Assert.assertEquals(clusterId, builder.getName());
  }

  @Test
  public void testGoodTableQualifier() {
    bigtableClusterName.toTableId(clusterId + "/" + BigtableClusterName.TABLE_SEPARATOR
        + "/foo");
  }

  @Test
  public void testNullQualifier() {
    expectedException.expect(NullPointerException.class);
    bigtableClusterName.toTableId(null);
  }

  @Test
  public void testBadQualifier() {
    expectedException.expect(IllegalStateException.class);
    bigtableClusterName.toTableId(clusterId.replace("some-cluster", "another-cluster")
        + "/" + BigtableClusterName.TABLE_SEPARATOR + "/foo");
  }

  @Test
  public void testBlankTableName() {
    expectedException.expect(IllegalStateException.class);
    bigtableClusterName.toTableId(clusterId + "/" + BigtableClusterName.TABLE_SEPARATOR
        + "/");
  }

  @Test
  public void testNoTableName() {
    expectedException.expect(IllegalStateException.class);
    bigtableClusterName.toTableId(clusterId + "/" + BigtableClusterName.TABLE_SEPARATOR);
  }
}
