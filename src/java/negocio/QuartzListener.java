package negocio;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import negocio.sincronizarBpcs;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import org.quartz.ee.servlet.QuartzInitializerListener;
import static org.quartz.ee.servlet.QuartzInitializerListener.QUARTZ_FACTORY_KEY;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class build the job executed while the application is alive.
 *
 * @author Camilo Rojas
 * @version 1.0 2019-09-13
 */
@WebListener
public class QuartzListener extends QuartzInitializerListener {

    private static final Logger LOG = LoggerFactory.getLogger(QuartzListener.class);

     /**
     * This method obtains a instance of Context application and 
     * create the job for application and triggers with periodicity
     * of execution job.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     * @see <a href="https://www.javamexico.org/blogs/jpaul/ejemplo_basico_quartz_221_tomcat_7054_con_edicion_de_expresion_cron">Example for bBuild Quartz Job</a>
     * @see <a href="http://www.cronmaker.com/">Generator periodicity expression for triggers quartz </a>
     */
    public void contextInitialized(ServletContextEvent sce) {
        //Obtains context application when init the application
        super.contextInitialized(sce);
        ServletContext ctx = sce.getServletContext();
        //Instance variable of library quartz
        StdSchedulerFactory factory = (StdSchedulerFactory) ctx.getAttribute(QUARTZ_FACTORY_KEY);
        try {
            Scheduler scheduler = factory.getScheduler();
            //Create a new job for application referencing the class to execute it
            JobDetail job = JobBuilder.newJob(sincronizarBpcs.class).build();
            //Create a new trigger with periodicity of execution job
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("simple").withSchedule(
                    CronScheduleBuilder.cronSchedule("0 0/5 * 1/1 * ? *")
            ).startNow().build();
            //Init variables Counter and bandExecution
            job.getJobDataMap().put("Counter", 0);
            job.getJobDataMap().put("bandExecution", true);
            //Relationing job with trigger of periodicity
            //scheduler.scheduleJob(job, trigger);
            //Init execution
            //scheduler.start();
        } catch (Exception e) {
            LOG.error("Ocurri\u00f3 un error al calendarizar el trabajo", e);
        }
    }

    /**
     * This method destroyd the context application obtained.
     *
     * @author Camilo Rojas
     * @version 1.0 2019-09-13
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        super.contextDestroyed(sce);
    }
    
}
