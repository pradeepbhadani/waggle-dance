/**
 * Copyright (C) 2016-2017 Expedia Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hotels.bdp.waggledance.api.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrimaryMetaStore extends AbstractMetaStore {

  private final static Logger LOG = LoggerFactory.getLogger(PrimaryMetaStore.class);

  private static final String EMPTY_PREFIX = "";
  private List<String> writableDatabaseWhitelist;

  public PrimaryMetaStore() {}

  public PrimaryMetaStore(
      String name,
      String remoteMetaStoreUris,
      AccessControlType accessControlType,
      String... writableDatabaseWhitelist) {
    super(name, remoteMetaStoreUris, accessControlType);
    this.writableDatabaseWhitelist = Arrays.asList(writableDatabaseWhitelist);
  }

  public PrimaryMetaStore(PrimaryMetaStore primaryMetaStore) {
    this(primaryMetaStore.getName(), primaryMetaStore.getRemoteMetaStoreUris(),
        primaryMetaStore.getAccessControlType());
    writableDatabaseWhitelist = new ArrayList<>(primaryMetaStore.getWritableDatabaseWhiteList());
  }

  @Override
  public FederationType getFederationType() {
    return FederationType.PRIMARY;
  }

  public List<String> getWritableDatabaseWhiteList() {
    if (writableDatabaseWhitelist == null) {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList(writableDatabaseWhitelist);
  }

  public void setWritableDatabaseWhiteList(List<String> writableDatabaseWhitelist) {
    this.writableDatabaseWhitelist = writableDatabaseWhitelist;
  }

  @Size(min = 0, max = 0)
  @NotNull
  @Override
  public String getDatabasePrefix() {
    // primary is always empty
    return EMPTY_PREFIX;
  }

  @Override
  public void setDatabasePrefix(String databasePrefix) {
    LOG.warn("Ignoring attempt to set prefix to '{}', the prefix for a primary metastore is always empty",
        databasePrefix);
  }
}
