/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.test;

import java.sql.SQLException;
import java.util.List;
import entidades.financiera.CXP;
import sys.util.CxpDTODao;
import dao.financiera.CXPDaoDB2;
import entidades.financiera.RESDIAN;
import sys.util.ResdianDTODao;
import dao.financiera.RESDIANDaoDB2;
import entidades.financiera.PUC;
import sys.util.PucDTODao;
import dao.financiera.PUCDaoDB2;
import sys.util.Sesion;
/**
 *
 * @author ebedoya
 */
public class PruebaCRUD {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Vainas para el primer CRUD de CXP
        // *********************************
        // *********************************
        
        /*CxpDTODao cxpdao = new cxpDaoDB2();
        
        // INSERTAR
        cxpDTO cxpdto = new cxpDTO();
        
        cxpdto.setCmid("CM");
        cxpdto.setCmfrc("USD");
        cxpdto.setCmtoc("BSV");
        cxpdto.setCmdat(201812);
        cxpdto.setCmexc(200.0000000);
        cxpdto.setCmexp(200.0000000);
        cxpdto.setCmexf(200.0000000);
        cxpdto.setCmwsid("QPADEV0013");
        cxpdto.setCmwusr("BAM02");
        cxpdto.setCmwdat(30130);
        cxpdto.setCmwtim(145932);
        
        try{
            // Llamado del metodo INSERT
            cxpdao.insert(cxpdto);
            
            // ACTUALIZAR
            cxpDTO cxptemp = new cxpDTO();
            cxptemp.setCmexc(898.0000000);
            cxptemp.setCmexp(898.0000000);
            cxptemp.setCmexf(898.0000000);
            cxptemp.setCmwsid("QPADEV0013");
            cxptemp.setCmwusr("BAM02");
            cxptemp.setCmwdat(30130);
            cxptemp.setCmwtim(111111);
            // Estos campos modifican es el condicional del WHERE
            cxptemp.setCmid("CM");
            cxptemp.setCmfrc("USD");
            cxptemp.setCmtoc("BSV");
            cxptemp.setCmdat(201812);
            // ***************************************************
            // Llamado del metodo UPDATE
            cxpdao.update(cxptemp);
            
            // ELIMINAR
            cxpdao.delete(new cxpDTO("CM", "USD", "BSV", 201810));
            
            // LISTAR
            List<cxpDTO> cxpdtos = cxpdao.select();
            for(cxpDTO cxpdtoxx: cxpdtos){
                System.out.println(cxpdtoxx);
                System.out.println();
            }
            
            
        } catch(SQLException e){
            System.out.println("Exception de la capa de prueba");
            e.printStackTrace();
        }*/
        
        // *********************************
        // *********************************
        
        // Vainas para el CRUD de Resdian
        // ******************************
        // ******************************
        
        /*ResdianDTODao resdiandao = new resdianDaoDB2();*/
        
        //INSERTAR
        /*resdianDTO resdiandto = new resdianDTO();
        
        resdiandto.setResolucion("100000003453 de 2018/10/24");
        resdiandto.setPrefijo("VE");
        resdiandto.setN_inicial(2000);
        resdiandto.setN_final(5000);
        resdiandto.setFech_ini(20181224);
        resdiandto.setFech_venc(20181224);*/
        
        /*try{
            // Llamado del metodo insert
            resdiandao.insert(resdiandto);
            
            // ACTUALIZAR
            resdianDTO resdiandto = new resdianDTO();
            resdiandto.setN_inicial(1000);
            resdiandto.setN_final(2000);
            resdiandto.setFech_ini(20181025);
            resdiandto.setFech_venc(20181025);
            // Estos campos modifican es el condicional del WHERE
            resdiandto.setResolucion("1000000000000000 de 2018/10/24");
            resdiandto.setPrefijo("CV");
            // ***************************************************
            // Llamado del metodo UPDATE
            resdiandao.update(resdiandto);
            
            // ELIMINAR
            resdiandao.delete(new resdianDTO("1000000000000000 de 2018/10/24", "CV"));
            
            // LISTAR
            /*List<resdianDTO> resdians = resdiandao.select();
            for(resdianDTO resdianxx: resdians){
                System.out.println(resdianxx);
                System.out.println("");
            }
        } catch(SQLException e){
            System.out.println("Exception de la capa de prueba");
            e.printStackTrace();
        }*/
        
        // *********************************
        // *********************************
        
        // Vainas para el CRUD de cuentas PUC
        // **********************************
        // **********************************
        Sesion sesion=new Sesion();
        PucDTODao pucdtodao = new PUCDaoDB2(sesion);
        
        // INSERTAR
        /*pucDTO pucdto = new pucDTO();*/
        
        /*pucdto.setCtcta("9999999");
        pucdto.setCtdesc("PRUEBA_DEV");
        pucdto.setCtctyp("AS");
        pucdto.setCtterc("");
        pucdto.setCtinve("");*/
        
        /*try{
            // Llamado del metodo insert
            pucdtodao.insert(pucdto);
            
            // ACTUALIZAR
            pucDTO pucdto = new pucDTO();
            
            pucdto.setCtdesc("PRUEBA_DEV_UPDATE");
            pucdto.setCtctyp("AS");
            pucdto.setCtterc("");
            pucdto.setCtinve("");
            // Estos campos modifican es el condicional del WHERE
            pucdto.setCtcta("9999999");
            // ***************************************************
            // Llamado del metodo UPDATE
            pucdtodao.update(pucdto);
            
            
             // ELIMINAR
             pucdtodao.delete(new pucDTO("9999999"));
             
             // LISTAR
             List<pucDTO> pucdtos = pucdtodao.select();
             for(pucDTO pucdtoxxx: pucdtos){
                 System.out.println(pucdtoxxx);
                 System.out.println("");
             }
             
             
        } catch(SQLException e){
            System.out.println("Exception de la capa de prueba");
            e.printStackTrace();
        }*/
    }
    
}
