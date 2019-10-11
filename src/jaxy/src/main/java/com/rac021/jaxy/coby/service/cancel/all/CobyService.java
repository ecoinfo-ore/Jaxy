
package com.rac021.jaxy.coby.service.cancel.all ;

/**
 *
 * @author ryahiaoui
 */

import javax.ws.rs.GET ;
import javax.ws.rs.Produces ;
import javax.ws.rs.HeaderParam ;
import javax.ws.rs.core.UriInfo ;
import javax.ws.rs.core.Context ;
import javax.ws.rs.core.Response ;
import javax.annotation.PostConstruct ;
import com.rac021.jaxy.coby.checker.TokenManager ;
import com.rac021.jaxy.coby.scheduler.COBYScheduler ;
import com.rac021.jaxy.api.qualifiers.ServiceRegistry;
import com.rac021.jaxy.api.qualifiers.security.Policy;
import com.rac021.jaxy.api.qualifiers.security.Secured;
import com.rac021.jaxy.api.exceptions.BusinessException;
import static com.rac021.jaxy.coby.scheduler.COBYScheduler.JOBS ;
import static com.rac021.jaxy.coby.scheduler.PipelineRunner.process ;
import static com.rac021.jaxy.coby.scheduler.COBYScheduler.SUBMITTED_JOB ;

/**
 *
 * @author R.Yahiaoui
 */

@ServiceRegistry("coby_cancel_all")
@Secured(policy = Policy.CustomSignOn )
// @Cipher( cipherType = { CipherTypes.AES_128_CBC, CipherTypes.AES_256_CBC } )

public class CobyService    {
 
    @PostConstruct
    public void init() {
    }

    public CobyService() {
    }
    
    @GET
    @Produces( {  "xml/plain" } )
    public Response cancel ( @HeaderParam("API-key-Token") String token ,
                             @HeaderParam("keep") String filterdIndex   , 
                             @Context UriInfo uriInfo ) throws BusinessException     {    
         
       // executorService.shutdownNow()       ;
        
        String login     = TokenManager.getLogin(token) ;
         
        if( JOBS != null && ! JOBS.isEmpty() )       {
            JOBS.removeIf( q -> q.trim()
                                 .startsWith(login)) ;
        }
        
        if( SUBMITTED_JOB != null )   {
          
           if( COBYScheduler.jobOwner == null ) {
               
               return Response.status(Response.Status.OK)
                              .entity(" \n All jobs Cleaned & Canceled ... \n " )
                              .build() ;
           }
           
           else if( COBYScheduler.jobOwner.equals(login )) {
               
                SUBMITTED_JOB.cancel(true) ;

                process.destroyForcibly()  ;

                return Response.status(Response.Status.OK)
                               .entity(" \n All jobs Cleaned & Canceled ... \n " )
                               .build() ;
           }
        }
        
        return Response.status(Response.Status.OK)
                       .entity(" \n  All jobs Cleared from the Queue \n "
                               + " No Current Job for the user [ " + login + " ] "
                               + " submited to Cancel \n " )
                       .build() ;
    }   

}

