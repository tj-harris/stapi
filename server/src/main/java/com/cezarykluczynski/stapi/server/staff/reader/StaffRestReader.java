package com.cezarykluczynski.stapi.server.staff.reader;


import com.cezarykluczynski.stapi.client.v1.rest.model.StaffBaseResponse;
import com.cezarykluczynski.stapi.client.v1.rest.model.StaffFullResponse;
import com.cezarykluczynski.stapi.model.staff.entity.Staff;
import com.cezarykluczynski.stapi.server.common.mapper.PageMapper;
import com.cezarykluczynski.stapi.server.common.reader.BaseReader;
import com.cezarykluczynski.stapi.server.common.reader.FullReader;
import com.cezarykluczynski.stapi.server.staff.dto.StaffRestBeanParams;
import com.cezarykluczynski.stapi.server.staff.mapper.StaffRestMapper;
import com.cezarykluczynski.stapi.server.staff.query.StaffRestQuery;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.inject.Inject;


@Service
public class StaffRestReader implements BaseReader<StaffRestBeanParams, StaffBaseResponse>, FullReader<String, StaffFullResponse> {

	private StaffRestQuery staffRestQuery;

	private StaffRestMapper staffRestMapper;

	private PageMapper pageMapper;

	@Inject
	public StaffRestReader(StaffRestQuery staffRestQuery, StaffRestMapper staffRestMapper, PageMapper pageMapper) {
		this.staffRestQuery = staffRestQuery;
		this.staffRestMapper = staffRestMapper;
		this.pageMapper = pageMapper;
	}

	@Override
	public StaffBaseResponse readBase(StaffRestBeanParams staffRestBeanParams) {
		Page<Staff> staffPage = staffRestQuery.query(staffRestBeanParams);
		StaffBaseResponse staffResponse = new StaffBaseResponse();
		staffResponse.setPage(pageMapper.fromPageToRestResponsePage(staffPage));
		staffResponse.getStaff().addAll(staffRestMapper.mapBase(staffPage.getContent()));
		return staffResponse;
	}

	@Override
	public StaffFullResponse readFull(String guid) {
		Preconditions.checkNotNull(guid, "GUID is required");
		StaffRestBeanParams staffRestBeanParams = new StaffRestBeanParams();
		staffRestBeanParams.setGuid(guid);
		Page<Staff> staffPage = staffRestQuery.query(staffRestBeanParams);
		StaffFullResponse staffResponse = new StaffFullResponse();
		staffResponse.setStaff(staffRestMapper.mapFull(Iterables.getOnlyElement(staffPage.getContent(), null)));
		return staffResponse;
	}

}
