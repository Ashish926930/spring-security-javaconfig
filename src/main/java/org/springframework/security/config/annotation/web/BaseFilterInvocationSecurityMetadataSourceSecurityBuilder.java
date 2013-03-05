/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.config.annotation.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.ConsensusBased;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.RequestMatcher;

/**
 * @author Rob Winch
 *
 */
abstract class BaseFilterInvocationSecurityMetadataSourceSecurityBuilder implements SecurityBuilder<FilterInvocationSecurityMetadataSource> {
    private List<UrlMapping> urlMappings = new ArrayList<UrlMapping>();

    List<UrlMapping> getUrlMappings() {
        return urlMappings;
    }

    void addMapping(UrlMapping urlMapping) {
        this.urlMappings.add(urlMapping);
    }

    void addMapping(int index, UrlMapping urlMapping) {
        this.urlMappings.add(index, urlMapping);
    }

    final AccessDecisionManager createDefaultAccessDecisionManager() {
        return new ConsensusBased(decisionVoters());
    }

    /**
     * @return
     */
    abstract List<AccessDecisionVoter> decisionVoters();

    LinkedHashMap<RequestMatcher,Collection<ConfigAttribute>> createRequestMap() {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher,Collection<ConfigAttribute>>();
        for(UrlMapping mapping : getUrlMappings()) {
            requestMap.put(mapping.getRequestMatcher(), mapping.getConfigAttrs());
        }
        return requestMap;
    }

    static class UrlMapping {
        private RequestMatcher requestMatcher;
        private Collection<ConfigAttribute> configAttrs;

        public UrlMapping(RequestMatcher requestMatcher,
                Collection<ConfigAttribute> configAttrs) {
            this.requestMatcher = requestMatcher;
            this.configAttrs = configAttrs;
        }

        public RequestMatcher getRequestMatcher() {
            return requestMatcher;
        }

        public Collection<ConfigAttribute> getConfigAttrs() {
            return configAttrs;
        }
    }
}