package com.bytatech.ayoos.repository.search;

import com.bytatech.ayoos.domain.Qualification;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Qualification entity.
 */
public interface QualificationSearchRepository extends ElasticsearchRepository<Qualification, Long> {
}
