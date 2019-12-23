package com.bytatech.ayoos.repository.search;

import com.bytatech.ayoos.domain.Reply;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Reply entity.
 */
public interface ReplySearchRepository extends ElasticsearchRepository<Reply, Long> {
}
