/*-
 * **************************************************-
 * InGrid CodeList Repository
 * ==================================================
 * Copyright (C) 2014 - 2022 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
package de.ingrid.codelistHandler.jobs;

import de.ingrid.codelistHandler.importer.Importer;
import de.ingrid.codelistHandler.importer.inspireRegistry.PriorityDatasetImporter;
import de.ingrid.codelistHandler.importer.inspireRegistry.SpatialScopeImporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.List;

public class UpdatePriorityDatasetJob extends QuartzJobBean {

    private final static Logger log = LogManager.getLogger(UpdatePriorityDatasetJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (log.isDebugEnabled()) {
            log.debug("Executing InspireRegistryJob...");
        }

        List<Importer> importers = getImporterFromBean(jobExecutionContext);
        importers.forEach(Importer::start);

        if (log.isDebugEnabled()) {
            log.debug("InspireRegistryJob finished");
        }
    }

    public List<Importer> getImporterFromBean(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SchedulerContext schedulerContext;
        try {
            schedulerContext = jobExecutionContext.getScheduler().getContext();
        } catch(SchedulerException e) {
            throw new JobExecutionException("Failure accessing scheduler context", e);
        }
        ApplicationContext appContext = (ApplicationContext)schedulerContext.get("applicationContext");

        List<Importer> list = new ArrayList<>();
        list.add((PriorityDatasetImporter) appContext.getBean("priorityDatasetImporter"));
        list.add((SpatialScopeImporter) appContext.getBean("spatialScopeImporter"));

        return list;
    }
}
