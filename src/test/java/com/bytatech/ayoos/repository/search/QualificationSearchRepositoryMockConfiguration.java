package com.bytatech.ayoos.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of QualificationSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class QualificationSearchRepositoryMockConfiguration {

    @MockBean
    private QualificationSearchRepository mockQualificationSearchRepository;

}
