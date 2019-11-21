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
