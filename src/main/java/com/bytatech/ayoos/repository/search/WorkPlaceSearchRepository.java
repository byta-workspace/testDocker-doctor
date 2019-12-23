package com.bytatech.ayoos.repository.search;

import com.bytatech.ayoos.domain.WorkPlace;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WorkPlace entity.
 */
public interface WorkPlaceSearchRepository extends ElasticsearchRepository<WorkPlace, Long> {
}
