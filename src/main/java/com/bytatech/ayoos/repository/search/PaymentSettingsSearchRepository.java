package com.bytatech.ayoos.repository.search;

import com.bytatech.ayoos.domain.PaymentSettings;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PaymentSettings entity.
 */
public interface PaymentSettingsSearchRepository extends ElasticsearchRepository<PaymentSettings, Long> {
}
