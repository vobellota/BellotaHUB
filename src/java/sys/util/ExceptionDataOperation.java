/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sys.util;

import java.util.Date;

/**
 * Esta clase es responsable por almacenar información de logs
 * para ser guardados en la base de datos (Objeto DTO)
 *
 * @author Camilo Rojas
 * @version 1.0 2019-09-13
 */
public class ExceptionDataOperation {

    private int idLog;
    private int idObjPg;
    private String idObjAS;
    private String idObjAS2;
    private String packageName;
    private String className;
    private String methodName;
    private String variableName;
    private String typeOperation;
    private Date dateCreationLog;
    private String message;
    private String status;
    private String codError;

    
    /**
     * Constructor vacío
     **/
    public ExceptionDataOperation() {
    }
    
    
 /**
     * Constructor. Almacena data de logs para ser guardados en la base de datos
     *
     * @param idLog, autoincremental de log en base de datos postgresql
     * @param idObjPg, Código de objeto postgresql el cual ejecuta cualquier tipo de operación ya sea de base de datos o conversiones o cualquier otra
     * @param idObjAS, Código de objeto DB2-AS400 el cual ejecuta cualquier tipo de operación ya sea de base de datos o conversiones o cualquier otra
     * @param idObjAS2, Código de objeto Ccompuesto DB2-AS400 el cual ejecuta cualquier tipo de operación ya sea de base de datos o conversiones o cualquier otra
     * @param packageName, nombre del paquete java
     * @param className,nombre de la clase java
     * @param methodName, nombre del método java
     * @param variableName, nombre de la variable de excepción en algún método java
     * @param status, 200 exitoso, 503 error (también puede tomar otros valores deseados)
     * @param message, mensaje de error lanzado por la operación fallida
     * @param codError, código de error lanzado por la operación fallida
     */
    public ExceptionDataOperation(int idLog, int idObjPg, String idObjAS, String idObjAS2, String packageName, String className, String methodName, String variableName, String typeOperation, Date dateCreationLog, String message, String status, String codError ) {
        this.idLog = idLog;
        this.idObjPg = idObjPg;
        this.idObjAS = idObjAS;
        this.idObjAS2 = idObjAS2;
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.variableName = variableName;
        this.typeOperation = typeOperation;
        this.dateCreationLog = dateCreationLog;
        this.message = message;
        this.status = status;
        this.codError = codError;
    }
    
     
/**
     * Constructor. Almacena data de logs para ser guardados en la base de datos
     *
     * @param idObjPg, Código de objeto postgresql el cual ejecuta cualquier tipo de operación ya sea de base de datos o conversiones o cualquier otra
     * @param idObjAS, Código de objeto DB2-AS400 el cual ejecuta cualquier tipo de operación ya sea de base de datos o conversiones o cualquier otra
     * @param idObjAS2, Código de objeto Ccompuesto DB2-AS400 el cual ejecuta cualquier tipo de operación ya sea de base de datos o conversiones o cualquier otra
     * @param packageName, nombre del paquete java
     * @param className,nombre de la clase java
     * @param methodName, nombre del método java
     * @param variableName, nombre de la variable de excepción en algún método java
     * @param status, 200 exitoso, 503 error (también puede tomar otros valores deseados)
     * @param message, mensaje de error lanzado por la operación fallida
     * @param codError, código de error lanzado por la operación fallida
     */
    public ExceptionDataOperation( int idObjPg, String idObjAS, String idObjAS2, String packageName, String className, String methodName, String variableName, String typeOperation, String message, String status, String codError ) {
        this.idObjPg = idObjPg;
        this.idObjAS = idObjAS;
        this.idObjAS2 = idObjAS2;
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.variableName = variableName;
        this.typeOperation = typeOperation;
        this.message = message;
        this.status = status;
        this.codError = codError;
    }


    /**
     * @return the packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName the packageName to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * @return the variableName
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * @param variableName the variableName to set
     */
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    /**
     * @return the typeOperation
     */
    public String getTypeOperation() {
        return typeOperation;
    }

    /**
     * @param typeOperation the typeOperation to set
     */
    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the idLog
     */
    public int getIdLog() {
        return idLog;
    }

    /**
     * @param idLog the idLog to set
     */
    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    /**
     * @return the idObjPg
     */
    public int getIdObjPg() {
        return idObjPg;
    }

    /**
     * @param idObjPg the idObjPg to set
     */
    public void setIdObjPg(int idObjPg) {
        this.idObjPg = idObjPg;
    }

    /**
     * @return the idObjAS
     */
    public String getIdObjAS() {
        return idObjAS;
    }

    /**
     * @param idObjAS the idObjAS to set
     */
    public void setIdObjAS(String idObjAS) {
        this.idObjAS = idObjAS;
    }

    /**
     * @return the idObjAS2
     */
    public String getIdObjAS2() {
        return idObjAS2;
    }

    /**
     * @param idObjAS2 the idObjAS2 to set
     */
    public void setIdObjAS2(String idObjAS2) {
        this.idObjAS2 = idObjAS2;
    }

    /**
     * @return the dateCreationLog
     */
    public Date getDateCreationLog() {
        return dateCreationLog;
    }

    /**
     * @param dateCreationLog the dateCreationLog to set
     */
    public void setDateCreationLog(Date dateCreationLog) {
        this.dateCreationLog = dateCreationLog;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the codError
     */
    public String getCodError() {
        return codError;
    }

    /**
     * @param codError the codError to set
     */
    public void setCodError(String codError) {
        this.codError = codError;
    }

}
