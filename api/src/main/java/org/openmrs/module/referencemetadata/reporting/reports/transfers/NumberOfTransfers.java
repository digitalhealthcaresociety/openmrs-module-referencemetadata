/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.referencemetadata.reporting.reports.transfers;

import org.openmrs.module.referencemetadata.reporting.reports.ReferenceApplicationReportManager;
import org.openmrs.module.reporting.ReportingConstants;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NumberOfTransfers extends ReferenceApplicationReportManager  {

	private static final String DATA_SET_UUID = "b183ed6a-ecd0-43a8-b3f5-4f0066327cda";

	public NumberOfTransfers() {
	}

	@Override
	public String getUuid() {
		return "b39c4c4c-4881-11e7-a919-92ebcb67fe33";
	}

	@Override
	public String getName() {
		return "Number of Transfers";
	}

	@Override
	public String getDescription() {
		return "Number of Transfers for a given location";
	}

	@Override
	public List<Parameter> getParameters() {
		List<Parameter> parameterArrayList = new ArrayList<Parameter>();
		parameterArrayList.add(ReportingConstants.LOCATION_PARAMETER);
		return parameterArrayList;
	}

	@Override
	public ReportDefinition constructReportDefinition() {
		ReportDefinition reportDef = new ReportDefinition();
		reportDef.setUuid(getUuid());
		reportDef.setName(getName());
		reportDef.setDescription(getDescription());
		reportDef.setParameters(getParameters());

		SqlDataSetDefinition sqlDataDef = new SqlDataSetDefinition();
		sqlDataDef.setUuid(DATA_SET_UUID);
		sqlDataDef.setName(getName());
		sqlDataDef.addParameters(getParameters());
		sqlDataDef.setSqlQuery(getSQLQuery());

		reportDef.addDataSetDefinition("Transfer Count", Mapped.mapStraightThrough(sqlDataDef));


		return reportDef;
	}

	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		List<ReportDesign> l = new ArrayList<ReportDesign>();
		l.add(ReportManagerUtil.createExcelDesign("ca5a731f-eee2-4fa5-b394-a6ec9fc41194", reportDefinition));
		return l;
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	private String getSQLQuery(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("select count(*) as Count from encounter e ");
		stringBuilder.append("where e.encounter_type=(select encounter_type_id from encounter_type where uuid='7b68d557-85ef-4fc8-b767-4fa4f5eb5c23') ");
		stringBuilder.append("and e.location_id=:location ");
		stringBuilder.append("and e.voided = false ");
		stringBuilder.append("group by e.encounter_type ");

		return stringBuilder.toString();
	}
}
