package com.bytatech.ayoos.repository.search;

import com.bytatech.ayoos.domain.DoctorSettings;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DoctorSettings entity.
 */
public interface DoctorSettingsSearchRepository extends ElasticsearchRepository<DoctorSettings, Long> {
}
