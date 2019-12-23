package com.bytatech.ayoos.repository.search;

import com.bytatech.ayoos.domain.ReservedSlot;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ReservedSlot entity.
 */
public interface ReservedSlotSearchRepository extends ElasticsearchRepository<ReservedSlot, Long> {
}
