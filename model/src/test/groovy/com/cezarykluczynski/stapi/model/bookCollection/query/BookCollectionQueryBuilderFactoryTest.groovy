package com.cezarykluczynski.stapi.model.bookCollection.query

import com.cezarykluczynski.stapi.model.common.cache.CachingStrategy
import org.springframework.data.jpa.repository.JpaContext
import spock.lang.Specification

class BookCollectionQueryBuilderFactoryTest extends Specification {

	private JpaContext jpaContextMock

	private CachingStrategy cachingStrategyMock

	private BookCollectionQueryBuilderFactory bookCollectionQueryBuilderFactory

	void setup() {
		jpaContextMock = Mock()
		cachingStrategyMock = Mock()
	}

	void "BookCollectionQueryBuilder is created"() {
		when:
		bookCollectionQueryBuilderFactory = new BookCollectionQueryBuilderFactory(jpaContextMock, cachingStrategyMock)

		then:
		bookCollectionQueryBuilderFactory != null
	}

}
