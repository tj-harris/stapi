package com.cezarykluczynski.stapi.etl.template.individual.processor;

import com.cezarykluczynski.stapi.etl.common.dto.EnrichablePair;
import com.cezarykluczynski.stapi.etl.common.processor.ItemEnrichingProcessor;
import com.cezarykluczynski.stapi.etl.template.individual.dto.IndividualTemplate;
import com.cezarykluczynski.stapi.etl.template.service.TemplateFinder;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Page;
import com.cezarykluczynski.stapi.sources.mediawiki.dto.Template;
import com.cezarykluczynski.stapi.util.constant.TemplateName;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

@Service
public class IndividualTemplateCompositeEnrichingProcessor implements ItemEnrichingProcessor<EnrichablePair<Page, IndividualTemplate>> {

	private TemplateFinder templateFinder;

	private IndividualTemplateDateOfDeathEnrichingProcessor individualTemplateDateOfDeathEnrichingProcessor;

	private IndividualTemplatePartsEnrichingProcessor individualTemplatePartsEnrichingProcessor;

	private IndividualMirrorAlternateUniverseEnrichingProcessor individualMirrorAlternateUniverseEnrichingProcessor;

	@Inject
	public IndividualTemplateCompositeEnrichingProcessor(TemplateFinder templateFinder,
			IndividualTemplateDateOfDeathEnrichingProcessor individualTemplateDateOfDeathEnrichingProcessor,
			IndividualTemplatePartsEnrichingProcessor individualTemplatePartsEnrichingProcessor,
			IndividualMirrorAlternateUniverseEnrichingProcessor individualMirrorAlternateUniverseEnrichingProcessor) {
		this.templateFinder = templateFinder;
		this.individualTemplateDateOfDeathEnrichingProcessor = individualTemplateDateOfDeathEnrichingProcessor;
		this.individualTemplatePartsEnrichingProcessor = individualTemplatePartsEnrichingProcessor;
		this.individualMirrorAlternateUniverseEnrichingProcessor = individualMirrorAlternateUniverseEnrichingProcessor;
	}

	@Override
	public void enrich(EnrichablePair<Page, IndividualTemplate> enrichablePair) throws Exception {
		IndividualTemplate individualTemplate = enrichablePair.getOutput();
		Page item = enrichablePair.getInput();

		Optional<Template> sidebarIndividualTemplateOptional = templateFinder.findTemplate(item, TemplateName.SIDEBAR_INDIVIDUAL);

		if (!sidebarIndividualTemplateOptional.isPresent()) {
			return;
		}

		Template template = sidebarIndividualTemplateOptional.get();

		individualTemplateDateOfDeathEnrichingProcessor.enrich(EnrichablePair.of(template, individualTemplate));
		individualTemplatePartsEnrichingProcessor.enrich(EnrichablePair.of(template.getParts(), individualTemplate));
		individualMirrorAlternateUniverseEnrichingProcessor.enrich(EnrichablePair.of(item, individualTemplate));
	}

}
