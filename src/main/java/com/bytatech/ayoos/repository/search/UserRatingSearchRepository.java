package com.bytatech.ayoos.repository.search;

import com.bytatech.ayoos.domain.UserRating;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserRating entity.
 */
public interface UserRatingSearchRepository extends ElasticsearchRepository<UserRating, Long> {
}
