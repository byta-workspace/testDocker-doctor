package com.bytatech.ayoos.repository.search;

import com.bytatech.ayoos.domain.SessionInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SessionInfo entity.
 */
public interface SessionInfoSearchRepository extends ElasticsearchRepository<SessionInfo, Long> {
}
