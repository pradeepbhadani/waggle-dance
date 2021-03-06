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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.hibernate.validator.HibernateValidator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public abstract class AbstractMetaStoreTest<T extends AbstractMetaStore> {

  protected final LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

  protected final T metaStore;

  public AbstractMetaStoreTest(T metaStore) {
    this.metaStore = metaStore;
  }

  private static MetastoreTunnel newMetastoreTunnel() {
    MetastoreTunnel metastoreTunnel = new MetastoreTunnel();
    metastoreTunnel.setRoute("user@jumpbox -> host");
    metastoreTunnel.setPrivateKeys("privateKeys");
    metastoreTunnel.setKnownHosts("knownHosts");
    return metastoreTunnel;
  }

  @Before
  public void before() {
    validator.setProviderClass(HibernateValidator.class);
    validator.afterPropertiesSet();

    metaStore.setRemoteMetaStoreUris("uri");
    metaStore.setName("name");
  }

  @Test
  public void typical() {
    Set<ConstraintViolation<T>> violations = validator.validate(metaStore);
    assertThat(violations.size(), is(0));
  }

  @Test
  public void nullRemoteMetaStoreUris() {
    metaStore.setRemoteMetaStoreUris(null);
    Set<ConstraintViolation<T>> violations = validator.validate(metaStore);
    assertThat(violations.size(), is(1));
  }

  @Test
  public void emptyRemoteMetaStoreUris() {
    metaStore.setRemoteMetaStoreUris(" ");
    Set<ConstraintViolation<T>> violations = validator.validate(metaStore);
    assertThat(violations.size(), is(1));
  }

  @Test
  public void validMetastoreTunnel() {
    MetastoreTunnel metastoreTunnel = newMetastoreTunnel();
    metaStore.setMetastoreTunnel(metastoreTunnel);
    Set<ConstraintViolation<T>> violations = validator.validate(metaStore);
    assertThat(violations.size(), is(0));
  }

  @Test
  public void invalidMetastoreTunnel() {
    MetastoreTunnel metastoreTunnel = newMetastoreTunnel();
    metastoreTunnel.setPort(-1);
    metaStore.setMetastoreTunnel(metastoreTunnel);
    Set<ConstraintViolation<T>> violations = validator.validate(metaStore);
    assertThat(violations.size(), is(1));
  }

  @Test
  public void nullName() {
    metaStore.setName(null);
    Set<ConstraintViolation<T>> violations = validator.validate(metaStore);
    assertThat(violations.size(), is(1));
  }

  @Test
  public void emptyName() {
    metaStore.setName(" ");
    Set<ConstraintViolation<T>> violations = validator.validate(metaStore);
    assertThat(violations.size(), is(1));
  }
}
