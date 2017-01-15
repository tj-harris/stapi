package com.cezarykluczynski.stapi.etl.template.movie.linker

import com.cezarykluczynski.stapi.etl.movie.creation.dto.MovieLinkedTitlesDTO
import com.cezarykluczynski.stapi.etl.movie.creation.processor.MovieClosingCreditsProcessor
import com.cezarykluczynski.stapi.etl.movie.creation.processor.MovieLinkedTitlesProcessor
import com.cezarykluczynski.stapi.model.movie.entity.Movie
import com.cezarykluczynski.stapi.sources.mediawiki.api.dto.PageSection
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page
import com.google.common.collect.Lists
import spock.lang.Specification

class MovieRealPeopleLinkingWorkerCompositeTest extends Specification {

	private MovieClosingCreditsProcessor movieClosingCreditsProcessorMock

	private MovieLinkedTitlesProcessor movieLinkedTitlesProcessorMock

	private MovieStaffLinkingWorkerComposite movieStaffLinkingWorkerCompositeMock

	private MoviePerformersLinkingWorkerComposite moviePerformersLinkingWorkerCompositeMock

	private MovieRealPeopleLinkingWorkerComposite movieRealPeopleLinkingWorkerComposite

	def setup() {
		movieClosingCreditsProcessorMock = Mock(MovieClosingCreditsProcessor)
		movieLinkedTitlesProcessorMock = Mock(MovieLinkedTitlesProcessor)
		movieStaffLinkingWorkerCompositeMock = Mock(MovieStaffLinkingWorkerComposite)
		moviePerformersLinkingWorkerCompositeMock = Mock(MoviePerformersLinkingWorkerComposite)
		movieRealPeopleLinkingWorkerComposite = new MovieRealPeopleLinkingWorkerComposite(movieClosingCreditsProcessorMock,
				movieLinkedTitlesProcessorMock, movieStaffLinkingWorkerCompositeMock, moviePerformersLinkingWorkerCompositeMock)
	}

	def "gets closing credits, then gets titles in sections, then passes results to movie linkers"() {
		given:
		MovieLinkedTitlesDTO movieLinkedTitlesDTO = Mock(MovieLinkedTitlesDTO)
		List<PageSection> pageSectionList = Lists.newArrayList()
		Page page = Mock(Page)
		Movie movie = Mock(Movie)

		when:
		movieRealPeopleLinkingWorkerComposite.link(page, movie)

		then:
		1 * movieClosingCreditsProcessorMock.process(page) >> pageSectionList
		1 * movieLinkedTitlesProcessorMock.process(pageSectionList) >> movieLinkedTitlesDTO
		1 * movieStaffLinkingWorkerCompositeMock.link(movieLinkedTitlesDTO, movie)
		1 * moviePerformersLinkingWorkerCompositeMock.link(movieLinkedTitlesDTO, movie)
		0 * _
	}

}
