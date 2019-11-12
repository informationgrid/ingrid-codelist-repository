package de.ingrid.codelistHandler.jobs;

import de.ingrid.codelistHandler.importer.priorityDataset.PriorityDatasetImporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class UpdatePriorityDatasetJob extends QuartzJobBean {

    private final static Logger log = LogManager.getLogger(UpdatePriorityDatasetJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (log.isDebugEnabled()) {
            log.debug("Executing UpdatePriorityDatasetJob...");
        }

        PriorityDatasetImporter priorityDatasetImporter = getImporterFromBean(jobExecutionContext);
        priorityDatasetImporter.start();

        if (log.isDebugEnabled()) {
            log.debug("UpdatePriorityDatasetJob finished");
        }
    }

    public PriorityDatasetImporter getImporterFromBean(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SchedulerContext schedulerContext;
        try {
            schedulerContext = jobExecutionContext.getScheduler().getContext();
        } catch(SchedulerException e) {
            throw new JobExecutionException("Failure accessing scheduler context", e);
        }
        ApplicationContext appContext = (ApplicationContext)schedulerContext.get("applicationContext");

        return (PriorityDatasetImporter) appContext.getBean("priorityDatasetImporter");
    }
}
