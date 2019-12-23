package com.bytatech.ayoos.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of PaymentSettingsSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class PaymentSettingsSearchRepositoryMockConfiguration {

    @MockBean
    private PaymentSettingsSearchRepository mockPaymentSettingsSearchRepository;

}
