package negocio;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import beansVista.distribucion.ProductosCompatiblesBean;

public class sincronizarBpcs implements Job {

    ProductosCompatiblesBean prod;

    /**
     * Constructed . Initialize object BusinessApp
     */
    public sincronizarBpcs() {
        prod = new ProductosCompatiblesBean();
    }

    public void execute(JobExecutionContext ctx) throws JobExecutionException {
        //System.out.printf(new Locale("es", "MX"), "%tc Ejecutando tarea...%n", new java.util.Date());
        prod.inSynchronizingItems();
        prod.inSynchronizingUnits();
    }
}
